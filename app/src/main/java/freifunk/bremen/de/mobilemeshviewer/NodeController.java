package freifunk.bremen.de.mobilemeshviewer;

import com.google.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.service.NodeCheckerService;

public class NodeController {

    @Inject
    private NodeCheckerService nodeCheckerService;

    private void start() {
        nodeCheckerService.startMonitoring();
    }

    private void stop() {
        nodeCheckerService.stopMonitoring();
    }
}