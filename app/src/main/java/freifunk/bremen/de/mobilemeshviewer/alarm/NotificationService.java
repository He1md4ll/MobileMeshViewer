package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.MeshViewerActivity;
import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayActivity;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.node.NodeActivity;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import roboguice.service.RoboService;

public class NotificationService extends RoboService {

    @Inject
    private NotificationManager notificationManager;
    @Inject
    private PreferenceController preferenceController;

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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNodeStatusChanged(NodeStatusChangedEvent event) {
        final Node node = event.getNode();
        final String notificationTitle = "State of observed node changed";
        final String notificationText = "State of node " + node.getName() + " changed to " + node.getStatus();
        Intent resultIntent = new Intent(this, NodeActivity.class);
        resultIntent.putExtra(NodeActivity.BUNDLE_NODE, node);
        PendingIntent resultPendingIntent = getPendingIntent(resultIntent);
        buildNotification(notificationTitle, notificationText, resultPendingIntent);
        Log.d(this.getClass().getSimpleName(), "Build notification to inform about node change");
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGatewayStatusChanged(GatewayStatusChangedEvent event) {
        final Gateway gateway = event.getGateway();
        final String notificationTitle = "State of gateway changed";
        final String notificationText = gateway.getName() + " changed to " + gateway.getUplink();
        Intent resultIntent = new Intent(this, GatewayActivity.class);
        resultIntent.putExtra(GatewayActivity.BUNDLE_GATEWAY, gateway);
        PendingIntent resultPendingIntent = getPendingIntent(resultIntent);
        buildNotification(notificationTitle, notificationText, resultPendingIntent);
        Log.d(this.getClass().getSimpleName(), "Build notification to inform about gateway change");
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onGatewayListUpdated(GatewayListUpdatedEvent ignored) {
        stopSelf();
    }

    /* package */void buildNotification(String title, String text, PendingIntent resultPendingIntent) {
        final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        final int color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        final Uri sound = preferenceController.getNotificationSound();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(resultPendingIntent)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(bm)
                        .setColor(color)
                        .setLights(color, 2000, 3000)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true);

        if (!TextUtils.isEmpty(sound.getPath())) {
            mBuilder.setSound(sound, AudioManager.STREAM_NOTIFICATION);
        }
        if (preferenceController.isNotificationVibrationEnabled()) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        notificationManager.notify(0, mBuilder.build());
    }

    /* package */PendingIntent getPendingIntent(Intent resultIntent) {
        final Intent[] intents = new Intent[]{getParentIntent(), resultIntent};
        return PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent getParentIntent() {
        return new Intent(this, MeshViewerActivity.class);
    }
}