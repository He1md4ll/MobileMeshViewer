package freifunk.bremen.de.mobilemeshviewer.gateway;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;

public class GatewayController {

    @Inject
    private GatewayChecker gatewayChecker;

    public List<Gateway> getGatewayList() {
        return gatewayChecker.fetchList().or(Lists.<Gateway>newArrayList());
    }
}