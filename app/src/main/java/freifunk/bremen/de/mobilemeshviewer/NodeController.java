package freifunk.bremen.de.mobilemeshviewer;

import com.google.inject.Inject;

import java.io.IOException;

import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.model.simple.NodeList;
import freifunk.bremen.de.mobilemeshviewer.service.NodeCheckerService;
import retrofit.Call;
import retrofit.Response;

public class NodeController {

    @Inject
    private NodeCheckerService nodeCheckerService;
    @Inject
    private RetrofitServiceManager retrofitServiceManager;


    public void start() {
        nodeCheckerService.startMonitoring();
    }

    public void stop() {
        nodeCheckerService.stopMonitoring();
    }

    public NodeList getSimpleNodeList() {
        try {
            final FreifunkRestConsumer freifunkService = retrofitServiceManager.getFreifunkService();
            final Call<NodeList> nodeList = freifunkService.getNodeList();
            final Response<NodeList> response = nodeList.execute();
            return response.body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}