package freifunk.bremen.de.mobilemeshviewer.binding;

import javax.inject.Singleton;

import dagger.Component;
import freifunk.bremen.de.mobilemeshviewer.MeshViewerActivity;
import freifunk.bremen.de.mobilemeshviewer.SettingsActivity;
import freifunk.bremen.de.mobilemeshviewer.alarm.BootUpReceiver;
import freifunk.bremen.de.mobilemeshviewer.alarm.CheckerService;
import freifunk.bremen.de.mobilemeshviewer.alarm.NotificationService;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayActivity;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayListFragment;
import freifunk.bremen.de.mobilemeshviewer.node.MyNodesFragment;
import freifunk.bremen.de.mobilemeshviewer.node.NodeActivity;
import freifunk.bremen.de.mobilemeshviewer.node.NodeListFragment;

@Singleton
@Component(modules = {MeshViewerModule.class})
public interface MeshViewerComponent {
    void inject(MeshViewerActivity meshViewerActivity);

    void inject(GatewayActivity gatewayActivity);

    void inject(MyNodesFragment myNodesFragment);

    void inject(NodeActivity nodeActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(NodeListFragment nodeListFragment);

    void inject(GatewayListFragment gatewayListFragment);

    void inject(NotificationService notificationService);

    void inject(CheckerService checkerService);

    void inject(BootUpReceiver bootUpReceiver);
}
