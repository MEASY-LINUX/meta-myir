DESCRIPTION = "i.MX U-Boot suppporting i.MX reference boards."

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"

UBOOT_SRC ?= "git:///home/myir/bsp/6.18/u-boot-imx;protocol=file"
SRCBRANCH = "lf_v2026.04"
LOCALVERSION ?= "-${SRCBRANCH}"
SRCREV = "a1fdcaf045110146197b7936807815dfd114cbe0"
BB_GIT_SHALLOW = "1"
BB_GIT_SHALLOW_DEPTH = "1"

DEPENDS += "\
    bc-native \
    bison-native \
    dtc-native \
    flex-native \
    gnutls-native \
    python3-setuptools-native \
    xxd-native \
"

B = "${WORKDIR}/build"

inherit fsl-u-boot-localversion pkgconfig

BOOT_TOOLS = "imx-boot-tools"
