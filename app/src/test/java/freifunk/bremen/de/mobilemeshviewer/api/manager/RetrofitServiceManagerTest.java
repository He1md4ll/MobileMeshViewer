package freifunk.bremen.de.mobilemeshviewer.api.manager;

import android.net.ConnectivityManager;

import com.google.common.base.Optional;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.FfhbRestConsumer;
import freifunk.bremen.de.mobilemeshviewer.api.FreifunkRestConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class RetrofitServiceManagerTest extends RobolectricTest {

    @Inject
    RetrofitServiceManager classUnderTest;
    private ConnectionManager connectionManager;

    @Override
    public void setUp() throws Exception {
        // Spy on object to create stub for specific mocking
        // Reason for stubbing: cannot mock Retrofit object
        connectionManager = Mockito.spy(new ConnectionManager(Mockito.mock(ConnectivityManager.class)));
        super.setUp();
    }

    @Test
    public void testGetFreifunkService() throws Exception {
        // Given
        Mockito.doReturn(Boolean.TRUE).when(connectionManager).isNetworkAvailable();

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
        Mockito.doReturn(Boolean.FALSE).when(connectionManager).isNetworkAvailable();

        // When - Exception
        classUnderTest.getFreifunkService();
    }

    @Test
    public void testGetFfhbService() throws Exception {
        // Given
        Mockito.doReturn(Boolean.TRUE).when(connectionManager).isNetworkAvailable();

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
        Mockito.doReturn(Boolean.FALSE).when(connectionManager).isNetworkAvailable();

        // When - Exception
        classUnderTest.getFfhbService();
    }

    @Override
    public TestModule getModuleForInjection() {
        return new CustomTestModule();
    }

    @Override
    public void inject() {
        component.inject(this);
    }

    private class CustomTestModule extends TestModule {
        @Override
        public ConnectionManager providesConnectionManager(ConnectivityManager connectivityManager) {
            return connectionManager;
        }
    }
}