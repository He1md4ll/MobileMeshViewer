package freifunk.bremen.de.mobilemeshviewer.gateway.model;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

// TODO: Refactor states of Ntp, Address, Dn and Uplink --> Interface
public class GatewayBO {

    private String name;
    private int ntpSum4 = 0;
    private int ntpSum6 = 0;
    private int addressesSum4 = 0;
    private int addressesSum6 = 0;
    private int dnsSum4 = 0;
    private int dnsSum6 = 0;
    private int uplinkSum4 = 0;
    private int uplinkSum6 = 0;

    public static GatewayBO countFromVpnServer(VpnServer vpnServer, GatewayBO gatewayBO) {
        gatewayBO.setName(vpnServer.getName());

        determineNtpStatus(vpnServer, gatewayBO);
        determineAddressStatus(vpnServer, gatewayBO);
        determineDnsStatus(vpnServer, gatewayBO);
        determineUplinkStatus(vpnServer, gatewayBO);

        return gatewayBO;
    }

    private static void determineUplinkStatus(VpnServer vpnServer, GatewayBO gatewayBO) {
        final Optional<Uplink> uplinkStatus = Optional.fromNullable(Iterables.getFirst(vpnServer.getUplink(), null));
        if (uplinkStatus.isPresent() && uplinkStatus.get().getIpv4() == 1) {
            gatewayBO.incUplinkSum4();
        }
        if (uplinkStatus.isPresent() && uplinkStatus.get().getIpv6() == 1) {
            gatewayBO.incUplinkSum6();
        }
    }

    private static void determineDnsStatus(VpnServer vpnServer, GatewayBO gatewayBO) {
        final Optional<Dn> dnsStatus = Optional.fromNullable(Iterables.getFirst(vpnServer.getDns(), null));
        if (dnsStatus.isPresent() && dnsStatus.get().getIpv4() == 1) {
            gatewayBO.incDnsSum4();
        }
        if (dnsStatus.isPresent() && dnsStatus.get().getIpv6() == 1) {
            gatewayBO.incDnsSum6();
        }
    }

    private static void determineAddressStatus(VpnServer vpnServer, GatewayBO gatewayBO) {
        final Optional<Address> addressStatus = Optional.fromNullable(Iterables.getFirst(vpnServer.getAddresses(), null));
        if (addressStatus.isPresent() && addressStatus.get().getIpv4() == 1) {
            gatewayBO.incAddressesSum4();
        }
        if (addressStatus.isPresent() && addressStatus.get().getIpv6() == 1) {
            gatewayBO.incAddressesSum6();
        }
    }

    private static void determineNtpStatus(VpnServer vpnServer, GatewayBO gatewayBO) {
        final Optional<Ntp> ntpStatus = Optional.fromNullable(Iterables.getFirst(vpnServer.getNtp(), null));
        if (ntpStatus.isPresent() && ntpStatus.get().getIpv4() == 1) {
            gatewayBO.incNtpSum4();
        }
        if (ntpStatus.isPresent() && ntpStatus.get().getIpv6() == 1) {
            gatewayBO.incNtpSum6();
        }
    }

    public void incNtpSum4() {
        ntpSum4++;
    }

    public void incNtpSum6() {
        ntpSum6++;
    }

    public void incAddressesSum4() {
        addressesSum4++;
    }

    public void incAddressesSum6() {
        addressesSum6++;
    }

    public void incDnsSum4() {
        dnsSum4++;
    }

    public void incDnsSum6() {
        dnsSum6++;
    }

    public void incUplinkSum4() {
        uplinkSum4++;
    }

    public void incUplinkSum6() {
        uplinkSum6++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNtpSum4() {
        return ntpSum4;
    }

    public int getNtpSum6() {
        return ntpSum6;
    }

    public int getAddressesSum4() {
        return addressesSum4;
    }

    public int getAddressesSum6() {
        return addressesSum6;
    }

    public int getDnsSum4() {
        return dnsSum4;
    }

    public int getDnsSum6() {
        return dnsSum6;
    }

    public int getUplinkSum4() {
        return uplinkSum4;
    }

    public int getUplinkSum6() {
        return uplinkSum6;
    }
}