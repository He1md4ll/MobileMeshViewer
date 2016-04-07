package freifunk.bremen.de.mobilemeshviewer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.event.NodeListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.node.NodeActivity;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import roboguice.service.RoboService;

public class NotificationService extends RoboService {

    @Inject
    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this.getClass().getSimpleName(), "Starting service to build notification");
        EventBus.getDefault().register(this);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.d(this.getClass().getSimpleName(), "Stopping service");
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onNodeStatusChanged(NodeStatusChangedEvent event) {
        final Node node = event.getNode();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("State of observed node changed")
                        .setContentText("State of node " + node.getName() + " changed to " + node.getStatus())
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, NodeActivity.class);
        resultIntent.putExtra(NodeActivity.BUNDLE_NODE, node);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NodeActivity.class);
        stackBuilder.addParentStack(MeshViewerActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(0, mBuilder.build());
        Log.d(this.getClass().getSimpleName(), "Build notification to inform about node change");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onNodeListUpdated(NodeListUpdatedEvent ignored) {
        stopSelf();
    }
}