package fr.xgouchet.xmleditor.core.actions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author Xavier Gouchet
 */
public abstract class AsyncAction<I, O> {

    private static final String TAG = AsyncAction.class.getSimpleName();

    private final I mInput;
    private O mOutput;
    private final AsyncActionListener<O> mListener;
    private Exception mException;

    protected AsyncAction(final @Nullable I input,
                          final @NonNull AsyncActionListener<O> listener) {
        mInput = input;
        mListener = listener;
    }

    /**
     * Performs the action (should be called in a background thread)
     */
    void performActionInBackground() {
        Log.d(TAG, "performActionInBackground()");
        try {
            mOutput = performAction(mInput);
            Log.d(TAG, "action was a success ! " + mInput + " -> " + mOutput);
        } catch (Exception e) {
            mException = e;
            Log.w(TAG, "action failed ! " + mInput + " -> " + mException);
        }
    }

    /**
     * Notifies the listener of the success or failure of the operation
     */
    void notifyListener() {
        Log.d(TAG, "notifyListener()");
        if (mException == null) {
            mListener.onActionPerformed(mOutput);
        } else {
            mListener.onActionFailed(mException);
        }
    }

    /**
     * Preforms an action based on the given input
     *
     * @param input the input
     * @return the output
     */
    @Nullable
    public abstract O performAction(final @Nullable I input) throws Exception;


}
