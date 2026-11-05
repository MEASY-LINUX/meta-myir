#!/bin/sh
set -eu
test_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
burn="$test_dir/../../meta-myir-bsp/recipes-myir/myir-fac-burn/files/myir-burn-emmc"
bash -n "$burn"

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
reject --execute
reject --execute --confirm WRONG
echo 'Factory geometry and confirmation tests passed; no devices accessed.'
