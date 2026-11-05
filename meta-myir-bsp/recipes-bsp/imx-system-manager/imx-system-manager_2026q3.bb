SUMMARY = "i.MX System Manager Firmware"
DESCRIPTION = "\
    The System Manager (SM) is a firmware that runs on a Cortex-M processor on \
    many NXP i.MX processors. The Cortex-M is the boot core, runs the boot ROM \
    which loads the SM (and other boot code), and then branches to the SM. The \
    SM then configures some aspects of the hardware such as isolation mechanisms \
    and then starts other cores in the system. After starting these cores, it \
    enters a service mode where it provides access to clocking, power, sensor, \
    and pin control via a client RPC API based on ARM's System Control and \
    Management Interface (SCMI)."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=f2a70813bc08547f509361c08b718861"

SRC_URI = "${IMX_SYSTEM_MANAGER_SRC};branch=${SRCBRANCH}"
IMX_SYSTEM_MANAGER_SRC ?= "git:///home/myir/bsp/6.18/imx-system-manager;protocol=file"
SRCBRANCH = "develop_sm"
SRCREV = "7ca42a23d7e89c47d5624a45c89efe8405256007"
BB_GIT_SHALLOW = "1"
BB_GIT_SHALLOW_DEPTH = "1"

require imx-system-manager.inc
