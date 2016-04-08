package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Statistics implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Statistics createFromParcel(Parcel in) {
                    return new Statistics(in);
                }

                public Statistics[] newArray(int size) {
                    return new Statistics[size];
                }
            };
    @SerializedName("uptime")
    @Expose
    private Double uptime;
    @SerializedName("clients")
    @Expose
    private int clients;
    @SerializedName("memory_usage")
    @Expose
    private Double memoryUsage;
    @SerializedName("rootfs_usage")
    @Expose
    private Double rootfsUsage;
    @SerializedName("gateway")
    @Expose
    private String gateway;
    @SerializedName("loadavg")
    @Expose
    private Double loadavg;
    @SerializedName("traffic")
    @Expose
    private Traffic traffic;


    public Statistics() {
    }

    public Statistics(Parcel in) {
        readFromParcel(in);
    }

    public Double getUptime() {
        return uptime;
    }

    public void setUptime(Double uptime) {
        this.uptime = uptime;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getRootfsUsage() {
        return rootfsUsage;
    }

    public void setRootfsUsage(Double rootfsUsage) {
        this.rootfsUsage = rootfsUsage;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Double getLoadavg() {
        return loadavg;
    }

    public void setLoadavg(Double loadavg) {
        this.loadavg = loadavg;
    }

    public Traffic getTraffic() {
        return traffic;
    }

    public void setTraffic(Traffic traffic) {
        this.traffic = traffic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(uptime);
        dest.writeInt(clients);
        dest.writeDouble(memoryUsage);
        dest.writeDouble(rootfsUsage);
        dest.writeString(gateway);
        dest.writeDouble(loadavg);
        dest.writeParcelable(traffic, flags);

    }

    private void readFromParcel(Parcel in) {
        uptime = in.readDouble();
        clients = in.readInt();
        memoryUsage = in.readDouble();
        rootfsUsage = in.readDouble();
        gateway = in.readString();
        loadavg = in.readDouble();
        traffic = in.readParcelable(Traffic.class.getClassLoader());
    }

}
