require recipes-connectivity/openssh/openssh_10.3p1.bb

FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-connectivity/openssh/openssh:"

COMPATIBLE_MACHINE = "(^myd-jmx95x-b0$)"
BBCLASSEXTEND = ""

SRC_URI[sha256sum] = "9ae44872090cf2f06b3cf9ff147e7f16f309671340763ed2f99248398ae29d5b"
