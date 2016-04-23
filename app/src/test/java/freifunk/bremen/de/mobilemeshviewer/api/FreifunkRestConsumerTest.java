package freifunk.bremen.de.mobilemeshviewer.api;

import com.google.inject.Inject;

import org.junit.Test;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import retrofit2.Call;

import static org.junit.Assert.assertFalse;

public class FreifunkRestConsumerTest extends RobolectricTest {

    @Inject
    private RetrofitServiceManager serviceManager;
    private FreifunkRestConsumer freifunkRestConsumer;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        freifunkRestConsumer = serviceManager.getFreifunkService();
    }

    @Test
    public void integrationTestGetNodeList() throws Exception {
        // When
        final Call<?> call = freifunkRestConsumer.getNodeList();

        // Then
        assertFalse(call.isExecuted());
        assertFalse(call.isCanceled());
    }

    @Test
    public void integrationTestGetNodeDetailList() throws Exception {
        // When
        final Call<?> call = freifunkRestConsumer.getNodeDetailList();

        // Then
        assertFalse(call.isExecuted());
        assertFalse(call.isCanceled());
    }
}