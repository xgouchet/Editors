package fr.xgouchet.axml;

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
import static org.mockito.Matchers.isNull;
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
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/empty.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);

        verify(mMockListener).startDocument();
        verify(mMockListener).startElement(eq("root"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));
        verify(mMockListener).endElement(eq("root"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endDocument();

    }

    @Test
    public void shouldParseNameSpaces() throws IOException {


        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
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
        verify(mMockListener).startElement(eq("root"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));
        verify(mMockListener).startElement(eq("subTag"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));
        verify(mMockListener).endElement(eq("subTag"), isNull(String.class), isNull(String.class));
        verify(mMockListener).startPrefixMapping(eq("foo"), eq("815"));
        verify(mMockListener).startElement(eq("tagWithNs"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));
        verify(mMockListener).startElement(eq("plop"), aryEq(new Attribute[]{}), eq("815"), eq("foo"));
        verify(mMockListener).endElement(eq("plop"), eq("815"), eq("foo"));
        verify(mMockListener).endElement(eq("tagWithNs"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endPrefixMapping(eq("foo"), eq("815"));
        verify(mMockListener).endElement(eq("root"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endPrefixMapping(eq("bar"), eq("42"));
        verify(mMockListener).endDocument();

    }

    @Test
    public void shouldParseAttributes() throws IOException {


        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
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
        verify(mMockListener).startElement(eq("root"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));
        verify(mMockListener).startElement(eq("attributed"), attributesCaptor.capture(), isNull(String.class), isNull(String.class));
        assertThat(attributesCaptor.getValue())
                .hasSize(1)
                .contains(new Attribute("key", "value"));
        verify(mMockListener).endElement(eq("attributed"), isNull(String.class), isNull(String.class));
        verify(mMockListener).startPrefixMapping(eq("foo"), eq("42"));
        verify(mMockListener).startElement(eq("attributed_with_ns"), attributesCaptor.capture(), isNull(String.class), isNull(String.class));
        assertThat(attributesCaptor.getValue())
                .hasSize(1)
                .contains(new Attribute("key", "spam", "42", "foo"));
        verify(mMockListener).endElement(eq("attributed_with_ns"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endPrefixMapping(eq("foo"), eq("42"));
        verify(mMockListener).endElement(eq("root"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endDocument();
    }

    @Test
    public void shouldParseTypedAttributes() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
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
        verify(mMockListener).startElement(eq("TextView"), attributesCaptor.capture(),isNull(String.class), isNull(String.class));
        verify(mMockListener).endElement(eq("TextView"),isNull(String.class), isNull(String.class));
        verify(mMockListener).endPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).endDocument();

        // Check typed attributes
        assertThat(attributesCaptor.getValue())
                .contains(new Attribute("paddingLeft", "4px", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("paddingTop", "8dp", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("paddingRight", "15pt", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("paddingBottom", "16mm", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("layout_height", "2in", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("textSize", "42sp", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("text", "@android:string/0x01040003", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("lines", "7", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("enabled", "true", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("drawableTop", "?android:attr/0x0101039D", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("id", "@id/0x7F060001", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("background", "@android:drawable/0x010800B2", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("layout_width", "-1", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("gravity", "17", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("alpha", "0.37", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("textColor", "#4488FF", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("textColorHint", "#80FF00FF", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("textColorLink", "#4673", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("shadowColor", "#632", "http://schemas.android.com/apk/res/android", "android"));

    }


    @Test
    public void shouldParseFractionAttributes() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
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
        verify(mMockListener).startElement(eq("set"), Matchers.<Attribute[]>any(), isNull(String.class), isNull(String.class));
        verify(mMockListener).startElement(eq("translate"), attributesCaptor.capture(), isNull(String.class), isNull(String.class));
        verify(mMockListener).endElement(eq("translate"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endElement(eq("set"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).endDocument();

        // Check typed attributes
        assertThat(attributesCaptor.getValue())
                .contains(new Attribute("fromXDelta", "42%", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("toXDelta", "-666%", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("fromYDelta", "300%", "http://schemas.android.com/apk/res/android", "android"))
                .contains(new Attribute("toYDelta", "-1000%", "http://schemas.android.com/apk/res/android", "android"));

    }

    @Test
    public void shouldParseText() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
        doNothing().when(mMockListener).text(anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/text.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);

        verify(mMockListener).startDocument();
        verify(mMockListener).startElement(eq("root"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));

        verify(mMockListener).startElement(eq("withtext"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));
        verify(mMockListener).text(eq("Lorem ipsum dolor sit amet"));
        verify(mMockListener).endElement(eq("withtext"), isNull(String.class), isNull(String.class));

        verify(mMockListener).startElement(eq("withcdata"), aryEq(new Attribute[]{}),isNull(String.class), isNull(String.class));
        verify(mMockListener).text(eq("Plop"));
        verify(mMockListener).endElement(eq("withcdata"), isNull(String.class), isNull(String.class));

        verify(mMockListener).endElement(eq("root"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endDocument();

    }


    @Test
    public void shouldParseTextUTF16() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
        doNothing().when(mMockListener).text(anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/encoding_utf16_LE.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);

        ArgumentCaptor<Attribute[]> attributesCaptor = ArgumentCaptor.forClass(Attribute[].class);
        verify(mMockListener).startDocument();
        verify(mMockListener).startPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).startElement(eq("shape"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));

        verify(mMockListener).startElement(eq("solid"), attributesCaptor.capture(), isNull(String.class), isNull(String.class));
        verify(mMockListener).endElement(eq("solid"), isNull(String.class), isNull(String.class));

        verify(mMockListener).endElement(eq("shape"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endPrefixMapping(eq("android"), eq("http://schemas.android.com/apk/res/android"));
        verify(mMockListener).endDocument();

        // Check typed attributes
        assertThat(attributesCaptor.getValue())
                .contains(new Attribute("color", "#4030B0E0", "http://schemas.android.com/apk/res/android", "android"));

    }


    @Test
    public void shouldParseTextUTF8VaryingCharLength() throws IOException {

        doNothing().when(mMockListener).startDocument();
        doNothing().when(mMockListener).startElement(anyString(), Matchers.<Attribute[]>any(), anyString(), anyString());
        doNothing().when(mMockListener).text(anyString());
        doNothing().when(mMockListener).endElement(anyString(), anyString(), anyString());
        doNothing().when(mMockListener).endDocument();

        // Select file
        File file = new File("testres/axml/encoding_utf8_varying.xml");
        FileInputStream inputStream = new FileInputStream(file);
        System.out.println(file.getPath());

        mParser.parse(inputStream, mMockListener);

        verify(mMockListener).startDocument();
        verify(mMockListener).startElement(eq("root"), aryEq(new Attribute[]{}),isNull(String.class), isNull(String.class));

        verify(mMockListener).startElement(eq("uni"), aryEq(new Attribute[]{}), isNull(String.class), isNull(String.class));
        verify(mMockListener).text(eq("▲é#ààç€¢æð»"));
        verify(mMockListener).endElement(eq("uni"), isNull(String.class), isNull(String.class));

        verify(mMockListener).endElement(eq("root"), isNull(String.class), isNull(String.class));
        verify(mMockListener).endDocument();

    }


}