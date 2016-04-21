package freifunk.bremen.de.mobilemeshviewer.gateway;

import com.google.common.base.Optional;

import freifunk.bremen.de.mobilemeshviewer.node.NodeDetailLoader;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class GatewayNodeDetailLoader extends NodeDetailLoader {

    @Override
    protected Optional<NodeDetail> doInBackground(String... params) {
        final Optional<Node> gatewayNodeOptional = nodeChecker.getGatewayByName(params[0]);
        if (gatewayNodeOptional.isPresent()) {
            return super.doInBackground(gatewayNodeOptional.get().getId());
        } else {
            return Optional.absent();
        }
    }
}