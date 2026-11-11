SUMMARY = "MYIR board identification"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit  systemd

MYIR_SWUPDATE_SCRIPT = "myir-swupdate.sh"
# Do not install the legacy updater with a hard-coded server and old SWU name.
SRC_URI = "file://hwrevision;subdir=${BP} \
           file://licenses/GPL-2;subdir=${BP} \
           file://myir-swupdate.service;subdir=${BP} \
           file://${MYIR_SWUPDATE_SCRIPT};subdir=${BP} \
           file://board_part_info.conf;subdir=${BP} \
           file://sw-versions;subdir=${BP} \
          "
S = "${UNPACKDIR}/${BP}"

HW_MAJOR ?= "1"
HW_MINOR ?= "0"

do_install() {
	install -d ${D}${systemd_system_unitdir}
    install -d ${D}${sysconfdir}
	install -m 644 ${S}/myir-swupdate.service ${D}${systemd_system_unitdir}/myir-swupdate.service
	install -m 755 ${S}/${MYIR_SWUPDATE_SCRIPT} ${D}${sysconfdir}/myir-swupdate.sh
	install -m 644 ${S}/sw-versions ${D}${sysconfdir}/sw-versions
	install -m 644 ${S}/hwrevision ${D}${sysconfdir}/hwrevision
    echo "${MACHINE} ${HW_MAJOR}.${HW_MINOR}" > ${D}${sysconfdir}/hwrevision
	install -m 644 ${S}/board_part_info.conf ${D}${sysconfdir}/board_part_info.conf
}


SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "myir-swupdate.service"
SYSTEMD_AUTO_ENABLE = "enable"
