SUMMARY = "Tools for the capture and replay of Vulkan API calls"
SECTION = "graphics"
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d2ddcd9b5c3b713fcf90c3223f6b10bd \
                    file://external/SPIRV-Reflect/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"
DEPENDS = "lz4 nlohmann-json spirv-headers vulkan-headers zstd"

SRCREV = "df6c07abdf95f9b37245dcf08b81fb4fdecbf3cb"
SPIRV_REFLECT_SRCREV = "62ca825c692334ed758027b908c5aafbbc76243f"

SRC_URI = " \
    https://codeload.github.com/LunarG/gfxreconstruct/tar.gz/${SRCREV};downloadfilename=gfxreconstruct-${SRCREV}.tar.gz;name=gfxreconstruct \
    https://codeload.github.com/KhronosGroup/SPIRV-Reflect/tar.gz/${SPIRV_REFLECT_SRCREV};downloadfilename=SPIRV-Reflect-${SPIRV_REFLECT_SRCREV}.tar.gz;name=spirv-reflect \
    file://0001-FindVulkanVersion.cmake-Look-for-vulkan-headers-in-s.patch \
    file://0002-Change-gfxreconstruct-layer-to-implicit-layer.patch \
"
SRC_URI[gfxreconstruct.sha256sum] = "11f21b5a62cba6c9bbeb118ca5b6792347298ff83f85974c7c472417f211c694"
SRC_URI[spirv-reflect.sha256sum] = "79f704c9e2b2a79e45aad29401453239551725a05fec630088d8a7729f95633a"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/gfxreconstruct-${SRCREV}"

python do_unpack:append() {
    import os
    import shutil

    spirv_reflect_dir = os.path.join(d.getVar("S"), "external", "SPIRV-Reflect")
    os.rmdir(spirv_reflect_dir)
    shutil.move(
        os.path.join(
            d.getVar("UNPACKDIR"),
            "SPIRV-Reflect-" + d.getVar("SPIRV_REFLECT_SRCREV"),
        ),
        spirv_reflect_dir,
    )
}

inherit cmake features_check

ANY_OF_DISTRO_FEATURES = "wayland x11"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland x11', d)}"

PACKAGECONFIG[wayland] = "-DBUILD_WSI_WAYLAND_SUPPORT=ON,-DBUILD_WSI_WAYLAND_SUPPORT=OFF,wayland-native wayland wayland-protocols"
PACKAGECONFIG[x11] = "-DBUILD_WSI_XCB_SUPPORT=ON,-DBUILD_WSI_XCB_SUPPORT=OFF,libxcb libx11 xcb-util-keysyms"

EXTRA_OECMAKE += "\
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_SYSROOT=${PKG_CONFIG_SYSROOT_DIR} \
    -DGFXRECON_ENABLE_OPENXR=OFF \
"

export SDKTARGETSYSROOT = "${STAGING_DIR_HOST}"

CXXFLAGS:append:class-target:arm = " -Wno-error=stringop-overflow"

# The lib is unversioned
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${datadir}/vulkan"

INSANE_SKIP:${PN} += "buildpaths"
