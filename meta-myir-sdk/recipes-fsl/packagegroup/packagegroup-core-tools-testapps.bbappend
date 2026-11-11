# gst-player has been integrated into gstreamer1.0-plugin-bad-v1.8.0 with version 1.0,
# need to remove the old one
# connmanctl / connmand 由本包组的 connman-* 三项引入；
# 板端网络由 systemd-networkd 管理，故在原有 :remove 字符串中一并摘除。
# 注意：不可另起一行 RDEPENDS:${PN}:remove = ...，会覆盖上面 ltp / gst-player-bin 的移除。
RDEPENDS:${PN}:remove = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', "gst-player-bin", "", d)} \
                          ${@bb.utils.contains('DISTRO_FEATURES', 'x11 opengl', "gst-player-bin", "", d)} \
                          ltp \
                          connman-tools connman-tests connman-client \
"

IMX_CORE_TOOLS = ""
IMX_CORE_TOOLS:imxgpu2d = " tslib"
IMX_CORE_TOOLS:imxpxp = " tslib"

PIPEWIRE_TOOLS = " \
    pipewire \
    pipewire-pulse \
    pipewire-spa-tools \
    pipewire-tools \
    ${PIPEWIRE_V4L2} \
"
PIPEWIRE_V4L2 = "pipewire-v4l2"
PIPEWIRE_V4L2:mx6-nxp-bsp = ""
PIPEWIRE_V4L2:mx7-nxp-bsp = ""

RDEPENDS:${PN} += " \
    ${IMX_CORE_TOOLS} \
    libgpiod-tools \
    ${PIPEWIRE_TOOLS} \
"
