package freifunk.bremen.de.mobilemeshviewer.gateway;

import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;

public class GatewayController {

    @Inject
    private GatewayCheckerService gatewayCheckerService;

    public void start() {
        gatewayCheckerService.startMonitoring();
    }

    public void stop() {
        gatewayCheckerService.stopMonitoring();
    }

    public List<Gateway> getGatewayList() {
        return gatewayCheckerService.fetchList();
    }
}