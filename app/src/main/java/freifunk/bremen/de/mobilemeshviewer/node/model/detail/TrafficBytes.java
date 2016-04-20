package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class TrafficBytes implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public TrafficBytes createFromParcel(Parcel in) {
                    return new TrafficBytes(in);
                }

                public TrafficBytes[] newArray(int size) {
                    return new TrafficBytes[size];
                }
            };
    @SerializedName("packets")
    @Expose
    private long packets;
    @SerializedName("bytes")
    @Expose
    private double bytes;

    public TrafficBytes() {
    }

    public TrafficBytes(Parcel in) {
        readFromParcel(in);
    }

    public long getPackets() {
        return packets;
    }

    public void setPackets(long packets) {
        this.packets = packets;
    }

    public double getBytes() {
        return bytes;
    }

    public void setBytes(double bytes) {
        this.bytes = bytes;
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
        dest.writeDouble(bytes);
        dest.writeLong(packets);
    }

    private void readFromParcel(Parcel in) {
        bytes = in.readDouble();
        packets = in.readLong();
    }
}
