package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.Intent;
import android.util.Log;

import com.google.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayChecker;
import freifunk.bremen.de.mobilemeshviewer.node.NodeChecker;
import roboguice.service.RoboIntentService;

public class CheckerService extends RoboIntentService {

    @Inject
    private NodeChecker nodeChecker;
    @Inject
    private GatewayChecker gatewayChecker;

    @Inject
    public CheckerService() {
        super(CheckerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i(this.getClass().getSimpleName(), "Executing service to reload data");
        nodeChecker.reloadList();
        gatewayChecker.reloadList();
    }
}