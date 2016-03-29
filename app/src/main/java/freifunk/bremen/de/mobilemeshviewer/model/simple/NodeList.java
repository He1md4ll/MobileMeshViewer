package freifunk.bremen.de.mobilemeshviewer.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NodeList {

    @SerializedName("nodes")
    @Expose
    public List<Node> nodes = new ArrayList<Node>();
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
}