# Copyright 2020-2026 NXP
DESCRIPTION = "cross-platform, high performance scoring engine for ML models"
SECTION = "devel"
LICENSE = "MIT & Apache-2.0"
LIC_FILES_CHKSUM_runtime = "file://LICENSE;md5=0f7e3b1308cb5c00b372a6e78835732d"
LIC_FILES_CHKSUM_model = "file://${S}/example-models/squeezenet/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
LIC_FILES_CHKSUM = "${LIC_FILES_CHKSUM_runtime} ${LIC_FILES_CHKSUM_model}"

DEPENDS = "libpng zlib"

inherit setuptools3

SRC_URI = "${ONNXRUNTIME_SRC};branch=${SRCBRANCH}"
ONNXRUNTIME_SRC ?= "gitsm://github.com/nxp-imx/onnxruntime-imx.git;protocol=https"

# Keep CMake's fixed FetchContent inputs under BitBake control so they are
# checksummed, stored in DL_DIR, and reusable without network access during
# configure or compile.
SRC_URI += " \
    https://github.com/abseil/abseil-cpp/archive/refs/tags/20250814.0.zip;name=ort_abseil;downloadfilename=onnxruntime-abseil-20250814.0.zip;subdir=${BP}/deps/abseil_cpp;striplevel=1 \
    https://github.com/google/re2/archive/refs/tags/2024-07-02.zip;name=ort_re2;downloadfilename=onnxruntime-re2-2024-07-02.zip;subdir=${BP}/deps/re2;striplevel=1 \
    https://github.com/google/googletest/archive/refs/tags/v1.17.0.zip;name=ort_googletest;downloadfilename=onnxruntime-googletest-v1.17.0.zip;subdir=${BP}/deps/googletest;striplevel=1 \
    https://github.com/protocolbuffers/protobuf/releases/download/v21.12/protoc-21.12-linux-x86_64.zip;name=ort_protoc;downloadfilename=onnxruntime-protoc-21.12-linux-x86_64.zip;subdir=${BP}/deps/protoc_binary \
    https://github.com/protocolbuffers/protobuf/archive/refs/tags/v21.12.zip;name=ort_protobuf;downloadfilename=onnxruntime-protobuf-v21.12.zip;subdir=${BP}/deps/protobuf;striplevel=1 \
    https://github.com/HowardHinnant/date/archive/refs/tags/v3.0.1.zip;name=ort_date;downloadfilename=onnxruntime-date-v3.0.1.zip;subdir=${BP}/deps/date;striplevel=1 \
    https://github.com/boostorg/mp11/archive/refs/tags/boost-1.82.0.zip;name=ort_mp11;downloadfilename=onnxruntime-mp11-boost-1.82.0.zip;subdir=${BP}/deps/mp11;striplevel=1 \
    https://github.com/nlohmann/json/archive/refs/tags/v3.11.3.zip;name=ort_json;downloadfilename=onnxruntime-json-v3.11.3.zip;subdir=${BP}/deps/nlohmann_json;striplevel=1 \
    https://github.com/pytorch/cpuinfo/archive/403d652dca4c1046e8145950b1c0997a9f748b57.zip;name=ort_cpuinfo;downloadfilename=onnxruntime-cpuinfo-403d652.zip;subdir=${BP}/deps/pytorch_cpuinfo;striplevel=1 \
    https://github.com/microsoft/GSL/archive/refs/tags/v4.0.0.zip;name=ort_gsl;downloadfilename=onnxruntime-gsl-v4.0.0.zip;subdir=${BP}/deps/gsl;striplevel=1 \
    https://github.com/dcleblanc/SafeInt/archive/refs/tags/3.0.28.zip;name=ort_safeint;downloadfilename=onnxruntime-safeint-3.0.28.zip;subdir=${BP}/deps/safeint;striplevel=1 \
    https://github.com/google/flatbuffers/archive/refs/tags/v23.5.26.zip;name=ort_flatbuffers;downloadfilename=onnxruntime-flatbuffers-v23.5.26.zip;subdir=${BP}/deps/flatbuffers;striplevel=1 \
    https://github.com/pybind/pybind11/archive/refs/tags/v3.0.2.zip;name=ort_pybind11;downloadfilename=onnxruntime-pybind11-v3.0.2.zip;subdir=${BP}/deps/pybind11;striplevel=1 \
    https://github.com/onnx/onnx/archive/refs/tags/v1.20.1.zip;name=ort_onnx;downloadfilename=onnxruntime-onnx-v1.20.1.zip;subdir=${BP}/deps/onnx;striplevel=1 \
    https://github.com/eigen-mirror/eigen/archive/1d8b82b0740839c0de7f1242a3585e3390ff5f33/eigen-1d8b82b0740839c0de7f1242a3585e3390ff5f33.zip;name=ort_eigen;downloadfilename=onnxruntime-eigen-1d8b82b.zip;subdir=${BP}/deps/eigen;striplevel=1 \
    https://github.com/dmlc/dlpack/archive/5c210da409e7f1e51ddf445134a4376fdbd70d7d.zip;name=ort_dlpack;downloadfilename=onnxruntime-dlpack-5c210da.zip;subdir=${BP}/deps/dlpack;striplevel=1 \
    https://github.com/ARM-software/kleidiai/archive/refs/tags/v1.20.0.tar.gz;name=ort_kleidiai;downloadfilename=onnxruntime-kleidiai-v1.20.0.tar.gz;subdir=${BP}/deps/kleidiai;striplevel=1 \
"

SRC_URI[ort_abseil.sha1sum] = "a9eb1d648cbca4d4d788737e971a6a7a63726b07"
SRC_URI[ort_re2.sha1sum] = "646e1728269cde7fcef990bf4a8e87b047882e88"
SRC_URI[ort_googletest.sha1sum] = "f638fa0e724760e2ba07ff8cfba32cd644e1ce28"
SRC_URI[ort_protoc.sha1sum] = "338462004aa5be9fba45b35b5b4be43f69b47a90"
SRC_URI[ort_protobuf.sha1sum] = "7cf2733949036c7d52fda017badcab093fe73bfa"
SRC_URI[ort_date.sha1sum] = "2dac0c81dc54ebdd8f8d073a75c053b04b56e159"
SRC_URI[ort_mp11.sha1sum] = "9bc9e01dffb64d9e0773b2e44d2f22c51aace063"
SRC_URI[ort_json.sha1sum] = "5e88795165cc8590138d1f47ce94ee567b85b4d6"
SRC_URI[ort_cpuinfo.sha1sum] = "30b2a07fe4bae8574f89176e56274cacdd6d135b"
SRC_URI[ort_gsl.sha1sum] = "cf368104cd22a87b4dd0c80228919bb2df3e2a14"
SRC_URI[ort_safeint.sha1sum] = "23f252040ff6cb9f1fd18575b32fa8fb5928daac"
SRC_URI[ort_flatbuffers.sha1sum] = "59422c3b5e573dd192fead2834d25951f1c1670c"
SRC_URI[ort_pybind11.sha1sum] = "a064e663b4d7a337ac291d1bef7337ef4e60a1ae"
SRC_URI[ort_onnx.sha1sum] = "30b80c81a1a381188896e86abe460c3c3f3091fd"
SRC_URI[ort_eigen.sha1sum] = "05b19b49e6fbb91246be711d801160528c135e34"
SRC_URI[ort_dlpack.sha1sum] = "e499c86e4e5c5268a87661d7ea39c27fae10907c"
SRC_URI[ort_kleidiai.sha1sum] = "6895e72b3d5cf1173358164cb3d64c9d7d33cc84"

# These archives and checksums come directly from ONNX Runtime's deps.txt.
# Keep the archive form so BitBake verifies the same inputs CMake expects.
INSANE_SKIP += "src-uri-bad"

inherit cmake python3native

OECMAKE_SOURCEPATH = "${S}/cmake"
OECMAKE_GENERATOR = "Unix Makefiles"

ONNXRUNTIME_DEPS_DIR = "${UNPACKDIR}/${BP}/deps"

EXTRA_OECMAKE += "\
    -DFETCHCONTENT_FULLY_DISCONNECTED=ON \
    -DFETCHCONTENT_SOURCE_DIR_ABSEIL_CPP=${ONNXRUNTIME_DEPS_DIR}/abseil_cpp/abseil-cpp-20250814.0 \
    -DFETCHCONTENT_SOURCE_DIR_RE2=${ONNXRUNTIME_DEPS_DIR}/re2/re2-2024-07-02 \
    -DFETCHCONTENT_SOURCE_DIR_GOOGLETEST=${ONNXRUNTIME_DEPS_DIR}/googletest/googletest-1.17.0 \
    -DFETCHCONTENT_SOURCE_DIR_PROTOC_BINARY=${ONNXRUNTIME_DEPS_DIR}/protoc_binary \
    -DFETCHCONTENT_SOURCE_DIR_PROTOBUF=${ONNXRUNTIME_DEPS_DIR}/protobuf/protobuf-21.12 \
    -DFETCHCONTENT_SOURCE_DIR_DATE=${ONNXRUNTIME_DEPS_DIR}/date/date-3.0.1 \
    -DFETCHCONTENT_SOURCE_DIR_MP11=${ONNXRUNTIME_DEPS_DIR}/mp11/mp11-boost-1.82.0 \
    -DFETCHCONTENT_SOURCE_DIR_NLOHMANN_JSON=${ONNXRUNTIME_DEPS_DIR}/nlohmann_json/json-3.11.3 \
    -DFETCHCONTENT_SOURCE_DIR_PYTORCH_CPUINFO=${ONNXRUNTIME_DEPS_DIR}/pytorch_cpuinfo/cpuinfo-403d652dca4c1046e8145950b1c0997a9f748b57 \
    -DFETCHCONTENT_SOURCE_DIR_GSL=${ONNXRUNTIME_DEPS_DIR}/gsl/GSL-4.0.0 \
    -DFETCHCONTENT_SOURCE_DIR_SAFEINT=${ONNXRUNTIME_DEPS_DIR}/safeint/SafeInt-3.0.28 \
    -DFETCHCONTENT_SOURCE_DIR_FLATBUFFERS=${ONNXRUNTIME_DEPS_DIR}/flatbuffers/flatbuffers-23.5.26 \
    -DFETCHCONTENT_SOURCE_DIR_PYBIND11_PROJECT=${ONNXRUNTIME_DEPS_DIR}/pybind11/pybind11-3.0.2 \
    -DFETCHCONTENT_SOURCE_DIR_ONNX=${ONNXRUNTIME_DEPS_DIR}/onnx/onnx-1.20.1 \
    -DFETCHCONTENT_SOURCE_DIR_EIGEN3=${ONNXRUNTIME_DEPS_DIR}/eigen/eigen-1d8b82b0740839c0de7f1242a3585e3390ff5f33 \
    -DFETCHCONTENT_SOURCE_DIR_DLPACK=${ONNXRUNTIME_DEPS_DIR}/dlpack/dlpack-5c210da409e7f1e51ddf445134a4376fdbd70d7d \
    -DFETCHCONTENT_SOURCE_DIR_KLEIDIAI=${ONNXRUNTIME_DEPS_DIR}/kleidiai \
    -DCMAKE_BUILD_TYPE=RelWithDebInfo \
    -Donnxruntime_BUILD_UNIT_TESTS=ON \
    -DCMAKE_VERBOSE_MAKEFILE:BOOL=ON \
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
    -DCMAKE_CXX_FLAGS='-I${B}' \
"

PYTHON_DEPENDS = "\
    ${PYTHON_PN} \
    ${PYTHON_PN}-pip-native \
    ${PYTHON_PN}-numpy \
    ${PYTHON_PN}-numpy-native \
    ${PYTHON_PN}-packaging-native\
    ${PYTHON_PN}-pybind11\
    ${PYTHON_PN}-pybind11-native\
"

PYTHON_RDEPENDS = "\
    ${PYTHON_PN} \
    ${PYTHON_PN}-numpy \
    ${PYTHON_PN}-protobuf \
    ${PYTHON_PN}-coloredlogs \
    ${PYTHON_PN}-flatbuffers \
    ${PYTHON_PN}-sympy \
"

PACKAGECONFIG ?= "crosscompiling sharedlib python kleidiai ${PACKAGECONFIG_NPU}"
PACKAGECONFIG_NPU                    = ""
PACKAGECONFIG_NPU:mx95-nxp-bsp       = "neutron bmm"
PACKAGECONFIG_NPU:mx952-nxp-bsp      = "neutron"
PACKAGECONFIG_NPU:mx943-nxp-bsp      = "neutron"
PACKAGECONFIG_NPU:mx937-nxp-bsp      = "neutron"
PACKAGECONFIG_NPU:mx8-nxp-bsp:imxgpu = "vsinpu"
PACKAGECONFIG_NPU:mx8mm-nxp-bsp      = ""

PACKAGECONFIG[bmm] = "-DNEUTRON_HAS_DRIVER_BMM=ON,-DNEUTRON_HAS_DRIVER_BMM=OFF"
PACKAGECONFIG[nsync] = "-Donnxruntime_USE_NSYNC=ON, -Donnxruntime_USE_NSYNC=OFF"
PACKAGECONFIG[prebuilt] = "-Donnxruntime_USE_PREBUILT_PB=ON, -Donnxruntime_USE_PREBUILT_PB=OFF"
PACKAGECONFIG[openmp] = "-Donnxruntime_USE_OPENMP=ON, -Donnxruntime_USE_OPENMP=OFF"
PACKAGECONFIG[trt] = "-Donnxruntime_USE_TRT=ON, -Donnxruntime_USE_TRT=OFF"
PACKAGECONFIG[nuphar] = "-Donnxruntime_USE_NUPHAR=ON, -Donnxruntime_USE_NUPHAR=OFF"
PACKAGECONFIG[brainslice] = "-Donnxruntime_USE_BRAINSLICE=ON, -Donnxruntime_USE_BRAINSLICE=OFF"
PACKAGECONFIG[python] = "-Donnxruntime_ENABLE_PYTHON=ON, -Donnxruntime_ENABLE_PYTHON=OFF, ${PYTHON_DEPENDS}, ${PYTHON_RDEPENDS}"
PACKAGECONFIG[sharedlib] = "-Donnxruntime_BUILD_SHARED_LIB=ON, -Donnxruntime_BUILD_SHARED_LIB=OFF"
PACKAGECONFIG[eigenblas] = "-Donnxruntime_USE_EIGEN_FOR_BLAS=ON, -Donnxruntime_USE_EIGEN_FOR_BLAS=OFF"
PACKAGECONFIG[openblas] = "-Donnxruntime_USE_OPENBLAS=ON, -Donnxruntime_USE_OPENBLAS=OFF"
PACKAGECONFIG[dnnl] = "-Donnxruntime_USE_DNNL=ON, -Donnxruntime_USE_DNNL=OFF"
PACKAGECONFIG[mklml] = "-Donnxruntime_USE_MKLML=ON, -Donnxruntime_USE_MKLML=OFF"
PACKAGECONFIG[gemmlowp] = "-Donnxruntime_USE_GEMMLOWP=ON, -Donnxruntime_USE_GEMMLOWP=OFF"
PACKAGECONFIG[ngraph] = "-Donnxruntime_USE_NGRAPH=ON, -Donnxruntime_USE_NGRAPH=OFF"
PACKAGECONFIG[openvino] = "-Donnxruntime_USE_OPENVINO=ON, -Donnxruntime_USE_OPENVINO=OFF"
PACKAGECONFIG[interop] = "-Donnxruntime_ENABLE_LANGUAGE_INTEROP_OPS=ON, -Donnxruntime_ENABLE_LANGUAGE_INTEROP_OPS=OFF"
PACKAGECONFIG[dml] = "-Donnxruntime_USE_DML=ON, -Donnxruntime_USE_DML=OFF"
PACKAGECONFIG[telemetry] = "-Donnxruntime_USE_TELEMETRY=ON, -Donnxruntime_USE_TELEMETRY=OFF"
PACKAGECONFIG[armnn-relu] = "-Donnxruntime_ARMNN_RELU_USE_CPU=ON, -Donnxruntime_ARMNN_RELU_USE_CPU=OFF"
PACKAGECONFIG[armnn-bn] = "-Donnxruntime_ARMNN_BN_USE_CPU=ON, -Donnxruntime_ARMNN_BN_USE_CPU=OFF"
PACKAGECONFIG[opschema] = "-Donnxruntime_PYBIND_EXPORT_OPSCHEMA=ON, -Donnxruntime_PYBIND_EXPORT_OPSCHEMA=OFF"
PACKAGECONFIG[nnapi] = "-Donnxruntime_USE_NNAPI_BUILTIN=ON, -Donnxruntime_USE_NNAPI_BUILTIN=OFF"
PACKAGECONFIG[tvm] = "-Donnxruntime_USE_TVM=ON, -Donnxruntime_USE_TVM=OFF"
PACKAGECONFIG[llvm] = "-Donnxruntime_USE_LLVM=ON, -Donnxruntime_USE_LLVM=OFF"
PACKAGECONFIG[microsoft] = "-Donnxruntime_ENABLE_MICROSOFT_INTERNAL=ON, -Donnxruntime_ENABLE_MICROSOFT_INTERNAL=OFF"
PACKAGECONFIG[eigenthreadpool] = "-Donnxruntime_USE_EIGEN_THREADPOOL=ON, -Donnxruntime_USE_EIGEN_THREADPOOL=OFF"
PACKAGECONFIG[tensorrt] = "-Donnxruntime_USE_TENSORRT=ON, -Donnxruntime_USE_TENSORRT=OFF"
PACKAGECONFIG[crosscompiling] = "-Donnxruntime_CROSS_COMPILING=ON, -Donnxruntime_CROSS_COMPILING=OFF "
PACKAGECONFIG[server] = "-Donnxruntime_BUILD_SERVER=ON, -Donnxruntime_BUILD_SERVER=OFF"
PACKAGECONFIG[x86] = "-Donnxruntime_BUILD:x86=ON, -Donnxruntime_BUILD:x86=OFF"
PACKAGECONFIG[fullprotobuf] = "-Donnxruntime_USE_FULL_PROTOBUF=ON, -Donnxruntime_USE_FULL_PROTOBUF=OFF"
PACKAGECONFIG[ops] = "-Donnxruntime_DISABLE_CONTRIB_OPS=ON, -Donnxruntime_DISABLE_CONTRIB_OPS=OFF"
PACKAGECONFIG[staticruntime] = "-Donnxruntime_MSVC_STATIC_RUNTIME=ON, -Donnxruntime_MSVC_STATIC_RUNTIME=OFF"
PACKAGECONFIG[runtests] = "-Donnxruntime_RUN_ONNX_TESTS=ON, -Donnxruntime_RUN_ONNX_TESTS=OFF"
PACKAGECONFIG[reports] = "-Donnxruntime_GENERATE_TEST_REPORTS=ON, -Donnxruntime_GENERATE_TEST_REPORTS=OFF"
PACKAGECONFIG[devmode] = "-Donnxruntime_DEV_MODE=ON, -Donnxruntime_DEV_MODE=OFF"
PACKAGECONFIG[cuda] = "-Donnxruntime_USE_CUDA=ON, -Donnxruntime_USE_CUDA=OFF"
PACKAGECONFIG[automl] = "-Donnxruntime_USE_AUTOML=ON, -Donnxruntime_USE_AUTOML=OFF"
PACKAGECONFIG[jemalloc] = "-Donnxruntime_USE_JEMALLOC=ON, -Donnxruntime_USE_JEMALLOC=OFF"
PACKAGECONFIG[mimalloc] = "-Donnxruntime_USE_MIMALLOC=ON, -Donnxruntime_USE_MIMALLOC=OFF"
PACKAGECONFIG[csharp] = "-Donnxruntime_BUILD_CSHARP=ON, -Donnxruntime_BUILD_CSHARP=OFF"
PACKAGECONFIG[java] = "-Donnxruntime_BUILD_JAVA=ON, -Donnxruntime_BUILD_JAVA=OFF"
PACKAGECONFIG[kleidiai] = "-Donnxruntime_USE_KLEIDIAI=ON, -Donnxruntime_USE_KLEIDIAI=OFF"
PACKAGECONFIG[neutron] = "-Donnxruntime_USE_NEUTRON=ON, -Donnxruntime_USE_NEUTRON=OFF, neutron nlohmann-json"
PACKAGECONFIG[vsinpu] = "-Donnxruntime_USE_VSINPU=ON, -Donnxruntime_USE_VSINPU=OFF, tim-vx"

SETUPTOOLS_SETUP_PATH = "${B}"

do_compile:append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'python', 'true', 'false', d)}; then
        # Copy 'setup.py' to build dir
        cp ${S}/setup.py ${B}

        cp -r ${S}/onnxruntime/python ${B}/onnxruntime/

        # Copy path file with path 'docs/python/README.rst' to build dir
        mkdir -p ${B}/docs/python && cp ${S}/docs/python/README.rst ${B}/docs/python

        setuptools3_do_compile

        git config --global --add safe.directory ${WORKDIR}/build/pybind11/src/pybind11
    fi
}

do_install:append() {
    CP_ARGS="-Prf --preserve=mode,timestamps --no-preserve=ownership"

    # Ensure target dir exists
    install -d ${D}${bindir}/${BP}

    # Copy squeezenet updated model from onnxruntime-imx repo
    if [ -d ${S}/example-models/ ]; then
        cp $CP_ARGS ${S}/example-models/squeezenet ${D}${bindir}/${BP}/
    fi

    # If cmake installs 'onnx_test_runner' at bindir level, move to package
    if [ -f ${D}${bindir}/onnx_test_runner ]; then
        mv ${D}${bindir}/onnx_test_runner ${D}${bindir}/${BP}/
    fi

    # Install onnxruntime_perf_test in main package
    install -m 0755 ${B}/onnxruntime_perf_test ${D}${bindir}/${BP}

    # Install test binaries and data in test package
    install -d ${D}${bindir}/${BP}/tests
    install -m 0744 ${B}/libcustom_op_library.so ${D}${bindir}/${BP}/tests
    install -m 0644 ${B}/libonnxruntime_runtime_path_test_shared_library.so ${D}${bindir}/${BP}/tests
    install -m 0744 ${B}/onnxruntime_global_thread_pools_test ${D}${bindir}/${BP}/tests
    install -m 0744 ${B}/onnxruntime_mlas_test ${D}${bindir}/${BP}/tests
    install -m 0744 ${B}/onnxruntime_shared_lib_test ${D}${bindir}/${BP}/tests
    install -m 0744 ${B}/onnxruntime_test_all ${D}${bindir}/${BP}/tests
    cp $CP_ARGS ${B}/testdata ${D}${bindir}/${BP}/tests

    if ${@bb.utils.contains('PACKAGECONFIG', 'python', 'true', 'false', d)}; then
        setuptools3_do_install
        find ${D}/${PYTHON_SITEPACKAGES_DIR} -type d -name "__pycache__" -exec rm -Rf {} +
        git config --global --unset-all safe.directory ${TMPDIR}/.*/${PN}/.*/build/pybind11/src/pybind11
    fi
    rm ${D}/${PYTHON_SITEPACKAGES_DIR}/${PN}/capi/libonnxruntime.so*
   
    # Copy label_image_onnx.py tool from onnxruntime-imx repo
    cp ${S}/onnxruntime/core/providers/neutron/tools/label_image_onnx.py ${D}${bindir}/${BP}/
}

# Make the package arch SOC-specific since the recipe now builds with SOC-specific configuration:
PACKAGE_ARCH = "${MACHINE_SOCARCH}"

# Adjust the Python runtime dependency inherited from setuptools3-base.bbclass
# since Python support for this recipe is conditional
RDEPENDS:${PN}:remove:class-target = " \
    ${@bb.utils.contains('PACKAGECONFIG', 'python', '', '${PYTHON_PN}-core', d)}"

# libonnxruntime_providers_shared.so is being packaged into -dev which is intended
INSANE_SKIP:${PN}-dev += "dev-elf buildpaths"

# A separate tests package for the test binaries not appearing in the main package
PACKAGE_BEFORE_PN = "${PN}-tests"
FILES:${PN}-tests = "${bindir}/${BP}/tests/*"

# libcustom_op_library.so is in bindir, which is intended;
# onnxruntime_shared_lib_test requires the shlib to be in the same directory as testdata to run properly
INSANE_SKIP:${PN}-tests += "libdir buildpaths"
INSANE_SKIP:${PN}-dbg += "libdir buildpaths"

INSANE_SKIP:${PN} += "buildpaths"
