SUMMARY = "MYD-JMX95 factory eMMC provisioning tools and payload"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "(myir-jmx95)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit systemd

SRC_URI = "file://myir-burn-emmc file://fac-burn-emmc.service"
S = "${UNPACKDIR}"
MYIR_FACTORY_ROOTFS_IMAGE ?= "myir-image-emmc"
MYIR_FACTORY_LAYOUT ?= "single"
MYIR_FACTORY_TARGET_MC ?= ""
MYIR_FACTORY_TARGET_DEPLOY_DIR ?= "${DEPLOY_DIR_IMAGE}"
MYIR_FACTORY_TARGET_DM_VERITY ?= "${ENABLE_DM_VERITY}"
MYIR_FACTORY_TARGET_AHAB ?= "${MYIR_AHAB_ENABLE}"
MYIR_FACTORY_TARGET_FITIMAGE_BOOT ?= "${MYIR_FITIMAGE_BOOT}"
MYIR_FACTORY_MCDEPENDS ?= ""
MYIR_FACTORY_POPULATE_INACTIVE_SLOT ?= "1"
MYIR_FACTORY_OVERLAY_SIZE_MIB ?= "${MYIR_OVERLAY_PARTITION_SIZE}"
MYIR_FACTORY_VERITY_ROOTFS_SIZE_MIB ?= "4096"
MYIR_FACTORY_ROOTFS_MODE = "${@'verity' if d.getVar('MYIR_FACTORY_TARGET_DM_VERITY') == '1' else 'ext4'}"
MYIR_FACTORY_BOOT_IMAGE = "${@'imx-boot-signed' if d.getVar('MYIR_FACTORY_TARGET_AHAB') == '1' else 'imx-boot'}"
MYIR_FACTORY_BOOT_SENTINEL = "${@'fitImage' if d.getVar('MYIR_FACTORY_TARGET_FITIMAGE_BOOT') == '1' else ('os_cntr_signed.bin' if d.getVar('MYIR_FACTORY_TARGET_AHAB') == '1' else d.getVar('KERNEL_IMAGETYPE') or 'Image')}"

python () {
    if d.getVar('MYIR_FACTORY_LAYOUT') not in ('single', 'ab'):
        bb.fatal('MYIR_FACTORY_LAYOUT must be single or ab')
    if d.getVar('MYIR_FACTORY_ROOTFS_IMAGE') != 'myir-image-emmc':
        bb.fatal('Factory payload must be myir-image-emmc, not the burn image')
    if d.getVar('MYIR_FACTORY_POPULATE_INACTIVE_SLOT') not in ('0', '1'):
        bb.fatal('MYIR_FACTORY_POPULATE_INACTIVE_SLOT must be "0" or "1"')
    if d.getVar('MYIR_FACTORY_TARGET_DM_VERITY') == '1':
        for name in ('MYIR_FACTORY_VERITY_ROOTFS_SIZE_MIB',
                     'MYIR_FACTORY_OVERLAY_SIZE_MIB'):
            value = d.getVar(name) or ''
            if not value.isdecimal() or int(value) <= 0:
                bb.fatal('%s must be a positive integer in MiB' % name)
    target_mc = d.getVar('MYIR_FACTORY_TARGET_MC')
    if target_mc and target_mc not in (d.getVar('BBMULTICONFIG') or '').split():
        bb.fatal('Factory target multiconfig %s is not enabled in BBMULTICONFIG' % target_mc)
}

# The product image never depends on this package: no reverse dependency cycle.
do_install[depends] += "${@'' if d.getVar('MYIR_FACTORY_TARGET_MC') else '%s:do_image_complete' % d.getVar('MYIR_FACTORY_ROOTFS_IMAGE')}"
do_install[mcdepends] += "${MYIR_FACTORY_MCDEPENDS}"

python do_install() {
    import pathlib
    import shutil
    from oe.bootfiles import get_boot_files

    dest = pathlib.Path(d.getVar('D'))
    source = pathlib.Path(d.getVar('UNPACKDIR'))
    deploy = pathlib.Path(d.getVar('MYIR_FACTORY_TARGET_DEPLOY_DIR'))
    payload = dest / d.getVar('ROOT_HOME').lstrip('/') / 'mfgimage'
    bootdir = payload / 'kernel_dtb'
    bootdir.mkdir(parents=True)

    boot_spec = d.getVar('IMAGE_BOOT_FILES')
    if d.getVar('MYIR_FACTORY_TARGET_MC'):
        # The factory OS is AHAB-only and writable.  Its IMAGE_BOOT_FILES may
        # contain OS containers even when the target uses a signed FIT.
        boot_files = (boot_spec or '').split()
        security_files = set((d.getVar('SECURE_BOOT_FILES') or '').split())
        kernel_image = d.getVar('KERNEL_IMAGETYPE') or 'Image'
        filtered = []
        for entry in boot_files:
            source_name = entry.split(';', 1)[0]
            if source_name in security_files or source_name in (kernel_image, 'fitImage'):
                continue
            filtered.append(entry)
        if d.getVar('MYIR_FACTORY_TARGET_FITIMAGE_BOOT') == '1':
            filtered.append('fitImage')
        elif d.getVar('MYIR_FACTORY_TARGET_AHAB') == '1':
            filtered.extend(sorted(security_files))
        else:
            filtered.append(kernel_image)
        boot_spec = ' '.join(filtered)

    # Use the same mapping API as the current WIC plugin, including subdirs.
    for src, dst in get_boot_files(str(deploy), boot_spec):
        relative = pathlib.PurePosixPath(dst)
        if relative.is_absolute() or '..' in relative.parts:
            bb.fatal('Invalid factory boot destination: %s' % dst)
        target = bootdir / dst
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(deploy / src, target)
        target.chmod(0o644)

    rootfs_suffix = 'ext4.verity' if d.getVar('MYIR_FACTORY_ROOTFS_MODE') == 'verity' else 'ext4'
    rootfs = '%s-%s.rootfs.%s' % (d.getVar('MYIR_FACTORY_ROOTFS_IMAGE'),
                                  d.getVar('MACHINE'), rootfs_suffix)
    for src, name in ((deploy / d.getVar('MYIR_FACTORY_BOOT_IMAGE'), 'imx-boot'),
                      (deploy / rootfs, 'rootfs-full.%s' % rootfs_suffix)):
        shutil.copyfile(src, payload / name)
        (payload / name).chmod(0o644)

    checksums = []
    for path in sorted(payload.rglob('*')):
        if path.is_file():
            digest = bb.utils.sha256_file(str(path))
            checksums.append('%s  %s\n' % (digest, path.relative_to(payload)))
    (payload / 'SHA256SUMS').write_text(''.join(checksums))

    sbindir = dest / d.getVar('sbindir').lstrip('/')
    sbindir.mkdir(parents=True, exist_ok=True)
    script = (source / 'myir-burn-emmc').read_text()
    script = script.replace('@LAYOUT@', d.getVar('MYIR_FACTORY_LAYOUT'))
    script = script.replace('@ROOT_HOME@', d.getVar('ROOT_HOME'))
    script = script.replace('@ROOTFS_MODE@', d.getVar('MYIR_FACTORY_ROOTFS_MODE'))
    script = script.replace('@ROOTFS_SUFFIX@', rootfs_suffix)
    script = script.replace('@BOOT_SENTINEL@', d.getVar('MYIR_FACTORY_BOOT_SENTINEL'))
    script = script.replace('@OVERLAY_MIB@', d.getVar('MYIR_FACTORY_OVERLAY_SIZE_MIB') or '2048')
    script = script.replace('@VERITY_ROOTFS_MIB@', d.getVar('MYIR_FACTORY_VERITY_ROOTFS_SIZE_MIB'))
    script = script.replace('@POPULATE_INACTIVE@', d.getVar('MYIR_FACTORY_POPULATE_INACTIVE_SLOT'))
    (sbindir / 'myir-burn-emmc').write_text(script)
    (sbindir / 'myir-burn-emmc').chmod(0o755)

    unitdir = dest / d.getVar('systemd_system_unitdir').lstrip('/')
    unitdir.mkdir(parents=True, exist_ok=True)
    unit = (source / 'fac-burn-emmc.service').read_text().replace('@SBINDIR@', d.getVar('sbindir'))
    (unitdir / 'fac-burn-emmc.service').write_text(unit)
}

SYSTEMD_SERVICE:${PN} = "fac-burn-emmc.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
FILES:${PN} += "${ROOT_HOME}/mfgimage"

RDEPENDS:${PN} = " \
    bash coreutils diffutils findutils grep mmc-utils dosfstools \
    e2fsprogs-e2fsck e2fsprogs-mke2fs e2fsprogs-resize2fs e2fsprogs-tune2fs \
    util-linux-blockdev util-linux-findmnt util-linux-flock util-linux-lsblk \
    util-linux-mount util-linux-partx util-linux-sfdisk util-linux-umount \
    util-linux-wipefs udev \
"
