#!/bin/sh
set -eu
audit_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
sh "$audit_dir/check-image.sh" "$audit_dir/fixtures/runtime.manifest"
for fixture in development incomplete malformed; do
    if sh "$audit_dir/check-image.sh" "$audit_dir/fixtures/$fixture.manifest"; then
        echo "FAIL: $fixture manifest was accepted" >&2
        exit 1
    fi
done
if sh "$audit_dir/check-image.sh" /dev/null; then
    echo "FAIL: empty manifest was accepted" >&2
    exit 1
fi
echo "Image audit positive and negative tests passed."
