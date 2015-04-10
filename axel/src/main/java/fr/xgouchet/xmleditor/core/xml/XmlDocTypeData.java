package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlDocTypeData extends XmlDataWithText {

    public XmlDocTypeData() {
        this("");
    }

    public XmlDocTypeData(final @NonNull String text) {
        super(XmlUtils.XML_DOCTYPE, text);
    }

    @Override
    public String toString() {
        return "XmlDocTypeData{" + getText() + '}';
    }
}
