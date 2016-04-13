package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeDetailLoader extends AsyncTaskLoader<NodeDetail> {

    @Inject
    private NodeController nodeController;
    NodeDetail node;

    @Inject
    public NodeDetailLoader(Context context) {
        super(context);
    }

    @Override
    public NodeDetail loadInBackground() {
        return nodeController.getDetailNodeById();
    }

    @Override
    public void deliverResult(NodeDetail node) {
        this.node = node;

        if (isStarted() && !this.node.equals(null)) {
            super.deliverResult(node);
        }
    }

    @Override
    protected void onStartLoading() {
        if (this.node != null) {
            deliverResult(this.node);
        }

        if (takeContentChanged() || this.node == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(NodeDetail node) {
        super.onCanceled(node);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (this.node != null) {
            this.node = null;
        }
    }
}