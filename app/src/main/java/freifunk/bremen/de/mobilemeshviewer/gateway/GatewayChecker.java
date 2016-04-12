package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.api.MortzuRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import retrofit.Call;
import retrofit.Response;

@Singleton
public class GatewayChecker {

    @Inject
    private RetrofitServiceManager retrofitServiceManager;
    private Optional<List<Gateway>> currentGatewayListOptional = Optional.absent();

    public Optional<List<Gateway>> fetchList() {
        return currentGatewayListOptional;
    }

    public void reloadList() {
        final Optional<List<Gateway>> newGatewayListOptional = Optional.of(loadList());
        currentGatewayListOptional = newGatewayListOptional;
        checkForChange(newGatewayListOptional);
        EventBus.getDefault().post(new GatewayListUpdatedEvent());
        Log.i(this.getClass().getSimpleName(), "Gateway list reloaded");
    }

    private void checkForChange(Optional<List<Gateway>> newGatewayListOptional) {
        //TODO: Implement comparison
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