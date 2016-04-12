package freifunk.bremen.de.mobilemeshviewer.gateway.model;

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
        if (vpnServer.getNtp().get(0).getIpv4() == 1) {
            gatewayBO.incNtpSum4();
        }
        if (vpnServer.getNtp().get(0).getIpv6() == 1) {
            gatewayBO.incNtpSum6();
        }
        if (vpnServer.getAddresses().get(0).getIpv4() == 1) {
            gatewayBO.incAddressesSum4();
        }
        if (vpnServer.getAddresses().get(0).getIpv6() == 1) {
            gatewayBO.incAddressesSum6();
        }
        if (vpnServer.getDns().get(0).getIpv4() == 1) {
            gatewayBO.incDnsSum4();
        }
        if (vpnServer.getDns().get(0).getIpv6() == 1) {
            gatewayBO.incDnsSum6();
        }
        if (vpnServer.getUplink().get(0).getIpv4() == 1) {
            gatewayBO.incUplinkSum4();
        }
        if (vpnServer.getUplink().get(0).getIpv6() == 1) {
            gatewayBO.incUplinkSum6();
        }
        return gatewayBO;
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