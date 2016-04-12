package freifunk.bremen.de.mobilemeshviewer.event;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;

public class GatewayStatusChangedEvent {

    private Gateway gateway;

    public GatewayStatusChangedEvent(Gateway gateway) {
        this.gateway = gateway;
    }

    public Gateway getGateway() {
        return gateway;
    }
}