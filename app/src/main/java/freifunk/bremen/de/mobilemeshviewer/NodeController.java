package freifunk.bremen.de.mobilemeshviewer;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.model.simple.NodeList;
import freifunk.bremen.de.mobilemeshviewer.service.NodeCheckerService;

public class NodeController {

    @Inject
    private NodeCheckerService nodeCheckerService;

    public void start() {
        nodeCheckerService.startMonitoring();
    }

    public void stop() {
        nodeCheckerService.stopMonitoring();
    }

    public List<Node> getSimpleNodeList() {
        final Optional<NodeList> nodeListOpt = nodeCheckerService.fetchList();
        if (nodeListOpt.isPresent()) {
            return nodeListOpt.get().getNodes();
        } else {
            return Lists.newArrayList();
        }
    }
}