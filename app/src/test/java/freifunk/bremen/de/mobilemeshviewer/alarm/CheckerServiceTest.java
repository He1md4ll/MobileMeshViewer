package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.Intent;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.gateway.GatewayChecker;
import freifunk.bremen.de.mobilemeshviewer.node.NodeChecker;

public class CheckerServiceTest extends RobolectricTest {

    @Inject
    private CheckerService classUnderTest;
    @Mock
    private NodeChecker nodeChecker;
    @Mock
    private GatewayChecker gatewayChecker;

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
    public AbstractModule getModuleForInjection() {
        return new TestModule();
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            // Replace injected class with mock
            bind(NodeChecker.class).toInstance(nodeChecker);
            bind(GatewayChecker.class).toInstance(gatewayChecker);
        }
    }
}