package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlProcessingInstruction extends XmlContent {

    private String mTarget;
    private String mInstruction;

    public XmlProcessingInstruction(final @NonNull String target, final @NonNull String instruction) {
        super(XmlUtils.XML_PROCESSING_INSTRUCTION);
        mTarget = target;
        mInstruction = instruction;
    }

    @NonNull
    public String getTarget() {
        return mTarget;
    }

    @NonNull
    public String getInstruction() {
        return mInstruction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlProcessingInstruction)) return false;

        XmlProcessingInstruction that = (XmlProcessingInstruction) o;

        if (!mInstruction.equals(that.mInstruction)) return false;
        if (!mTarget.equals(that.mTarget)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mTarget.hashCode();
        result = 31 * result + mInstruction.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "<?" + mTarget + ' ' + mInstruction + "?>";
    }
}
