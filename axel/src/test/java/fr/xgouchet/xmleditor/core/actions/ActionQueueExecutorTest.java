package fr.xgouchet.xmleditor.core.actions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowHandlerThread;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


/**
 * @author Xavier Gouchet
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class ActionQueueExecutorTest {

    private ActionQueueExecutor mActionQueueExecutor;
    private AsyncAction mMockAction;

    @Before
    public void setUp() {
        mActionQueueExecutor = new ActionQueueExecutor();
        mMockAction = mock(AsyncAction.class);
    }

    @Test
    public void shouldPerformActionAndNotifyResults() throws InterruptedException {

        doNothing().when(mMockAction).performActionInBackground();
        doNothing().when(mMockAction).notifyListener();

        mActionQueueExecutor.queueAction(mMockAction);

        // Give it a bit of time, then let the foreground handler process messages
        Thread.sleep(200);
        Robolectric.getForegroundThreadScheduler().runOneTask();

        // Verify we were called
        verify(mMockAction).performActionInBackground();
        verify(mMockAction).notifyListener();
    }

}