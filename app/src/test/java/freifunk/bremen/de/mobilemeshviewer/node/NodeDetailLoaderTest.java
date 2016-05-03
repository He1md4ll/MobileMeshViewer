package freifunk.bremen.de.mobilemeshviewer.node;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.shadows.ShadowApplication;

import freifunk.bremen.de.mobilemeshviewer.RobolectricTest;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.event.NodeDetailNotFoundEvent;
import freifunk.bremen.de.mobilemeshviewer.node.model.detail.NodeDetail;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeDetailLoaderTest extends RobolectricTest {

    @Mock
    private NodeChecker nodeChecker;

    @Inject
    private NodeDetailLoader classUnderTest;
    private Optional<NodeDetail> result = Optional.absent();
    private boolean verify = Boolean.FALSE;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        EventBus.getDefault().register(this);
    }

    @Override
    public void teardown() {
        EventBus.getDefault().unregister(this);
        super.teardown();
    }

    @Test
    public void testDoInBackgroundNotFound() throws Exception {
        // Given
        final String nodeDetailId = "testID";
        final Optional<NodeDetail> testOptioanl = Optional.absent();
        Mockito.when(nodeChecker.getDetailNodeById(nodeDetailId)).thenReturn(testOptioanl);

        // When
        classUnderTest.execute(nodeDetailId);
        ShadowApplication.runBackgroundTasks();

        // Then
        Mockito.verify(nodeChecker).getDetailNodeById(nodeDetailId);
        assertThat(verify).isTrue();
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void testDoInBackgroundFound() throws Exception {
        // Given
        final String nodeDetailId = "testID";
        final NodeDetail nodeDetail = Mockito.mock(NodeDetail.class);
        final Optional<NodeDetail> testOptioanl = Optional.of(nodeDetail);
        Mockito.when(nodeChecker.getDetailNodeById(nodeDetailId)).thenReturn(testOptioanl);

        // When
        classUnderTest.execute(nodeDetailId);
        ShadowApplication.runBackgroundTasks();

        // Then
        Mockito.verify(nodeChecker).getDetailNodeById(nodeDetailId);
        assertThat(verify).isTrue();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(nodeDetail);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNodeDetailFound(NodeDetailFoundEvent event) {
        result = Optional.of(event.getNode());
        verify = Boolean.TRUE;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNodeDetailNotFound(NodeDetailNotFoundEvent ignored) {
        verify = Boolean.TRUE;
    }

    @Override
    public AbstractModule getModuleForInjection() {
        return new TestModule();
    }

    private class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(NodeChecker.class).toInstance(nodeChecker);
        }
    }
}