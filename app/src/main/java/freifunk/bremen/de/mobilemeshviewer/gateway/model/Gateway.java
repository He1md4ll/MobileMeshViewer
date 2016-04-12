package freifunk.bremen.de.mobilemeshviewer.gateway.model;

public class Gateway {
    private String name;
    private IpStatus ntp;
    private IpStatus addresses;
    private IpStatus dns;
    private IpStatus uplink;

    public Gateway(String name, IpStatus ntp, IpStatus addresses, IpStatus dns, IpStatus uplink) {
        this.name = name;
        this.ntp = ntp;
        this.addresses = addresses;
        this.dns = dns;
        this.uplink = uplink;
    }

    public static Gateway fromGatewayBO(GatewayBO gatewayBO, int breakEven) {
        final IpStatus ntpStatus = IpStatus.determineStatus(gatewayBO.getNtpSum4(), gatewayBO.getNtpSum6(), breakEven);
        final IpStatus addressesStatus = IpStatus.determineStatus(gatewayBO.getAddressesSum4(), gatewayBO.getAddressesSum6(), breakEven);
        final IpStatus dnsStatus = IpStatus.determineStatus(gatewayBO.getDnsSum4(), gatewayBO.getDnsSum6(), breakEven);
        final IpStatus uplinkStatus = IpStatus.determineStatus(gatewayBO.getUplinkSum4(), gatewayBO.getUplinkSum6(), breakEven);
        return new Gateway(gatewayBO.getName(), ntpStatus, addressesStatus, dnsStatus, uplinkStatus);
    }

    public String getName() {
        return name;
    }

    public IpStatus getNtp() {
        return ntp;
    }

    public IpStatus getAddresses() {
        return addresses;
    }

    public IpStatus getDns() {
        return dns;
    }

    public IpStatus getUplink() {
        return uplink;
    }

    @Override
    public String toString() {
        return name + " [" + uplink + "]";
    }
}