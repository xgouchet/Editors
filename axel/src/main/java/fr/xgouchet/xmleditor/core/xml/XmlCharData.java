package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlCharData extends XmlDataWithText {

    public XmlCharData() {
        this("");
    }

    public XmlCharData(final @NonNull String text) {
        super(XmlUtils.XML_CDATA, text);
    }

    @Override
    public String toString() {
        return "XmlCharData{" + getText() + '}';
    }
}
