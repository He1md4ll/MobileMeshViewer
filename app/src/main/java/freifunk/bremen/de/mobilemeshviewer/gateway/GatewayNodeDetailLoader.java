package freifunk.bremen.de.mobilemeshviewer.gateway;

import com.google.common.base.Optional;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.Checkable;
import freifunk.bremen.de.mobilemeshviewer.node.NodeChecker;
import freifunk.bremen.de.mobilemeshviewer.node.NodeDetailLoader;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class GatewayNodeDetailLoader extends NodeDetailLoader {

    @Inject
    public GatewayNodeDetailLoader(Checkable<Node> nodeChecker) {
        super(nodeChecker);
    }

    @Override
    protected Optional<NodeDetail> doInBackground(String... params) {
        final Optional<Node> gatewayNodeOptional = ((NodeChecker) nodeChecker).getGatewayByName(params[0]);
        if (gatewayNodeOptional.isPresent()) {
            return super.doInBackground(gatewayNodeOptional.get().getId());
        } else {
            return Optional.absent();
        }
    }
}