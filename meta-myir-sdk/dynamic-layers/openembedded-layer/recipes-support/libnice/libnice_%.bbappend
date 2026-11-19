PACKAGECONFIG:append = "${@bb.utils.contains('MYIR_ARA2_ENABLE', '1', ' gstreamer', '', d)}"
