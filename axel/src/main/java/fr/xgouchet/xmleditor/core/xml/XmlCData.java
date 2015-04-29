package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public final class XmlCData extends XmlBasicContent {

    public XmlCData() {
        this("");
    }

    public XmlCData(final @NonNull String text) {
        super(XmlUtils.XML_CDATA, text);
    }

    @Override
    public String toString() {
        return "<![CDATA[" + getText() + "]]>";
    }
}
