package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * * A text holder xml data (Comments, Text, CData, DocType)
 * <p/>
 *
 * @author Xavier Gouchet
 */
abstract class XmlDataWithText extends XmlData {

    private String mText;

    XmlDataWithText(final @XmlUtils.XmlNodeType int type, final @NonNull String text) {
        super(type);
        mText = text;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    public void setText(final @NonNull String text) {
        mText = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlDataWithText)) return false;

        XmlDataWithText that = (XmlDataWithText) o;

        if (that.mType != mType) return false;

        return mText.equals(that.mText);
    }

    @Override
    public int hashCode() {
        int result = mType;
        result = 31 * result + mText.hashCode();
        return result;
    }

}
