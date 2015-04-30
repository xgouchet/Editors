package fr.xgouchet.xmleditor.core.actions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Xavier Gouchet
 */
@SuppressWarnings("unchecked")
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class ActionQueueExecutorTest {

    private ActionQueueExecutor mActionQueueExecutor;
    private AsyncAction mMockAction;
    private AsyncActionListener mMockListener;

    @Before
    public void setUp() {
        mActionQueueExecutor = new ActionQueueExecutor();
        mMockAction = mock(AsyncAction.class);
        mMockListener = mock(AsyncActionListener.class);
    }

    @Test
    public void shouldPerformActionAndNotifyResults() throws Exception {
        Object input = new Object();
        Object output = new Object();

        when(mMockAction.performAction(any())).thenReturn(output);
        doNothing().when(mMockListener).onActionPerformed(any());

        mActionQueueExecutor.queueAction(mMockAction, input, mMockListener);

        // Give it a bit of time, then let the foreground handler process messages
        Thread.sleep(200);
        Robolectric.getForegroundThreadScheduler().runOneTask();

        // Verify we were called
        verify(mMockAction).performAction(eq(input));
        verify(mMockListener).onActionPerformed(eq(output));
    }


    @Test
    public void shouldPerformActionAndNotifyFailures() throws Exception {
        Object input = new Object();
        Exception exception = new Exception("Fail !");

        when(mMockAction.performAction(any())).thenThrow(exception);
        doNothing().when(mMockListener).onActionFailed(Matchers.<Exception>any());

        mActionQueueExecutor.queueAction(mMockAction, input, mMockListener);

        // Give it a bit of time, then let the foreground handler process messages
        Thread.sleep(200);
        Robolectric.getForegroundThreadScheduler().runOneTask();

        // Verify we were called
        verify(mMockAction).performAction(eq(input));
        verify(mMockListener).onActionFailed(eq(exception));
    }
}