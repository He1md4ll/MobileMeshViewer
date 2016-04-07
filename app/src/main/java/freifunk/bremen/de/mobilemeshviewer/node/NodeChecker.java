package freifunk.bremen.de.mobilemeshviewer.node;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.SettingsActivity;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.NodeListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.NodeList;
import retrofit.Call;
import retrofit.Response;

public class NodeChecker {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    private RetrofitServiceManager retrofitServiceManager;
    private Optional<NodeList> currentNodeListOptional = Optional.absent();

    public Optional<NodeList> fetchList() {
        if (currentNodeListOptional.isPresent()) {
            return currentNodeListOptional;
        } else {
            return loadList();
        }
    }

    public void reloadList() {
        final Optional<NodeList> newNodeListOptional = loadList();
        checkForChange(newNodeListOptional);
        EventBus.getDefault().post(new NodeListUpdatedEvent());
        Log.i(this.getClass().getSimpleName(), "Node list reloaded");
    }

    private void checkForChange(Optional<NodeList> newNodeListOptional) {
        final List<Node> observedNodeList = getObservedNodeList();
        final List<Node> newNodeList = Lists.newArrayList(newNodeListOptional.get().getNodes());

        newNodeList.retainAll(observedNodeList);

        Log.d(this.getClass().getSimpleName(), "Determining node status of observed nodes");
        for (Node observedNode : observedNodeList) {
            final int newNodeIndex = newNodeList.indexOf(observedNode);
            final Node newNode = newNodeList.get(newNodeIndex);
            determineStatus(observedNode, newNode);
        }
    }

    private void determineStatus(Node observedNode, Node newNode) {
        if (newNode != null && newNode.getStatus().getOnline() != observedNode.getStatus().getOnline()) {
            EventBus.getDefault().post(new NodeStatusChangedEvent(newNode));
        }
    }

    private List<Node> getObservedNodeList() {
        Log.d(this.getClass().getSimpleName(), "Retrieving observed node list");
        final String jsonNodeList = sharedPreferences.getString(SettingsActivity.NODE_LIST_KEY, "");
        return Optional.fromNullable(new Gson().<List<Node>>fromJson(jsonNodeList, new TypeToken<List<Node>>() {
        }.getType())).or(Lists.<Node>newArrayList());
    }

    private Optional<NodeList> loadList() {
        final FreifunkRestConsumer freifunkService;
        Optional<NodeList> nodeListOpt = Optional.absent();
        try {
            freifunkService = retrofitServiceManager.getFreifunkService();
            Call<NodeList> call = freifunkService.getNodeList();
            Response<NodeList> response = call.execute();
            if (response.isSuccess()) {
                nodeListOpt = Optional.fromNullable(response.body());
                if (!currentNodeListOptional.isPresent()) {
                    currentNodeListOptional = nodeListOpt;
                }
                Log.d(this.getClass().getSimpleName(), "Checked for new node list from server");
            } else {
                Log.w(this.getClass().getSimpleName(), "Response no success, error code: " + response.code());
            }
        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Unable to fetch NodeList");
        }
        return nodeListOpt;
    }
}