#!/bin/bash
set -eo pipefail

if [ "$#" -ne 2 ]; then
    echo "Usage: bash check-sdk.sh SDK_ENVIRONMENT_SETUP OUTPUT_DIRECTORY" >&2
    exit 2
fi
sdk_environment=$(realpath "$1")
smoke_output=$(realpath -m "$2")
smoke_source=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)
# SDK environment scripts may legitimately reference unset shell variables.
source "$sdk_environment"
set -u
: "${SDKTARGETSYSROOT:?Missing SDK target sysroot}"
: "${OECORE_NATIVE_SYSROOT:?Missing SDK host sysroot}"

cmake -S "$smoke_source" -B "$smoke_output" -G Ninja \
    -DCMAKE_TOOLCHAIN_FILE="$OECORE_NATIVE_SYSROOT/usr/share/cmake/OEToolchainConfig.cmake"
cmake --build "$smoke_output"
for executable in runtime-smoke qt-smoke opencv-smoke; do
    # Cross-built executables must not run on the build host.
    LC_ALL=C ${READELF:-readelf} -h "$smoke_output/$executable" | grep -q 'Machine:.*AArch64'
    LC_ALL=C ${READELF:-readelf} -d "$smoke_output/$executable"
done
echo "SDK C/C++, pkg-config and Qt CMake compile/link checks passed."
