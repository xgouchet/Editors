package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlTextData extends XmlDataWithText {


    public XmlTextData() {
        this("");
    }

    public XmlTextData(final @NonNull String text) {
        super(XmlUtils.XML_TEXT, text);
    }

    @Override
    public String toString() {
        return "XmlTextData{" + getText() + '}';
    }
}
