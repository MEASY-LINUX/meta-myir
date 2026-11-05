SUMMARY = "MYD-JMX95 eMMC U-Boot environment layout"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
SRC_URI = "file://fw_env.config"
S = "${UNPACKDIR}"
RDEPENDS:${PN} = "libubootenv-bin"
CONFFILES:${PN} = "${sysconfdir}/fw_env.config"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/fw_env.config ${D}${sysconfdir}/fw_env.config
}
