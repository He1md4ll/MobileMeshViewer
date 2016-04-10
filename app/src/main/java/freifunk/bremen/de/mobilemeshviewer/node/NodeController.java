package freifunk.bremen.de.mobilemeshviewer.node;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
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
    private PreferenceController preferenceController;

    public List<Node> getSimpleNodeList() {
        final Optional<NodeList> nodeListOpt = nodeChecker.fetchList();
        if (nodeListOpt.isPresent()) {
            return nodeListOpt.get().getNodes();
        } else {
            return Lists.newArrayList();
        }
    }

    public void startNodeAlarm() {
        //TODO: Listen for interval changes
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, -1000, preferenceController.getAlarmInterval(), getPendingIntent());
    }

    public void stopNodeAlarm() {
        alarmManager.cancel(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        final Intent alarmIntent = new Intent(context, NodeAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}