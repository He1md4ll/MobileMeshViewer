package freifunk.bremen.de.mobilemeshviewer.service;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.NodeChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.model.simple.NodeList;
import retrofit.Call;
import retrofit.Response;

public class NodeCheckerService {

    public static final int DELAY = 15;

    @Inject
    private RetrofitServiceManager retrofitServiceManager;

    private NodeList currentList;
    private ScheduledExecutorService executor;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final Optional<NodeList> nodeListOpt = fetchList();
            if (nodeListOpt.isPresent()) {
                final NodeList nodeList = nodeListOpt.get();
                if (checkForChange(nodeList)) {
                    currentList = nodeList;
                    EventBus.getDefault().post(new NodeChangedEvent());
                }
            }
        }

        private boolean checkForChange(NodeList newNodeList) {
            //TODO: Implement comparison
            return false;
        }
    };

    public void startMonitoring() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, DELAY, DELAY, TimeUnit.SECONDS);
    }

    public void stopMonitoring() {
        executor.shutdown();
    }

    public Optional<NodeList> fetchList() {
        final FreifunkRestConsumer freifunkService;
        Optional<NodeList> nodeListOpt = Optional.absent();
        try {
            freifunkService = retrofitServiceManager.getFreifunkService();
            Call<NodeList> call = freifunkService.getNodeList();
            Response<NodeList> response = call.execute();
            if (response.isSuccess()) {
                nodeListOpt = Optional.fromNullable(response.body());
                Log.d(this.getClass().getSimpleName(), "Checked for new NodeList from server");
            } else {
                Log.w(this.getClass().getSimpleName(), "Response no success, error code: " + response.code());
            }
        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Unable to fetch NodeList");
        }
        return nodeListOpt;
    }
}