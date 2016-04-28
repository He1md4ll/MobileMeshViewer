package freifunk.bremen.de.mobilemeshviewer.event;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;

public class NodeDetailFoundEvent {

    private NodeDetail node;

    public NodeDetailFoundEvent(@NonNull NodeDetail node) {
        Preconditions.checkNotNull(node);
        this.node = node;
    }

    public NodeDetail getNode() {
        return node;
    }
}
