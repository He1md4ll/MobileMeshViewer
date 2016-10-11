package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.Checkable;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerApp;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class CheckerService extends IntentService {

    @Inject
    Checkable<Node> nodeChecker;
    @Inject
    Checkable<Gateway> gatewayChecker;

    public CheckerService() {
        super(CheckerService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MeshViewerApp) getApplication()).getMeshViewerComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i(this.getClass().getSimpleName(), "Executing service to reload data");
        nodeChecker.reloadList();
        gatewayChecker.reloadList();
    }
}