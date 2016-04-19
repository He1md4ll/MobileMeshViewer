package freifunk.bremen.de.mobilemeshviewer.event;

public class NodeListUpdatedEvent {

    private boolean success;

    public NodeListUpdatedEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}