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
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, -1, 1000 * 60, getPendingIntent());
    }

    public void stopNodeAlarm() {
        alarmManager.cancel(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        final Intent alarmIntent = new Intent(context, NodeAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
    }
}