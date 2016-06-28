package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowService;
import org.robolectric.util.ServiceController;

import java.util.List;

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

    @Mock
    private PreferenceController preferenceController;
    @Inject
    private NotificationService classUnderTest;
    private ServiceController<NotificationService> controller;
    private ShadowService shadowService;
    private NotificationManager notificationManager;
    private ShadowNotificationManager shadowNotificationManager;

    @Override
    public void setUp() throws Exception {
        notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);
        super.setUp();
        controller = ServiceController.of(Robolectric.getShadowsAdapter(), classUnderTest, new Intent()).create().attach().startCommand(0, 0);
        shadowService = Shadows.shadowOf(controller.get());
        shadowNotificationManager = Shadows.shadowOf(notificationManager);
    }

    @Override
    public void tearDown() {
        controller.destroy();
        super.tearDown();
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

    @Override
    public AbstractModule getModuleForInjection() {
        return new TestModule();
    }

    private PendingIntent getPendingIntent() {
        final Intent resultIntent = new Intent(RuntimeEnvironment.application, NodeActivity.class);
        return classUnderTest.getPendingIntent(resultIntent);
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            // Replace injected class with mock
            bind(PreferenceController.class).toInstance(preferenceController);
            bind(NotificationManager.class).toInstance(notificationManager);
        }
    }
}