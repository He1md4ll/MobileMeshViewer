package freifunk.bremen.de.mobilemeshviewer;

import com.google.inject.AbstractModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RobolectricTest {

    @Before
    public void setUp() throws Exception {
        // Create mock for private members of test
        MockitoAnnotations.initMocks(this);

        // Override injector and perform injection
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, getModuleForInjection());
        RoboInjector injector = RoboGuice.getInjector(RuntimeEnvironment.application);
        injector.injectMembersWithoutViews(this);
    }

    @After
    public void teardown() {
        RoboGuice.Util.reset();
    }

    public AbstractModule getModuleForInjection() {
        return new TestModule();
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            // Replace injected class with mock
        }
    }
}