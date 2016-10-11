package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;

public class BootUpReceiverTest extends RobolectricTest {

    @Mock
    private AlarmController alarmController;
    @Mock
    private PreferenceController preferenceController;

    private BootUpReceiver receiver;
    private ShadowApplication shadowApplication;
    private Intent alarmIntent;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
        alarmIntent = new Intent(Intent.ACTION_ALL_APPS);
        receiver = new BootUpReceiver();
        shadowApplication.registerReceiver(receiver, new IntentFilter(Intent.ACTION_ALL_APPS));
    }

    @Test
    public void handleReceiveAutoStart() throws Exception {
        // Given
        Mockito.when(preferenceController.isAutostartEnabled()).thenReturn(true);
        shadowApplication.sendBroadcast(alarmIntent);

        // When
        receiver.getResultData();

        // Then
        Mockito.verify(alarmController).startNodeAlarm();
    }

    @Test
    public void handleReceiveNoAutoStart() throws Exception {
        // Given
        Mockito.when(preferenceController.isAutostartEnabled()).thenReturn(false);
        shadowApplication.sendBroadcast(alarmIntent);

        // When
        receiver.getResultData();

        // Then
        Mockito.verify(alarmController, Mockito.never()).startNodeAlarm();
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
        public PreferenceController providesPreferenceController(SharedPreferences sharedPreferences) {
            return preferenceController;
        }

        @Override
        public AlarmController providesAlarmController(AlarmManager alarmManager, Context context, PreferenceController preferenceController) {
            return alarmController;
        }
    }
}