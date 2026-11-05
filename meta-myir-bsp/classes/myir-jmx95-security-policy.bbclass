# Remove this initial-port guard only with a tested signing implementation.
python () {
    switches = (
        'MYIR_SECURE_BOOT', 'MYIR_AHAB_ENABLE', 'ENABLE_DM_VERITY',
        'ENABLE_OVERLAYFS', 'ENABLE_ROOTHASH_SIGN', 'MYIR_FITIMAGE_BOOT',
        'ENABLE_FITIMAGE_SIGN', 'UBOOT_SIGN_ENABLE', 'FIT_GENERATE_KEYS',
    )
    invalid = [name for name in switches if d.getVar(name) != '0']
    invalid += [name for name in ('MYIR_OPTEE_STORAGE', 'MYIR_RPMB_MODE',
                                 'DUAL_ROOTFS', 'SIGNED_BOOT') if d.getVar(name)]
    if {'sec_boot', 'secureboot'} & set((d.getVar('DISTRO_FEATURES') or '').split()):
        invalid.append('DISTRO_FEATURES security flag')
    if invalid:
        bb.fatal('MYD-JMX95 initial port reserves but does not yet implement these '
                 'security settings: %s. Keep them disabled until the signing, '
                 'initramfs and WIC paths are ported and validated.' % ', '.join(invalid))
}
