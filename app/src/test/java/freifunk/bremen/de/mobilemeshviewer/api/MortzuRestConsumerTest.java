package freifunk.bremen.de.mobilemeshviewer.api;

import com.google.inject.Inject;

import org.junit.Test;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import retrofit2.Call;

import static org.assertj.core.api.Assertions.assertThat;

public class MortzuRestConsumerTest extends RobolectricTest {

    @Inject
    private RetrofitServiceManager serviceManager;
    private MortzuRestConsumer mortzuRestConsumer;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mortzuRestConsumer = serviceManager.getMortzuService();
    }

    @Test
    public void integrationTestGetGatewayList() throws Exception {
        // When
        final Call<?> call = mortzuRestConsumer.getGatewayList();
        // Then
        assertThat(call.isExecuted()).isFalse();
        assertThat(call.isCanceled()).isFalse();
    }

}