package fr.xgouchet.xmleditor.core.actions;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.model.XmlNodeFactory;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlCData;
import fr.xgouchet.xmleditor.core.xml.XmlComment;
import fr.xgouchet.xmleditor.core.xml.XmlDocumentDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlInternalDTD;
import fr.xgouchet.xmleditor.core.xml.XmlProcessingInstruction;
import fr.xgouchet.xmleditor.core.xml.XmlPublicDTD;
import fr.xgouchet.xmleditor.core.xml.XmlSystemDTD;
import fr.xgouchet.xmleditor.core.xml.XmlText;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Xavier Gouchet
 */
public class WriterXmlVisitorTest {

    private SaveXmlAction.WriterXmlVisitor mWriterXmlVisitor;
    private OutputStream mOutputStream;

    @Before
    public void setUp() throws IOException {
        mOutputStream = new FakeOutputStream();
        mWriterXmlVisitor = new SaveXmlAction.WriterXmlVisitor(mOutputStream);
    }

    @Test
    public void shouldNotWriteDocumentNode() throws Exception {
        XmlNode node = XmlNodeFactory.createDocument();
        mWriterXmlVisitor.onVisitNode(node, 0);

        verifyOutput("");
    }


    @Test
    public void shouldWriteDocumentDeclaration() throws Exception {
        XmlDocumentDeclaration docDecl = new XmlDocumentDeclaration("foo", "bar", false);

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_DOCUMENT_DECLARATION);
        when(node.getData()).thenReturn(docDecl);

        mWriterXmlVisitor.onVisitNode(node, 0);
        mWriterXmlVisitor.onNodeVisited(node, 0);

        verifyOutput("<?xml version=\"foo\" encoding=\"bar\" standalone=\"no\"?>\n");

    }

    @Test
    public void shouldWriteSystemDTD() throws Exception {
        XmlSystemDTD systemDTD = new XmlSystemDTD("foo", "bar");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_DOCTYPE);
        when(node.getData()).thenReturn(systemDTD);

        mWriterXmlVisitor.onVisitNode(node, 0);
        mWriterXmlVisitor.onNodeVisited(node, 0);

        verifyOutput("<!DOCTYPE foo SYSTEM \"bar\">\n");
    }

    @Test
    public void shouldWriteSystemDTDWithInternal() throws Exception {
        XmlSystemDTD systemDTD = new XmlSystemDTD("foo", "bar", "spam");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_DOCTYPE);
        when(node.getData()).thenReturn(systemDTD);

        mWriterXmlVisitor.onVisitNode(node, 0);
        mWriterXmlVisitor.onNodeVisited(node, 0);

        verifyOutput("<!DOCTYPE foo SYSTEM \"bar\" [spam]>\n");
    }

    @Test
    public void shouldWritePublicDTD() throws Exception {
        XmlPublicDTD publicDTD = new XmlPublicDTD("foo", "bacon", "bar");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_DOCTYPE);
        when(node.getData()).thenReturn(publicDTD);

        mWriterXmlVisitor.onVisitNode(node, 0);
        mWriterXmlVisitor.onNodeVisited(node, 0);

        verifyOutput("<!DOCTYPE foo PUBLIC \"bacon\" \"bar\">\n");
    }

    @Test
    public void shouldWritePublicDTDWithInternal() throws Exception {
        XmlPublicDTD publicDTD = new XmlPublicDTD("foo", "bacon", "bar", "spam");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_DOCTYPE);
        when(node.getData()).thenReturn(publicDTD);

        mWriterXmlVisitor.onVisitNode(node, 0);
        mWriterXmlVisitor.onNodeVisited(node, 0);

        verifyOutput("<!DOCTYPE foo PUBLIC \"bacon\" \"bar\" [spam]>\n");
    }

    @Test
    public void shouldWriteInternalDTD() throws Exception {
        XmlInternalDTD internalDTD = new XmlInternalDTD("foo", "spam");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_DOCTYPE);
        when(node.getData()).thenReturn(internalDTD);

        mWriterXmlVisitor.onVisitNode(node, 0);
        mWriterXmlVisitor.onNodeVisited(node, 0);

        verifyOutput("<!DOCTYPE foo [spam]>\n");
    }

    @Test
    public void shouldWriteNodeWithNamespaceAndAttributes() throws Exception {
        XmlElement element = new XmlElement("foo", "spam", "spam.com");
        element.addAttribute(new XmlAttribute("bar", "42"));
        element.addAttribute(new XmlAttribute("eggs", "666", "spam", "spam.com"));

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_ELEMENT);
        when(node.getData()).thenReturn(element);
        when(node.isLeaf()).thenReturn(false);

        mWriterXmlVisitor.onVisitNode(node, 2);
        mWriterXmlVisitor.onNodeVisited(node, 2);

        verifyOutput("  <spam:foo spam:eggs=\"666\" bar=\"42\">\n  </spam:foo>\n");
    }

    @Test
    public void shouldWriteEmptyNodeWithAttributes() throws Exception {
        XmlElement element = new XmlElement("foo");
        element.addAttribute(new XmlAttribute("bar", "42"));
        element.addAttribute(new XmlAttribute("eggs", "666", "spam", "spam.com"));

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_ELEMENT);
        when(node.getData()).thenReturn(element);
        when(node.isLeaf()).thenReturn(true);

        mWriterXmlVisitor.onVisitNode(node, 2);
        mWriterXmlVisitor.onNodeVisited(node, 2);

        verifyOutput("  <foo spam:eggs=\"666\" bar=\"42\"/>\n");
    }

    @Test
    public void shouldWriteComment() throws Exception {
        XmlComment comment = new XmlComment("foo");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_COMMENT);
        when(node.getData()).thenReturn(comment);

        mWriterXmlVisitor.onVisitNode(node, 3);
        mWriterXmlVisitor.onNodeVisited(node, 3);

        verifyOutput("    <!--foo-->\n");
    }


    @Test
    public void shouldWriteText() throws Exception {
        XmlText text = new XmlText("foo");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_TEXT);
        when(node.getData()).thenReturn(text);

        mWriterXmlVisitor.onVisitNode(node, 3);
        mWriterXmlVisitor.onNodeVisited(node, 3);

        verifyOutput("    foo\n");
    }

    @Test
    public void shouldWriteCData() throws Exception {
        XmlCData cData = new XmlCData("foo");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_CDATA);
        when(node.getData()).thenReturn(cData);

        mWriterXmlVisitor.onVisitNode(node, 2);
        mWriterXmlVisitor.onNodeVisited(node, 2);

        verifyOutput("  <![CDATA[foo]]>\n");
    }

    @Test
    public void shouldWriteProcessingInstructions() throws Exception {
        XmlProcessingInstruction processingInstruction = new XmlProcessingInstruction("foo", "spam");

        XmlNode node = mock(XmlNode.class);
        when(node.getDataType()).thenReturn(XmlUtils.XML_PROCESSING_INSTRUCTION);
        when(node.getData()).thenReturn(processingInstruction);

        mWriterXmlVisitor.onVisitNode(node, 2);
        mWriterXmlVisitor.onNodeVisited(node, 2);

        verifyOutput("  <?foo spam?>\n");
    }

    private void verifyOutput(String expected) {
        assertThat(mOutputStream.toString()).isEqualTo(expected);
    }

    private class FakeOutputStream extends OutputStream {

        final StringBuilder mBuilder = new StringBuilder();

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            mBuilder.append(new String(buffer, offset, count));
        }

        @Override
        public void write(int i) throws IOException {
            mBuilder.append((char) i);
        }

        @Override
        public String toString() {
            return mBuilder.toString();
        }
    }
}