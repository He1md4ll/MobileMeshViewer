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

    @Inject
    private SharedPreferences sharedPreferences;

    public void addNodeToObservedNodeList(Node node) {
        Log.d(this.getClass().getSimpleName(), "Adding observed node to shared preferences");
        final List<Node> observedNodeList = getObservedNodeList();
        observedNodeList.remove(node);
        observedNodeList.add(node);
        final String jsonString = new Gson().toJson(observedNodeList, new TypeToken<List<Node>>() {
        }.getType());
        sharedPreferences.edit().remove(SettingsActivity.NODE_LIST_KEY).putString(SettingsActivity.NODE_LIST_KEY, jsonString).apply();
    }

    public List<Node> getObservedNodeList() {
        Log.d(this.getClass().getSimpleName(), "Loading observed node list from shared preferences");
        final String jsonNodeList = sharedPreferences.getString(SettingsActivity.NODE_LIST_KEY, "");
        return Optional.fromNullable(new Gson().<List<Node>>fromJson(jsonNodeList, new TypeToken<List<Node>>() {
        }.getType())).or(Lists.<Node>newArrayList());
    }
}
