package fr.xgouchet.xmleditor.core.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Xavier Gouchet
 */
public class FileInputStreamProvider implements InputStreamProvider<FileInputStream> {

    private final File mFile;

    public FileInputStreamProvider(final @NonNull File file) {
        mFile = file;
    }

    @NonNull
    @Override
    public FileInputStream provideInputStream() throws IOException {
        return new FileInputStream(mFile);
    }
}
