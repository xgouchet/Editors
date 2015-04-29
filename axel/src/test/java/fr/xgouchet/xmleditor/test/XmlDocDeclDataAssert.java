package fr.xgouchet.xmleditor.test;

import fr.xgouchet.xmleditor.core.xml.XmlDocumentDeclaration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Xavier Gouchet
 */
public class XmlDocDeclDataAssert extends XmlDataAssert<XmlDocumentDeclaration> {

    protected XmlDocDeclDataAssert(XmlDocumentDeclaration actual) {
        super(actual, XmlDocDeclDataAssert.class);
    }

    /**
     * Verify that this data has the given version
     *
     * @return this assertion object
     */
    public XmlDocDeclDataAssert hasVersion(String version) {
        isNotNull();

        assertThat(actual.getVersion())
                .overridingErrorMessage("Expected version to be <%s> but was <%s>", version, actual.getVersion())
                .isEqualTo(version);

        return this;
    }


    /**
     * Verify that this data has the given encoding
     *
     * @return this assertion object
     */
    public XmlDocDeclDataAssert hasEncoding(String encoding) {
        isNotNull();

        assertThat(actual.getEncoding())
                .overridingErrorMessage("Expected version to be <%s> but was <%s>", encoding, actual.getEncoding())
                .isEqualTo(encoding);

        return this;
    }


    /**
     * Verify that this data is standalone
     *
     * @return this assertion object
     */
    public XmlDataAssert isStandalone(boolean standalone) {
        isNotNull();

        assertThat(actual.isStandalone())
                .overridingErrorMessage("Expected " + (standalone ? "to be" : "not to be") + " standalone")
                .isEqualTo(standalone);

        return this;
    }
}
