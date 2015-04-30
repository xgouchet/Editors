package fr.xgouchet.xmleditor.core.actions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileNotFoundException;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;
import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.utils.FileInputStreamProvider;

import static fr.xgouchet.xmleditor.test.Assertions.assertThat;

/**
 * @author Xavier Gouchet
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class XmlLoaderTest {
    private XmlLoader mLoader;
    private AsyncActionListener<XmlNode> mListener;
    private XmlNode mOutput;
    private Exception mException;

    @Before
    public void setUp() {
        File file = new File("../testres/axel/empty.xml");
        FileInputStreamProvider provider = new FileInputStreamProvider(file);
        mListener = new AsyncActionListener<XmlNode>() {

            @Override
            public void onActionPerformed(XmlNode output) {
                mOutput = output;
            }

            @Override
            public void onActionFailed(Exception e) {
                throw new RuntimeException("FAIL!", e);
            }
        };

        mOutput = null;
        mException = null;
        mLoader = new XmlLoader(provider, mListener);
    }

    @Test
    public void shouldLoadXml() throws FileNotFoundException {

        mLoader.performActionInBackground();
        mLoader.notifyListener();

        // Verify parsed doc
        assertThat(mOutput)
                .isDocument()
                .hasChildrenCount(2);

        assertThat(mOutput.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(mOutput.getChild(1))
                .isElement("root")
                .hasChildrenCount(0);

    }
}