package freifunk.bremen.de.mobilemeshviewer.api;

import org.junit.Test;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import retrofit2.Call;

import static org.assertj.core.api.Assertions.assertThat;

public class FreifunkRestConsumerTest extends RobolectricTest {

    @Inject
    RetrofitServiceManager serviceManager;
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
        assertThat(call.isExecuted()).isFalse();
        assertThat(call.isCanceled()).isFalse();
    }

    @Test
    public void integrationTestGetNodeDetailList() throws Exception {
        // When
        final Call<?> call = freifunkRestConsumer.getNodeDetailList();

        // Then
        assertThat(call.isExecuted()).isFalse();
        assertThat(call.isCanceled()).isFalse();
    }

    @Override
    public void inject() {
        component.inject(this);
    }
}