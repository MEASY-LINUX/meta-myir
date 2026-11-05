SUMMARY = "PPP configuration for Quectel EC20"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=012014e923710289dc42fed1dfa3a267"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS:${PN} = "ppp"
SRC_URI = "file://LICENSE file://quectel-dial file://quectel-chat-connect file://quectel-chat-disconnect"
S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/ppp/chatscripts ${D}${sysconfdir}/ppp/peers
    install -m 0644 ${S}/quectel-dial ${D}${sysconfdir}/ppp/peers/
    install -m 0644 ${S}/quectel-chat-connect ${S}/quectel-chat-disconnect ${D}${sysconfdir}/ppp/chatscripts/
}
