SUMMARY = "Software for capturing GLES calls of an application"
DESCRIPTION = "\
PATrace is software for capturing GLES calls of an application and \
replaying them on a different device, keeping the GPU workload the \
same. It's similar to the open source Apitrace project, but optimised \
for performance measurements."
SECTION = "console/tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a1fcde5f6838616beb564260554a388f"
DEPENDS = "wayland"

SRCREV = "9e0f8ce6b5d6056a4ede30ece59836a7bcaf5ea0"
LIBCOLLECTOR_SRCREV = "946904c232710ca91433e9f6287cd5174efa71ea"
OPENGL_REGISTRY_SRCREV = "ae3d1d0ed562d36d41f7e15f25934c05d896635d"
EGL_REGISTRY_SRCREV = "b055c9b483e70ecd57b3cf7204db21f5a06f9ffe"
OPENCL_HEADERS_SRCREV = "8c4f0111ccf2350a51655d8bbe862c7405f2c0db"
HWCPIPE_SRCREV = "e4cfb5557aaa9e95a32f8b3d2f0f3f3f781f48ea"
TRACETOOLTESTS_SRCREV = "26c13310d9851237f8628d7d6a29bd04e7eac96e"
VULKAN_HEADERS_SRCREV = "c5b7a2fa18750e435e91e06a50cdc5451c5b9abd"
JSONCPP_SRCREV = "8190e061bc2d95da37479a638aa2c9e483e58ec6"

SRC_URI = " \
           https://codeload.github.com/ARM-software/patrace/tar.gz/${SRCREV};downloadfilename=patrace-${SRCREV}.tar.gz;name=patrace \
           https://codeload.github.com/ARM-software/libcollector/tar.gz/${LIBCOLLECTOR_SRCREV};downloadfilename=libcollector-${LIBCOLLECTOR_SRCREV}.tar.gz;name=libcollector \
           https://codeload.github.com/KhronosGroup/OpenGL-Registry/tar.gz/${OPENGL_REGISTRY_SRCREV};downloadfilename=OpenGL-Registry-${OPENGL_REGISTRY_SRCREV}.tar.gz;name=opengl \
           https://codeload.github.com/KhronosGroup/EGL-Registry/tar.gz/${EGL_REGISTRY_SRCREV};downloadfilename=EGL-Registry-${EGL_REGISTRY_SRCREV}.tar.gz;name=egl \
           https://codeload.github.com/KhronosGroup/OpenCL-Headers/tar.gz/${OPENCL_HEADERS_SRCREV};downloadfilename=OpenCL-Headers-${OPENCL_HEADERS_SRCREV}.tar.gz;name=opencl \
           https://codeload.github.com/ARM-software/HWCPipe/tar.gz/${HWCPIPE_SRCREV};downloadfilename=HWCPipe-${HWCPIPE_SRCREV}.tar.gz;name=hwcpipe \
           https://codeload.github.com/ARM-software/tracetooltests/tar.gz/${TRACETOOLTESTS_SRCREV};downloadfilename=tracetooltests-${TRACETOOLTESTS_SRCREV}.tar.gz;name=tests \
           https://codeload.github.com/KhronosGroup/Vulkan-Headers/tar.gz/${VULKAN_HEADERS_SRCREV};downloadfilename=Vulkan-Headers-${VULKAN_HEADERS_SRCREV}.tar.gz;name=vulkanheaders \
           https://codeload.github.com/open-source-parsers/jsoncpp/tar.gz/${JSONCPP_SRCREV};downloadfilename=jsoncpp-${JSONCPP_SRCREV}.tar.gz;name=jsoncpp \
           file://0001-CMakeLists.txt-Switch-to-python3.patch \
           file://0001-fastforward-CMakeLists.txt-Fix-call_parser_src_gener.patch \
           file://0002-fakedriver-Fix-cmake-3.5-syntax.patch \
           file://0001-LF-17811-update-xdg-header-for-wrynose.patch \
           file://0001-patrace-Fix-Wayland-window-creation-LF-17811.patch \
"
SRC_URI[patrace.sha256sum] = "7fa33cc377f7f4decabe791c47611879fed8263d9bec88d1ccdfebf54b579386"
SRC_URI[libcollector.sha256sum] = "45602fd4a08fd973fc1255af489746e81c515f7a16912aaf880fe9132964b82f"
SRC_URI[opengl.sha256sum] = "c490013cafac89477508acae755e39804312ec990758e293648d68238d123849"
SRC_URI[egl.sha256sum] = "bbd72e775630d8e7107878eb818a6c27aca9a57542848c2a70e9e00ba9b3b2e9"
SRC_URI[opencl.sha256sum] = "65d81ce5cd48cf849b2cc87d00e2d915b74b9e1b8cc1ae82e40a5a40e511135c"
SRC_URI[hwcpipe.sha256sum] = "0cbff60d344cd97bb94b4b81bec92ba956401f5953e2f74ec29cc3f33281466b"
SRC_URI[tests.sha256sum] = "ea8055fe80201fa3e55378263ae00b8b087446af9fe2cc096a137d6116a4a8fc"
SRC_URI[vulkanheaders.sha256sum] = "1b81b8fa2a8dc5eeed0b9176f83c43c93917c285f9abc25fcf0f8aa2595c050c"
SRC_URI[jsoncpp.sha256sum] = "ce2c210831145b7ad879469eec52f67acea4d4e0d6bf9ec64f6cd63705156594"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/patrace-${SRCREV}"

python do_unpack:append() {
    import os
    import shutil

    def replace_source(unpacked_name, *destination_parts):
        destination = os.path.join(d.getVar("S"), *destination_parts)
        os.rmdir(destination)
        shutil.move(os.path.join(d.getVar("UNPACKDIR"), unpacked_name), destination)

    replace_source(
        "libcollector-" + d.getVar("LIBCOLLECTOR_SRCREV"),
        "thirdparty", "libcollector",
    )
    replace_source(
        "OpenGL-Registry-" + d.getVar("OPENGL_REGISTRY_SRCREV"),
        "thirdparty", "opengl-registry",
    )
    replace_source(
        "EGL-Registry-" + d.getVar("EGL_REGISTRY_SRCREV"),
        "thirdparty", "egl-registry",
    )
    replace_source(
        "OpenCL-Headers-" + d.getVar("OPENCL_HEADERS_SRCREV"),
        "thirdparty", "opencl-headers",
    )
    replace_source(
        "libGPUCounters-" + d.getVar("HWCPIPE_SRCREV"),
        "thirdparty", "hwcpipe",
    )
    replace_source(
        "tracetooltests-" + d.getVar("TRACETOOLTESTS_SRCREV"),
        "thirdparty", "tracetooltests",
    )
    replace_source(
        "Vulkan-Headers-" + d.getVar("VULKAN_HEADERS_SRCREV"),
        "thirdparty", "libcollector", "external", "Vulkan-Headers",
    )
    replace_source(
        "jsoncpp-" + d.getVar("JSONCPP_SRCREV"),
        "thirdparty", "libcollector", "external", "jsoncpp",
    )
}

inherit cmake pkgconfig

OECMAKE_GENERATOR = "Unix Makefiles"

OECMAKE_SOURCEPATH = "${S}/patrace/project/cmake"

EXTRA_OECMAKE = " \
    -DWINDOWSYSTEM=wayland \
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
    -DCMAKE_INSTALL_PREFIX:PATH=/opt/${BPN}"

#FIXME:error: 'uint32_t' was not declared in this scope
do_configure:prepend(){
    sed -i '/#include <memory>/a #include <cstdint>' ${S}/thirdparty/hwcpipe/backend/device/include/device/handle.hpp
}

# FIXME: Don't ignore problems
CFLAGS += "-Wno-maybe-uninitialized -Wno-old-style-definition -Wno-error=attributes"
CXXFLAGS += "-Wno-range-loop-construct -Wno-unused-variable -Wno-old-style-definition  -Wno-error=attributes"

do_install:append() {
    ln -s libEGL.so       ${D}/opt/${BPN}/${baselib}/libEGL.so.1
    ln -s libEGL.so       ${D}/opt/${BPN}/${baselib}/libEGL.so.1.4.0
    ln -s libGLESv1_CM.so ${D}/opt/${BPN}/${baselib}/libGLESv1_CM.so.1
    ln -s libGLESv1_CM.so ${D}/opt/${BPN}/${baselib}/libGLESv1_CM.so.1.1.0
    ln -s libGLESv2.so    ${D}/opt/${BPN}/${baselib}/libGLESv2.so.2
    ln -s libGLESv2.so    ${D}/opt/${BPN}/${baselib}/libGLESv2.so.2.1.0
}

FILES:${PN} = "/opt"
INSANE_SKIP:${PN}-src += "buildpaths"
COMPATIBLE_MACHINE = "(mx9-nxp-bsp)"
