package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlComment extends XmlBasicContent {


    public XmlComment() {
        this("");
    }

    public XmlComment(final @NonNull String text) {
        super(XmlUtils.XML_COMMENT, text);
    }

    @Override
    public String toString() {
        return "<!--" + getText() + "-->";
    }
}
