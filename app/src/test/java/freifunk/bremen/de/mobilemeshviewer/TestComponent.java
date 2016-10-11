package freifunk.bremen.de.mobilemeshviewer;

import javax.inject.Singleton;

import dagger.Component;
import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmControllerTest;
import freifunk.bremen.de.mobilemeshviewer.alarm.BootUpReceiverTest;
import freifunk.bremen.de.mobilemeshviewer.alarm.CheckerServiceTest;
import freifunk.bremen.de.mobilemeshviewer.alarm.NotificationServiceTest;
import freifunk.bremen.de.mobilemeshviewer.api.FfhbRestConsumerTest;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumerTest;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManagerTest;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerComponent;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerModule;
import freifunk.bremen.de.mobilemeshviewer.converter.NodeDetailConverterTest;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayListFragmentTest;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayNodeDetailLoaderTest;
import freifunk.bremen.de.mobilemeshviewer.node.NodeDetailLoaderTest;

@Singleton
@Component(modules = {MeshViewerModule.class})
public interface TestComponent extends MeshViewerComponent {
    void inject(AlarmControllerTest test);

    void inject(BootUpReceiverTest test);

    void inject(CheckerServiceTest test);

    void inject(NotificationServiceTest test);

    void inject(RetrofitServiceManagerTest test);

    void inject(FfhbRestConsumerTest test);

    void inject(FreifunkRestConsumerTest test);

    void inject(NodeDetailConverterTest test);

    void inject(GatewayListFragmentTest test);

    void inject(GatewayNodeDetailLoaderTest test);

    void inject(NodeDetailLoaderTest test);

    void inject(MeshViewerActivityTest test);
}