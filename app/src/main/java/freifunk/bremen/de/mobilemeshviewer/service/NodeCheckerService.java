package freifunk.bremen.de.mobilemeshviewer.service;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private Optional<NodeList> currentNodeListOptional = Optional.absent();
    private ScheduledExecutorService executor;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final Optional<NodeList> nodeListOpt = loadList();
            if (checkForChange(nodeListOpt)) {
                currentNodeListOptional = nodeListOpt;
                EventBus.getDefault().post(new NodeChangedEvent());
            }
        }

        private boolean checkForChange(Optional<NodeList> nodeListOpt) {
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
        return currentNodeListOptional.or(loadList());
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