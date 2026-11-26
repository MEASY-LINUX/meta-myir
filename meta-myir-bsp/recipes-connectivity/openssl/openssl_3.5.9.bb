require recipes-connectivity/openssl/openssl_3.5.7.bb

FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-connectivity/openssl/openssl:${COREBASE}/meta/recipes-connectivity/openssl/files:"

COMPATIBLE_MACHINE = "(^myd-jmx95x-b0$)"
BBCLASSEXTEND = ""

SRC_URI:remove = "http://www.openssl.org/source/openssl-${PV}.tar.gz"
SRC_URI:prepend = "https://github.com/openssl/openssl/releases/download/openssl-${PV}/openssl-${PV}.tar.gz "
SRC_URI[sha256sum] = "603f5602e2eef00d77fbd429d34dcd5822bb301757a1bc9cdb24c670f1eb859a"
