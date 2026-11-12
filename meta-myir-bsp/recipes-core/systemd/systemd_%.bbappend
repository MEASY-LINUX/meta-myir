FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-units-systemd-udevd-Set-PrivateMounts-to-no.patch"

SRC_URI:append:myd-jmx95x-b0 = " \
    file://10-no-osc.conf \
    file://81-serial-term.sh \
"

PACKAGECONFIG:remove = "osc-context"

do_install:append:myd-jmx95x-b0() {
    install -Dm 0644 ${UNPACKDIR}/10-no-osc.conf \
        ${D}${sysconfdir}/systemd/system/serial-getty@ttyLP0.service.d/10-no-osc.conf
    install -Dm 0644 ${UNPACKDIR}/81-serial-term.sh \
        ${D}${sysconfdir}/profile.d/81-serial-term.sh
}
