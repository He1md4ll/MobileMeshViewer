package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;

public class AlarmReceiver extends BroadcastReceiver {

    private static boolean alarmProcessing = false;

    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        if (!alarmProcessing) {
            alarmProcessing = true;
            EventBus.getDefault().register(this);
            final Intent nodeServiceIntent = new Intent(context, CheckerService.class);
            context.startService(nodeServiceIntent);
            final Intent notificationServiceIntent = new Intent(context, NotificationService.class);
            context.startService(notificationServiceIntent);
            Log.i(this.getClass().getSimpleName(), "Received NodeAlarm from manager");
        } else {
            Log.w(this.getClass().getSimpleName(), "Last update still running: Skipping alarm");
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNodeListUpdated(GatewayListUpdatedEvent ignored) {
        EventBus.getDefault().unregister(this);
        alarmProcessing = false;
        Log.i(this.getClass().getSimpleName(), "Alarm successfully processed");
    }
}