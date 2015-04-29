package fr.xgouchet.xmleditor.core.xml;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class XmlElementTest {


    private XmlElement mData;

    @Before
    public void setUp() {
        mData = new XmlElement("foo");
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