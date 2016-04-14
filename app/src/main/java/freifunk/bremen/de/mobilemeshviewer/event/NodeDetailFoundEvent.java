package freifunk.bremen.de.mobilemeshviewer.event;

import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;

public class NodeDetailFoundEvent {

    private NodeDetail node;

    public NodeDetailFoundEvent(NodeDetail node) {
        this.node = node;
    }

    public NodeDetail getNode() {
        return node;
    }
}
