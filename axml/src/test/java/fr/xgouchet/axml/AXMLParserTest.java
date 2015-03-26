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
import static org.mockito.Matchers.anyByte;
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
    public void shouldParseTypedAttributes() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), anyString(), anyString(), Matchers.<Attribute[]>any());
        doNothing().when(mMockListener).startPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/typed_attrs.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);


        ArgumentCaptor<Attribute[]> attributesCaptor = ArgumentCaptor.forClass(Attribute[].class);

        verify(mMockListener).startDocument();
        verify(mMockListener).startPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).startElement(eq(""), eq("TextView"), eq("TextView"), attributesCaptor.capture());
        verify(mMockListener).endElement(eq(""), eq("TextView"), eq("TextView"));
        verify(mMockListener).endPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).endDocument();

        // Check typed attributes
        assertThat(attributesCaptor.getValue())
                .contains(new Attribute("paddingLeft", "4px", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("paddingTop", "8dp", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("paddingRight", "15pt", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("paddingBottom", "16mm", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("layout_height", "2in", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("textSize", "42sp", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("text", "@android:string/0x01040003", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("lines", "7", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("enabled", "true", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("drawableTop", "?android:attr/0x0101039D", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("id", "@id/0x7F060001", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("background", "@android:drawable/0x010800B2", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("layout_width", "-1", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("gravity", "17", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("alpha", "0.37", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("textColor", "#4488FF", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("textColorHint", "#80FF00FF", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("textColorLink", "#4673", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("shadowColor", "#632", "android", "http://schemas.android.com/apk/res/android"));

    }


    @Test
    public void shouldParseFractionAttributes() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), anyString(), anyString(), Matchers.<Attribute[]>any());
        doNothing().when(mMockListener).startPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endPrefixMapping(anyString(), anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/typed_attrs_2.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);


        ArgumentCaptor<Attribute[]> attributesCaptor = ArgumentCaptor.forClass(Attribute[].class);

        verify(mMockListener).startDocument();
        verify(mMockListener).startPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).startElement(eq(""), eq("set"), eq("set"), Matchers.<Attribute[]>any());
        verify(mMockListener).startElement(eq(""), eq("translate"), eq("translate"), attributesCaptor.capture());
        verify(mMockListener).endElement(eq(""), eq("translate"), eq("translate"));
        verify(mMockListener).endElement(eq(""), eq("set"), eq("set"));
        verify(mMockListener).endPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).endDocument();

        // Check typed attributes
        assertThat(attributesCaptor.getValue())
                .contains(new Attribute("fromXDelta", "42%", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("toXDelta", "-666%", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("fromYDelta", "300%", "android", "http://schemas.android.com/apk/res/android"))
                .contains(new Attribute("toYDelta", "-1000%", "android", "http://schemas.android.com/apk/res/android"));

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