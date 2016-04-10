package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.Intent;
import android.util.Log;

import com.google.inject.Inject;

import roboguice.service.RoboIntentService;

public class NodeCheckerService extends RoboIntentService {

    @Inject
    private NodeChecker nodeChecker;

    @Inject
    public NodeCheckerService() {
        super(NodeCheckerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i(this.getClass().getSimpleName(), "Executing service to reload node list");
        nodeChecker.reloadList();
    }
}