package fr.xgouchet.xmleditor.core.xml;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import fr.xgouchet.xmleditor.AxelTestApplication;
import fr.xgouchet.xmleditor.BuildConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 18, application = AxelTestApplication.class)
public class XmlAttributeTest {

    @Test
    public void shouldBuildQualifiedName() {
        XmlAttribute attr;

        attr = new XmlAttribute("foo", "bar");
        assertThat(attr.getQualifiedName()).isEqualTo("foo");

        attr = new XmlAttribute("foo", "bar", "prefix", "");
        assertThat(attr.getQualifiedName()).isEqualTo("foo");

        attr = new XmlAttribute("foo", "bar", "", "uri");
        assertThat(attr.getQualifiedName()).isEqualTo("foo");

        attr = new XmlAttribute("foo", "bar", "prefix", "uri");
        assertThat(attr.getQualifiedName()).isEqualTo("prefix:foo");
    }
}