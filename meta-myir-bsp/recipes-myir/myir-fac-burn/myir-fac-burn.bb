SUMMARY = "MYD-JMX95 factory eMMC provisioning tools and payload"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit systemd

SRC_URI = "file://myir-burn-emmc file://fac-burn-emmc.service"
S = "${UNPACKDIR}"
MYIR_FACTORY_ROOTFS_IMAGE ?= "myir-image-emmc"
MYIR_FACTORY_LAYOUT ?= "single"

python () {
    if d.getVar('MYIR_FACTORY_LAYOUT') not in ('single', 'ab'):
        bb.fatal('MYIR_FACTORY_LAYOUT must be single or ab')
    if d.getVar('MYIR_FACTORY_ROOTFS_IMAGE') != 'myir-image-emmc':
        bb.fatal('Factory payload must be myir-image-emmc, not the burn image')
}

# The product image never depends on this package: no reverse dependency cycle.
do_install[depends] += "${MYIR_FACTORY_ROOTFS_IMAGE}:do_image_complete"

python do_install() {
    import pathlib
    import shutil
    from oe.bootfiles import get_boot_files

    dest = pathlib.Path(d.getVar('D'))
    source = pathlib.Path(d.getVar('UNPACKDIR'))
    deploy = pathlib.Path(d.getVar('DEPLOY_DIR_IMAGE'))
    payload = dest / d.getVar('ROOT_HOME').lstrip('/') / 'mfgimage'
    bootdir = payload / 'kernel_dtb'
    bootdir.mkdir(parents=True)

    # Use the same mapping API as the current WIC plugin, including subdirs.
    for src, dst in get_boot_files(str(deploy), d.getVar('IMAGE_BOOT_FILES')):
        relative = pathlib.PurePosixPath(dst)
        if relative.is_absolute() or '..' in relative.parts:
            bb.fatal('Invalid factory boot destination: %s' % dst)
        target = bootdir / dst
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(deploy / src, target)
        target.chmod(0o644)

    rootfs = '%s-%s.rootfs.ext4' % (d.getVar('MYIR_FACTORY_ROOTFS_IMAGE'), d.getVar('MACHINE'))
    for src, name in ((deploy / 'imx-boot', 'imx-boot'),
                      (deploy / rootfs, 'rootfs-full.ext4')):
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
    (sbindir / 'myir-burn-emmc').write_text(script)
    (sbindir / 'myir-burn-emmc').chmod(0o755)

    unitdir = dest / d.getVar('systemd_system_unitdir').lstrip('/')
    unitdir.mkdir(parents=True, exist_ok=True)
    unit = (source / 'fac-burn-emmc.service').read_text().replace('@SBINDIR@', d.getVar('sbindir'))
    (unitdir / 'fac-burn-emmc.service').write_text(unit)
}

SYSTEMD_SERVICE:${PN} = "fac-burn-emmc.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
FILES:${PN} += "${ROOT_HOME}/mfgimage"

RDEPENDS:${PN} = " \
    bash coreutils diffutils findutils grep mmc-utils dosfstools \
    e2fsprogs-e2fsck e2fsprogs-resize2fs e2fsprogs-tune2fs \
    util-linux-blockdev util-linux-findmnt util-linux-flock util-linux-lsblk \
    util-linux-mount util-linux-partx util-linux-sfdisk util-linux-umount \
    util-linux-wipefs udev \
"
