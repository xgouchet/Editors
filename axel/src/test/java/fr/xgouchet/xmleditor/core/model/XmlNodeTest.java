package fr.xgouchet.xmleditor.core.model;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.xgouchet.xmleditor.core.xml.XmlCharData;
import fr.xgouchet.xmleditor.core.xml.XmlCommentData;
import fr.xgouchet.xmleditor.core.xml.XmlData;
import fr.xgouchet.xmleditor.core.xml.XmlDocDeclData;
import fr.xgouchet.xmleditor.core.xml.XmlDocTypeData;
import fr.xgouchet.xmleditor.core.xml.XmlDocumentData;
import fr.xgouchet.xmleditor.core.xml.XmlElementData;
import fr.xgouchet.xmleditor.core.xml.XmlProcInstrData;
import fr.xgouchet.xmleditor.core.xml.XmlTextData;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

import static fr.xgouchet.xmleditor.test.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class XmlNodeTest {

//    private XmlData mXmlData;
//    private XmlNode mXmlNode;

    @Before
    public void setUp() {
//        mXmlData = mock(XmlData.class);
//        mXmlNode = new XmlNode(mXmlData);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void shouldComputeXPath_BasicStructure() {
        XmlNode foo = new XmlNode(new XmlElementData("foo"));
        XmlNode bar = new XmlNode(foo, new XmlElementData("bar"));
        XmlNode eggs = new XmlNode(bar, new XmlElementData("eggs"));
        XmlNode bacon = new XmlNode(bar, new XmlElementData("bacon"));

        assertThat(foo).hasXPath("/foo");
        assertThat(bar).hasXPath("/foo/bar");
        assertThat(eggs).hasXPath("/foo/bar/eggs");
        assertThat(bacon).hasXPath("/foo/bar/bacon");
    }

    @Test
    public void shouldComputeXPath_BasicStructureWithNs() {
        XmlNode foo = new XmlNode(new XmlElementData("foo"));
        XmlNode bar = new XmlNode(foo, new XmlElementData("bar", "plop", "plop.com"));
        XmlNode eggs = new XmlNode(bar, new XmlElementData("eggs"));
        XmlNode bacon = new XmlNode(bar, new XmlElementData("bacon", "bip", "bip.com"));

        assertThat(foo).hasXPath("/foo");
        assertThat(bar).hasXPath("/foo/plop:bar");
        assertThat(eggs).hasXPath("/foo/plop:bar/eggs");
        assertThat(bacon).hasXPath("/foo/plop:bar/bip:bacon");
    }

    @Test
    public void shouldComputeXPath_WithIndex() {
        XmlNode foo = new XmlNode(new XmlElementData("foo"));
        XmlNode bar = new XmlNode(foo, new XmlElementData("bar", "plop", "plop.com"));
        XmlNode eggs0 = new XmlNode(bar, new XmlElementData("eggs"));
        XmlNode bacon = new XmlNode(bar, new XmlElementData("bacon", "bip", "bip.com"));
        XmlNode eggs1 = new XmlNode(bar, new XmlElementData("eggs"));
        XmlNode eggs2 = new XmlNode(bar, new XmlElementData("eggs"));

        assertThat(foo).hasXPath("/foo");
        assertThat(bar).hasXPath("/foo/plop:bar");
        assertThat(eggs0).hasXPath("/foo/plop:bar/eggs[1]");
        assertThat(bacon).hasXPath("/foo/plop:bar/bip:bacon");
        assertThat(eggs1).hasXPath("/foo/plop:bar/eggs[2]");
        assertThat(eggs2).hasXPath("/foo/plop:bar/eggs[3]");
    }

    @Test
    public void shouldComputeXPathForNonElementNodes() {
        XmlNode doc = new XmlNode(new XmlDocumentData());
        XmlNode docDecl = new XmlNode(new XmlDocDeclData());
        XmlNode docType = new XmlNode(new XmlDocTypeData("html"));
        XmlNode foo = new XmlNode(new XmlElementData("foo"));
        XmlNode fooProcInstr = new XmlNode(foo, new XmlProcInstrData("plop", "toto"));
        XmlNode bar = new XmlNode(foo, new XmlElementData("bar", "plop", "plop.com"));
        XmlNode barComment = new XmlNode(bar, new XmlCommentData("plop"));
        XmlNode eggs = new XmlNode(bar, new XmlElementData("eggs"));
        XmlNode eggsText = new XmlNode(eggs, new XmlTextData("plop"));
        XmlNode bacon = new XmlNode(bar, new XmlElementData("bacon", "bip", "bip.com"));
        XmlNode baconCData = new XmlNode(bacon, new XmlCharData("plop"));

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
        XmlNode doc = new XmlNode(new XmlDocumentData());
        XmlNode docDecl = new XmlNode(doc, new XmlDocDeclData());
        XmlNode docType = new XmlNode(doc, new XmlDocTypeData("html"));
        XmlNode foo = new XmlNode(doc, new XmlElementData("foo"));
        XmlNode fooProcInstr = new XmlNode(foo, new XmlProcInstrData("plop", "toto"));
        XmlNode bar = new XmlNode(foo, new XmlElementData("bar", "plop", "plop.com"));
        XmlNode barComment = new XmlNode(bar, new XmlCommentData("plop"));
        XmlNode eggs = new XmlNode(bar, new XmlElementData("eggs"));
        XmlNode eggsText = new XmlNode(eggs, new XmlTextData("plop"));
        XmlNode bacon = new XmlNode(bar, new XmlElementData("bacon", "bip", "bip.com"));
        XmlNode baconCData = new XmlNode(bacon, new XmlCharData("plop"));
        XmlNode baconComment = new XmlNode(bacon, new XmlCommentData("plop"));


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