package fr.xgouchet.xmleditor.core.model;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;
import fr.xgouchet.xmleditor.core.xml.XmlCData;
import fr.xgouchet.xmleditor.core.xml.XmlComment;
import fr.xgouchet.xmleditor.core.xml.XmlDocument;
import fr.xgouchet.xmleditor.core.xml.XmlDocumentDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlProcessingInstruction;
import fr.xgouchet.xmleditor.core.xml.XmlSystemDTD;
import fr.xgouchet.xmleditor.core.xml.XmlText;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

import static fr.xgouchet.xmleditor.test.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class XmlNodeTest {


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {

    }

    @Test
    public void shouldComputeXPath_BasicStructure() {
        XmlNode foo = new XmlNode(new XmlElement("foo"));
        XmlNode bar = new XmlNode(foo, new XmlElement("bar"));
        XmlNode eggs = new XmlNode(bar, new XmlElement("eggs"));
        XmlNode bacon = new XmlNode(bar, new XmlElement("bacon"));

        assertThat(foo).hasXPath("/foo");
        assertThat(bar).hasXPath("/foo/bar");
        assertThat(eggs).hasXPath("/foo/bar/eggs");
        assertThat(bacon).hasXPath("/foo/bar/bacon");
    }

    @Test
    public void shouldComputeXPath_BasicStructureWithNs() {
        XmlNode foo = new XmlNode(new XmlElement("foo"));
        XmlNode bar = new XmlNode(foo, new XmlElement("bar", "plop", "plop.com"));
        XmlNode eggs = new XmlNode(bar, new XmlElement("eggs"));
        XmlNode bacon = new XmlNode(bar, new XmlElement("bacon", "bip", "bip.com"));

        assertThat(foo).hasXPath("/foo");
        assertThat(bar).hasXPath("/foo/plop:bar");
        assertThat(eggs).hasXPath("/foo/plop:bar/eggs");
        assertThat(bacon).hasXPath("/foo/plop:bar/bip:bacon");
    }

    @Test
    public void shouldComputeXPath_WithIndex() {
        XmlNode foo = new XmlNode(new XmlElement("foo"));
        XmlNode bar = new XmlNode(foo, new XmlElement("bar", "plop", "plop.com"));
        XmlNode eggs0 = new XmlNode(bar, new XmlElement("eggs"));
        XmlNode bacon = new XmlNode(bar, new XmlElement("bacon", "bip", "bip.com"));
        XmlNode eggs1 = new XmlNode(bar, new XmlElement("eggs"));
        XmlNode eggs2 = new XmlNode(bar, new XmlElement("eggs"));

        assertThat(foo).hasXPath("/foo");
        assertThat(bar).hasXPath("/foo/plop:bar");
        assertThat(eggs0).hasXPath("/foo/plop:bar/eggs[1]");
        assertThat(bacon).hasXPath("/foo/plop:bar/bip:bacon");
        assertThat(eggs1).hasXPath("/foo/plop:bar/eggs[2]");
        assertThat(eggs2).hasXPath("/foo/plop:bar/eggs[3]");
    }

    @Test
    public void shouldComputeXPathForNonElementNodes() {
        XmlNode doc = new XmlNode(new XmlDocument());
        XmlNode docDecl = new XmlNode(new XmlDocumentDeclaration());
        XmlNode docType = new XmlNode(new XmlSystemDTD("foo", "foo.dtd"));
        XmlNode foo = new XmlNode(new XmlElement("foo"));
        XmlNode fooProcInstr = new XmlNode(foo, new XmlProcessingInstruction("plop", "toto"));
        XmlNode bar = new XmlNode(foo, new XmlElement("bar", "plop", "plop.com"));
        XmlNode barComment = new XmlNode(bar, new XmlComment("plop"));
        XmlNode eggs = new XmlNode(bar, new XmlElement("eggs"));
        XmlNode eggsText = new XmlNode(eggs, new XmlText("plop"));
        XmlNode bacon = new XmlNode(bar, new XmlElement("bacon", "bip", "bip.com"));
        XmlNode baconCData = new XmlNode(bacon, new XmlCData("plop"));

        assertThat(doc).hasXPath("");
        assertThat(docDecl).hasXPath(" {Header}");
        assertThat(docType).hasXPath(" {DocType}");

        assertThat(foo).hasXPath("/foo");
        assertThat(fooProcInstr).hasXPath("/foo {Processing Instruction}");

        assertThat(bar).hasXPath("/foo/plop:bar");
        assertThat(barComment).hasXPath("/foo/plop:bar {Comment}");

        assertThat(eggs).hasXPath("/foo/plop:bar/eggs");
        assertThat(eggsText).hasXPath("/foo/plop:bar/eggs {Text}");

        assertThat(bacon).hasXPath("/foo/plop:bar/bip:bacon");
        assertThat(baconCData).hasXPath("/foo/plop:bar/bip:bacon {CData}");
    }

    @Test
    public void shouldFindChildWithType() {
        XmlNode doc = new XmlNode(new XmlDocument());
        XmlNode docDecl = new XmlNode(doc, new XmlDocumentDeclaration());
        XmlNode docType = new XmlNode(doc, new XmlSystemDTD("foo", "foo.dtd"));
        XmlNode foo = new XmlNode(doc, new XmlElement("foo"));
        XmlNode fooProcInstr = new XmlNode(foo, new XmlProcessingInstruction("plop", "toto"));
        XmlNode bar = new XmlNode(foo, new XmlElement("bar", "plop", "plop.com"));
        XmlNode barComment = new XmlNode(bar, new XmlComment("plop"));
        XmlNode eggs = new XmlNode(bar, new XmlElement("eggs"));
        XmlNode eggsText = new XmlNode(eggs, new XmlText("plop"));
        XmlNode bacon = new XmlNode(bar, new XmlElement("bacon", "bip", "bip.com"));
        XmlNode baconCData = new XmlNode(bacon, new XmlCData("plop"));
        XmlNode baconComment = new XmlNode(bacon, new XmlComment("plop"));


        // From root
        assertThat(doc.findNodeWithType(XmlUtils.XML_DOCUMENT))
                .isSameAs(doc);
        assertThat(doc.findNodeWithType(XmlUtils.XML_DOCUMENT_DECLARATION))
                .isSameAs(docDecl);
        assertThat(doc.findNodeWithType(XmlUtils.XML_DOCTYPE))
                .isSameAs(docType);
        assertThat(doc.findNodeWithType(XmlUtils.XML_COMMENT))
                .isSameAs(barComment);
        assertThat(doc.findNodeWithType(XmlUtils.XML_CDATA))
                .isSameAs(baconCData);
        assertThat(doc.findNodeWithType(XmlUtils.XML_TEXT))
                .isSameAs(eggsText);
        assertThat(doc.findNodeWithType(XmlUtils.XML_ELEMENT))
                .isSameAs(foo);
        assertThat(doc.findNodeWithType(XmlUtils.XML_PROCESSING_INSTRUCTION))
                .isSameAs(fooProcInstr);

        // From bacon
        assertThat(bacon.findNodeWithType(XmlUtils.XML_DOCUMENT))
                .isNull();
        assertThat(bacon.findNodeWithType(XmlUtils.XML_DOCUMENT_DECLARATION))
                .isNull();
        assertThat(bacon.findNodeWithType(XmlUtils.XML_DOCTYPE))
                .isNull();
        assertThat(bacon.findNodeWithType(XmlUtils.XML_COMMENT))
                .isSameAs(baconComment);
        assertThat(bacon.findNodeWithType(XmlUtils.XML_CDATA))
                .isSameAs(baconCData);
        assertThat(bacon.findNodeWithType(XmlUtils.XML_TEXT))
                .isNull();
        assertThat(bacon.findNodeWithType(XmlUtils.XML_ELEMENT))
                .isSameAs(bacon);
        assertThat(bacon.findNodeWithType(XmlUtils.XML_PROCESSING_INSTRUCTION))
                .isNull();
    }

}