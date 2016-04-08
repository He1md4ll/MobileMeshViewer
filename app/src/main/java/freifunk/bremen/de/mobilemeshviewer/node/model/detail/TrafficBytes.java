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
    private int packets;
    @SerializedName("bytes")
    @Expose
    private int bytes;

    public TrafficBytes() {
    }

    public TrafficBytes(Parcel in) {
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
    }

    private void readFromParcel(Parcel in) {
        bytes = in.readInt();
        packets = in.readInt();
    }
}
