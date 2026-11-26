#!/bin/sh
#
# i.MX Yocto Project Build Environment Setup Script
#
# Copyright (C) 2011-2016 Freescale Semiconductor
# Copyright 2017, 2019-2024 NXP
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

. sources/meta-myir/tools/setup-utils.sh

MYIR_SETUP_ROOT=$PWD
PROGNAME="setup-environment"
exit_message ()
{
   echo "To return to this build environment later please run:"
   echo "    source setup-environment <build_dir>"

}

usage()
{
    echo ""
    echo "Usage: source sources/meta-myir/tools/myir-setup-release.sh [OPTIONS]"
    echo ""
    echo "Optional parameters:"
    echo "  -b BUILD_DIR    Specify build directory (default: 'build')"
    echo "  -h              Display this help message"
    echo ""
    echo "Machine selection:"
    echo "  default: myd-jmx95x-b0"
    echo "  any machine with a matching meta-myir-bsp/conf/machine/*.conf"
    echo ""
    echo "Supported NXP's i.MX distros:"
    ls sources/meta-myir/meta-myir-sdk/conf/distro/fsl-*.conf 2>/dev/null \
        | sed 's/\.conf//g' \
        | sed -r 's/^.+\///' \
        | sort \
        | sed 's/^/  /'
    echo ""
}

myir_setup_cleanup()
{

    unset MYIR_SETUP_ROOT BUILD_DIR FSLDISTRO
    unset fsl_setup_help fsl_setup_error fsl_setup_flag
    unset usage append_optional_layer
    unset ARM_DIR
    unset optional_layer SECURITY_KEYS_SOURCE
    exit_message
}

# get command line options
OLD_OPTIND=$OPTIND
unset FSLDISTRO

while getopts "k:r:t:b:e:gh" fsl_setup_flag
do
    case $fsl_setup_flag in
        b) BUILD_DIR="$OPTARG";
           echo -e "\n Build directory is " $BUILD_DIR
           ;;
        h) fsl_setup_help='true';
           ;;
        \?) fsl_setup_error='true';
           ;;
    esac
done
shift $((OPTIND-1))
if [ $# -ne 0 ]; then
    fsl_setup_error=true
    echo -e "Invalid command line ending: '$@'"
fi
OPTIND=$OLD_OPTIND
if test $fsl_setup_help; then
    usage && myir_setup_cleanup && return 1
elif test $fsl_setup_error; then
    myir_setup_cleanup && return 1
fi


if [ -z "$DISTRO" ]; then
    if [ -z "$FSLDISTRO" ]; then
        FSLDISTRO='fsl-imx-xwayland'
    fi
else
    FSLDISTRO="$DISTRO"
fi

if [ -z "$BUILD_DIR" ]; then
    BUILD_DIR='build'
fi

if [ -z "$MACHINE" ]; then
    echo setting to default machine
    MACHINE='myd-jmx95x-b0'
fi

if [ ! -f "$MYIR_SETUP_ROOT/sources/meta-myir/meta-myir-bsp/conf/machine/$MACHINE.conf" ]; then
    echo "ERROR: Machine configuration not found for MACHINE=$MACHINE."
    return 1
fi

case $FSLDISTRO in
*wayland*)
    : ok
    ;;
*)
    echo "ERROR: Only Wayland distros are supported for $MACHINE."
    return 1
    ;;
esac

if [ ! -f "$MYIR_SETUP_ROOT/sources/meta-swupdate/conf/layer.conf" ]; then
    echo "ERROR: Missing sources/meta-swupdate (wrynose)."
    echo "See sources/meta-myir/docs/myd-jmx95x-update-port.md."
    return 1
fi

# Override the click-through in meta-freescale
FSL_EULA_FILE=$MYIR_SETUP_ROOT/sources/meta-myir/LICENSE.txt

# Set up the basic yocto environment
DISTRO=$FSLDISTRO MACHINE=$MACHINE . "./$PROGNAME" "$BUILD_DIR" || return 1

# Point to the current directory since the last command changed the directory to $BUILD_DIR
BUILD_DIR=.

if [ ! -e $BUILD_DIR/conf/local.conf ]; then
    echo -e "\n ERROR - No build directory is set yet. Run the 'setup-environment' script before running this script to create " $BUILD_DIR
    echo -e "\n"
    return 1
fi

# On the first script run, backup the local.conf file
# Consecutive runs, it restores the backup and changes are appended on this one.
if [ ! -e $BUILD_DIR/conf/local.conf.org ]; then
    cp $BUILD_DIR/conf/local.conf $BUILD_DIR/conf/local.conf.org
else
    cp $BUILD_DIR/conf/local.conf.org $BUILD_DIR/conf/local.conf
fi

echo >> conf/local.conf
echo "# Switch to Debian packaging and include package-management in the image" >> conf/local.conf
echo "PACKAGE_CLASSES = \"package_deb\"" >> conf/local.conf
echo "EXTRA_IMAGE_FEATURES += \"package-management\"" >> conf/local.conf
echo >> conf/local.conf
echo "# Optional product features. Set only the required features to 1." >> conf/local.conf
echo "MEASY_RT_ENABLE = \"0\"" >> conf/local.conf
echo "MYIR_SECURE_BOOT = \"0\"" >> conf/local.conf
echo "ENABLE_DM_VERITY = \"0\"" >> conf/local.conf
echo "OTA_SUPPORT = \"0\"" >> conf/local.conf
echo "# Isolate the writable factory SD only for dm-verity product builds." >> conf/local.conf
echo "BBMULTICONFIG := \"\${@'target factory' if d.getVar('ENABLE_DM_VERITY') == '1' else ''}\"" >> conf/local.conf

# BitBake requires multiconfig entry files in the build directory.  Keep only
# these entry points here; the maintained policies live in meta-measy-security
# and inherit the MACHINE selected above (B0, B1, or a later JMX95 revision).
mkdir -p conf/multiconfig
echo '# MACHINE is inherited from the top-level build configuration.' > conf/multiconfig/target.conf
echo 'require ${BSPDIR}/sources/meta-measy-linux/meta-measy-security/conf/multiconfig/myd-jmx95-dmverity-target.conf' >> conf/multiconfig/target.conf
echo '# MACHINE is inherited from the top-level build configuration.' > conf/multiconfig/factory.conf
echo 'require ${BSPDIR}/sources/meta-measy-linux/meta-measy-security/conf/multiconfig/myd-jmx95-dmverity-factory.conf' >> conf/multiconfig/factory.conf

if [ ! -e $BUILD_DIR/conf/bblayers.conf.org ]; then
    cp $BUILD_DIR/conf/bblayers.conf $BUILD_DIR/conf/bblayers.conf.org
else
    cp $BUILD_DIR/conf/bblayers.conf.org $BUILD_DIR/conf/bblayers.conf
fi


echo "" >> $BUILD_DIR/conf/bblayers.conf
echo "# i.MX Yocto Project Release layers" >> $BUILD_DIR/conf/bblayers.conf
hook_in_layer meta-myir/meta-myir-bsp
hook_in_layer meta-myir/meta-myir-sdk
hook_in_layer meta-myir/meta-myir-ml
hook_in_layer meta-myir/meta-myir-v2x
hook_in_layer meta-nxp-demo-experience
hook_in_layer meta-nxp-connectivity/meta-nxp-matter-baseline
hook_in_layer meta-nxp-connectivity/meta-nxp-openthread

echo "" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-arm/meta-arm\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-arm/meta-arm-toolchain\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-clang\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-openembedded/meta-gnome\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-openembedded/meta-networking\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-openembedded/meta-filesystems\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-openembedded/meta-perl\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-qt6\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-security/meta-parsec\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-security/meta-tpm\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-security\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-virtualization\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-freescale-ml\"" >> $BUILD_DIR/conf/bblayers.conf
echo "BBLAYERS += \"\${BSPDIR}/sources/meta-swupdate\"" >> $BUILD_DIR/conf/bblayers.conf

append_optional_layer()
{
    optional_layer="$1"
    if [ -f "${MYIR_SETUP_ROOT}/sources/${optional_layer}/conf/layer.conf" ]; then
        printf '\n## Optional layer: %s\n' "${optional_layer}" >> "$BUILD_DIR/conf/bblayers.conf"
        printf 'BBLAYERS += "${BSPDIR}/sources/%s"\n' "${optional_layer}" >> "$BUILD_DIR/conf/bblayers.conf"
    fi
}

append_optional_layer meta-measy-linux/meta-measy-support
append_optional_layer meta-measy-linux/meta-measy-security
append_optional_layer meta-measy-linux/meta-measy-ota

# Keep TOPDIR/keys stable while allowing production keys to live outside Git.
SECURITY_KEYS_SOURCE="${MEASY_SECURITY_KEYS_DIR:-${MYIR_SETUP_ROOT}/sources/meta-measy-linux/meta-measy-security/files/keys}"
if [ -d "${SECURITY_KEYS_SOURCE}" ]; then
    if [ -L "$BUILD_DIR/keys" ] || [ ! -e "$BUILD_DIR/keys" ]; then
        ln -sfn "${SECURITY_KEYS_SOURCE}" "$BUILD_DIR/keys"
        echo "Secure boot keys: $BUILD_DIR/keys -> ${SECURITY_KEYS_SOURCE}"
    else
        echo "WARNING: $BUILD_DIR/keys exists and is not a symlink; leaving it unchanged."
    fi
else
    echo "WARNING: Secure boot keys not found at ${SECURITY_KEYS_SOURCE}"
fi

# NXP development board images are more useful with the empty root password.
echo -e "\n====================================== Important Note =========================================="
echo "This setup is optimized for NXP development boards and is not appropriate for production boards."

bitbake-config-build enable-fragment core/yocto/root-login-with-empty-password

echo -e "================================================================================================\n"

echo BSPDIR=$BSPDIR
echo BUILD_DIR=$BUILD_DIR

cd  $BUILD_DIR
myir_setup_cleanup
