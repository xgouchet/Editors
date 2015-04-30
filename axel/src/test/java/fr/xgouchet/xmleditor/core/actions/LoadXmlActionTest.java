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
public class LoadXmlActionTest {

    private LoadXmlAction mLoader;

    @Before
    public void setUp() {
        mLoader = new LoadXmlAction();
    }

    @Test
    public void shouldLoadXml() throws Exception {
        File file = new File("../testres/axel/empty.xml");
        FileInputStreamProvider provider = new FileInputStreamProvider(file);

        // perform action
        XmlNode output = mLoader.performAction(provider);

        // Verify parsed doc
        assertThat(output)
                .isDocument()
                .hasChildrenCount(2);

        assertThat(output.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(output.getChild(1))
                .isElement("root")
                .hasChildrenCount(0);

    }
}