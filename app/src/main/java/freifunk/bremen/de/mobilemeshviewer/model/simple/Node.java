package freifunk.bremen.de.mobilemeshviewer.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Node {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("position")
    @Expose
    public Position position;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("status")
    @Expose
    public Status status;
}