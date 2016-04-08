package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeDetail implements Parcelable {

    public static final Creator CREATOR =
            new Creator() {
                public NodeDetail createFromParcel(Parcel in) {
                    return new NodeDetail(in);
                }

                public NodeDetail[] newArray(int size) {
                    return new NodeDetail[size];
                }
            };
    @SerializedName("lastseen")
    @Expose
    private String lastseen;
    @SerializedName("nodeinfo")
    @Expose
    private Nodeinfo nodeinfo;
    @SerializedName("flagsNode")
    @Expose
    private Flags flagsNode;
    @SerializedName("firstseen")
    @Expose
    private String firstseen;
    @SerializedName("statistics")
    @Expose
    private Statistics statistics;

    public NodeDetail() {
    }

    public NodeDetail(Parcel in) {
        readFromParcel(in);
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public Nodeinfo getNodeinfo() {
        return nodeinfo;
    }

    public void setNodeInfo(Nodeinfo nodeinfo) {
        this.nodeinfo = nodeinfo;
    }

    public Flags getFlagsNode() {
        return flagsNode;
    }

    public void setFlagsNode(Flags flagsNode) {
        this.flagsNode = flagsNode;
    }

    public String getFirstseen() {
        return firstseen;
    }

    public void setFirstseen(String firstseen) {
        this.firstseen = firstseen;
    }

    public void setStatistics(Statistics statistics){
        this.statistics = statistics;
    }

    public Statistics getStatistics(){
        return this.statistics;
    }

    @Override
    public String toString() {
        return lastseen + " [" + firstseen + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastseen);
        dest.writeParcelable(nodeinfo, flags);
        dest.writeParcelable(flagsNode, flags);
        dest.writeString(firstseen);
        dest.writeParcelable(statistics, flags);
    }

    private void readFromParcel(Parcel in) {
        lastseen = in.readString();
        nodeinfo = in.readParcelable(Nodeinfo.class.getClassLoader());
        flagsNode = in.readParcelable(Flags.class.getClassLoader());
        firstseen = in.readString();
        statistics = in.readParcelable(Statistics.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        /*if (this == o) return true;
        if (!(o instanceof NodeDetail)) return false;
        NodeDetail node = (NodeDetail) o;
        return Objects.equal(id, node.id);*/
        return false;
    }

    @Override
    public int hashCode() {
        /*return Objects.hashCode(id);*/
        return 0;
    }
}