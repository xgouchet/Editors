package fr.xgouchet.xmleditor.core.actions;

import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.model.XmlNodeFactory;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Xavier Gouchet
 */
public class SaveXmlActionTest {

    private SaveXmlAction mAction;

    @Before
    public void setUp() {
        mAction = new SaveXmlAction();
    }

    @Test
    public void shouldSaveDocument() throws Exception {
        XmlNode doc = XmlNodeFactory.createDocument();
        XmlNodeFactory.createDocumentDeclaration(doc, "1.0", "utf-8");
        XmlNodeFactory.createComment(doc, "Test");
        XmlNode root = XmlNodeFactory.createElement(doc, "root");
        XmlNode div = XmlNodeFactory.createElement(root, "div",
                Collections.singleton(new XmlAttribute("class", "foo")));
        XmlNodeFactory.createText(div, "Hello World");
        XmlNodeFactory.createProcessingInstruction(root, "php", "echo('!');");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        mAction.performAction(new Pair<>(doc, outputStream));

        String string = new String(outputStream.toByteArray(), "UTF-8");
        assertThat(string)
                .isEqualTo("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<!--Test-->\n" +
                        "<root>\n" +
                        "  <div class=\"foo\">\n" +
                        "    Hello World\n" +
                        "  </div>\n" +
                        "  <?php echo('!');?>\n" +
                        "</root>\n");

    }
}