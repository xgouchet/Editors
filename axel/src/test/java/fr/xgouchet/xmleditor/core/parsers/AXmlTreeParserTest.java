package fr.xgouchet.xmleditor.core.parsers;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.assertj.core.util.CollectionFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.Collections;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;
import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlCData;
import fr.xgouchet.xmleditor.core.xml.XmlProcessingInstruction;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

import static org.assertj.core.api.Assertions.assertThat;

import static fr.xgouchet.xmleditor.test.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class AXmlTreeParserTest {

    private AXmlTreeParser mTreeParser;

    @Before
    public void setUp() {
        mTreeParser = new AXmlTreeParser() {
            @Override
            public XmlNode parse(@NonNull InputStream inputStream, @Nullable String encoding) throws XmlParseException {
                return getDocument();
            }
        };
    }

    @Test
    public void shouldCreateDocument() {
        mTreeParser.startDocument();
        mTreeParser.addDocumentDeclaration("foo", "bar", true);

        XmlNode doc = mTreeParser.getDocument();
        assertThat(doc)
                .isDocument()
                .hasChildrenCount(1);

        XmlNode docDecl = doc.getChild(0);
        assertThat(docDecl)
                .isDocDecl("foo", "bar", true)
                .hasChildrenCount(0);
    }


    @Test
    public void shouldCreateStructure() {
        mTreeParser.startDocument();

        mTreeParser.startElement("foo", Collections.<XmlAttribute>emptyList());
        mTreeParser.startElement("bar", Collections.<XmlAttribute>emptyList());
        mTreeParser.endElement();
        mTreeParser.startElement("eggs", "spam", "spam.com", Collections.<XmlAttribute>emptyList());
        mTreeParser.endElement();
        mTreeParser.endElement();

        XmlNode doc = mTreeParser.getDocument();
        assertThat(doc)
                .isDocument()
                .hasChildrenCount(1);

        XmlNode foo = doc.getChild(0);
        assertThat(foo)
                .isElement("foo")
                .hasChildrenCount(2);

        XmlNode bar = foo.getChild(0);
        assertThat(bar)
                .isElement("bar")
                .hasChildrenCount(0);

        XmlNode eggs = foo.getChild(1);
        assertThat(eggs)
                .isElement("spam:eggs")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldWriteText() {
        mTreeParser.startDocument();

        mTreeParser.startElement("foo", Collections.<XmlAttribute>emptyList());
        mTreeParser.addText("Hello World");
        mTreeParser.endElement();

        XmlNode doc = mTreeParser.getDocument();
        assertThat(doc)
                .isDocument()
                .hasChildrenCount(1);

        XmlNode foo = doc.getChild(0);
        assertThat(foo)
                .isElement("foo")
                .hasChildrenCount(1);

        XmlNode text = foo.getChild(0);
        assertThat(text)
                .isText("Hello World")
                .hasChildrenCount(0);

    }


    @Test
    public void shouldWriteCData() {
        mTreeParser.startDocument();

        mTreeParser.startElement("foo", Collections.<XmlAttribute>emptyList());
        mTreeParser.addCData("Hello World");
        mTreeParser.endElement();

        XmlNode doc = mTreeParser.getDocument();
        assertThat(doc)
                .hasChildrenCount(1)
                .hasType(XmlUtils.XML_DOCUMENT);

        XmlNode foo = doc.getChild(0);
        assertThat(foo)
                .hasChildrenCount(1)
                .hasType(XmlUtils.XML_ELEMENT);

        XmlNode cdata = foo.getChild(0);
        assertThat(cdata)
                .hasChildrenCount(0)
                .hasType(XmlUtils.XML_CDATA);

        assertThat(((XmlCData) cdata.getData()).getText())
                .isEqualTo("Hello World");
    }

    @Test
    public void shouldWriteProcInstr() {
        mTreeParser.startDocument();

        mTreeParser.startElement("foo", Collections.<XmlAttribute>emptyList());
        mTreeParser.addProcessingInstruction("plop", "Hello World");
        mTreeParser.endElement();

        XmlNode doc = mTreeParser.getDocument();
        assertThat(doc)
                .hasChildrenCount(1)
                .hasType(XmlUtils.XML_DOCUMENT);

        XmlNode foo = doc.getChild(0);
        assertThat(foo)
                .hasChildrenCount(1)
                .hasType(XmlUtils.XML_ELEMENT);

        XmlNode pi = foo.getChild(0);
        assertThat(pi)
                .hasChildrenCount(0)
                .hasType(XmlUtils.XML_PROCESSING_INSTRUCTION);

        assertThat(((XmlProcessingInstruction) pi.getData()).getTarget())
                .isEqualTo("plop");
        assertThat(((XmlProcessingInstruction) pi.getData()).getInstruction())
                .isEqualTo("Hello World");
    }
}