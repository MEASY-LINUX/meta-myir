SUMMARY = "MYD-JMX95 SD factory image for explicit eMMC provisioning"
LICENSE = "MIT"
COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"

inherit core-image

IMAGE_FEATURES += "ssh-server-openssh allow-empty-password allow-root-login empty-root-password"
IMAGE_INSTALL += "myir-fac-burn board-info"
IMAGE_INSTALL:remove = "udev-extraconf"

# This SD image keeps the machine's ordinary boot + rootfs WIC layout.
# Only the explicitly invoked factory tool partitions the eMMC target.
export IMAGE_BASENAME = "myir-image-burn"
