#!/bin/sh
set -eu
test_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
burn="$test_dir/../../meta-myir-bsp/recipes-myir/myir-fac-burn/files/myir-burn-emmc"
burn_recipe="$test_dir/../../meta-myir-bsp/recipes-myir/myir-fac-burn/myir-fac-burn.bb"
burn_service="$test_dir/../../meta-myir-bsp/recipes-myir/myir-fac-burn/files/fac-burn-emmc.service"
burn_image="$test_dir/../../meta-myir-sdk/recipes-fsl/images/myir-image-burn.bb"
security_dir="$test_dir/../../../meta-measy-linux/meta-measy-security"
target_mc="$security_dir/conf/multiconfig/myd-jmx95-dmverity-target.conf"
factory_mc="$security_dir/conf/multiconfig/myd-jmx95-dmverity-factory.conf"
security_inc="$test_dir/../../meta-myir-bsp/conf/machine/include/myir-jmx95-security.inc"
ota_burn_append="$test_dir/../../../meta-measy-linux/meta-measy-ota/recipes-myir/myir-fac-burn/myir-fac-burn.bbappend"

bash -n "$burn"
grep -Fq 'COMPATIBLE_MACHINE = "(myir-jmx95)"' "$burn_recipe"
grep -Fq 'COMPATIBLE_MACHINE = "(myir-jmx95)"' "$burn_image"
grep -Fq 'SYSTEMD_AUTO_ENABLE:${PN} = "enable"' "$burn_recipe"
if grep -Fq 'ConditionPathExists=' "$burn_service"; then
    echo "Factory burn service must start automatically without a transient authorization file" >&2
    exit 1
fi
if grep -Eq '/dev/mmcblk[01]([^0-9]|$)|/sys/(class/block|block)/mmcblk[01]([^0-9]|$)' "$burn"; then
    echo "Factory burn script must discover SD and eMMC devices dynamically" >&2
    exit 1
fi

if grep -Eq '^[[:space:]]*MACHINE[[:space:]]*=' "$target_mc" "$factory_mc"; then
    echo "Multiconfig policy must inherit MACHINE from the top-level build" >&2
    exit 1
fi
grep -Fq 'MACHINEOVERRIDES =. "myir-jmx95:"' "$security_inc"
grep -Fq 'MYIR_FACTORY_TARGET_OTA := "${OTA_SUPPORT}"' "$factory_mc"
grep -Fq 'OTA_SUPPORT = "0"' "$factory_mc"
grep -Fq "MYIR_FACTORY_POPULATE_INACTIVE_SLOT =" "$ota_burn_append"
grep -Fq "'4096' if d.getVar('MYIR_FACTORY_TARGET_OTA') == '1'" "$ota_burn_append"
grep -Fq 'MYIR_FACTORY_VERITY_ROOTFS_SIZE_MIB ?= "4096"' "$burn_recipe"

for layout in single ab; do
    for size in 16384 32768 65536; do
        bash "$burn" --plan "$layout" "$size" 5368709120 |
            awk -v layout="$layout" -v total="$size" '
                /^start=/ {
                    split($0, fields, /[=, ]+/)
                    start = fields[2] + 0
                    size = fields[4] + 0
                    count++
                    if (count == 1 && start != 20480) exit 1
                    if (start < previous_end || size <= 0) exit 1
                    if (count % 2 == 0) {
                        if (size * 512 < 5368709120 + 64 * 1048576) exit 1
                        if (root_size && size != root_size) exit 1
                        root_size = size
                    }
                    previous_end = start + size
                }
                END {
                    if (count != (layout == "ab" ? 4 : 2)) exit 1
                    if (previous_end > (total - 50) * 2048) exit 1
                }
            '
    done
done

verity_bytes=$((3500 * 1048576))
verity_slot_mib=4096
overlay_min_mib=4096
for layout in single ab; do
    expected=3
    [ "$layout" = single ] || expected=5
    for size in 16384 32768 65536; do
        table=$(bash "$burn" --plan-verity "$layout" "$size" \
            "$verity_bytes" "$verity_slot_mib" "$overlay_min_mib")
        printf '%s\n' "$table" |
            awk -v expected="$expected" -v total="$size" \
                -v slot="$verity_slot_mib" -v overlay_min="$overlay_min_mib" '
                /^start=/ {
                    split($0, fields, /[=, ]+/)
                    start = fields[2] + 0
                    size = fields[4] + 0
                    count++
                    if (start < previous_end || size <= 0) exit 1
                    if (count % 2 == 0 && size != slot * 2048) exit 1
                    if (count == expected && size < overlay_min * 2048) exit 1
                    previous_end = start + size
                }
                END {
                    if (count != expected) exit 1
                    if (previous_end != (total - 50) * 2048) exit 1
                }
            '
        expected_label=dos
        [ "$layout" = single ] || expected_label=gpt
        printf '%s\n' "$table" | grep -Fqx "label: $expected_label"
    done
done

reject() {
    if bash "$burn" "$@"; then
        echo "Unexpected acceptance: $*" >&2
        exit 1
    fi
}
reject --plan single 4096 5368709120
reject --plan ab 8192 5368709120
reject --plan invalid 32768 5368709120
reject --plan ab -1 5368709120
reject --plan ab 2097152 5368709120
reject --plan ab 32768 invalid
reject --plan ab '32768+1' 5368709120
reject --plan
reject --plan-verity ab 32768 5368709120 4096 4096
reject --plan-verity ab 32768 "$verity_bytes" invalid 4096
reject --plan-verity ab 32768 "$verity_bytes" 4096 invalid
reject --plan-verity ab 8192 "$verity_bytes" 4096 4096
reject --execute
reject --execute --confirm WRONG
echo 'Factory geometry and confirmation tests passed; no devices accessed.'
