# Use the current meta-swupdate recipe and merge only the board requirements.
FILESEXTRAPATHS:prepend:myd-jmx95x-b0 := "${THISDIR}/files:"
SRC_URI:append:myd-jmx95x-b0 = " file://myir-jmx95.cfg file://99-myir-manual"

# Installation is not authorization to update a board or expose its web API.
SYSTEMD_AUTO_ENABLE:${PN}:myd-jmx95x-b0 = "disable"
SYSTEMD_AUTO_ENABLE:${PN}-progress:myd-jmx95x-b0 = "disable"
SYSTEMD_AUTO_ENABLE:${PN}-usb:myd-jmx95x-b0 = "disable"

do_install:append:myd-jmx95x-b0() {
    install -m 0644 ${UNPACKDIR}/99-myir-manual ${D}${libdir}/swupdate/conf.d/
}
