package freifunk.bremen.de.mobilemeshviewer.node;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class NodeController {

    @Inject
    private NodeChecker nodeChecker;

    public List<Node> getSimpleNodeList() {
        return nodeChecker.fetchList().or(Lists.<Node>newArrayList());
    }

    public NodeDetail getDetailNodeById(String id) {
        //TODO: Cache json
        return nodeChecker.getDetailNodeById(id);
    }
}