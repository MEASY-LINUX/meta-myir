SUMMARY = "MYIR startup script"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=893e842b220e4ff976ebe9a212f5e51d"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
SRC_URI = "file://autorun.service file://autorun.sh file://licenses/GPL-2"
S = "${UNPACKDIR}"
inherit systemd

do_install() {
    install -d ${D}${systemd_system_unitdir} ${D}${bindir}
    install -m 0644 ${S}/autorun.service ${D}${systemd_system_unitdir}/
    install -m 0755 ${S}/autorun.sh ${D}${bindir}/
}

SYSTEMD_SERVICE:${PN} = "autorun.service"
SYSTEMD_AUTO_ENABLE = "enable"
