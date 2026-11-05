#!/bin/sh
set -eu

if [ "$#" -ne 1 ] || [ ! -s "$1" ]; then
    echo "Usage: sh check-image.sh NONEMPTY_IMAGE_MANIFEST" >&2
    exit 2
fi

# Inspect the installed package manifest, not the recipe build dependency list.
# SDK development packages are intentionally outside this runtime-only check.
awk '
    NF != 3 {
        print "Malformed manifest entry: " $0 > "/dev/stderr"
        failed = 1
        next
    }
    {
        installed[$1] = 1
        if ($1 ~ /-(dev|staticdev)$/ ||
            $1 ~ /^(gcc|g\+\+|cpp|clang|clang\+\+)(-[0-9]+)?$/ ||
            $1 ~ /^(qemu|xen-tools)(-|$)/ || $1 == "ntp") {
            print "Unexpected runtime package: " $1 > "/dev/stderr"
            failed = 1
        }
    }
    END {
        split("packagegroup-myir-jmx95 myir-hmi eeprom-pnsn mali-imx clinfo optee-examples python3-cryptography", required, " ")
        for (i in required) {
            if (!(required[i] in installed)) {
                print "Missing runtime package: " required[i] > "/dev/stderr"
                failed = 1
            }
        }
        exit failed ? 1 : 0
    }
' "$1"
echo "MYD-JMX95 runtime package audit passed."
