SUMMARY = "Tools for tracing OpenGL, Direct3D, and other graphics APIs"
HOMEPAGE = "http://apitrace.github.io/"
SECTION = "console/tools"
LICENSE = "MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aeb969185a143c3c25130bc2c3ef9a50 \
                    file://thirdparty/snappy/COPYING;md5=f62f3080324a97b3159a7a7e61812d0c"

DEPENDS = "zlib libpng procps python3-native"

SRCREV = "de5e6b93dac8e81d339d6afa9b27c693a55a8147"
GLTRIM_TESTS_SRCREV = "12ceee777636bfca4a35390697e8e702254a82b2"
BROTLI_SRCREV = "9801a2c5d6c67c467ffad676ac301379bb877fc3"
DIRECTXMATH_SRCREV = "e95d84892ec894967c48151f9106bd4388e85e63"
GTEST_SRCREV = "58d77fa8070e8cec2dc1ed015d66b454c8d78850"
LIBBACKTRACE_SRCREV = "8602fda64e78f1f46563220f2ee9f7e70819c51d"
LIBPNG_SRCREV = "0a158f3506502dfa23edfc42790dfaed82efba17"
SNAPPY_SRCREV = "2b63814b15a2aaae54b7943f0cd935892fae628f"
ZLIB_SRCREV = "21767c654d31d2dccdde4330529775c6c5fd5389"
BENCHMARK_SRCREV = "bf585a2789e30585b4e3ce6baf11ef2750b54677"
SNAPPY_GTEST_SRCREV = "18f8200e3079b0e54fa00cb7ac55d4c39dcf6da6"

SRC_URI = " \
    https://codeload.github.com/nxp-imx/apitrace-imx/tar.gz/${SRCREV};downloadfilename=apitrace-imx-${SRCREV}.tar.gz;name=apitrace \
    https://codeload.github.com/apitrace/gltrim-tests/tar.gz/${GLTRIM_TESTS_SRCREV};downloadfilename=gltrim-tests-${GLTRIM_TESTS_SRCREV}.tar.gz;name=tests \
    https://codeload.github.com/google/brotli/tar.gz/${BROTLI_SRCREV};downloadfilename=brotli-${BROTLI_SRCREV}.tar.gz;name=brotli \
    https://codeload.github.com/microsoft/DirectXMath/tar.gz/${DIRECTXMATH_SRCREV};downloadfilename=DirectXMath-${DIRECTXMATH_SRCREV}.tar.gz;name=directxmath \
    https://codeload.github.com/google/googletest/tar.gz/${GTEST_SRCREV};downloadfilename=googletest-${GTEST_SRCREV}.tar.gz;name=gtest \
    https://codeload.github.com/ianlancetaylor/libbacktrace/tar.gz/${LIBBACKTRACE_SRCREV};downloadfilename=libbacktrace-${LIBBACKTRACE_SRCREV}.tar.gz;name=libbacktrace \
    https://codeload.github.com/apitrace/libpng/tar.gz/${LIBPNG_SRCREV};downloadfilename=libpng-${LIBPNG_SRCREV}.tar.gz;name=libpng \
    https://codeload.github.com/google/snappy/tar.gz/${SNAPPY_SRCREV};downloadfilename=snappy-${SNAPPY_SRCREV}.tar.gz;name=snappy \
    https://codeload.github.com/madler/zlib/tar.gz/${ZLIB_SRCREV};downloadfilename=zlib-${ZLIB_SRCREV}.tar.gz;name=zlib \
    https://codeload.github.com/google/benchmark/tar.gz/${BENCHMARK_SRCREV};downloadfilename=benchmark-${BENCHMARK_SRCREV}.tar.gz;name=benchmark \
    https://codeload.github.com/google/googletest/tar.gz/${SNAPPY_GTEST_SRCREV};downloadfilename=googletest-${SNAPPY_GTEST_SRCREV}.tar.gz;name=snappy-gtest \
"
SRC_URI[apitrace.sha256sum] = "d2e0ba36f060aced8dadc83f94aa173493bb8e7eae4369ce409c849992833625"
SRC_URI[tests.sha256sum] = "f6e14f45b7c4b74e997b13524be4b74b7b7875cd651fa94c3d15336580a80e13"
SRC_URI[brotli.sha256sum] = "5761da8724ba9709a61f0697522419772d3a6c6236a30f5c8b62cdb60f3a7416"
SRC_URI[directxmath.sha256sum] = "fc55a1f5bd2507f59baabb2e6ce5deb959157d18fdeb82364b61c23840b0c27d"
SRC_URI[gtest.sha256sum] = "c6ab3b6b33f51ef7465921f8f8c10c15d7cbc510761a15a18ad85babf6d73278"
SRC_URI[libbacktrace.sha256sum] = "201bb83fe8fa09a271c4abc9f0c174effb0c59d0de7c84153847008535d5bedb"
SRC_URI[libpng.sha256sum] = "f25814725ff41419a489ed9634f4b92d75891da68ccb541b68ec9a78963b444f"
SRC_URI[snappy.sha256sum] = "d400d896e5d2a7baed561c2b276cf1a23cc2af90a71df3cde4bef8d1db6658e6"
SRC_URI[zlib.sha256sum] = "ef47b0fbe646d69a2fc5ba012cb278de8e8946a8e9649f83a807cc05559f0eff"
SRC_URI[benchmark.sha256sum] = "8222ebe7d9049c4b8bb3f930e3ca08e232f5587ad4121619f73f6ce374391c70"
SRC_URI[snappy-gtest.sha256sum] = "d518c1c146c5a87cdedbee8535a396f95d91e8b39baa539fd4c50bd62148d199"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/apitrace-imx-${SRCREV}"

python do_unpack:append() {
    import os
    import shutil

    def replace_source(unpacked_name, *destination_parts):
        destination = os.path.join(d.getVar("S"), *destination_parts)
        os.rmdir(destination)
        shutil.move(os.path.join(d.getVar("UNPACKDIR"), unpacked_name), destination)

    replace_source(
        "gltrim-tests-" + d.getVar("GLTRIM_TESTS_SRCREV"),
        "frametrim", "tests",
    )
    replace_source(
        "brotli-" + d.getVar("BROTLI_SRCREV"),
        "thirdparty", "brotli",
    )
    replace_source(
        "DirectXMath-" + d.getVar("DIRECTXMATH_SRCREV"),
        "thirdparty", "directxmath",
    )
    replace_source(
        "googletest-" + d.getVar("GTEST_SRCREV"),
        "thirdparty", "gtest",
    )
    replace_source(
        "libbacktrace-" + d.getVar("LIBBACKTRACE_SRCREV"),
        "thirdparty", "libbacktrace",
    )
    replace_source(
        "libpng-" + d.getVar("LIBPNG_SRCREV"),
        "thirdparty", "libpng",
    )
    replace_source(
        "snappy-" + d.getVar("SNAPPY_SRCREV"),
        "thirdparty", "snappy",
    )
    replace_source(
        "zlib-" + d.getVar("ZLIB_SRCREV"),
        "thirdparty", "zlib",
    )
    replace_source(
        "benchmark-" + d.getVar("BENCHMARK_SRCREV"),
        "thirdparty", "snappy", "third_party", "benchmark",
    )
    replace_source(
        "googletest-" + d.getVar("SNAPPY_GTEST_SRCREV"),
        "thirdparty", "snappy", "third_party", "googletest",
    )
}

inherit cmake pkgconfig

PACKAGECONFIG ??= " \
    egl \
    ${PACKAGECONFIG_BACKEND} \
    ${PACKAGECONFIG_GPU2D} \
"

PACKAGECONFIG_BACKEND:mx6-nxp-bsp = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'waffle', \
       bb.utils.contains('DISTRO_FEATURES',     'x11',    'x11', \
                                                             '', d), d)} \
"
PACKAGECONFIG_BACKEND:mx7-nxp-bsp = "${PACKAGECONFIG_BACKEND:mx6-nxp-bsp}"
PACKAGECONFIG_BACKEND:mx8-nxp-bsp = "waffle"
PACKAGECONFIG_BACKEND:mx9-nxp-bsp = "waffle"

PACKAGECONFIG_GPU2D             = ""
PACKAGECONFIG_GPU2D:imxgpu2d    = "vivante"
PACKAGECONFIG_GPU2D:mx9-nxp-bsp = ""

PACKAGECONFIG[egl] = "-DENABLE_EGL=ON,-DENABLE_EGL=OFF,virtual/egl"
PACKAGECONFIG[gui] = "-DENABLE_GUI=ON,-DENABLE_GUI=OFF"
PACKAGECONFIG[multiarch] = "-DENABLE_MULTIARCH=ON,-DENABLE_MULTIARCH=OFF"
PACKAGECONFIG[waffle] = "-DENABLE_WAFFLE=ON,-DENABLE_WAFFLE=OFF,waffle"
PACKAGECONFIG[x11] = "-DENABLE_X11=ON,-DENABLE_X11=OFF"
PACKAGECONFIG[vivante] = "-DENABLE_VIVANTE=ON,-DENABLE_VIVANTE=OFF,virtual/libg2d"

EXTRA_OECMAKE += "\
    -DENABLE_STATIC_LIBGCC=OFF \
    -DENABLE_STATIC_LIBSTDCXX=OFF \
    -DPython3_ROOT_DIR=/usr/bin/python3-native \
"

# Use the bundled snappy library
EXTRA_OECMAKE += "\
    -DENABLE_STATIC_SNAPPY=ON \
"

SECURITY_CFLAGS:toolchain-clang = ""

PACKAGE_ARCH = "${MACHINE_SOCARCH}"

COMPATIBLE_MACHINE = "(imxgpu)"
