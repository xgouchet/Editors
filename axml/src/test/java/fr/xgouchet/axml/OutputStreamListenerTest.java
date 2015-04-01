package fr.xgouchet.axml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OutputStreamListenerTest {

    private ByteArrayOutputStream mOutputStream;
    private OutputStreamListener mListener;

    @Before
    public void setUp() throws Exception {
        mOutputStream = new ByteArrayOutputStream();
        mListener = new OutputStreamListener(mOutputStream);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldWriteHeader() throws IOException {
        mListener.startDocument();
        mListener.endDocument();

        String result = new String(mOutputStream.toByteArray(), "UTF-8");

        assertThat(result).isEqualTo("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    }

    @Test
    public void shouldWriteStructure() throws IOException {
        mListener.startDocument();
        mListener.startElement("root", new Attribute[0], null, null);
        mListener.startElement("foo", new Attribute[0], null, null);
        mListener.startElement("spam", new Attribute[0], null, null);
        mListener.endElement("spam", null, null);
        mListener.endElement("foo", null, null);
        mListener.startElement("bar", new Attribute[0], null, null);
        mListener.startElement("bacon", new Attribute[0], null, null);
        mListener.text("eggs");
        mListener.endElement("bacon", null, null);
        mListener.endElement("bar", null, null);
        mListener.endElement("root", null, null);
        mListener.endDocument();

        String result = new String(mOutputStream.toByteArray(), "UTF-8");

        assertThat(result).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<root>\n" +
                        "  <foo>\n" +
                        "    <spam/>\n" +
                        "  </foo>\n" +
                        "  <bar>\n" +
                        "    <bacon>eggs</bacon>\n" +
                        "  </bar>\n" +
                        "</root>\n");
    }

    @Test
    public void shouldWriteAttributes() throws IOException {
        Attribute[] attributes = new Attribute[]{
                new Attribute("x", "42"),
                new Attribute("y", "Lorem ipsum dolor sit amet"),
                new Attribute("z", ""),
        };

        mListener.startDocument();
        mListener.startElement("foo", new Attribute[0], null, null);
        mListener.startElement("bar", attributes, null, null);
        mListener.endElement("bar", null, null);
        mListener.endElement("foo", null, null);
        mListener.endDocument();

        String result = new String(mOutputStream.toByteArray(), "UTF-8");

        assertThat(result).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<foo>\n" +
                        "  <bar x=\"42\" y=\"Lorem ipsum dolor sit amet\" z=\"\"/>\n" +
                        "</foo>\n");

    }

    @Test
    public void shouldIncludeNamespaces() throws IOException {
        String prefix = "plop";
        String uri = "http://www.namespace.com/custom/test";

        Attribute[] attributes = new Attribute[]{
                new Attribute("x", "42", uri, prefix),
                new Attribute("y", "Lorem ipsum dolor sit amet"),
                new Attribute("z", "kamoulox", uri, prefix),
        };

        mListener.startDocument();
        mListener.startPrefixMapping(prefix, uri);
        mListener.startElement("foo", new Attribute[0], null, null);
        mListener.startElement("bar", attributes, null, null);
        mListener.endElement("bar", null, "bar");
        mListener.startElement("bacon", new Attribute[0], uri, prefix);
        mListener.endElement("bacon", uri, prefix);
        mListener.endElement("foo", null, null);
        mListener.endPrefixMapping(prefix, uri);
        mListener.endDocument();

        String result = new String(mOutputStream.toByteArray(), "UTF-8");

        assertThat(result).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<foo xmlns:plop=\"http://www.namespace.com/custom/test\">\n" +
                        "  <bar plop:x=\"42\" y=\"Lorem ipsum dolor sit amet\" plop:z=\"kamoulox\"/>\n" +
                        "  <plop:bacon/>\n" +
                        "</foo>\n");


    }

    @Test
    public void shouldIncludeAllNamespaces() throws IOException {
        String prefix1 = "plop";
        String uri1 = "http://www.namespace.com/custom/plop";

        String prefix2 = "blob";
        String uri2 = "http://www.namespace.com/custom/blob";

        mListener.startDocument();
        mListener.startPrefixMapping(prefix1, uri1);
        mListener.startPrefixMapping(prefix2, uri2);
        mListener.startElement("foo", new Attribute[0], null, null);

        mListener.endElement("foo", null, null);
        mListener.endPrefixMapping(prefix2, uri2);
        mListener.endPrefixMapping(prefix1, uri1);
        mListener.endDocument();

        String result = new String(mOutputStream.toByteArray(), "UTF-8");

        assertThat(result).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<foo xmlns:blob=\"http://www.namespace.com/custom/blob\" xmlns:plop=\"http://www.namespace.com/custom/plop\"/>\n");


    }
}