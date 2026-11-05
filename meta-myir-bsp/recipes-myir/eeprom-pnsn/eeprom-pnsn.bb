SUMMARY = "MYIR PN/SN discovery service"
# Preserve the reference recipe's license declaration for the supplied assets.
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
SRC_URI = "file://eeprom-pnsn.service file://libmyir_code.so file://MEasyListen-DEV"
S = "${UNPACKDIR}"
inherit systemd

do_install() {
    install -d ${D}${systemd_system_unitdir} ${D}${bindir} ${D}${libdir}
    install -m 0644 ${S}/eeprom-pnsn.service ${D}${systemd_system_unitdir}/
    install -m 0755 ${S}/MEasyListen-DEV ${D}${bindir}/
    install -m 0644 ${S}/libmyir_code.so ${D}${libdir}/
}

SYSTEMD_SERVICE:${PN} = "eeprom-pnsn.service"
SYSTEMD_AUTO_ENABLE = "enable"
# The supplied ELF has an unversioned SONAME; it is a runtime library.
FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/libmyir_code.so"
INSANE_SKIP:${PN} += "already-stripped"
