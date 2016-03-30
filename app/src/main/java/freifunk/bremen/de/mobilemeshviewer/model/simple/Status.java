package freifunk.bremen.de.mobilemeshviewer.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("clients")
    @Expose
    private Long clients;
    @SerializedName("lastcontact")
    @Expose
    private String lastcontact;
    @SerializedName("online")
    @Expose
    private Boolean online;

    public Long getClients() {
        return clients;
    }

    public String getLastcontact() {
        return lastcontact;
    }

    public Boolean getOnline() {
        return online;
    }

    @Override
    public String toString() {
        return online ? "Online" : "Offline";
    }
}