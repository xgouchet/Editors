package fr.xgouchet.xmleditor.core.actions;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
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
public class AsyncActionTest {

    private AsyncAction mMockWrappedAction;
    private AsyncActionListener mMockListener;
    private Object mInput;
    private AsyncAction mAction;

    @Before
    public void setUp() {
        mMockWrappedAction = mock(AsyncAction.class);
        mMockListener = mock(AsyncActionListener.class);
        mInput = new Object();

        mAction = new AsyncActionWrapper(mInput, mMockListener, mMockWrappedAction);
    }


    @Test
    public void shouldHandleAndNotifySuccess() throws Exception {
        Object output = new Object();
        when(mMockWrappedAction.performAction(any())).thenReturn(output);
        doNothing().when(mMockListener).onActionPerformed(any());

        // should not see the exception here
        mAction.performActionInBackground();
        verify(mMockWrappedAction).performAction(eq(mInput));

        // should notify
        mAction.notifyListener();
        verify(mMockListener).onActionPerformed(eq(output));
    }

    @Test
    public void shouldHandleAndNotifyException() throws Exception {
        Exception e = new Exception("WTF");
        when(mMockWrappedAction.performAction(any())).thenThrow(e);
        doNothing().when(mMockListener).onActionFailed(Matchers.<Exception>any());

        // should not see the exception here
        mAction.performActionInBackground();
        verify(mMockWrappedAction).performAction(eq(mInput));

        // should notify
        mAction.notifyListener();
        verify(mMockListener).onActionFailed(eq(e));
    }


    /**
     * A Wrapper class, makes it easy to test AsyncAction
     */
    private class AsyncActionWrapper extends AsyncAction<Object, Object> {

        private final AsyncAction<Object, Object> mWrappedAction;

        protected AsyncActionWrapper(final @Nullable Object input,
                                     final @NonNull AsyncActionListener<Object> listener,
                                     final @NonNull AsyncAction<Object, Object> wrappedAction) {
            super(input, listener);
            mWrappedAction = wrappedAction;
        }

        @Nullable
        @Override
        public Object performAction(@Nullable Object input) throws Exception {
            return mWrappedAction.performAction(input);
        }
    }
}