package freifunk.bremen.de.mobilemeshviewer.event;

public class ReloadFinishedEvent {

    private boolean success;

    public ReloadFinishedEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}