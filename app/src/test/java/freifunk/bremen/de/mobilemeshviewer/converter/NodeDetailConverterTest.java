package freifunk.bremen.de.mobilemeshviewer.converter;

import android.content.Context;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

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

    @Test
    public void testConvertUptimeMinute() throws Exception {
        // Given
        final double uptime = 60;
        final double uptime1 = 119;

        // When
        String uptimeString = classUnderTest.convertUptime(uptime);
        String uptimeString1 = classUnderTest.convertUptime(uptime1);

        // Then
        assertThat(uptimeString).isEqualTo("1 Minute");
        assertThat(uptimeString).isEqualTo("1 Minute");
    }

    @Test
    public void testConvertUptimeMinutes() throws Exception {
        // Given
        final double uptime = 0;
        final double uptime1 = 59;
        final double uptime2 = 120;
        final double uptime3 = 3599;

        // When
        String uptimeString = classUnderTest.convertUptime(uptime);
        String uptimeString1 = classUnderTest.convertUptime(uptime1);
        String uptimeString2 = classUnderTest.convertUptime(uptime2);
        String uptimeString3 = classUnderTest.convertUptime(uptime3);


        // Then
        assertThat(uptimeString).isEqualTo("0 Minuten");
        assertThat(uptimeString1).isEqualTo("0 Minuten");
        assertThat(uptimeString2).isEqualTo("2 Minuten");
        assertThat(uptimeString3).isEqualTo("59 Minuten");
    }

    @Test
    public void testConvertUptimeHour() throws Exception {
        // Given
        final double uptime = 3600;
        final double uptime1 = 7199;

        // When
        String uptimeString = classUnderTest.convertUptime(uptime);
        String uptimeString1 = classUnderTest.convertUptime(uptime1);

        // Then
        assertThat(uptimeString).isEqualTo("1 Stunde");
        assertThat(uptimeString).isEqualTo("1 Stunde");
    }

    @Test
    public void testConvertUptimeHours() throws Exception {
        // Given
        final double uptime = 7200;
        final double uptime1 = 86399;

        // When
        String uptimeString = classUnderTest.convertUptime(uptime);
        String uptimeString1 = classUnderTest.convertUptime(uptime1);


        // Then
        assertThat(uptimeString).isEqualTo("2 Stunden");
        assertThat(uptimeString1).isEqualTo("23 Stunden");
    }

    @Test
    public void testConvertUptimeDay() throws Exception {
        // Given
        final double uptime = 86400;
        final double uptime1 = 172799;

        // When
        String uptimeString = classUnderTest.convertUptime(uptime);
        String uptimeString1 = classUnderTest.convertUptime(uptime1);

        // Then
        assertThat(uptimeString).isEqualTo("1 Tag");
        assertThat(uptimeString1).isEqualTo("1 Tag");

    }

    @Test
    public void testConvertUptimeDays() throws Exception {
        // Given
        final double uptime = 172800;
        final double uptime1 = 345600;

        // When
        String uptimeString = classUnderTest.convertUptime(uptime);
        String uptimeString1 = classUnderTest.convertUptime(uptime1);

        // Then
        assertThat(uptimeString).isEqualTo("2 Tage");
        assertThat(uptimeString1).isEqualTo("4 Tage");

    }
}