package freifunk.bremen.de.mobilemeshviewer.gateway;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import org.greenrobot.eventbus.EventBus;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.util.ActivityController;

import freifunk.bremen.de.mobilemeshviewer.Checkable;
import freifunk.bremen.de.mobilemeshviewer.ListLoader;
import freifunk.bremen.de.mobilemeshviewer.MeshViewerActivity;
import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.R;
import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.event.GatewayListUpdatedEvent;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.Gateway;
import freifunk.bremen.de.mobilemeshviewer.gateway.model.IpStatus;

import static org.assertj.core.api.Assertions.assertThat;


public class GatewayListFragmentTest extends RobolectricTest {

    @Mock
    private ListLoader<Gateway> gatewayListLoader;
    @Mock
    private AlarmController alarmController;
    private GatewayListFragment classUnderTest;
    private ActivityController<MeshViewerActivity> controller;

    @Override
    public void setUp() throws Exception {
        classUnderTest = new GatewayListFragment();
        super.setUp();
        controller = Robolectric.buildActivity(MeshViewerActivity.class)
                .create()
                .start()
                .resume()
                .visible();
    }

    @Test
    public void testOnActivityCreated() throws Exception {
        // Then
        assertThat(classUnderTest.getAdapter()).isNotNull();
    }

    @Test
    public void testOnCreateOptionsMenu() throws Exception {
        // Given
        final Toolbar toolbar = (Toolbar) classUnderTest.getActivity().findViewById(R.id.toolbar);
        final Menu menu = toolbar.getMenu();

        // When
        classUnderTest.onCreateOptionsMenu(menu, null);

        // Then
        assertThat(toolbar.isShown()).isTrue();
        assertThat(menu.size()).isZero();
        assertThat(menu.hasVisibleItems()).isFalse();

    }

    @Test
    public void testOnListItemClick() throws Exception {
        // Given
        final int position = 0;
        final Gateway gateway = new Gateway("test", IpStatus.BOTH, IpStatus.BOTH, IpStatus.BOTH, IpStatus.BOTH);
        final ShadowListView shadowListView = Shadows.shadowOf(classUnderTest.getListView());
        final ShadowActivity shadowActivity = Shadows.shadowOf(controller.get());

        // When
        classUnderTest.getAdapter().add(gateway);
        shadowListView.performItemClick(position);

        // Then
        final Intent intent = shadowActivity.getNextStartedActivity();
        assertThat(intent).isNotNull();
        assertThat(intent.getParcelableExtra(GatewayActivity.BUNDLE_GATEWAY)).isNotNull();
        assertThat(intent.getParcelableExtra(GatewayActivity.BUNDLE_GATEWAY)).isEqualTo(gateway);
    }

    @Test
    public void testOnGatewayListUpdated() throws Exception {
        // Given
        final GatewayListUpdatedEvent event = new GatewayListUpdatedEvent(Boolean.TRUE);

        // When
        EventBus.getDefault().post(event);

        // Then
        Mockito.verify(gatewayListLoader, Mockito.times(2)).onContentChanged();
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
        public AlarmController providesAlarmController(AlarmManager alarmManager, Context context, PreferenceController preferenceController) {
            return alarmController;
        }

        @Override
        public ListLoader<Gateway> providesGatewayListLoader(Context context, Checkable<Gateway> gatewayCheckable) {
            return gatewayListLoader;
        }

        @Override
        public GatewayListFragment providesGatewayListFragment() {
            return classUnderTest;
        }
    }
}