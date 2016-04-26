package freifunk.bremen.de.mobilemeshviewer.api;

import android.content.Context;

import com.google.inject.Inject;

import org.junit.Test;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.api.manager.RetrofitServiceManager;
import freifunk.bremen.de.mobilemeshviewer.converter.NodeDetailConverter;
import retrofit2.Call;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeDetailConverterTest extends RobolectricTest {

    @Inject
    private NodeDetailConverter classUnderTest;

    @Mock
    Context context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public AbstractModule getModuleForInjection() {
        return new TestModule();
    }

    @Test
    public void testConvertUptimeMinute() throws Exception {
        // Given
        Mockito.when(context.getString(Mockitor.anyString())).thenReturn("");
        final double uptime = "0";

        // When
        String uptimeString = classUnderTest.convertUptime(uptime)

        // Then
        assertEquals(uptimeString, "0 Minuten");
    }

}