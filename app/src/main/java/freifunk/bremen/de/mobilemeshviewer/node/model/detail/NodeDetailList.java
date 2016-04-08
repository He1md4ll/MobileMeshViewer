package freifunk.bremen.de.mobilemeshviewer.node.model.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeDetailList {

    @SerializedName("nodes")
    @Expose
    private String nodes;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getNodes() {
        return nodes;
    }

    public String getVersion() {
        return version;
    }

    public String getTimestamp() {
        return timestamp;
    }
}