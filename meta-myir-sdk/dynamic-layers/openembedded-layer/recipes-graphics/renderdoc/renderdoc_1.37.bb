SUMMARY = "RenderDoc recipe providing renderdoccmd"
DESCRIPTION = "RenderDoc is a frame-capture based graphics debugger"
HOMEPAGE = "https://github.com/baldurk/renderdoc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=5536c2b72eeed14bafaf4d2a6c032b87"

SRCREV = "cd94206b0fd995bfb3e5ed95c1e68d4f5d38ea7e"
SRC_URI = " \
    https://codeload.github.com/baldurk/${BPN}/tar.gz/${SRCREV};downloadfilename=${BPN}-${SRCREV}.tar.gz \
"
SRC_URI[sha256sum] = "aecbff4d72bb3df5f02fa4211dabebde9b12dcd42bb8221417c9e4a0b334bce8"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/${BPN}-${SRCREV}"

DEPENDS += "virtual/libx11 virtual/libgl libxcb xcb-util-keysyms"

RDEPENDS:${PN} = "libxcb xcb-util-keysyms"

inherit cmake pkgconfig python3native features_check

REQUIRED_DISTRO_FEATURES = "x11 opengl"

python __anonymous () {
    # only works on glibc systems
    if d.getVar('TCLIBC') != "glibc":
        raise bb.parse.SkipRecipe("incompatible with %s C library" % d.getVar('TCLIBC'))
}

COMPATIBLE_HOST = "(x86_64|i.86|arm|aarch64).*-linux"

EXTRA_OECMAKE += "\
    -DENABLE_QRENDERDOC=OFF \
    -DENABLE_PYRENDERDOC=OFF \
    -DENABLE_RENDERDOCCMD=ON \
    -DCMAKE_BUILD_TYPE=Release \
    -DHOST_NATIVE_CPP_COMPILER="${BUILD_CXX}" \
"

FILES:${PN} += "${libdir}"
FILES:${PN}-dev = "${includedir}"
