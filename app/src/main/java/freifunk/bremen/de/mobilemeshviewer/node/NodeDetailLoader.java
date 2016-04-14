package freifunk.bremen.de.mobilemeshviewer.node;

import android.os.AsyncTask;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;

public class NodeDetailLoader extends AsyncTask<String, Void, NodeDetail> {

    @Inject
    private NodeController nodeController;
    NodeDetail node;

    @Override
    protected NodeDetail doInBackground(String... params) {
        return nodeController.getDetailNodeById(params[0]);
    }

    @Override
    protected void onPostExecute(NodeDetail nodeDetail) {
        EventBus.getDefault().post(new NodeDetailFoundEvent(nodeDetail));
        super.onPostExecute(nodeDetail);
    }
}