# Disabled security interface for the initial wrynose port.
# Signing classes, initramfs and WIC integration require separate validation.
MYIR_SECURE_BOOT ?= "0"
MYIR_AHAB_ENABLE ?= "${MYIR_SECURE_BOOT}"
MYIR_AHAB_CST_DIR ?= "${TOPDIR}/keys/cst"
MYIR_AHAB_CST_SRK_INDEX ?= "1"
MYIR_AHAB_KEY_TYPE ?= "ecc"
MYIR_AHAB_KEY_CURVE ?= "secp384r1"
MYIR_AHAB_DIGEST ?= "sha384"
MYIR_AHAB_SRK_TABLE ?= "${MYIR_AHAB_CST_DIR}/crts/SRK_1_2_3_4_tables.bin"
MYIR_AHAB_SRK_FUSE ?= "${MYIR_AHAB_CST_DIR}/crts/SRK_1_2_3_4_fuses.bin"
DUAL_ROOTFS ?= ""
SIGNED_BOOT ?= ""
SECURE_BOOT_FILES ?= " \
    os_cntr_signed.bin \
    os_cntr_signed_4k-lt9611-lvds1-ov5640.bin \
    os_cntr_signed_dsi-mipi101c.bin \
    os_cntr_signed_lt9611-lvds1-ov5640.bin \
    os_cntr_signed_lvds0-lvds1-ov5640.bin \
    os_cntr_signed_lvds-dual.bin \
"

ENABLE_DM_VERITY ?= "0"
ENABLE_OVERLAYFS ?= "0"
ENABLE_ROOTHASH_SIGN ?= "0"
MYIR_FITIMAGE_BOOT ?= "0"
ENABLE_FITIMAGE_SIGN ?= "0"
UBOOT_SIGN_ENABLE ?= "0"
FIT_GENERATE_KEYS ?= "0"

DM_VERITY_IMAGE ?= "${@bb.utils.contains('ENABLE_DM_VERITY', '1', 'myir-image-emmc', '', d)}"
DM_VERITY_IMAGE_TYPE ?= "ext4"
DM_VERITY_SEPARATE_HASH ?= "0"
MYIR_DM_VERITY_OVERLAY ?= "${ENABLE_OVERLAYFS}"
MYIR_OVERLAY_PARTITION_SIZE ?= "2048"
MYIR_DM_VERITY_SIGN_ROOTHASH ?= "${ENABLE_ROOTHASH_SIGN}"
MYIR_FIT_KEY_DIR ?= "${TOPDIR}/keys/fit"
MYIR_FIT_HASH_ALG ?= "sha256"
MYIR_FIT_SIGN_ALG ?= "rsa2048"

# Never auto-provision a test RPMB key when enabling another feature.
MYIR_OPTEE_STORAGE ?= ""
MYIR_RPMB_MODE ?= ""

# Do not silently produce an unsigned image when a reserved switch is enabled.
INHERIT += "myir-jmx95-security-policy"
