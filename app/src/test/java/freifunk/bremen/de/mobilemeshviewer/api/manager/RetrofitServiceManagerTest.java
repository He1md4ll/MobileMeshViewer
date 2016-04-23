package freifunk.bremen.de.mobilemeshviewer.api.manager;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.MortzuRestConsumer;

import static org.junit.Assert.assertTrue;

public class RetrofitServiceManagerTest extends RobolectricTest {

    @Inject
    private RetrofitServiceManager classUnderTest;
    private ConnectionManager connectionManager;

    @Override
    public void setUp() throws Exception {
        // Spy on object to create stub for specific mocking
        // Reason for stubbing: cannot mock Retrofit object
        connectionManager = Mockito.spy(new ConnectionManager());
        super.setUp();
    }

    /**
     * @return module to override bindings
     */
    @Override
    public AbstractModule getModuleForInjection() {
        return new TestModule();
    }

    @Test
    public void testGetFreifunkService() throws Exception {
        // Given
        Mockito.when(connectionManager.isNetworkAvailable()).thenReturn(Boolean.TRUE);

        // When
        Optional<FreifunkRestConsumer> freifunkRestConsumerOptional = Optional.fromNullable(classUnderTest.getFreifunkService());

        // Then
        assertTrue(freifunkRestConsumerOptional.isPresent());
        Mockito.verify(connectionManager).isNetworkAvailable();
        Mockito.verify(connectionManager).getRetrofitConnection(ConnectionManager.URL_FREIFUNK);
    }

    @Test(expected = IOException.class)
    public void testGetFreifunkServiceNoConnection() throws Exception {
        // Given
        Mockito.when(connectionManager.isNetworkAvailable()).thenReturn(Boolean.FALSE);

        // When - Exception
        classUnderTest.getFreifunkService();
    }

    @Test
    public void testGetMortzuService() throws Exception {
        // Given
        Mockito.when(connectionManager.isNetworkAvailable()).thenReturn(Boolean.TRUE);

        // When
        Optional<MortzuRestConsumer> mortzuRestConsumerOptional = Optional.fromNullable(classUnderTest.getMortzuService());

        // Then
        assertTrue(mortzuRestConsumerOptional.isPresent());
        Mockito.verify(connectionManager).isNetworkAvailable();
        Mockito.verify(connectionManager).getRetrofitConnection(ConnectionManager.URL_MORTZU);
    }

    @Test(expected = IOException.class)
    public void testGetMortzuServiceNoConnection() throws Exception {
        // Given
        Mockito.when(connectionManager.isNetworkAvailable()).thenReturn(Boolean.FALSE);

        // When - Exception
        classUnderTest.getMortzuService();
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            // Replace injected class with mock
            bind(ConnectionManager.class).toInstance(connectionManager);
        }
    }
}