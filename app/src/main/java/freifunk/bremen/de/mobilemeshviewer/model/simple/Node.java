package freifunk.bremen.de.mobilemeshviewer.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Node {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("position")
    @Expose
    private Position position;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private Status status;

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name + " [" + status + "]";
    }
}