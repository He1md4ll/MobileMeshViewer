package freifunk.bremen.de.mobilemeshviewer;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class PreferenceController {


    public static final String PREF_NODE_LIST_KEY = "pref_nodeList";
    public static final String PREF_ALARM_INTERVAL = "pref_sync_frequency";
    private static final long MINUTE_MULTIPLIER = 60 * 1000;
    private static final String DEFAULT_ALARM_INTERVAL = "5";
    @Inject
    private SharedPreferences sharedPreferences;

    public void addNodeToObservedNodeList(Node node) {
        final List<Node> observedNodeList = getObservedNodeList();
        observedNodeList.remove(node);
        observedNodeList.add(node);
        final String jsonString = new Gson().toJson(observedNodeList, new TypeToken<List<Node>>() {
        }.getType());
        sharedPreferences.edit().remove(PREF_NODE_LIST_KEY).putString(PREF_NODE_LIST_KEY, jsonString).apply();
        Log.d(this.getClass().getSimpleName(), "Added observed node to shared preferences");
    }

    public List<Node> getObservedNodeList() {
        Log.d(this.getClass().getSimpleName(), "Loading observed node list from shared preferences");
        final String jsonNodeList = sharedPreferences.getString(PREF_NODE_LIST_KEY, "");
        return Optional.fromNullable(new Gson().<List<Node>>fromJson(jsonNodeList, new TypeToken<List<Node>>() {
        }.getType())).or(Lists.<Node>newArrayList());
    }

    public long getAlarmInterval() {
        final String alarmInterval = sharedPreferences.getString(PREF_ALARM_INTERVAL, DEFAULT_ALARM_INTERVAL);
        return Long.valueOf(alarmInterval) * MINUTE_MULTIPLIER;
    }
}