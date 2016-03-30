package freifunk.bremen.de.mobilemeshviewer;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.model.simple.Node;

public class NodeListLoader extends AsyncTaskLoader<List<Node>> {

    @Inject
    private NodeController nodeController;
    private List<Node> nodeList;

    @Inject
    public NodeListLoader(Context context) {
        super(context);
    }

    @Override
    public List<Node> loadInBackground() {
        return nodeController.getSimpleNodeList();
    }

    @Override
    public void deliverResult(List<Node> nodeList) {
        this.nodeList = nodeList;

        if (isStarted()) {
            super.deliverResult(nodeList);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (this.nodeList != null) {
            deliverResult(this.nodeList);
        }

        // TODO: Implement event mechanism to inform about nodeChange
        // TODO: Register event observer
        boolean nodeChange = false;

        if (takeContentChanged() || this.nodeList == null || nodeChange) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Node> nodeList) {
        super.onCanceled(nodeList);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (this.nodeList != null) {
            this.nodeList = null;
        }
        //TODO: Unregister event observer
    }
}