# Copyright 2026 MYIR
# SPDX-License-Identifier: MIT

SUMMARY = "MYIR eMMC image for MYD-JMX95X"
DESCRIPTION = "MYIR board validation image based on the current NXP multimedia image"
LICENSE = "MIT"

COMPATIBLE_MACHINE = "(myd-jmx95x-b0)"

# Reuse the current BSP package/API definitions instead of carrying a copy from
# the older Yocto release.
require recipes-fsl/images/imx-image-multimedia.bb

inherit populate_sdk_qt6

CONFLICT_DISTRO_FEATURES = "directfb"

# Match the established MYD-JS8MPQ trimming model: do not install a compiler
# toolchain in the target rootfs. populate_sdk still receives the image package
# set plus dev/dbg/src complementary packages from populate_sdk_base.
IMAGE_FEATURES:remove = "tools-sdk"

# The reference MYIR multimedia image does not install Docker.
CORE_IMAGE_EXTRA_INSTALL:remove = "docker"

# Qt's SDK class adds staticdev-pkgs. MYIR ships shared libraries and their
# development metadata in the SDK, but does not require every static archive.
SDKIMAGE_FEATURES:remove = "staticdev-pkgs"

# The common i.MX SDK also lists the i.MX233/i.MX28-only USB loader.
# It has no role on i.MX95; keep the cross-toolchain, libraries and UUU tools.
TOOLCHAIN_HOST_TASK:remove = "nativesdk-mxsldr"

# GPU development files and offline shader compilers belong in the SDK.
# Runtime packages are selected by packagegroup-fsl-tools-gpu. Leave all
# dependency-required packages, including Qt static archives, installable.
TOOLCHAIN_TARGET_TASK:append = " \
    mali-imx-dev \
    mali-imx-opencl-icd-dev \
    libegl-mesa-dev \
    libgles1-mesa-dev \
    libgles2-mesa-dev \
"

IMAGE_INSTALL_OPENCV = ""
IMAGE_INSTALL_OPENCV:imxgpu = "${IMAGE_INSTALL_OPENCV_PKGS}"
IMAGE_INSTALL_OPENCV_PKGS = " \
    opencv-apps \
    opencv-samples \
    python3-opencv \
"

IMAGE_INSTALL_PARSEC = " \
    packagegroup-security-tpm2 \
    packagegroup-security-parsec \
    swtpm \
    softhsm \
    os-release \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-client optee-os', '', d)} \
"

IMAGE_INSTALL_PKCS11 = "opensc pkcs11-provider"

IMAGE_INSTALL:append = " \
    packagegroup-myir-jmx95 \
    myir-hmi \
    curl \
    tzdata \
    packagegroup-qt6-imx \
    qtvirtualkeyboard \
    qtimageformats \
    ${IMAGE_INSTALL_OPENCV} \
    ${IMAGE_INSTALL_PARSEC} \
    ${IMAGE_INSTALL_PKCS11} \
"

export IMAGE_BASENAME = "myir-image-emmc"

# A single populate_sdk invocation must also produce the matching image.
addtask populate_sdk after do_image_complete
