package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class NodeDetailList {

    @SerializedName("nodes")
    @Expose
    private Map<String, NodeDetail> nodes;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public Map<String, NodeDetail> getNodes() {
        return nodes;
    }

    public String getVersion() {
        return version;
    }

    public String getTimestamp() {
        return timestamp;
    }
}