package freifunk.bremen.de.mobilemeshviewer.service;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import freifunk.bremen.de.mobilemeshviewer.api.MortzuRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.model.full.gateway.Gateway;
import freifunk.bremen.de.mobilemeshviewer.model.simple.NodeList;
import retrofit.Call;
import retrofit.Response;

public class GatewayCheckerService {

    public static final int DELAY = 15;

    @Inject
    private RetrofitServiceManager retrofitServiceManager;

    private List<Gateway> currentGatewayList;
    private ScheduledExecutorService executor;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final List<Gateway> gatewayList = fetchList();
            if (checkForChange(gatewayList)) {
                currentGatewayList = gatewayList;
                EventBus.getDefault().post(new GatewayChangedEvent());
            }

        }

        private boolean checkForChange(List<Gateway> gatewayList) {
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

    public List<Gateway> fetchList() {
        final MortzuRestConsumer mortzuService;
        List<Gateway> gatewayList = Lists.newArrayList();
        Optional<NodeList> nodeListOpt = Optional.absent();
        try {
            mortzuService = retrofitServiceManager.getMortzuService();
            Call<List<Gateway>> call = mortzuService.getGatewayList();
            Response<List<Gateway>> response = call.execute();
            if (response.isSuccess()) {
                gatewayList = response.body();
                Log.d(this.getClass().getSimpleName(), "Checked for new GatewayList from server");
            } else {
                Log.w(this.getClass().getSimpleName(), "Response no success, error code: " + response.code());
            }
        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Unable to fetch GatewayList");
        }
        return gatewayList;
    }
}