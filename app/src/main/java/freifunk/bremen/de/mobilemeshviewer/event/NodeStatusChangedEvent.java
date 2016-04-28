package freifunk.bremen.de.mobilemeshviewer.event;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeStatusChangedEvent {

    private Node node;

    public NodeStatusChangedEvent(@NonNull Node node) {
        Preconditions.checkNotNull(node);
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}