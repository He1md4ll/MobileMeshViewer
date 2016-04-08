package freifunk.bremen.de.mobilemeshviewer.node;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.NodeList;

public class NodeController {

    @Inject
    private NodeChecker nodeChecker;
    @Inject
    private AlarmManager alarmManager;
    @Inject
    private Context context;
    @Inject
    private SharedPreferences sharedPreferences;

    public List<Node> getSimpleNodeList() {
        final Optional<NodeList> nodeListOpt = nodeChecker.fetchList();
        if (nodeListOpt.isPresent()) {
            return nodeListOpt.get().getNodes();
        } else {
            return Lists.newArrayList();
        }
    }

    public void startNodeAlarm() {
        PendingIntent pendingAlarmIntent = stopNodeAlarm();
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingAlarmIntent);
    }

    public PendingIntent stopNodeAlarm() {
        final Intent alarmIntent = new Intent(context, NodeAlarmReceiver.class);
        final PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.cancel(pendingAlarmIntent);
        return pendingAlarmIntent;
    }
}