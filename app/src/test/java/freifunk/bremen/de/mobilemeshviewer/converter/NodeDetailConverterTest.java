package freifunk.bremen.de.mobilemeshviewer.converter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import freifunk.bremen.de.mobilemeshviewer.BuildConfig;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Autoupdater;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.Traffic;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.TrafficBytes;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.TrafficBytesDropped;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class NodeDetailConverterTest {

    private NodeDetailConverter classUnderTest;
    private Autoupdater autoupdater;
    private Traffic traffic;
    private TrafficBytesDropped trafficBytesDropped;

    @Before
    public void setUp() {
        classUnderTest = new NodeDetailConverter(RuntimeEnvironment.application);
        autoupdater = new Autoupdater();
        traffic = new Traffic();
        trafficBytesDropped = new TrafficBytesDropped();
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
        assertThat(uptimeString1).isEqualTo("1 Minute");
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
        assertThat(uptimeString1).isEqualTo("1 Stunde");
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

    @Test
    public void testConvertAutoUpdateEnabled() throws Exception {
        // Given
        final String branch = "stable";
        final boolean autoupdaterEnabled = true;
        autoupdater = new Autoupdater();
        autoupdater.setBranch(branch);
        autoupdater.setEnabled(autoupdaterEnabled);

        // When
        String autoupdaterString = classUnderTest.convertAutoUpdate(autoupdater);

        //Then
        assertThat(autoupdaterString).isEqualTo("aktiviert " + "(" + branch + ")");
    }

    @Test
    public void testConvertAutoUpdateDisabled() throws Exception {
        // Given
        final String branch = "stable";
        final boolean autoupdaterEnabled = false;
        autoupdater.setBranch(branch);
        autoupdater.setEnabled(autoupdaterEnabled);

        // When
        String autoupdaterString = classUnderTest.convertAutoUpdate(autoupdater);

        //Then
        assertThat(autoupdaterString).isEqualTo("deaktiviert " + "(" + branch + ")");
    }

    @Test
    public void testConvertTrafficZero() throws Exception {
        //Given
        TrafficBytes trafficBytes = new TrafficBytes();
        trafficBytes.setBytes(0);
        trafficBytesDropped.setBytes(0);
        traffic.setRx(trafficBytes);
        traffic.setTx(trafficBytesDropped);

        //When
        String trafficString = classUnderTest.convertTraffic(traffic);

        //Then
        assertThat(trafficString).isEqualTo("0GB / 0GB (out/in)");
    }

    @Test
    public void testConvertTraffic() throws Exception {
        //Given
        final double einGB = 1073741824;
        TrafficBytes trafficBytes = new TrafficBytes();
        trafficBytes.setBytes(einGB);
        trafficBytesDropped.setBytes(einGB);
        traffic.setRx(trafficBytes);
        traffic.setTx(trafficBytesDropped);

        //When
        String trafficString = classUnderTest.convertTraffic(traffic);

        //Then
        assertThat(trafficString).isEqualTo("1GB / 1GB (out/in)");
    }
    @Test
    public void testConvertTraffic1() throws Exception {
        //Given
        final double einGB = 1073741824;
        TrafficBytes trafficBytes = new TrafficBytes();
        trafficBytes.setBytes(einGB*5);
        trafficBytesDropped.setBytes(einGB*10);
        traffic.setRx(trafficBytes);
        traffic.setTx(trafficBytesDropped);

        //When
        String trafficString = classUnderTest.convertTraffic(traffic);

        //Then
        assertThat(trafficString).isEqualTo("10GB / 5GB (out/in)");
    }

    @Test
    public void testConvertDate() throws Exception {
        //Given
        long oneDayInMS = 86400000;
        Date now = new Date();
        now.setTime(now.getTime() - oneDayInMS);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.GERMANY);

        //When
        String dateString = classUnderTest.convertDate(formatter.format(now));

        //Then
        assertThat(dateString).isEqualTo("1 Tag");
    }

    @Test
    public void testConvertDate1() throws Exception {
        //Given
        long oneDayInMS = 86400000;
        Date now = new Date();
        now.setTime(now.getTime() - oneDayInMS * 5);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.GERMANY);

        //When
        String dateString = classUnderTest.convertDate(formatter.format(now));

        //Then
        assertThat(dateString).isEqualTo("5 Tage");
    }
}