package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.util.Log;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.api.MortzuRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.CheckServer;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.GatewayBO;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.VpnServer;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class GatewayChecker {

    @Inject
    private PreferenceController preferenceController;
    @Inject
    private RetrofitServiceManager retrofitServiceManager;
    private Optional<List<Gateway>> currentGatewayListOptional = Optional.absent();

    public Optional<List<Gateway>> fetchList() {
        return currentGatewayListOptional;
    }

    public void reloadList() {
        List<Gateway> newGatewayList = loadList();
        currentGatewayListOptional = Optional.of(newGatewayList);
        Collections.sort(currentGatewayListOptional.get());
        checkForChange(newGatewayList);
        EventBus.getDefault().post(new GatewayListUpdatedEvent());
        Log.i(this.getClass().getSimpleName(), "Gateway list list reloaded");
    }

    private void checkForChange(List<Gateway> newGatewayListImut) {
        Log.d(this.getClass().getSimpleName(), "Comparing gateway status");
        List<Gateway> currentGatewayList = preferenceController.getGatewayList();
        List<Gateway> newGatewayList = Lists.newArrayList(newGatewayListImut);

        for (final Gateway newGateway : newGatewayList) {
            Optional<Gateway> currentGatewayOptional = Iterables.tryFind(currentGatewayList, new Predicate<Gateway>() {
                @Override
                public boolean apply(Gateway input) {
                    return input.equals(newGateway);
                }
            });
            if (currentGatewayOptional.isPresent()) {
                compareState(currentGatewayOptional.get(), newGateway);
            } else {
                preferenceController.addGatewayToGatewayList(newGateway);
            }
        }
    }

    private void compareState(Gateway currentGateway, Gateway newGateway) {
        if (currentGateway.getUplink() != newGateway.getUplink() || currentGateway.getAddresses() != newGateway.getAddresses()) {
            preferenceController.addGatewayToGatewayList(newGateway);
            EventBus.getDefault().post(new GatewayStatusChangedEvent(newGateway));
        }
    }

    private List<Gateway> loadList() {
        final MortzuRestConsumer mortzuService;
        List<Gateway> gatewayList = Lists.newArrayList();
        try {
            mortzuService = retrofitServiceManager.getMortzuService();
            Call<List<CheckServer>> call = mortzuService.getGatewayList();
            Response<List<CheckServer>> response = call.execute();
            if (response.isSuccessful()) {
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
        Preconditions.checkArgument(!Iterables.isEmpty(checkServerList));
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

        return Lists.newArrayList(Lists.transform(Arrays.asList(gatewayBOArray), gatewayBoToGateway));
    }
}