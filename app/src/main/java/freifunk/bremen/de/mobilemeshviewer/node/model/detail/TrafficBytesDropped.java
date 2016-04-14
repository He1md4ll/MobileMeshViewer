package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class TrafficBytesDropped implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public TrafficBytesDropped createFromParcel(Parcel in) {
                    return new TrafficBytesDropped(in);
                }

                public TrafficBytesDropped[] newArray(int size) {
                    return new TrafficBytesDropped[size];
                }
            };
    @SerializedName("packets")
    @Expose
    private long packets;
    @SerializedName("bytes")
    @Expose
    private long bytes;
    @SerializedName("dropped")
    @Expose
    private long dropped;

    public TrafficBytesDropped() {
    }

    public TrafficBytesDropped(Parcel in) {
        readFromParcel(in);
    }

    public long getPackets() {
        return packets;
    }

    public void setPackets(long packets) {
        this.packets = packets;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getDropped() {
        return dropped;
    }

    public void setDropped(long dropped) {
        this.dropped = dropped;
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
        dest.writeLong(bytes);
        dest.writeLong(packets);
        dest.writeLong(dropped);
    }

    private void readFromParcel(Parcel in) {
        bytes = in.readLong();
        packets = in.readLong();
        dropped = in.readLong();
    }
}
