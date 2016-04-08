package freifunk.bremen.de.mobilemeshviewer.node.model.simple;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NodeDetailList {

    @SerializedName("nodes")
    @Expose
    private List<Node> nodes = new ArrayList<Node>();
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public List<Node> getNodes() {
        return nodes;
    }

    public String getVersion() {
        return version;
    }

    public String getTimestamp() {
        return timestamp;
    }
}