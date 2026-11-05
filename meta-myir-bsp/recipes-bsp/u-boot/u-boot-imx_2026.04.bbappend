FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-imx:"
SRC_URI:append:myd-jmx95x-b0 = " \
    file://0001-efi-capsule-key-generation-dependency.patch \
    file://0002-myd-jmx95-link-current-nxp-board-helpers.patch \
    file://myd-jmx95-yocto.cfg \
"
