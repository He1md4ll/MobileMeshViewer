package freifunk.bremen.de.mobilemeshviewer.event;

public class NodeListUpdatedEvent extends ReloadFinishedEvent {

    public NodeListUpdatedEvent(boolean success) {
        super(success);
    }
}