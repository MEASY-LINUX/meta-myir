SUMMARY = "MYD-JMX95 manual update tools"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"
inherit packagegroup

# Do not use swupdate-tools: it pulls in USB-triggered update services.
# SDK complementary packages supply headers and libswupdate development files.
RDEPENDS:${PN} = " \
    lua json-c \
    swupdate swupdate-www swupdate-progress swupdate-client swupdate-tools-ipc \
"
