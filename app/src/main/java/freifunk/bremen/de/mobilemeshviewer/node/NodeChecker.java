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
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChange;
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
        final Optional<NodeList> oldNodeListOptional = currentNodeListOptional.or(newNodeListOptional);
        currentNodeListOptional = newNodeListOptional;
        checkForChange(oldNodeListOptional);
        EventBus.getDefault().post(new NodeListUpdatedEvent());
        Log.i(this.getClass().getSimpleName(), "Node list reloaded");
    }

    private void checkForChange(Optional<NodeList> oldNodeListOptional) {
        final List<Node> observedNodeList = getOberservedNodeList();
        final List<Node> currentNodeList = Lists.newArrayList(currentNodeListOptional.get().getNodes());
        final List<Node> oldNodeList = Lists.newArrayList(oldNodeListOptional.get().getNodes());

        currentNodeList.retainAll(observedNodeList);
        oldNodeList.retainAll(observedNodeList);

        for (Node observedNode : observedNodeList) {
            Log.d(this.getClass().getSimpleName(), "Determining node status of observed nodes");
            final int currentNodeIndex = currentNodeList.indexOf(observedNode);
            final Node currentNode = currentNodeList.get(currentNodeIndex);
            final int oldNodeIndex = oldNodeList.indexOf(observedNode);
            final Node oldNode = oldNodeList.get(oldNodeIndex);
            determineStatus(currentNode, oldNode);
        }
    }

    private void determineStatus(Node currentNode, Node oldNode) {
        if (currentNode == null && oldNode != null) {
            EventBus.getDefault().post(new NodeStatusChangedEvent(NodeStatusChange.DISAPPEARED, currentNode));
        } else if (currentNode != null && oldNode == null) {
            EventBus.getDefault().post(new NodeStatusChangedEvent(NodeStatusChange.APPEARED, currentNode));
        } else if (currentNode != null && currentNode.getStatus().getOnline() != oldNode.getStatus().getOnline()) {
            if (currentNode.getStatus().getOnline()) {
                EventBus.getDefault().post(new NodeStatusChangedEvent(NodeStatusChange.WENT_ONLINE, currentNode));
            } else {
                EventBus.getDefault().post(new NodeStatusChangedEvent(NodeStatusChange.WENT_OFFLINE, currentNode));
            }
        }
    }

    private List<Node> getOberservedNodeList() {
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