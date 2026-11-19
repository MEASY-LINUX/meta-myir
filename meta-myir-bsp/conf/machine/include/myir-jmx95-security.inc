# MYD-JMX95X security image configuration for the wrynose/6.18 SDK.
# Reuse the provisioned signing keys in ${TOPDIR}/keys. Key generation and
# replacement are intentionally outside the image build.
# Recipes select common JMX95 behavior through this internal family override,
# independently of whether a machine is named myd-jmx95, myd-jmx95x-b0, etc.
MACHINEOVERRIDES =. "myir-jmx95:"

MYIR_SECURITY_KEY_DIR ?= "${TOPDIR}/keys"

ENABLE_DM_VERITY ?= "0"
# The JMX95 dm-verity profile is a complete security mode: writable overlay,
# signed root hash and signed FIT are mandatory and cannot drift independently.
ENABLE_OVERLAYFS = "${ENABLE_DM_VERITY}"
ENABLE_ROOTHASH_SIGN = "${ENABLE_DM_VERITY}"
MYIR_FITIMAGE_BOOT = "${ENABLE_DM_VERITY}"
ENABLE_FITIMAGE_SIGN = "${ENABLE_DM_VERITY}"

# Basic AHAB mode remains compatible with the 6.12 DISTRO_FEATURES switch.
# MYIR_SECURE_BOOT is an equivalent explicit switch for new build configs.
MYIR_SECURE_BOOT ?= "0"
DISTRO_FEATURES:append = "${@' sec_boot' if d.getVar('MYIR_SECURE_BOOT') == '1' or d.getVar('ENABLE_DM_VERITY') == '1' else ''}"
MYIR_AHAB_ENABLE = "${@'1' if d.getVar('MYIR_SECURE_BOOT') == '1' or d.getVar('ENABLE_DM_VERITY') == '1' or bb.utils.contains('DISTRO_FEATURES', 'sec_boot', True, False, d) else '0'}"

MYIR_ROOT_PASSWORD_HASH_FILE ?= "${MEASY_SECURITY_LAYERDIR}/files/keys/myir-jmx95-root-password.hash"
MYIR_SECURE_LOGIN_CLASS = "${@' myir-secure-login' if d.getVar('MYIR_AHAB_ENABLE') == '1' else ''}"
IMAGE_CLASSES:append:pn-myir-image-emmc = "${MYIR_SECURE_LOGIN_CLASS}"
IMAGE_CLASSES:append:pn-myir-image-burn = "${MYIR_SECURE_LOGIN_CLASS}"

# A/B is an OTA property, not a secure-boot property.  AHAB and dm-verity
# images remain single-slot unless the external OTA layer explicitly opts in.
DUAL_ROOTFS = "${@'dual' if d.getVar('OTA_SUPPORT') == '1' else ''}"
SIGNED_BOOT = "${@bb.utils.contains('MYIR_AHAB_ENABLE', '1', 'signed', '', d)}"

# i.MX95 AHAB uses ECC secp384r1 and SHA-384.
MYIR_AHAB_CST_DIR ?= "${MYIR_SECURITY_KEY_DIR}/cst"
MYIR_AHAB_CST_CRYPTO ?= "ecc"
MYIR_AHAB_CST_KEY_CURVE ?= "secp384r1"
MYIR_AHAB_CST_DIG_ALGO ?= "sha384"
MYIR_AHAB_CST_SRK_CA ?= "0"
MYIR_AHAB_CST_SRK_INDEX ?= "1"
MYIR_AHAB_CST_SRK ?= "${MYIR_AHAB_CST_DIR}/crts/SRK_1_2_3_4_tables.bin"
MYIR_AHAB_CST_SRK_FUSE ?= "${MYIR_AHAB_CST_DIR}/crts/SRK_1_2_3_4_fuses.bin"

SECURE_BOOT_FILES = " \
    os_cntr_signed.bin \
    os_cntr_signed_root.bin \
    os_cntr_signed_4k-lt9611-lvds1-ov5640.bin \
    os_cntr_signed_dsi-mipi101c.bin \
    os_cntr_signed_lt9611-lvds1-ov5640.bin \
    os_cntr_signed_lvds0-lvds1-ov5640.bin \
    os_cntr_signed_lvds-dual.bin \
"

IMAGE_BOOT_FILES:append = " \
    ${@bb.utils.contains('MYIR_AHAB_ENABLE', '1', bb.utils.contains('MYIR_FITIMAGE_BOOT', '1', 'fitImage', '${SECURE_BOOT_FILES}', d), '', d)} \
"
IMAGE_BOOT_FILES:remove = " \
    ${@bb.utils.contains('MYIR_AHAB_ENABLE', '1', '${KERNEL_IMAGETYPE}', '', d)} \
"

DM_VERITY_IMAGE ?= "${@bb.utils.contains('ENABLE_DM_VERITY', '1', 'myir-image-emmc', '', d)}"
DM_VERITY_IMAGE_TYPE ?= "ext4"
DM_VERITY_SEPARATE_HASH ?= "0"
MYIR_DM_VERITY_OVERLAY ?= "${ENABLE_OVERLAYFS}"
MYIR_OVERLAY_PARTITION_SIZE ?= "2048"
MYIR_DM_VERITY_SIGN_ROOTHASH ?= "${ENABLE_ROOTHASH_SIGN}"
MYIR_DM_VERITY_PRIVATE_KEY ?= "${MYIR_SECURITY_KEY_DIR}/fit/dev.key"
MYIR_DM_VERITY_CERTIFICATE ?= "${MYIR_SECURITY_KEY_DIR}/fit/dev.crt"
MYIR_FIT_HASH_ALG ?= "sha256"
MYIR_FIT_SIGN_ALG ?= "rsa2048"

# In wrynose, FIT assembly is a separate recipe. The kernel only deploys the
# extra artifacts consumed by linux-yocto-fitimage; it remains an Image build.
KERNEL_CLASSES:append = "${@bb.utils.contains('MYIR_FITIMAGE_BOOT', '1', ' kernel-fit-extra-artifacts', '', d)}"
INITRAMFS_IMAGE = "${@bb.utils.contains('ENABLE_DM_VERITY', '1', 'myir-initramfs-dmverity', '', d)}"
INITRAMFS_IMAGE_BUNDLE = "${@bb.utils.contains('ENABLE_DM_VERITY', '1', '0', '', d)}"

# FIT signing and public-key injection into U-Boot.
UBOOT_SIGN_ENABLE = "${ENABLE_FITIMAGE_SIGN}"
UBOOT_SIGN_ENABLE:pn-imx-boot = "0"
FIT_KERNEL_SIGN_ENABLE = "${ENABLE_FITIMAGE_SIGN}"
FIT_GENERATE_KEYS = "0"
UBOOT_SIGN_KEYDIR = "${MYIR_SECURITY_KEY_DIR}/fit"
UBOOT_SIGN_KEYNAME = "dev"
UBOOT_SIGN_IMG_KEYNAME = "dev2"
FIT_HASH_ALG = "${MYIR_FIT_HASH_ALG}"
FIT_SIGN_ALG = "${MYIR_FIT_SIGN_ALG}"
FIT_CONF_DEFAULT_DTB ?= "${KERNEL_DEVICETREE_BASENAME}.dtb"

INHERIT += "${@bb.utils.contains('ENABLE_DM_VERITY', '1', 'myir-boot-signed myir-dmverity', bb.utils.contains('MYIR_AHAB_ENABLE', '1', 'myir-boot-signed', '', d), d)}"

# Preserve the load address used by the validated 6.12 full-chain image.
UBOOT_ENTRYPOINT:mx95-generic-bsp = "${@bb.utils.contains('MYIR_FITIMAGE_BOOT', '1', '0x94000000', '0x20008000', d)}"

# The development full-chain profile uses real eMMC RPMB with the OP-TEE test
# key. Production builds should override MYIR_RPMB_MODE to "prod" only after
# provisioning the device-specific RPMB key.
MYIR_OPTEE_STORAGE ?= "${@bb.utils.contains('ENABLE_DM_VERITY', '1', 'rpmb', 'ree', d)}"
MYIR_RPMB_MODE ?= "${@bb.utils.contains('ENABLE_DM_VERITY', '1', 'test', 'emu', d)}"
