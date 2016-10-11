package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.Intent;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import freifunk.bremen.de.mobilemeshviewer.Checkable;
import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;

public class CheckerServiceTest extends RobolectricTest {

    CheckerService classUnderTest;
    @Mock
    private Checkable<Node> nodeChecker;
    @Mock
    private Checkable<Gateway> gatewayChecker;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = Robolectric.buildService(CheckerService.class).create().attach().startCommand(0, 0).get();
        //component.inject(classUnderTest);
    }

    @Test
    public void testOnHandleIntent() throws Exception {
        // Given
        final Intent intent = new Intent(RuntimeEnvironment.application, CheckerService.class);

        // When
        classUnderTest.onHandleIntent(intent);

        // Then
        Mockito.verify(nodeChecker).reloadList();
        Mockito.verify(gatewayChecker).reloadList();
    }

    @Override
    public TestModule getModuleForInjection() {
        return new CustomTestModule();
    }

    @Override
    public void inject() {
        component.inject(this);
    }

    public class CustomTestModule extends TestModule {

        @Override
        public Checkable<Node> providesNodeChecker(PreferenceController preferenceController, RetrofitServiceManager retrofitServiceManager) {
            return nodeChecker;
        }

        @Override
        public Checkable<Gateway> providesGatewayChecker(PreferenceController preferenceController, RetrofitServiceManager retrofitServiceManager) {
            return gatewayChecker;
        }
    }
}