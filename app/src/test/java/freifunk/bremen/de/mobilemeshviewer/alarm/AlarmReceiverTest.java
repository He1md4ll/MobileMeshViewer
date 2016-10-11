package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.Intent;
import android.content.IntentFilter;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import freifunk.bremen.de.mobilemeshviewer.BuildConfig;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class AlarmReceiverTest {

    private ShadowApplication shadowApplication;
    private AlarmReceiver classUnderTest;

    @Before
    public void setUp() throws Exception {
        shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
        final Intent alarmIntent = new Intent(Intent.ACTION_ALL_APPS);
        classUnderTest = new AlarmReceiver();
        shadowApplication.registerReceiver(classUnderTest, new IntentFilter(Intent.ACTION_ALL_APPS));
        shadowApplication.sendBroadcast(alarmIntent);
    }

    @Test
    public void testOnReceiveAServices() throws Exception {
        // Given
        final Intent intentNotification = new Intent(shadowApplication.getApplicationContext(), NotificationService.class);
        final Intent intentChecker = new Intent(shadowApplication.getApplicationContext(), CheckerService.class);

        // When
        classUnderTest.getResultData();

        // Then
        assertThat(shadowApplication.getNextStartedService()).isEqualToComparingOnlyGivenFields(intentNotification);
        assertThat(shadowApplication.getNextStartedService()).isEqualToComparingOnlyGivenFields(intentChecker);
    }

    @Test
    public void testOnReceiveBroadcast() throws Exception {
        // When
        classUnderTest.getResultData();

        // Then
        assertThat(AlarmReceiver.isAlarmProcessing()).isTrue();
    }

    @Test
    public void testOnReceiveStop() throws Exception {
        // When
        classUnderTest.getResultData();

        // Then
        assertThat(AlarmReceiver.isAlarmProcessing()).isTrue();

        // When
        EventBus.getDefault().post(new GatewayListUpdatedEvent(Boolean.TRUE));

        // Then
        assertThat(AlarmReceiver.isAlarmProcessing()).isFalse();
    }
}