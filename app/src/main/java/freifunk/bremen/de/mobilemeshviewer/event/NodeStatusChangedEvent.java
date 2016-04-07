package freifunk.bremen.de.mobilemeshviewer.event;

import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeStatusChangedEvent {

    private Node node;

    public NodeStatusChangedEvent(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}