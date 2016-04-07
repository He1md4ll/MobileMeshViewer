package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NodeAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent nodeServiceIntent = new Intent(context, NodeCheckerService.class);
        context.startService(nodeServiceIntent);
        Log.i(this.getClass().getSimpleName(), "Received NodeAlarm from manager");
    }
}