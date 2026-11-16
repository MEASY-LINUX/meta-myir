# MYD-JMX95X SWUpdate configuration, ported from the MYD-JS8MPQ reference layer.
# A full defconfig replaces the upstream one; signature/encryption are enabled.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://defconfig"
SRC_URI += "file://swu_public.pem"
SRC_URI += "file://swupdate.cfg"

# CONFIG_SYSTEMD=y requires libsystemd in recipe-sysroot; gpgme is kept for
# parity with the reference crypto setup.
DEPENDS:append = " systemd gpgme"

# swupdate.inc derives DEPENDS from defconfig in anonymous python; register it
# as a parse input so edits invalidate the cache.
python () {
    bb.parse.mark_dependency(d, bb.fetch2.localpath('file://defconfig', d))
}

do_install:append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${UNPACKDIR}/swupdate.cfg ${D}${sysconfdir}/swupdate.cfg
}

FILES:${PN} += "${sysconfdir}/swupdate.cfg"
