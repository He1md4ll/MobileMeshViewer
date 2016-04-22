package freifunk.bremen.de.mobilemeshviewer.event;

public class GatewayListUpdatedEvent extends ReloadFinishedEvent {

    public GatewayListUpdatedEvent(boolean success) {
        super(success);
    }
}