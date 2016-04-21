package freifunk.bremen.de.mobilemeshviewer.node;

import android.os.AsyncTask;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailNotFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;

public class NodeDetailLoader extends AsyncTask<String, Void, Optional<NodeDetail>> {

    @Inject
    protected NodeChecker nodeChecker;

    @Override
    protected Optional<NodeDetail> doInBackground(String... params) {
        return nodeChecker.getDetailNodeById(params[0]);
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