package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.util.Log;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.api.MortzuRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.CheckServer;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.GatewayBO;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.VpnServer;
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
        List<Gateway> newGatewayList = loadList();
        currentGatewayListOptional = Optional.<List<Gateway>>of(Lists.newArrayList(newGatewayList));
        checkForChange(newGatewayList);
        EventBus.getDefault().post(new GatewayListUpdatedEvent());
        Log.i(this.getClass().getSimpleName(), "CheckServer list reloaded");
    }

    private void checkForChange(List<Gateway> newGatewayList) {
        //TODO: Implement comparison
        if (currentGatewayListOptional.isPresent()) {
            List<Gateway> currentGatewayList = currentGatewayListOptional.get();
            for (Gateway currentGateway : currentGatewayList) {
            }
        }
    }

    private List<Gateway> loadList() {
        final MortzuRestConsumer mortzuService;
        List<Gateway> gatewayList = Lists.newArrayList();
        try {
            mortzuService = retrofitServiceManager.getMortzuService();
            Call<List<CheckServer>> call = mortzuService.getGatewayList();
            Response<List<CheckServer>> response = call.execute();
            if (response.isSuccess()) {
                Log.d(this.getClass().getSimpleName(), "Checked for new GatewayList from server");
                gatewayList = transformCheckServerToGateway(response.body());
            } else {
                Log.w(this.getClass().getSimpleName(), "Response no success, error code: " + response.code());
            }
        } catch (IOException | IllegalArgumentException e) {
            Log.w(this.getClass().getSimpleName(), "Unable to fetch GatewayList");
        }
        return gatewayList;
    }

    private List<Gateway> transformCheckServerToGateway(List<CheckServer> checkServerList) {
        Preconditions.checkArgument(checkServerList.size() > 0);
        Log.d(this.getClass().getSimpleName(), "Transforming GatewayList");
        final int vpnServerCount = checkServerList.get(0).getVpnServers().size();
        final int breakEven = vpnServerCount / 2;
        final GatewayBO[] gatewayBOArray = new GatewayBO[vpnServerCount];

        for (int i = 0; i < checkServerList.size(); i++) {
            final List<VpnServer> vpnServerList = checkServerList.get(i).getVpnServers();
            Collections.sort(vpnServerList);
            for (int j = 0; j < vpnServerList.size(); j++) {
                gatewayBOArray[j] = Optional.fromNullable(gatewayBOArray[j]).or(new GatewayBO());
                gatewayBOArray[j] = GatewayBO.countFromVpnServer(vpnServerList.get(j), gatewayBOArray[j]);
            }
        }

        Function<GatewayBO, Gateway> gatewayBoToGateway =
                new Function<GatewayBO, Gateway>() {
                    public Gateway apply(GatewayBO i) {
                        return Gateway.fromGatewayBO(i, breakEven);
                    }
                };

        return Lists.transform(Arrays.asList(gatewayBOArray), gatewayBoToGateway);
    }
}