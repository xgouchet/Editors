package fr.xgouchet.xmleditor.core.xml;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class XmlElementDataTest {


    private XmlElementData mData;

    @Before
    public void setUp() {
        mData = new XmlElementData("foo");
    }

    @Test
    public void shouldExtractDeclaredNamespaces() {
        mData.addAttribute(new XmlAttribute("bar", "bar.com", "xmlns", null));
        mData.addAttribute(new XmlAttribute("eggs", "eggs.com", "xmlns", ""));
        mData.addAttribute(new XmlAttribute("bacon", "bacon.com", "xmlns", "anything"));
        mData.addAttribute(new XmlAttribute("spam", "spam.com"));

        assertThat(mData.getDeclaredNamespaces())
                .containsOnly(
                        XmlNamespace.from("bar", "bar.com"),
                        XmlNamespace.from("eggs", "eggs.com"),
                        XmlNamespace.from("bacon", "bacon.com")
                );

    }
}