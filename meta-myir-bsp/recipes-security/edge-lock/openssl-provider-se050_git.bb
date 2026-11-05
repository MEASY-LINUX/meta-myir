SUMMARY = "OpenSSL Provider for SE050"
DESCRIPTION = "An OpenSSL provider for NXP EdgeLock SE050 secure element product family."
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRCREV = "12999e82cc24733e3194689b959c79f4e9f3da05"
SIMW_SRCREV = "abc6557eb0f7a703384065026b659d1166473b48"

SRC_URI = " \
    https://codeload.github.com/NXPPlugNTrust/se05x-openssl-provider/tar.gz/${SRCREV};downloadfilename=se05x-openssl-provider-${SRCREV}.tar.gz;name=provider \
    https://codeload.github.com/NXP/plug-and-trust/tar.gz/${SIMW_SRCREV};downloadfilename=plug-and-trust-${SIMW_SRCREV}.tar.gz;name=simw \
"
SRC_URI[provider.sha256sum] = "2ed295e5c9b6c1e2ac22782c1da13127013af38e012127de811cf386a5ef1200"
SRC_URI[simw.sha256sum] = "4e2a9e218df242d878335f0839980edd25da4cece1dc1f169e8fb91d6abbe53d"
INSANE_SKIP += "src-uri-bad"

S = "${UNPACKDIR}/se05x-openssl-provider-${SRCREV}"

python do_unpack:append() {
    import os
    import shutil

    simw_dir = os.path.join(d.getVar("S"), "simw_lib")
    os.rmdir(simw_dir)
    shutil.move(
        os.path.join(d.getVar("UNPACKDIR"), "plug-and-trust-" + d.getVar("SIMW_SRCREV")),
        simw_dir,
    )
}


PV = "1.1.2+git${SRCREV}"

inherit cmake

PACKAGECONFIG ??= "host-openssl"

PACKAGECONFIG[host-mbedtls] = "-DPTMW_HostCrypto=MBEDTLS,,mbedtls,,,host-openssl host-none"
PACKAGECONFIG[host-openssl] = "-DPTMW_HostCrypto=OPENSSL,,openssl,,,host-mbedtls host-none"
PACKAGECONFIG[host-none]    = "-DPTMW_HostCrypto=None   ,,       ,,,host-mbedtls host-openssl"

EXTRA_OECMAKE = " \
    -DPTMW_Applet=${APPLET} \
    -DPTMW_SE05X_Ver=${APPLET_VERSION} \
    -DPTMW_SE05X_Auth=${APPLET_AUTH} \
    "

APPLET         ?= "SE05X_C"
APPLET_VERSION ?= "07_02"
APPLET_AUTH    ?= "None"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
