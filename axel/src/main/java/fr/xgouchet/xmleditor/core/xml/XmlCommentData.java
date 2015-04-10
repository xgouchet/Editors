package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlCommentData extends XmlDataWithText {


    public XmlCommentData() {
        this("");
    }

    public XmlCommentData(final @NonNull String text) {
        super(XmlUtils.XML_COMMENT, text);
    }

    @Override
    public String toString() {
        return "XmlCommentData{" + getText() + '}';
    }
}
