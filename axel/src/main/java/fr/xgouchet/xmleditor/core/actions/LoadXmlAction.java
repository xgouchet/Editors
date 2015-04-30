package fr.xgouchet.xmleditor.core.actions;

import android.support.annotation.Nullable;

import java.io.InputStream;

import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.parsers.XmlTreePullParser;
import fr.xgouchet.xmleditor.core.utils.InputStreamProvider;

/**
 * This class loads a an input stream and try to convert it into an XmlNode tree.
 *
 * @author Xavier Gouchet
 */
public class LoadXmlAction implements AsyncAction<InputStreamProvider<?>, XmlNode> {

    private static final int DETECTION_BUFFER_MAX_SIZE = 512;


    @Nullable
    @Override
    public XmlNode performAction(final @Nullable InputStreamProvider<?> provider)
            throws Exception {

        if (provider == null) {
            throw new IllegalArgumentException("Unexpected null InputStreamProvider");
        }

        InputStream input = provider.provideInputStream();

        // If possible, avoid creating new input streams over and over again
        if (input.markSupported()) {
            input.mark(DETECTION_BUFFER_MAX_SIZE + 1);
        }

        // TODO detect encoding
        String encoding = null;
        // ...

        // reset input stream
        if (input.markSupported()) {
            input.reset();
        } else {
            input.close();
            input = provider.provideInputStream();
        }

        // Parse the document
        XmlTreePullParser parser = new XmlTreePullParser();
        XmlNode root = parser.parse(input, encoding);

        // close the input stream
        input.close();

        return root;
    }
}
