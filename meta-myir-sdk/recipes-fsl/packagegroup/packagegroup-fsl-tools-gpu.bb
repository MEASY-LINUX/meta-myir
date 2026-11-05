# Copyright (C) 2012-2014, 2016 Freescale Semiconductor
# Copyright (C) 2015, 2016 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Package group related to i.MX GPU support."
SUMMARY = "FSL Community package group - tools/gpu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    ${SOC_TOOLS_DRM} \
    ${SOC_TOOLS_GPU} \
    ${SOC_TOOLS_NO_GPU} \
"

SOC_TOOLS_DRM ??= ""
SOC_TOOLS_DRM:imxdrm ??= " \
    libdrm-tests"

SOC_TOOLS_GPU ??= ""
SOC_TOOLS_GPU:imxgpu ??= " \
    gputop \
    imx-gpu-sdk \
    ${SOC_TOOLS_GPU_APITRACE} \
    ${SOC_TOOLS_GPU_MALI} \
    ${SOC_TOOLS_GPU_VIVANTE}"

SOC_TOOLS_GPU_APITRACE = ""
SOC_TOOLS_GPU_APITRACE:imxgpu3d = " \
    apitrace"

SOC_TOOLS_GPU_MALI = ""
SOC_TOOLS_GPU_MALI:imxmali = " \
    ${@'${SOC_TOOLS_GPU_MALI_DUAL_DRIVER_MESA}' if d.getVar('IMX_MALI_DUAL_DRIVER') == '1' else ''} \
    mali-imx \
    mali-imx-opencl-icd \
    patrace"
# Keep headers and offline shader compilers in the SDK, not in the rootfs.
# The runtime packages retain the SONAME links used by mali-imx-boot.
SOC_TOOLS_GPU_MALI_DUAL_DRIVER_MESA = " \
    libegl-mesa \
    libgles1-mesa \
    libgles2-mesa"

SOC_TOOLS_GPU_VIVANTE = ""
SOC_TOOLS_GPU_VIVANTE:imxviv = " \
    libgles1-imx \
    imx-gpu-viv-tools"

SOC_TOOLS_NO_GPU ??= ""
SOC_TOOLS_NO_GPU:mx93-nxp-bsp ??= " \
    apitrace \
    libgles1-mesa"
SOC_TOOLS_NO_GPU:mx943-nxp-bsp ??= " \
    apitrace \
    libgles1-mesa"
