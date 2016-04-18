package freifunk.bremen.de.mobilemeshviewer.node;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.NodeListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.NodeList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class NodeChecker {

    @Inject
    private PreferenceController preferenceController;
    @Inject
    private RetrofitServiceManager retrofitServiceManager;
    private Optional<NodeList> currentNodeListOptional = Optional.absent();
    @Inject
    private Gson gson;

    public Optional<NodeList> fetchList() {
        return currentNodeListOptional;
    }

    public void reloadList() {
        final Optional<NodeList> newNodeListOptional = loadList();
        currentNodeListOptional = newNodeListOptional;
        Collections.sort(currentNodeListOptional.get().getNodes());
        checkForChange(newNodeListOptional);
        EventBus.getDefault().post(new NodeListUpdatedEvent());
        Log.i(this.getClass().getSimpleName(), "Node list reloaded");
    }

    private void checkForChange(Optional<NodeList> newNodeListOptional) {
        final List<Node> observedNodeList = preferenceController.getObservedNodeList();
        final List<Node> newNodeList = Lists.newArrayList(newNodeListOptional.get().getNodes());

        Log.d(this.getClass().getSimpleName(), "Determining node status of observed nodes");
        for (final Node observedNode : observedNodeList) {
            Optional<Node> newNodeOptional = Iterables.tryFind(newNodeList, new Predicate<Node>() {
                @Override
                public boolean apply(Node input) {
                    return input.equals(observedNode);
                }
            });
            if (newNodeOptional.isPresent()) {
                determineStatus(observedNode, newNodeOptional.get());
            } else {
                Log.w(this.getClass().getSimpleName(), "Observed node " + observedNode.getName() + " could not be found new list. Preserving old state.");
            }
        }
    }

    private void determineStatus(Node observedNode, Node newNode) {
        if (newNode != null && newNode.getStatus().getOnline() != observedNode.getStatus().getOnline()) {
            preferenceController.addNodeToObservedNodeList(newNode);
            EventBus.getDefault().post(new NodeStatusChangedEvent(newNode));
        }
    }

    private Optional<NodeList> loadList() {
        final FreifunkRestConsumer freifunkService;
        Optional<NodeList> nodeListOpt = Optional.absent();
        try {
            freifunkService = retrofitServiceManager.getFreifunkService();
            Call<NodeList> call = freifunkService.getNodeList();
            Response<NodeList> response = call.execute();
            if (response.isSuccessful()) {
                nodeListOpt = Optional.fromNullable(response.body());
                Log.d(this.getClass().getSimpleName(), "Checked for new node list from server");
            } else {
                Log.w(this.getClass().getSimpleName(), "Response no success, error code: " + response.code());
            }
        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Unable to fetch NodeList");
        }
        return nodeListOpt;
    }

    public NodeDetail getDetailNodeById(String id) {
        final FreifunkRestConsumer freifunkService;
        NodeDetail node = new NodeDetail();
        try {
            freifunkService = retrofitServiceManager.getFreifunkService();
            Call<ResponseBody> call = freifunkService.getNodeDetailList();
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                InputStream is = response.body().byteStream();
                JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("nodes")){
                        reader.beginObject();
                    }else if (name.equals(id)) {
                        node = gson.fromJson(reader, new TypeToken<NodeDetail>() {
                        }.getType());
                        break;
                    }else {
                        reader.skipValue();
                    }
                }
                reader.close();
                Log.d(this.getClass().getSimpleName(), "Checked for new node list from server");
            } else {
                Log.w(this.getClass().getSimpleName(), "Response no success, error code: " + response.code());
            }
        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Unable to fetch NodeList");
        }
        return node;
    }
}