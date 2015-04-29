package fr.xgouchet.xmleditor.test;

import org.assertj.core.api.AbstractAssert;

import fr.xgouchet.xmleditor.core.xml.XmlContent;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Xavier Gouchet
 */
public class XmlDataAssert<D extends XmlContent> extends AbstractAssert<XmlDataAssert<D>, D> {

    protected XmlDataAssert(D actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verify that this data has the given type
     *
     * @return this assertion object
     */
    public XmlDataAssert hasType(@XmlUtils.XmlNodeType int type) {
        isNotNull();

        assertThat(actual.getType())
                .overridingErrorMessage("Expected type to be <%d> but was <%d>", type, actual.getType())
                .isEqualTo(type);

        return this;
    }
}
