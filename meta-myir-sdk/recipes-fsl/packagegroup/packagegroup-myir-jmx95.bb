SUMMARY = "MYD-JMX95 runtime applications and board utilities"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
inherit packagegroup

# Development packages are supplied by SDK dev-pkgs, not installed in rootfs.
RDEPENDS:${PN} = " \
    packagegroup-imx-core-tools \
    v4l-utils tcpdump libubootenv-bin uboot-env u-boot-imx-env \
    tslib tslib-calibrate tslib-conf tslib-uinput tslib-tests \
    iperf3 alsa-utils serialcheck libdrm-tests sqlite3 hostapd \
    ppp ppp-quectel quectel-cm \
    libmodbus bridge-utils net-tools vim \
    clinfo optee-examples python3-cryptography \
    autorun-script auto-wifi nxp-bt-init myir-regulatory \
    myir-test-function myir-lvgl myir-tools board-info eeprom-pnsn \
    kernel-module-nxp-wlan firmware-nxp-wifi-nxpaw693-sdio \
"
