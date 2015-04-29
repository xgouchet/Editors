package fr.xgouchet.xmleditor.core.parsers;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;
import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

import static org.assertj.core.api.Assertions.assertThat;

import static fr.xgouchet.xmleditor.test.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class XmlTreePullParserTest {

    private XmlTreePullParser mXmlTreePullParser;

    @Before
    public void setUp() throws XmlPullParserException {
        mXmlTreePullParser = new XmlTreePullParser();
    }

    @Test
    public void shouldParseSimpleDocument() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/empty.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // Parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .isDocument()
                .hasChildrenCount(2);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(1);
        assertThat(root)
                .isElement("root")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseText() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/text.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        mXmlTreePullParser.setTrimTextWhiteSpaces(false);
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(2);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(1);
        assertThat(root)
                .isElement("root")
                .hasChildrenCount(2);

        XmlNode withText = root.getChild(0);
        assertThat(withText)
                .isElement("withtext")
                .hasChildrenCount(1);

        XmlNode text = withText.getChild(0);
        assertThat(text)
                .isText("\n        Hello world\n    ")
                .hasChildrenCount(0);

        XmlNode withTextAndNode = root.getChild(1);
        assertThat(withTextAndNode)
                .isElement("withtextandnode")
                .hasChildrenCount(3);

        assertThat(withTextAndNode.getChild(0))
                .isText("Lorem ")
                .hasChildrenCount(0);

        XmlNode b = withTextAndNode.getChild(1);
        assertThat(b)
                .isElement("b")
                .hasChildrenCount(1);
        assertThat(b.getChild(0))
                .isText("ipsum")
                .hasChildrenCount(0);

        assertThat(withTextAndNode.getChild(2))
                .isText(" dolor sit amet.")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseTextTrimmed() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/text.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        mXmlTreePullParser.setTrimTextWhiteSpaces(true);
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(2);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(1);
        assertThat(root)
                .isElement("root")
                .hasChildrenCount(2);

        XmlNode withText = root.getChild(0);
        assertThat(withText)
                .isElement("withtext")
                .hasChildrenCount(1);

        XmlNode text = withText.getChild(0);
        assertThat(text)
                .isText("Hello world")
                .hasChildrenCount(0);

        XmlNode withTextAndNode = root.getChild(1);
        assertThat(withTextAndNode)
                .isElement("withtextandnode")
                .hasChildrenCount(3);

        assertThat(withTextAndNode.getChild(0))
                .isText("Lorem")
                .hasChildrenCount(0);

        XmlNode b = withTextAndNode.getChild(1);
        assertThat(b)
                .isElement("b")
                .hasChildrenCount(1);
        assertThat(b.getChild(0))
                .isText("ipsum")
                .hasChildrenCount(0);

        assertThat(withTextAndNode.getChild(2))
                .isText("dolor sit amet.")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseCData() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/cdata.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(2);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(1);
        assertThat(root)
                .isElement("root")
                .hasChildrenCount(2);

        assertThat(root.getChild(0))
                .isCData("Hello world")
                .hasChildrenCount(0);

        assertThat(root.getChild(1))
                .isCData("Lorem <b>ipsum</b> dolor sit amet.")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseAttributes() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/attributes.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(2);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(1);
        assertThat(root)
                .isElement("root")
                .hasChildrenCount(1);

        assertThat(root.getChild(0))
                .isElement("withattr")
                .hasChildrenCount(0)
                .hasAttributes(
                        new XmlAttribute("foo", ""),
                        new XmlAttribute("bar", "spam"));
    }

    @Test
    public void shouldParseComments() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/comments.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(4);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isComment("comment before root")
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(2);
        assertThat(root)
                .isElement("root")
                .hasChildrenCount(1);

        assertThat(root.getChild(0))
                .isComment("comment in root")
                .hasChildrenCount(0);

        assertThat(doc.getChild(3))
                .isComment("comment after root")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseProcessingInstructions() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/procinstr.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(4);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isProcessingInstruction("php", "    doSomething();\n    thenDoSomethingElse();\n")
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(2);
        assertThat(root)
                .isElement("root")
                .hasChildrenCount(1);

        assertThat(root.getChild(0))
                .isProcessingInstruction("empty", "")
                .hasChildrenCount(0);

        assertThat(doc.getChild(3))
                .isProcessingInstruction("foo", "onceAgain")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseSystemDoctype() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/doctype_system.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(3);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isSystemDoctype("foo", "sample.dtd", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(2))
                .isElement("foo")
                .hasChildrenCount(0);
    }


    @Test
    public void shouldParsePublicDoctype() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/doctype_public.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(3);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isPublicDoctype("foo", "sample", "http://www.sample.dtd", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(2))
                .isElement("foo")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseSystemCombinedDoctype() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/doctype_system_internal.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(3);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isSystemDoctype("foo", "sample.dtd", "<!ELEMENT foo (#PCDATA)>")
                .hasChildrenCount(0);

        assertThat(doc.getChild(2))
                .isElement("foo")
                .hasChildrenCount(0);
    }


    @Test
    public void shouldParsePublicInternalDoctype() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/doctype_public_internal.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(3);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isPublicDoctype("foo", "sample", "http://www.sample.dtd", "<!ELEMENT foo (#PCDATA)>")
                .hasChildrenCount(0);

        assertThat(doc.getChild(2))
                .isElement("foo")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseInternalDoctype() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/doctype_internal.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(3);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        assertThat(doc.getChild(1))
                .isInternalDoctype("foo", "<!ELEMENT foo (#PCDATA)>")
                .hasChildrenCount(0);

        assertThat(doc.getChild(2))
                .isElement("foo")
                .hasChildrenCount(0);
    }

    @Test
    public void shouldParseNamespaces() throws IOException, XmlParseException {
        // Select file
        File file = new File("../testres/axel/namespaces.xml");
        FileInputStream inputStream = new FileInputStream(file);

        // parse
        XmlNode doc = mXmlTreePullParser.parse(inputStream, "UTF-8");

        // Asserts
        assertThat(doc)
                .hasType(XmlUtils.XML_DOCUMENT)
                .hasChildrenCount(2);

        assertThat(doc.getChild(0))
                .isDocDecl("1.0", "utf-8", null)
                .hasChildrenCount(0);

        XmlNode root = doc.getChild(1);
        assertThat(root)
                .isElement("root", "default.com")
                .hasAttributes(new XmlAttribute("foo", "foo.com", "xmlns", ""),
                        new XmlAttribute("xmlns", "default.com"))
                .hasChildrenCount(2);

        assertThat(root.getChild(0))
                .isElement("nons", "default.com")
                .hasAttributes(new XmlAttribute("foo", "666"))
                .hasChildrenCount(0);

        XmlNode withNS = root.getChild(1);
        assertThat(withNS)
                .isElement("withns", "default.com")
                .hasAttributes(
                        new XmlAttribute("spam", "spam.com", "xmlns", ""),
                        new XmlAttribute("foo", "42", "spam", "spam.com"))
                .hasChildrenCount(1);

        assertThat(withNS.getChild(0))
                .isElement("spam:child", "spam.com")
                .hasAttributes(
                        new XmlAttribute("eggs", "eggs.com", "xmlns", ""),
                        new XmlAttribute("bar", "bacon", "eggs", "eggs.com"))
                .hasChildrenCount(0);
    }
}