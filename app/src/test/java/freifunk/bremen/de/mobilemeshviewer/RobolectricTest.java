package freifunk.bremen.de.mobilemeshviewer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerApp;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerModule;

@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public abstract class RobolectricTest {

    protected TestComponent component;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        component = DaggerTestComponent.builder()
                .meshViewerModule(getModuleForInjection()).build();
        ((MeshViewerApp) RuntimeEnvironment.application).setComponent(component);
        inject();
    }

    public TestModule getModuleForInjection() {
        return new TestModule();
    }

    public abstract void inject();

    public class TestModule extends MeshViewerModule {

        public TestModule() {
            super(RuntimeEnvironment.application);
        }
    }
}