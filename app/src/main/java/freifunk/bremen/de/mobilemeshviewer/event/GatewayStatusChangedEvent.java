package freifunk.bremen.de.mobilemeshviewer.event;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;

public class GatewayStatusChangedEvent {

    private Gateway gateway;

    public GatewayStatusChangedEvent(@NonNull Gateway gateway) {
        Preconditions.checkNotNull(gateway);
        this.gateway = gateway;
    }

    public Gateway getGateway() {
        return gateway;
    }
}