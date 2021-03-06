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
public class XmlAttributedContentTest {

    private XmlAttributedContent mData;

    @Before
    public void setUp() {
        mData = new XmlAttributedContent(XmlUtils.XML_ELEMENT) {
        };
    }

    @Test
    public void shouldAddAttributes() {
        XmlAttribute attr0 = new XmlAttribute("foo", "42");
        XmlAttribute attr1 = new XmlAttribute("bar", "toto");
        XmlAttribute attr2 = new XmlAttribute("spam", "", "pref", "uri");

        mData.addAttribute(attr0);
        mData.addAttribute(attr1);
        mData.addAttribute(attr2);

        assertThat(mData.getAttributes())
                .containsOnly(attr0, attr1, attr2);
    }

    @Test
    public void shouldRemoveAttributes() {
        shouldAddAttributes();

        XmlAttribute attr0 = new XmlAttribute("foo", "42");
        XmlAttribute attr1 = new XmlAttribute("bar", "toto");
        XmlAttribute attr2 = new XmlAttribute("spam", "", "pref", "uri");

        mData.removeAttribute(attr1);


        assertThat(mData.getAttributes())
                .containsOnly(attr0, attr2);
    }

    @Test
    public void shouldUpdateAttributes() {
        shouldAddAttributes();

        XmlAttribute attr0 = new XmlAttribute("foo", "666");
        XmlAttribute attr1 = new XmlAttribute("bar", "tata");
        XmlAttribute attr2 = new XmlAttribute("spam", "something", "pref", "uri_bis");

        mData.addAttribute(attr0);
        mData.addAttribute(attr1);
        mData.addAttribute(attr2);


        assertThat(mData.getAttributes())
                .contains(attr0, attr1, attr2);
    }

    @Test
    public void shouldGetAttributeByQualifiedName() {
        shouldAddAttributes();

        XmlAttribute attr0 = new XmlAttribute("foo", "42");
        XmlAttribute attr1 = new XmlAttribute("bar", "toto");
        XmlAttribute attr2 = new XmlAttribute("spam", "", "pref", "uri");

        assertThat(mData.getAttribute("foo")).isEqualTo(attr0);
        assertThat(mData.getAttribute("bar")).isEqualTo(attr1);
        assertThat(mData.getAttribute("pref:spam")).isEqualTo(attr2);

    }


}