package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import freifunk.bremen.de.mobilemeshviewer.api.MortzuRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import retrofit.Call;
import retrofit.Response;

public class GatewayCheckerService {

    public static final int DELAY = 15;

    @Inject
    private RetrofitServiceManager retrofitServiceManager;

    private Optional<List<Gateway>> currentGatewayListOptional = Optional.absent();
    private ScheduledExecutorService executor;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final List<Gateway> gatewayList = loadList();
            if (checkForChange(gatewayList)) {
                currentGatewayListOptional = Optional.of(gatewayList);
                EventBus.getDefault().post(new GatewayListUpdatedEvent());
            }

        }

        private boolean checkForChange(List<Gateway> gatewayList) {
            List<Gateway> currentGatewayList = currentGatewayListOptional.get();
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
        return currentGatewayListOptional.or(loadList());
    }

    private List<Gateway> loadList() {
        final MortzuRestConsumer mortzuService;
        List<Gateway> gatewayList = Lists.newArrayList();
        try {
            mortzuService = retrofitServiceManager.getMortzuService();
            Call<List<Gateway>> call = mortzuService.getGatewayList();
            Response<List<Gateway>> response = call.execute();
            if (response.isSuccess()) {
                gatewayList = response.body();
                if (!currentGatewayListOptional.isPresent()) {
                    currentGatewayListOptional = Optional.of(gatewayList);
                }
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