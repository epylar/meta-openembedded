SUMMARY = "netcf"
DESCRIPTION = "netcf is a cross-platform network configuration library."
HOMEPAGE = "https://fedorahosted.org/netcf/"
SECTION = "libs"
LICENSE = "LGPLv2.1"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbb461211a33b134d42ed5ee802b37ff"

SRCREV = "9158278ad35b46ce9a49b2e887483c6d8c287994"
PV = "0.2.8+git${SRCPV}"

SRC_URI = "git://git.fedorahosted.org/netcf.git;protocol=git \
"

DEPENDS += "augeas libnl libxslt libxml2 gnulib"

S = "${WORKDIR}/git"

inherit gettext autotools-brokensep pkgconfig systemd

EXTRA_OECONF_append_class-target = " --with-driver=redhat"

PACKAGECONFIG ??= "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "systemd", "", d)}"
PACKAGECONFIG[systemd] = "--with-sysinit=systemd,--with-sysinit=initscripts,"

do_configure_prepend() {
	cd ${S}
	rm -f .gitmodules
	./bootstrap --gnulib-srcdir=${STAGING_DATADIR}/gnulib
}

do_install_append() {
    if ${@base_contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
       install -d ${D}${systemd_unitdir}/system
       if [ -d "${D}${libdir}/systemd/system" ]; then
           mv ${D}${libdir}/systemd/system/* ${D}${systemd_unitdir}/system/
           rm -rf ${D}${libdir}/systemd/
       else
           mv ${D}${nonarch_libdir}/systemd/system/* ${D}${systemd_unitdir}/system/
           rm -rf ${D}${nonarch_libdir}/systemd/
       fi
    else
       mv ${D}${sysconfdir}/rc.d/init.d/ ${D}${sysconfdir}
       rm -rf ${D}${sysconfdir}/rc.d/
    fi
}

FILES_${PN} += " \
        ${libdir} \
        ${nonarch_libdir} \
        "

SYSTEMD_SERVICE_${PN} = "netcf-transaction.service"
