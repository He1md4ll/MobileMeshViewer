package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anon on 08.04.2016.
 */
public class Flags implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Flags createFromParcel(Parcel in) {
                    return new Flags(in);
                }

                public Flags[] newArray(int size) {
                    return new Flags[size];
                }
            };
    @SerializedName("gateway")
    @Expose
    private String gateway;
    @SerializedName("online")
    @Expose
    private Boolean online;

    public Flags() {
    }

    public Flags(Parcel in) {
        readFromParcel(in);
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return online ? "Online" : "Offline";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gateway);
        dest.writeByte((byte) (online ? 1 : 0));
    }

    private void readFromParcel(Parcel in) {
        gateway = in.readString();
        online = in.readByte() != 0;
    }
}
