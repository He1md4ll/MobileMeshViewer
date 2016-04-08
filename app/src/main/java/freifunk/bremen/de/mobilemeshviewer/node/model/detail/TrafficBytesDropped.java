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
    private int packets;
    @SerializedName("bytes")
    @Expose
    private int bytes;
    @SerializedName("dropped")
    @Expose
    private int dropped;

    public TrafficBytesDropped() {
    }

    public TrafficBytesDropped(Parcel in) {
        readFromParcel(in);
    }

    public int getPackets() {
        return packets;
    }

    public void setPackets(int packets) {
        this.packets = packets;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public int getDropped() {
        return dropped;
    }

    public void setDropped(int dropped) {
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
        dest.writeInt(bytes);
        dest.writeInt(packets);
        dest.writeInt(dropped);
    }

    private void readFromParcel(Parcel in) {
        bytes = in.readInt();
        packets = in.readInt();
        dropped = in.readInt();
    }
}
