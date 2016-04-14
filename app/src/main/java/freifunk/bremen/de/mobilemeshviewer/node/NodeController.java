package freifunk.bremen.de.mobilemeshviewer.node;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.NodeList;

public class NodeController {

    @Inject
    private NodeChecker nodeChecker;

    public List<Node> getSimpleNodeList() {
        final Optional<NodeList> nodeListOpt = nodeChecker.fetchList();
        if (nodeListOpt.isPresent()) {
            return nodeListOpt.get().getNodes();
        } else {
            return Lists.newArrayList();
        }
    }

    public NodeDetail getDetailNodeById(String id) {
        //TODO: Cache json
        return nodeChecker.getDetailNodeById(id);
    }
}