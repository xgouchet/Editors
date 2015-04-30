package fr.xgouchet.xmleditor.core.editor;

import org.apache.maven.artifact.ant.shaded.StringOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;
import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.utils.FileInputStreamProvider;

import static fr.xgouchet.xmleditor.test.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class XmlEditorTest {

    private XmlEditor mEditor;

    @Before
    public void setUp() {
        mEditor = new XmlEditor();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void shouldLoadXmlFromInputStream() throws FileNotFoundException, InterruptedException {
        File file = new File("../testres/axel/empty.xml");
        FileInputStreamProvider provider = new FileInputStreamProvider(file);

        mEditor.loadDocument(provider);

        Thread.sleep(200);
        Robolectric.getForegroundThreadScheduler().runOneTask();


        // Verify parsed doc
        XmlNode doc = mEditor.getDocument();
        assertThat(doc)
                .isDocument()
                .hasChildrenCount(2);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isElement("root")
                .hasChildrenCount(0);

    }

    @Test
    public void shouldClearDocument() throws FileNotFoundException, InterruptedException {
        shouldLoadXmlFromInputStream();
        XmlNode previousDoc = mEditor.getDocument();

        mEditor.clearDocument();

        // Verify cleared doc
        XmlNode doc = mEditor.getDocument();
        assertThat(doc)
                .isNotSameAs(previousDoc)
                .isDocument()
                .hasChildrenCount(1);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", null, null)
                .hasChildrenCount(0);
    }

    @Test
    public void shouldSaveDocument() throws FileNotFoundException, InterruptedException {
        shouldLoadXmlFromInputStream();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mEditor.saveDocument(output);
    }
}