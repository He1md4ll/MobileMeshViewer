package freifunk.bremen.de.mobilemeshviewer.api;

import org.junit.Test;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import retrofit2.Call;

import static org.assertj.core.api.Assertions.assertThat;

public class FfhbRestConsumerTest extends RobolectricTest {

    @Inject
    RetrofitServiceManager serviceManager;
    private FfhbRestConsumer ffhbRestConsumer;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ffhbRestConsumer = serviceManager.getFfhbService();
    }

    @Test
    public void integrationTestGetGatewayList() throws Exception {
        // When
        final Call<?> call = ffhbRestConsumer.getGatewayList();
        // Then
        assertThat(call.isExecuted()).isFalse();
        assertThat(call.isCanceled()).isFalse();
    }

    @Override
    public void inject() {
        component.inject(this);
    }

}