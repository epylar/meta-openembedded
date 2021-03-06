SUMMARY = "VPX multi-format codec"
DESCRIPTION = "The BSD-licensed libvpx reference implementation provides en- and decoders for VP8 and VP9 bitstreams."
HOMEPAGE = "http://www.webmproject.org/code/"
BUGTRACKER = "http://code.google.com/p/webm/issues/list"
SECTION = "libs/multimedia"
LICENSE = "BSD"

LIC_FILES_CHKSUM = "file://LICENSE;md5=d5b04755015be901744a78cc30d390d4"

SRC_URI += "http://storage.googleapis.com/downloads.webmproject.org/releases/webm/${BP}.tar.bz2 \
            file://libvpx-configure-support-blank-prefix.patch \
           "
SRC_URI[md5sum] = "63b1d7f59636a42eeeee9225cc14e7de"
SRC_URI[sha256sum] = "f582d9b2d60a592a4a3d8c32965ca2d2167e9ade38c6c30bac8801ff66a118e4"

# ffmpeg links with this and fails 
# sysroots/armv4t-oe-linux-gnueabi/usr/lib/libvpx.a(vpx_encoder.c.o)(.text+0xc4): unresolvable R_ARM_THM_CALL relocation against symbol `memcpy@@GLIBC_2.4'
ARM_INSTRUCTION_SET = "arm"

CFLAGS += "-fPIC"

export CC
export LD = "${CC}"

VPXTARGET_armv5te = "armv5te-linux-gcc"
VPXTARGET_armv6 = "armv6-linux-gcc"
VPXTARGET_armv7a = "armv7-linux-gcc"
VPXTARGET ?= "generic-gnu"

CONFIGUREOPTS = " \
    --target=${VPXTARGET} \
    --enable-vp8 \
    --enable-vp9 \
    --enable-libs \
    --disable-install-docs \
    --disable-static \
    --enable-shared \
    --prefix=${prefix} \
    --libdir=${libdir} \
"

do_configure() {
    ${S}/configure ${CONFIGUREOPTS}
}

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install DESTDIR=${D}
}

