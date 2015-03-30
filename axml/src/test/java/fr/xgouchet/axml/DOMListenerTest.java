package fr.xgouchet.axml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import static org.assertj.core.api.Assertions.assertThat;


public class DOMListenerTest {

    private DOMListener mDOMListener;

    @Before
    public void setUp() throws Exception {
        mDOMListener = new DOMListener();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldCreateDocument() {
        mDOMListener.startDocument();
        mDOMListener.endDocument();

        assertThat(mDOMListener.getDocument()).isNotNull();
    }


    /**
     * Simulates the following structure :
     * <pre><code>
     * &lt;foo>
     *   &lt;bar>
     *     &lt;spam/&gt;
     *   &lt;/bar>
     *   &lt;bacon>
     *   &lt;/bacon>
     *  &lt;/foo>
     * </code></pre>
     */
    @Test
    public void shouldHandleNodeStructure() {
        mDOMListener.startDocument();
        mDOMListener.startElement("foo", new Attribute[0], null, null);
        mDOMListener.startElement("bar", new Attribute[0], null, null);
        mDOMListener.startElement("spam", new Attribute[0], null, null);
        mDOMListener.endElement("spam", null, "spam");
        mDOMListener.endElement("bar", null, "bar");
        mDOMListener.startElement("bacon", new Attribute[0], null, null);
        mDOMListener.endElement("bacon", null, null);
        mDOMListener.endElement("foo", null, null);

        mDOMListener.endDocument();


        Document doc = mDOMListener.getDocument();
        assertThat(doc).isNotNull();

        Element foo = doc.getDocumentElement();
        assertThat(foo.getTagName()).isEqualTo("foo");
        assertThat(foo.getChildNodes().getLength()).isEqualTo(2);

        Element bar = (Element) foo.getFirstChild();
        assertThat(bar.getTagName()).isEqualTo("bar");
        assertThat(bar.getChildNodes().getLength()).isEqualTo(1);

        Element spam = (Element) bar.getFirstChild();
        assertThat(spam.getTagName()).isEqualTo("spam");
        assertThat(spam.getChildNodes().getLength()).isEqualTo(0);

        Element bacon = (Element) foo.getLastChild();
        assertThat(bacon.getTagName()).isEqualTo("bacon");
        assertThat(bacon.getChildNodes().getLength()).isEqualTo(0);
    }

    @Test
    public void shouldHandleNodeAttributes() {
        Attribute[] attributes = new Attribute[]{
                new Attribute("x", "42"),
                new Attribute("y", "Lorem ipsum dolor sit amet"),
                new Attribute("z", ""),
        };

        mDOMListener.startDocument();
        mDOMListener.startElement("foo", new Attribute[0], null, null);
        mDOMListener.startElement("bar", attributes, null, null);
        mDOMListener.endElement("bar", null, null);
        mDOMListener.endElement("foo", null, null);
        mDOMListener.endDocument();

        Document doc = mDOMListener.getDocument();
        assertThat(doc).isNotNull();

        Element foo = doc.getDocumentElement();
        assertThat(foo.getAttributes().getLength()).isEqualTo(0);

        Element bar = (Element) foo.getFirstChild();
        assertThat(bar.getAttributes().getLength()).isEqualTo(3);
        assertThat(bar.getAttribute("x")).isEqualTo("42");
        assertThat(bar.getAttribute("y")).isEqualTo("Lorem ipsum dolor sit amet");
        assertThat(bar.getAttribute("z")).isEqualTo("");
    }

    @Test
    public void shouldHandleNamespace() {
        String prefix = "plop";
        String uri = "http://www.namespace.com/custom/test";

        Attribute[] attributes = new Attribute[]{
                new Attribute("x", "42", uri, prefix),
                new Attribute("y", "Lorem ipsum dolor sit amet"),
                new Attribute("z", "kamoulox", uri, prefix),
        };

        mDOMListener.startDocument();
        mDOMListener.startElement("foo", new Attribute[0], null, null);
        mDOMListener.startPrefixMapping(prefix, uri);
        mDOMListener.startElement("bar", attributes, null, null);
        mDOMListener.endElement("bar", null, "bar");
        mDOMListener.startElement("bacon", new Attribute[0], uri, "plop");
        mDOMListener.endElement("bacon", uri, "plop:");
        mDOMListener.endPrefixMapping(prefix, uri);
        mDOMListener.endElement("foo", null, null);

        mDOMListener.endDocument();

        Document doc = mDOMListener.getDocument();
        assertThat(doc).isNotNull();

        Element foo = doc.getDocumentElement();
        assertThat(foo.getNamespaceURI()).isNull();
        assertThat(foo.getTagName()).isEqualTo("foo");
        assertThat(foo.getAttributes().getLength()).isEqualTo(0);

        Element bar = (Element) foo.getFirstChild();
        assertThat(bar.getTagName()).isEqualTo("bar");
        assertThat(bar.getAttributes().getLength()).isEqualTo(3);
        assertThat(bar.getAttribute("plop:x")).isEqualTo("42");
        assertThat(bar.getAttribute("plop:y")).isEqualTo("");
        assertThat(bar.getAttribute("y")).isEqualTo("Lorem ipsum dolor sit amet");
        assertThat(bar.getAttribute("plop:z")).isEqualTo("kamoulox");
        assertThat(bar.getAttribute("z")).isEqualTo("");

        Element bacon = (Element) foo.getLastChild();
        assertThat(bacon.getTagName()).isEqualTo("plop:bacon");
        assertThat(bacon.getAttributes().getLength()).isEqualTo(0);
    }

    @Test
    public void shouldHandleText() {
        mDOMListener.startDocument();

        mDOMListener.startElement("span", new Attribute[0], null, null);
        mDOMListener.text("Lorem ipsum ");
        mDOMListener.startElement("b", new Attribute[0], null, null);
        mDOMListener.text("dolor");
        mDOMListener.endElement("b", null, null);
        mDOMListener.text(" sit amet");
        mDOMListener.endElement("span", null, null);

        mDOMListener.endDocument();

        Document doc = mDOMListener.getDocument();
        assertThat(doc).isNotNull();

        Element span = doc.getDocumentElement();
        NodeList children = span.getChildNodes();
        assertThat(children.getLength()).isEqualTo(3);

        assertThat(children.item(0)).isInstanceOf(Text.class);
        assertThat(((Text)children.item(0)).getWholeText()).isEqualTo("Lorem ipsum ");
        assertThat(children.item(1)).isInstanceOf(Element.class);
        assertThat(children.item(1).getFirstChild()).isInstanceOf(Text.class);
        assertThat(((Text)children.item(1).getFirstChild()).getWholeText()).isEqualTo("dolor");
        assertThat(children.item(2)).isInstanceOf(Text.class);
        assertThat(((Text)children.item(2)).getWholeText()).isEqualTo(" sit amet");

    }
}