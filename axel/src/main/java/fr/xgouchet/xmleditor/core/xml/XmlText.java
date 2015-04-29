package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlText extends XmlBasicContent {


    public XmlText() {
        this("");
    }

    public XmlText(final @NonNull String text) {
        super(XmlUtils.XML_TEXT, text);
    }

    @Override
    public String toString() {
        return "\"" + getText() + "\"";
    }
}
