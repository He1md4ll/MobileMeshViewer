package freifunk.bremen.de.mobilemeshviewer;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class PreferenceController {


    public static final String PREF_NODE_LIST_KEY = "pref_nodeList";
    public static final String PREF_GATEWAY_LIST_KEY = "pref_gatewayList";
    public static final String PREF_NODE_DETAIL_LIST_KEY = "pref_nodeDetailList";
    public static final String PREF_ALARM_INTERVAL = "pref_sync_frequency";
    public static final String PREF_NODE_MONITROING = "pref_node_mon";
    public static final String PREF_GATEWAY_MONITROING = "pref_gate_mon";
    public static final String PREF_NODE_DETAIL_LAST_UPDATE = "pref_node_detail_last_update";
    private static final long MINUTE_MULTIPLIER = 60 * 1000;
    private static final String DEFAULT_ALARM_INTERVAL = "5";
    private static final Boolean DEFAULT_NODE_MONITROING = Boolean.FALSE;
    private static final Boolean DEFAULT_GATEWAY_MONITROING = Boolean.FALSE;

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

    public void addGatewayToGatewayList(Gateway gateway) {
        List<Gateway> gatewayList = getGatewayList();
        gatewayList.remove(gateway);
        gatewayList.add(gateway);
        updateGatewayList(gatewayList);
    }

    public void updateGatewayList(List<Gateway> gatewayList) {
        final String jsonString = new Gson().toJson(gatewayList, new TypeToken<List<Gateway>>() {
        }.getType());
        sharedPreferences.edit().remove(PREF_GATEWAY_LIST_KEY).putString(PREF_GATEWAY_LIST_KEY, jsonString).apply();
        Log.d(this.getClass().getSimpleName(), "Saved updated gateway list to shared preferences");
    }

    public List<Gateway> getGatewayList() {
        Log.d(this.getClass().getSimpleName(), "Loading gateway list from shared preferences");
        final String jsonNodeList = sharedPreferences.getString(PREF_GATEWAY_LIST_KEY, "");
        return Optional.fromNullable(new Gson().<List<Gateway>>fromJson(jsonNodeList, new TypeToken<List<Gateway>>() {
        }.getType())).or(Lists.<Gateway>newArrayList());
    }

    public long getAlarmInterval() {
        final String alarmInterval = sharedPreferences.getString(PREF_ALARM_INTERVAL, DEFAULT_ALARM_INTERVAL);
        return Long.valueOf(alarmInterval) * MINUTE_MULTIPLIER;
    }

    public long getLastNodeDetailUpdate() {
        return sharedPreferences.getLong(PREF_NODE_DETAIL_LAST_UPDATE, 0);
    }

    public void saveNodeDetailInputStream(final InputStream stream) {
        try {
            String content = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
            sharedPreferences.edit().putString(PREF_NODE_DETAIL_LIST_KEY, content).apply();
            sharedPreferences.edit().putLong(PREF_NODE_DETAIL_LAST_UPDATE, new Date().getTime()).apply();
            Closeables.closeQuietly(stream);
        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Error while parsing InputStream of NodeDetail list", e);
        }
    }

    public String getNodeDetailString() {
        return sharedPreferences.getString(PREF_NODE_DETAIL_LIST_KEY, "");
    }

    public boolean isNodeMonitoringEnabled() {
        return sharedPreferences.getBoolean(PREF_NODE_MONITROING, DEFAULT_NODE_MONITROING);
    }

    public boolean isGatewayMonitoringEnabled() {
        return sharedPreferences.getBoolean(PREF_GATEWAY_MONITROING, DEFAULT_GATEWAY_MONITROING);
    }
}