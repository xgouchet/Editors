package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlProcInstrData extends XmlData {

    private String mTarget;
    private String mInstruction;

    public XmlProcInstrData(final @NonNull String target) {
        this(target, "");
    }

    public XmlProcInstrData(final @NonNull String target, final @NonNull String instruction) {
        super(XmlUtils.XML_PROCESSING_INSTRUCTION);
        mTarget = target;
        mInstruction = instruction;
    }

    public String getTarget() {
        return mTarget;
    }

    public String getInstruction() {
        return mInstruction;
    }

    public void setTarget(final @NonNull String target) {
        mTarget = target;
    }

    public void setInstruction(final @NonNull String instruction) {
        mInstruction = instruction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlProcInstrData)) return false;

        XmlProcInstrData that = (XmlProcInstrData) o;

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
        return "XmlProcInstrData{" + mTarget + ' ' + mInstruction + '}';
    }
}
