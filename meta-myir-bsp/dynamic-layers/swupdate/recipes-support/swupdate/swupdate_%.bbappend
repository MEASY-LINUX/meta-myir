# Use the current meta-swupdate recipe and merge only the board requirements.
FILESEXTRAPATHS:prepend:myd-jmx95x-b0 := "${THISDIR}/files:"
SRC_URI:append:myd-jmx95x-b0 = " file://myir-jmx95.cfg file://99-myir-manual"

# Upstream reads these files in anonymous Python to derive DEPENDS. Track them
# as parse inputs as well as fetch inputs, so fragment edits invalidate cache.
python () {
    for config in [bb.fetch2.localpath('file://defconfig', d)] + find_cfgs(d):
        bb.parse.mark_dependency(d, config)
}

# Installation is not authorization to update a board or expose its web API.
SYSTEMD_AUTO_ENABLE:${PN}:myd-jmx95x-b0 = "disable"
SYSTEMD_AUTO_ENABLE:${PN}-progress:myd-jmx95x-b0 = "disable"
SYSTEMD_AUTO_ENABLE:${PN}-usb:myd-jmx95x-b0 = "disable"

do_install:append:myd-jmx95x-b0() {
    install -m 0644 ${UNPACKDIR}/99-myir-manual ${D}${libdir}/swupdate/conf.d/
}
