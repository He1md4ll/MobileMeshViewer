package freifunk.bremen.de.mobilemeshviewer.api.manager;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.FfhbRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(freifunkRestConsumerOptional.isPresent()).isTrue();
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
    public void testGetFfhbService() throws Exception {
        // Given
        Mockito.when(connectionManager.isNetworkAvailable()).thenReturn(Boolean.TRUE);

        // When
        Optional<FfhbRestConsumer> ffhbRestConsumerOptional = Optional.fromNullable(classUnderTest.getFfhbService());

        // Then
        assertThat(ffhbRestConsumerOptional.isPresent()).isTrue();
        Mockito.verify(connectionManager).isNetworkAvailable();
        Mockito.verify(connectionManager).getRetrofitConnection(ConnectionManager.URL_FFHB);
    }

    @Test(expected = IOException.class)
    public void testGetFfhbServiceNoConnection() throws Exception {
        // Given
        Mockito.when(connectionManager.isNetworkAvailable()).thenReturn(Boolean.FALSE);

        // When - Exception
        classUnderTest.getFfhbService();
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            // Replace injected class with mock
            bind(ConnectionManager.class).toInstance(connectionManager);
        }
    }
}