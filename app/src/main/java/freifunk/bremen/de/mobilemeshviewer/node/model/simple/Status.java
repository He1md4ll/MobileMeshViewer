package freifunk.bremen.de.mobilemeshviewer.node.model.simple;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status implements Parcelable {

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Status createFromParcel(Parcel in) {
                    return new Status(in);
                }

                public Status[] newArray(int size) {
                    return new Status[size];
                }
            };
    @SerializedName("clients")
    @Expose
    private Long clients;
    @SerializedName("lastcontact")
    @Expose
    private String lastcontact;
    @SerializedName("online")
    @Expose
    private Boolean online;

    public Status() {
    }

    public Status(Parcel in) {
        readFromParcel(in);
    }

    public Long getClients() {
        return clients;
    }

    public void setClients(Long clients) {
        this.clients = clients;
    }

    public String getLastcontact() {
        return lastcontact;
    }

    public void setLastcontact(String lastcontact) {
        this.lastcontact = lastcontact;
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
        dest.writeLong(clients);
        dest.writeString(lastcontact);
        dest.writeByte((byte) (online ? 1 : 0));
    }

    private void readFromParcel(Parcel in) {
        clients = in.readLong();
        lastcontact = in.readString();
        online = in.readByte() != 0;
    }
}