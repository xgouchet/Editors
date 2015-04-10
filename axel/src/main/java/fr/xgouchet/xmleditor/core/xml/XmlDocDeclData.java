package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 * @author Xavier Gouchet
 */
public final class XmlDocDeclData extends XmlDataWithAttributes {

    public static final String ENCODING = "encoding";
    public static final String VERSION = "version";
    public static final String STANDALONE = "standalone";

    public static final String DEFAULT_VERSION = "1.0";
    public static final String STANDALONE_YES = "yes";
    public static final String STANDALONE_NO = "no";


    public XmlDocDeclData() {
        super(XmlUtils.XML_DOCUMENT_DECLARATION);
        setVersion(DEFAULT_VERSION);
    }

    @Override
    public void addAttribute(XmlAttribute attribute) {
        throw new UnsupportedOperationException("Attributes cannot be modified directly on a DocumentDeclaration object");
    }

    @Override
    public void removeAttribute(XmlAttribute attribute) {
        throw new UnsupportedOperationException("Attributes cannot be modified directly on a DocumentDeclaration object");
    }

    public void setVersion(final @NonNull String version) {
        super.addAttribute(new XmlAttribute(VERSION, version));
    }

    public void setEncoding(final @NonNull String encoding) {
        super.addAttribute(new XmlAttribute(ENCODING, encoding));
    }

    public void setStandalone(final boolean standalone) {
        super.addAttribute(new XmlAttribute(STANDALONE, standalone ? STANDALONE_YES : STANDALONE_NO));
    }

    @NonNull
    public String getVersion() {
        XmlAttribute attribute = getAttribute(VERSION);
        if (attribute == null) {
            return DEFAULT_VERSION;
        } else {
            return attribute.getValue();
        }
    }

    @Nullable
    public String getEncoding() {
        XmlAttribute attribute = getAttribute(ENCODING);
        if (attribute == null) {
            return null;
        } else {
            return attribute.getValue();
        }
    }

    public boolean isStandalone() {
        XmlAttribute attribute = getAttribute(STANDALONE);
        if (attribute == null) {
            return true;
        } else {
            return attribute.getValue().equals("YES");
        }
    }


}
