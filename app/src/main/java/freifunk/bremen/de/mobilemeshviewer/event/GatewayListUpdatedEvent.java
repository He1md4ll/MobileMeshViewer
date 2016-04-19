package freifunk.bremen.de.mobilemeshviewer.event;

public class GatewayListUpdatedEvent {

    private boolean success;

    public GatewayListUpdatedEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}