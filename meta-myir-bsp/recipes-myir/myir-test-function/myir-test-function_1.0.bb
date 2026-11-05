SUMMARY = "MYIR UART and watchdog test applications"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
SRC_URI = "file://uart_test.c;subdir=${BP} file://watchdog.c;subdir=${BP} file://Makefile;subdir=${BP}"
S = "${UNPACKDIR}/${BP}"

do_compile() {
    oe_runmake uart_test watchdog_test
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/uart_test ${S}/watchdog_test ${D}${bindir}/
}
