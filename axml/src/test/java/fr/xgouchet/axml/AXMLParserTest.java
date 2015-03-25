package fr.xgouchet.axml;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AXMLParserTest {

    private AXMLParser mParser;
    private AXMLParser.Listener mMockListener;

    @Before
    public void setUp() {
        mParser = new AXMLParser();
        mMockListener = mock(AXMLParser.Listener.class);
    }

    @Test(expected = NullPointerException.class)
    public void shouldHandleNullInputStream_SAX() throws IOException {
        mParser.parse(null, mMockListener);
    }

    @Test(expected = NullPointerException.class)
    public void shouldHandleNullListener() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        mParser.parse(inputStream, null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldHandleNullInputStream_DOM() {
        mParser.parse(null);
    }

    @Test
    public void shouldParseEmptyDocument() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), anyString(), anyString(), Matchers.<Attribute[]>any());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/empty.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);

        verify(mMockListener).startDocument();
        verify(mMockListener).startElement(eq(""), eq("root"), eq("root"), aryEq(new Attribute[]{}));
        verify(mMockListener).endElement(eq(""), eq("root"), eq("root"));
        verify(mMockListener).endDocument();

    }

    @Test
    public void shouldParseNameSpaces() throws IOException {


        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), anyString(), anyString(), Matchers.<Attribute[]>any());
        doNothing().when(mMockListener).startPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/namespaces.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);

        verify(mMockListener).startDocument();
        verify(mMockListener).startPrefixMapping(eq("bar"), eq("42"));
        verify(mMockListener).startElement(eq(""), eq("root"), eq("root"), aryEq(new Attribute[]{}));
        verify(mMockListener).startElement(eq(""), eq("subTag"), eq("subTag"), aryEq(new Attribute[]{}));
        verify(mMockListener).endElement(eq(""), eq("subTag"), eq("subTag"));
        verify(mMockListener).startPrefixMapping(eq("foo"), eq("815"));
        verify(mMockListener).startElement(eq(""), eq("tagWithNs"), eq("tagWithNs"), aryEq(new Attribute[]{}));
        verify(mMockListener).startElement(eq("815"), eq("plop"), eq("foo:plop"), aryEq(new Attribute[]{}));
        verify(mMockListener).endElement(eq("815"), eq("plop"), eq("foo:plop"));
        verify(mMockListener).endElement(eq(""), eq("tagWithNs"), eq("tagWithNs"));
        verify(mMockListener).endPrefixMapping(eq("foo"), eq("815"));
        verify(mMockListener).endElement(eq(""), eq("root"), eq("root"));
        verify(mMockListener).endPrefixMapping(eq("bar"), eq("42"));
        verify(mMockListener).endDocument();

    }

    @Test
    public void shouldParseAttributes() throws IOException {


        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), anyString(), anyString(), Matchers.<Attribute[]>any());
        doNothing().when(mMockListener).startPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/attributes.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);


        ArgumentCaptor<Attribute[]> attributesCaptor = ArgumentCaptor.forClass(Attribute[].class);

        verify(mMockListener).startDocument();
        verify(mMockListener).startElement(eq(""), eq("root"), eq("root"), aryEq(new Attribute[]{}));
        verify(mMockListener).startElement(eq(""), eq("attributed"), eq("attributed"), attributesCaptor.capture());
        assertThat(attributesCaptor.getValue())
                .hasSize(1)
                .contains(new Attribute("key", "value"));
        verify(mMockListener).endElement(eq(""), eq("attributed"), eq("attributed"));
        verify(mMockListener).startPrefixMapping(eq("foo"), eq("42"));
        verify(mMockListener).startElement(eq(""), eq("attributed_with_ns"), eq("attributed_with_ns"), attributesCaptor.capture());
        assertThat(attributesCaptor.getValue())
                .hasSize(1)
                .contains(new Attribute("key", "spam", "foo", "42"));
        verify(mMockListener).endElement(eq(""), eq("attributed_with_ns"), eq("attributed_with_ns"));
        verify(mMockListener).endPrefixMapping(eq("foo"), eq("42"));
        verify(mMockListener).endElement(eq(""), eq("root"), eq("root"));
        verify(mMockListener).endDocument();

        // TODO should parse typed attributes
    }

    @Test
    public void shouldParseText() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), anyString(), anyString(), Matchers.<Attribute[]>any());
        doNothing().when(mMockListener).text(anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/text.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);

        verify(mMockListener).startDocument();
        verify(mMockListener).startElement(eq(""), eq("root"), eq("root"), aryEq(new Attribute[]{}));

        verify(mMockListener).startElement(eq(""), eq("withtext"), eq("withtext"), aryEq(new Attribute[]{}));
        verify(mMockListener).text(eq("Lorem ipsum dolor sit amet"));
        verify(mMockListener).endElement(eq(""), eq("withtext"), eq("withtext"));

        verify(mMockListener).startElement(eq(""), eq("withcdata"), eq("withcdata"), aryEq(new Attribute[]{}));
        verify(mMockListener).text(eq("Plop"));
        verify(mMockListener).endElement(eq(""), eq("withcdata"), eq("withcdata"));

        verify(mMockListener).endElement(eq(""), eq("root"), eq("root"));
        verify(mMockListener).endDocument();

    }

}