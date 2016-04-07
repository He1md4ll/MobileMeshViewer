package freifunk.bremen.de.mobilemeshviewer.event;

import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeStatusChangedEvent {

    private Node node;
    private NodeStatusChange statusChange;

    public NodeStatusChangedEvent(NodeStatusChange statusChange, Node node) {
        this.node = node;
        this.statusChange = statusChange;
    }

    public Node getNode() {
        return node;
    }

    public NodeStatusChange getStatusChange() {
        return statusChange;
    }
}