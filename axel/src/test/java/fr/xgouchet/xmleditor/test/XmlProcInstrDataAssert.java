package fr.xgouchet.xmleditor.test;

import fr.xgouchet.xmleditor.core.xml.XmlProcessingInstruction;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Xavier Gouchet
 */
public class XmlProcInstrDataAssert extends XmlDataAssert<XmlProcessingInstruction> {

    protected XmlProcInstrDataAssert(XmlProcessingInstruction actual) {
        super(actual, XmlProcInstrDataAssert.class);
    }

    /**
     * Verify that this data has the given target
     *
     * @return this assertion object
     */
    public XmlProcInstrDataAssert hasTarget(String target) {
        isNotNull();

        assertThat(actual.getTarget())
                .overridingErrorMessage(
                        "Expected target to be <%s> but was <%s>",
                        target,
                        actual.getTarget())
                .isEqualTo(target);

        return this;
    }

    /**
     * Verify that this data has the given instruction
     *
     * @return this assertion object
     */
    public XmlProcInstrDataAssert hasInstruction(String instruction) {
        isNotNull();

        assertThat(actual.getInstruction())
                .overridingErrorMessage(
                        "Expected instruction to be <%s> but was <%s>",
                        instruction,
                        actual.getInstruction())
                .isEqualTo(instruction);

        return this;
    }
}
