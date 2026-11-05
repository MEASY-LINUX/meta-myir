SUMMARY = "MYIR audio test script and sample media"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=a3ac5472be79591e880a452856ca24d1"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
FILESEXTRAPATHS:prepend := "${THISDIR}/files/myd-lmx9x-11x11:"
SRC_URI = " \
    file://etc/myir_test/myir_audio_play;subdir=${BP} \
    file://usr/share/myir/song.mp3;subdir=${BP} \
    file://usr/share/myir/song.wav;subdir=${BP} \
    file://licenses/GPL-2;subdir=${BP} \
"
S = "${UNPACKDIR}/${BP}"
RDEPENDS:${PN} = "gstreamer1.0-plugins-base-apps"

do_install() {
    install -d ${D}${datadir}/myir/Music ${D}${sysconfdir}/myir_test
    install -m 0755 ${S}/etc/myir_test/myir_audio_play ${D}${sysconfdir}/myir_test/
    install -m 0644 ${S}/usr/share/myir/song.mp3 ${S}/usr/share/myir/song.wav ${D}${datadir}/myir/Music/
}
FILES:${PN} += "${datadir}/myir/Music"
