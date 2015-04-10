package fr.xgouchet.xmleditor.core.xml;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


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