package fr.xgouchet.xmleditor.core.xml;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlNamespaceTest {

    @Test
    public void shouldEnsureNamespaceCoherence() {
        XmlNamespace ns;

        ns = XmlNamespace.from("prefix", null);
        assertThat(ns).isNull();

        ns = XmlNamespace.from("prefix", "");
        assertThat(ns).isNull();

        ns = XmlNamespace.from(null, "http://uri.com");
        assertThat(ns).isNull();

        ns = XmlNamespace.from("", "http://uri.com");
        assertThat(ns).isNull();

        ns = XmlNamespace.from("prefix", "http://uri.com");
        assertThat(ns.getPrefix()).isEqualTo("prefix");
        assertThat(ns.getUri()).isEqualTo("http://uri.com");
    }

}