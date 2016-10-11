package freifunk.bremen.de.mobilemeshviewer.node;

import android.os.AsyncTask;

import com.google.common.base.Optional;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.Checkable;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailNotFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeDetailLoader extends AsyncTask<String, Void, Optional<NodeDetail>> {

    protected Checkable<Node> nodeChecker;

    @Inject
    public NodeDetailLoader(Checkable<Node> nodeChecker) {
        this.nodeChecker = nodeChecker;
    }

    @Override
    protected Optional<NodeDetail> doInBackground(String... params) {
        return ((NodeChecker) nodeChecker).getDetailNodeById(params[0]);
    }

    @Override
    protected void onPostExecute(Optional<NodeDetail> nodeDetailOptional) {
        if (nodeDetailOptional.isPresent()) {
            EventBus.getDefault().post(new NodeDetailFoundEvent(nodeDetailOptional.get()));
        } else {
            EventBus.getDefault().post(new NodeDetailNotFoundEvent());
        }
    }
}