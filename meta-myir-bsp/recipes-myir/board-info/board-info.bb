SUMMARY = "MYIR board identification"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Do not install the legacy updater with a hard-coded server and old SWU name.
HW_MAJOR ?= "1"
HW_MINOR ?= "0"

do_install() {
    install -d ${D}${sysconfdir}
    echo "${MACHINE} ${HW_MAJOR}.${HW_MINOR}" > ${D}${sysconfdir}/hwrevision
}
