
package freifunk.bremen.de.mobilemeshviewer.gateway.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("ipv4")
    @Expose
    private Integer ipv4;
    @SerializedName("ipv6")
    @Expose
    private Integer ipv6;

    /**
     * @return The ipv4
     */
    public Integer getIpv4() {
        return ipv4;
    }

    /**
     * @param ipv4 The ipv4
     */
    public void setIpv4(Integer ipv4) {
        this.ipv4 = ipv4;
    }

    /**
     * @return The ipv6
     */
    public Integer getIpv6() {
        return ipv6;
    }

    /**
     * @param ipv6 The ipv6
     */
    public void setIpv6(Integer ipv6) {
        this.ipv6 = ipv6;
    }
}