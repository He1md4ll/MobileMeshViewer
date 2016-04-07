
package freifunk.bremen.de.mobilemeshviewer.gateway.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VpnServer {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ntp")
    @Expose
    private List<Ntp> ntp = new ArrayList<Ntp>();
    @SerializedName("addresses")
    @Expose
    private List<Address> addresses = new ArrayList<Address>();
    @SerializedName("dns")
    @Expose
    private List<Dn> dns = new ArrayList<Dn>();
    @SerializedName("uplink")
    @Expose
    private List<Uplink> uplink = new ArrayList<Uplink>();

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The ntp
     */
    public List<Ntp> getNtp() {
        return ntp;
    }

    /**
     * @param ntp The ntp
     */
    public void setNtp(List<Ntp> ntp) {
        this.ntp = ntp;
    }

    /**
     * @return The addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses The addresses
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return The dns
     */
    public List<Dn> getDns() {
        return dns;
    }

    /**
     * @param dns The dns
     */
    public void setDns(List<Dn> dns) {
        this.dns = dns;
    }

    /**
     * @return The uplink
     */
    public List<Uplink> getUplink() {
        return uplink;
    }

    /**
     * @param uplink The uplink
     */
    public void setUplink(List<Uplink> uplink) {
        this.uplink = uplink;
    }

    @Override
    public String toString() {
        return uplink.get(0).getIpv4() == 1 || uplink.get(0).getIpv6() == 1 ? "Online" : "Offline";
    }
}