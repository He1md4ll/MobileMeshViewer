package freifunk.bremen.de.mobilemeshviewer.node.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NodeList {

    @SerializedName("nodes")
    @Expose
    private List<Node> nodes = new ArrayList<Node>();
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public List<Node> getNodes() {
        return nodes;
    }

    public String getVersion() {
        return version;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}