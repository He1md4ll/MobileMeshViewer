package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Nodeinfo implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Nodeinfo createFromParcel(Parcel in) {
                    return new Nodeinfo(in);
                }

                public Nodeinfo[] newArray(int size) {
                    return new Nodeinfo[size];
                }
            };

    public Nodeinfo() {
    }

    public Nodeinfo(Parcel in) {
        readFromParcel(in);
    }

    @SerializedName("hardware")
    @Expose
    private Hardware hardware;
    @SerializedName("node_id")
    @Expose
    private String nodeId;
    @SerializedName("software")
    @Expose
    private Software software;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("system")
    @Expose
    private System system;
    @SerializedName("network")
    @Expose
    private Network network;
    @SerializedName("hostname")
    @Expose
    private String hostname;
    @SerializedName("location")
    @Expose
    private Location location;

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(hardware, flags);
        dest.writeString(nodeId);
        dest.writeParcelable(software, flags);
        dest.writeParcelable(system, flags);
        dest.writeParcelable(network, flags);
        dest.writeString(hostname);
        dest.writeParcelable(owner, flags);
        dest.writeParcelable(location, flags);
    }

    private void readFromParcel(Parcel in) {
        hardware = in.readParcelable(Hardware.class.getClassLoader());
        nodeId = in.readString();
        software = in.readParcelable(Software.class.getClassLoader());
        system = in.readParcelable(System.class.getClassLoader());
        network = in.readParcelable(Network.class.getClassLoader());
        hostname = in.readString();
        owner = in.readParcelable(Owner.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
    }

}
