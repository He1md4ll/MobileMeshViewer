package freifunk.bremen.de.mobilemeshviewer;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.node.MyNodesActivity;

import static org.assertj.core.api.Assertions.assertThat;

// HINT: Visibility checks are not possible due to a bug in Robolectric
//       visible() method of ActivityController breaks drawer functionality
public class MeshViewerActivityTest extends RobolectricTest {

    @Mock
    private AlarmController alarmController;
    private MeshViewerActivity classUnderTest;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        classUnderTest = Robolectric.buildActivity(MeshViewerActivity.class).create().get();
    }

    @Test
    public void testOnCreate() throws Exception {
        // Given - When
        final ShadowActivity shadowActivity = Shadows.shadowOf(classUnderTest);
        final ViewPager viewPager = (ViewPager) shadowActivity.findViewById(R.id.container);
        final TabLayout tabLayout = (TabLayout) shadowActivity.findViewById(R.id.tabs);

        // Then
        assertThat(shadowActivity.callGetThemeResId()).isEqualTo(R.style.AppTheme);
        assertThat(classUnderTest.getSupportActionBar()).isNotNull();
        assertThat(viewPager.getAdapter()).isNotNull();
        assertThat(viewPager.getAdapter()).isInstanceOf(MeshViewerActivity.SectionsPagerAdapter.class);
        assertThat(tabLayout.getTabCount()).isEqualTo(2);

        Mockito.verify(alarmController).startNodeAlarm();
    }

    @Test
    public void testOnBackPressedDrawerOpen() throws Exception {
        // Given
        final ShadowActivity shadowActivity = Shadows.shadowOf(classUnderTest);
        final DrawerLayout drawer = (DrawerLayout) shadowActivity.findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);

        // When
        classUnderTest.onBackPressed();

        // Then
        assertThat(drawer.isDrawerOpen(GravityCompat.START)).isFalse();
        assertThat(classUnderTest.isFinishing()).isFalse();
    }

    @Test
    public void testOnBackPressedDrawerClosed() throws Exception {
        // Given
        final ShadowActivity shadowActivity = Shadows.shadowOf(classUnderTest);
        final DrawerLayout drawer = (DrawerLayout) shadowActivity.findViewById(R.id.drawer_layout);

        // When
        classUnderTest.onBackPressed();

        // Then
        assertThat(drawer.isDrawerOpen(GravityCompat.START)).isFalse();
        assertThat(classUnderTest.isFinishing()).isTrue();
    }

    @Test
    public void testOnNavigationItemSelectedMyNodes() throws Exception {
        final Intent resultIntent = new Intent(RuntimeEnvironment.application, MyNodesActivity.class);
        navigationTestHelper(R.id.nav_myNodes, resultIntent);
    }

    @Test
    public void testOnNavigationItemSelectedSettings() throws Exception {
        final Intent resultIntent = new Intent(RuntimeEnvironment.application, SettingsActivity.class);
        navigationTestHelper(R.id.nav_settings, resultIntent);
    }

    private void navigationTestHelper(int itemId, Intent resultIntent) {
        // Given
        final ShadowActivity shadowActivity = Shadows.shadowOf(classUnderTest);
        final NavigationView navigationView = (NavigationView) shadowActivity.findViewById(R.id.nav_view);
        final MenuItem item = navigationView.getMenu().findItem(itemId);

        // When
        classUnderTest.onNavigationItemSelected(item);

        // Then
        final Intent actualIntent = shadowActivity.getNextStartedActivity();
        assertThat(actualIntent).isNotNull();
        assertThat((Object) actualIntent).isEqualToComparingOnlyGivenFields(resultIntent);
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
    }
}