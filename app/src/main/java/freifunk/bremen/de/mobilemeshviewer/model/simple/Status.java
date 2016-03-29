package freifunk.bremen.de.mobilemeshviewer.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("clients")
    @Expose
    public Long clients;
    @SerializedName("lastcontact")
    @Expose
    public String lastcontact;
    @SerializedName("online")
    @Expose
    public Boolean online;

    @Override
    public String toString() {
        return online ? "Online" : "Offline";
    }
}