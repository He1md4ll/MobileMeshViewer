package freifunk.bremen.de.mobilemeshviewer.gateway.model;

public enum IpStatus {
    IPv4,
    IPv6,
    BOTH,
    NONE;

    public static IpStatus determineStatus(int ipv4, int ipv6, int breakpoint) {
        IpStatus status;
        if (ipv4 >= breakpoint && ipv6 >= breakpoint) {
            status = BOTH;
        } else if (ipv4 >= breakpoint) {
            status = IPv4;
        } else if (ipv6 >= breakpoint) {
            status = IPv6;
        } else {
            status = NONE;
        }
        return status;
    }
}
