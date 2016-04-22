package freifunk.bremen.de.mobilemeshviewer;

import android.content.SharedPreferences;
import android.net.Uri;
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
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class PreferenceController {

    public static final String PREF_NODE_LIST_KEY = "pref_nodeList";
    public static final String PREF_GATEWAY_LIST_KEY = "pref_gatewayList";
    public static final String PREF_NODE_DETAIL_LIST_KEY = "pref_nodeDetailList";
    public static final String PREF_ALARM_INTERVAL = "pref_sync_frequency";
    public static final String PREF_NODE_MONITORING = "pref_node_mon";
    public static final String PREF_GATEWAY_MONITORING = "pref_gate_mon";
    public static final String PREF_NODE_DETAIL_LAST_UPDATE = "pref_node_detail_last_update";
    public static final String PREF_NOTIFICATION_SOUND = "pref_ringtone";
    public static final String PREF_VIBRATION = "pref_vibrate";
    public static final String PREF_AUTOSTART = "pref_autostart";
    private static final long MINUTE_MULTIPLIER = 60 * 1000;
    private static final String DEFAULT_ALARM_INTERVAL = "5";
    private static final Boolean DEFAULT_NODE_MONITORING = Boolean.FALSE;
    private static final Boolean DEFAULT_GATEWAY_MONITORING = Boolean.FALSE;
    private static final Boolean DEFAULT_VIBRATION = Boolean.FALSE;
    private static final Boolean DEFAULT_AUTOSTART = Boolean.FALSE;

    @Inject
    private SharedPreferences sharedPreferences;

    public void addNodeToObservedList(Node item) {
        final List<Node> observedList = getObservedNodeList();
        updateObservedList(observedList, item, PREF_NODE_LIST_KEY);
    }

    public void addGatewayToObservedList(Gateway item) {
        final List<Gateway> observedList = getObservedGatewayList();
        updateObservedList(observedList, item, PREF_GATEWAY_LIST_KEY);
    }

    public List<Node> getObservedNodeList() {
        Log.d(this.getClass().getSimpleName(), "Loading observed list from shared preferences");
        final String jsonList = sharedPreferences.getString(PREF_NODE_LIST_KEY, "");
        return Optional.fromNullable(new Gson().<List<Node>>fromJson(jsonList, new TypeToken<List<Node>>() {
        }.getType())).or(Lists.<Node>newArrayList());
    }

    public List<Gateway> getObservedGatewayList() {
        Log.d(this.getClass().getSimpleName(), "Loading observed list from shared preferences");
        final String jsonList = sharedPreferences.getString(PREF_NODE_LIST_KEY, "");
        return Optional.fromNullable(new Gson().<List<Gateway>>fromJson(jsonList, new TypeToken<List<Gateway>>() {
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
        return sharedPreferences.getBoolean(PREF_NODE_MONITORING, DEFAULT_NODE_MONITORING);
    }

    public boolean isGatewayMonitoringEnabled() {
        return sharedPreferences.getBoolean(PREF_GATEWAY_MONITORING, DEFAULT_GATEWAY_MONITORING);
    }

    public Uri getNotificationSound() {
        return Uri.parse(sharedPreferences.getString(PREF_NOTIFICATION_SOUND, ""));
    }

    public boolean isNotificationVibrationEnabled() {
        return sharedPreferences.getBoolean(PREF_VIBRATION, DEFAULT_VIBRATION);
    }

    public boolean isAutostartEnabled() {
        return sharedPreferences.getBoolean(PREF_AUTOSTART, DEFAULT_AUTOSTART);
    }

    private <T> void updateObservedList(List<T> observedList, T item, String pref) {
        observedList.remove(item);
        observedList.add(item);
        final String jsonString = new Gson().toJson(observedList, new TypeToken<List<T>>() {
        }.getType());
        sharedPreferences.edit().remove(pref).putString(pref, jsonString).apply();
        Log.d(this.getClass().getSimpleName(), "Added item to observed list in shared preferences");
    }
}