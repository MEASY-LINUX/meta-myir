# Keep runtime Quick3D, not example payloads or explicit development packages.
QT6_IMAGE_INSTALL_QUICK3D:myd-jmx95x-b0 = "qtquick3d"
# The reference MYIR group ships its own HMI, not the NXP GoPoint demo suite.
RDEPENDS:${PN}:remove:myd-jmx95x-b0 = "packagegroup-imx-gopoint"
