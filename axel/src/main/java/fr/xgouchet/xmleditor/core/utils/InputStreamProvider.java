package fr.xgouchet.xmleditor.core.utils;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream provider is a utility to provide several times the same input stream,
 * allowing it to be read multiple time.
 * <p/>
 * This class's contract is that each time the provideInputStream method is called, the provided
 * input stream will provided the exact same stream of data
 *
 * @author Xavier Gouchet
 */
public interface InputStreamProvider<I extends InputStream> {

    /**
     * @return the provided input stream (non null)
     */
    @NonNull
    I provideInputStream() throws IOException;
}
