SUMMARY = "Network benchmark tool"
DESCRIPTION = "\
iperf is a tool for active measurements of the maximum achievable bandwidth \
on IP networks. It supports tuning of various parameters related to timing, \
protocols, and buffers. For each test it reports the bandwidth, loss, and \
other parameters."

HOMEPAGE = "https://software.es.net/iperf/"
SECTION = "console/network"
BUGTRACKER = "https://github.com/esnet/iperf/issues"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f2eb355b6d3b9d63b6b7a861cdc62440"

SRC_URI = "git://github.com/esnet/iperf.git;branch=master;protocol=https;tag=${PV} \
           file://0002-Remove-pg-from-profile_CFLAGS.patch \
           file://0001-configure.ac-check-for-CPP-prog.patch \
          "
SRCREV = "926bd905501b792bd082d2200d1d1a84aa543e3a"

COMPATIBLE_MACHINE = "(^myd-jmx95x-b0$)"

CVE_STATUS[CVE-2026-101276] = "fixed-version: Fixed in upstream 3.22 by d58ad3d6014ba9217c95031f8937cda39e5b3d61"
CVE_STATUS[CVE-2026-101283] = "fixed-version: Fixed in upstream 3.22 by d08588aceee48fa45832cae5e32616abb33cb0fc"

RDEPENDS:${PN} = "libgcc"

inherit autotools

PACKAGECONFIG ?= "openssl"
PACKAGECONFIG[lksctp] = "ac_cv_header_netinet_sctp_h=yes,ac_cv_header_netinet_sctp_h=no,lksctp-tools"
PACKAGECONFIG[openssl] = "--with-openssl=${RECIPE_SYSROOT}${prefix},--without-openssl,openssl"

CFLAGS += "-D_GNU_SOURCE"

BBCLASSEXTEND = "native nativesdk"
