package freifunk.bremen.de.mobilemeshviewer.binding;


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import freifunk.bremen.de.mobilemeshviewer.Checkable;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayChecker;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.node.NodeChecker;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class MobileMeshViewerBindings extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Checkable<Node>>() {
        }).to(NodeChecker.class);
        bind(new TypeLiteral<Checkable<Gateway>>() {
        }).to(GatewayChecker.class);
    }
}