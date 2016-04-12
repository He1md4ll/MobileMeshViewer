
package freifunk.bremen.de.mobilemeshviewer.gateway.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckServer {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("vpn-servers")
    @Expose
    private List<VpnServer> vpnServers = new ArrayList<VpnServer>();
    @SerializedName("lastupdated")
    @Expose
    private String lastupdated;

    /**
     * @return The uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid The uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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
     * @return The provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider The provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return The vpnServers
     */
    public List<VpnServer> getVpnServers() {
        return vpnServers;
    }

    /**
     * @param vpnServers The vpn-servers
     */
    public void setVpnServers(List<VpnServer> vpnServers) {
        this.vpnServers = vpnServers;
    }

    /**
     * @return The lastupdated
     */
    public String getLastupdated() {
        return lastupdated;
    }

    /**
     * @param lastupdated The lastupdated
     */
    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }
}