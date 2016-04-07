package freifunk.bremen.de.mobilemeshviewer.node;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.SettingsActivity;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.NodeList;

public class NodeController {

    private static final long intervall = 1000 * 60 * 5;

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

    public void addNodeToObservedNodeList(Node node) {
        final List<Node> observedNodeList = getObservedNodeList();
        observedNodeList.remove(node);
        observedNodeList.add(node);
        final String jsonString = new Gson().toJson(observedNodeList, new TypeToken<List<Node>>() {
        }.getType());
        sharedPreferences.edit().putString(SettingsActivity.NODE_LIST_KEY, jsonString).apply();
    }

    public List<Node> getObservedNodeList() {
        final String jsonNodeList = sharedPreferences.getString(SettingsActivity.NODE_LIST_KEY, "");
        return Optional.fromNullable(new Gson().<List<Node>>fromJson(jsonNodeList, new TypeToken<List<Node>>() {
        }.getType())).or(Lists.<Node>newArrayList());
    }

    public void startNodeAlarm() {
        PendingIntent pendingAlarmIntent = stopNodeAlarm();
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, intervall, intervall, pendingAlarmIntent);
    }

    public PendingIntent stopNodeAlarm() {
        final Intent alarmIntent = new Intent(context, NodeAlarmReceiver.class);
        final PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.cancel(pendingAlarmIntent);
        return pendingAlarmIntent;
    }
}