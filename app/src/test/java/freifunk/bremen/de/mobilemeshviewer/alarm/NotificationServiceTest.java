package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowService;

import java.util.List;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeStatusChangedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.IpStatus;
import freifunk.bremen.de.mobilemeshviewer.node.NodeActivity;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Node;
import freifunk.bremen.de.mobilemeshviewer.node.model.simple.Status;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationServiceTest extends RobolectricTest {

    @Inject
    NotificationManager notificationManager;
    @Mock
    private PreferenceController preferenceController;
    private NotificationService classUnderTest;
    private ShadowService shadowService;

    private ShadowNotificationManager shadowNotificationManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = Robolectric.buildService(NotificationService.class).create().attach().startCommand(0, 0).get();
        shadowNotificationManager = Shadows.shadowOf(notificationManager);
        shadowService = Shadows.shadowOf(classUnderTest);
    }

    @Test
    public void testServiceStart() throws Exception {
        // Then
        assertThat(shadowService.isStoppedBySelf()).isFalse();
    }

    @Test
    public void testServiceStop() throws Exception {
        // When
        EventBus.getDefault().post(new GatewayListUpdatedEvent(Boolean.TRUE));

        // Then
        assertThat(shadowService.isStoppedBySelf()).isTrue();
    }

    @Test
    public void testBuildNotification() throws Exception {
        // Given
        final PendingIntent pendingIntent = getPendingIntent();
        final Uri uri = new Uri.Builder().path("test").build();

        Mockito.when(preferenceController.getNotificationSound()).thenReturn(uri);
        Mockito.when(preferenceController.isNotificationVibrationEnabled()).thenReturn(Boolean.TRUE);

        // When
        classUnderTest.buildNotification("", "", pendingIntent);

        // Then
        final List<Notification> allNotifications = shadowNotificationManager.getAllNotifications();

        assertThat(shadowService.isStoppedBySelf()).isFalse();
        assertThat(allNotifications).hasSize(1);

        final Notification notification = allNotifications.get(0);
        assertThat(notification.contentIntent).isEqualToComparingOnlyGivenFields(pendingIntent);
        assertThat(notification.sound).isEqualTo(uri);
        assertThat(notification.defaults).isEqualTo(Notification.DEFAULT_VIBRATE);
    }

    @Test
    public void testBuildNotificationDisabled() throws Exception {
        // Given
        final PendingIntent pendingIntent = getPendingIntent();
        final Uri uri = new Uri.Builder().path(null).build();

        Mockito.when(preferenceController.getNotificationSound()).thenReturn(uri);
        Mockito.when(preferenceController.isNotificationVibrationEnabled()).thenReturn(Boolean.FALSE);

        // When
        classUnderTest.buildNotification("", "", pendingIntent);

        // Then
        final List<Notification> allNotifications = shadowNotificationManager.getAllNotifications();

        assertThat(shadowService.isStoppedBySelf()).isFalse();
        assertThat(allNotifications).hasSize(1);

        final Notification notification = allNotifications.get(0);
        assertThat(notification.contentIntent).isEqualToComparingOnlyGivenFields(pendingIntent);
        assertThat(notification.sound).isNull();
        assertThat(notification.defaults).isZero();
    }

    @Test(expected = NullPointerException.class)
    public void testOnNodeStatusChangedNull() throws Exception {
        // When
        EventBus.getDefault().post(new NodeStatusChangedEvent(null));
    }

    @Test
    public void testOnNodeStatusChanged() throws Exception {
        // Given
        final Status status = new Status();
        status.setOnline(Boolean.TRUE);
        final Node node = new Node();
        node.setName("test");
        node.setStatus(status);
        final Uri uri = new Uri.Builder().path("test").build();
        Mockito.when(preferenceController.getNotificationSound()).thenReturn(uri);

        // When
        EventBus.getDefault().post(new NodeStatusChangedEvent(node));

        // Then
        assertThat(shadowService.isStoppedBySelf()).isFalse();
        assertThat(shadowNotificationManager.getAllNotifications()).hasSize(1);
    }


    @Test(expected = NullPointerException.class)
    public void testOnGatewayStatusChangedNull() throws Exception {
        // When
        EventBus.getDefault().post(new GatewayStatusChangedEvent(null));
    }

    @Test
    public void testOnGatewayStatusChanged() throws Exception {
        // Given
        final Gateway gateway = new Gateway("test", IpStatus.BOTH, IpStatus.BOTH, IpStatus.BOTH, IpStatus.BOTH);
        final Uri uri = new Uri.Builder().path("test").build();
        Mockito.when(preferenceController.getNotificationSound()).thenReturn(uri);

        // When
        EventBus.getDefault().post(new GatewayStatusChangedEvent(gateway));

        // Then
        assertThat(shadowService.isStoppedBySelf()).isFalse();
        assertThat(shadowNotificationManager.getAllNotifications()).hasSize(1);
    }

    @Test
    public void testGetPendingIntent() throws Exception {
        // Given
        final Intent intent = new Intent(RuntimeEnvironment.application, this.getClass());

        // When
        final PendingIntent pendingIntent = classUnderTest.getPendingIntent(intent);

        // Then
        assertThat(pendingIntent).isNotNull();
    }

    private PendingIntent getPendingIntent() {
        final Intent resultIntent = new Intent(RuntimeEnvironment.application, NodeActivity.class);
        return classUnderTest.getPendingIntent(resultIntent);
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
    }
}