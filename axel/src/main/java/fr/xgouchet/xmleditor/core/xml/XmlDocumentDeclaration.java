package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public final class XmlDocumentDeclaration extends XmlAttributedContent {

    public static final String ENCODING = "encoding";
    public static final String VERSION = "version";
    public static final String STANDALONE = "standalone";

    public static final String DEFAULT_VERSION = "1.0";
    public static final String STANDALONE_YES = "yes";
    public static final String STANDALONE_NO = "no";


    public XmlDocumentDeclaration() {
        this(null, null, null);
    }

    public XmlDocumentDeclaration(final @Nullable String version) {
        this(version, null, null);
    }

    public XmlDocumentDeclaration(final @Nullable String version,
                                  final @Nullable String encoding) {
        this(version, encoding, null);
    }

    public XmlDocumentDeclaration(final @Nullable String version,
                                  final @Nullable String encoding,
                                  final @Nullable Boolean standalone) {
        super(XmlUtils.XML_DOCUMENT_DECLARATION);
        if (version == null) {
            setVersion(DEFAULT_VERSION);
        } else {
            setVersion(version);
        }

        if (encoding != null) {
            setEncoding(encoding);
        }

        if (standalone != null) {
            setStandalone(standalone);
        }
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
            return attribute.getValue().equalsIgnoreCase(STANDALONE_YES);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"");
        builder.append(getVersion());
        builder.append("\"");

        if (hasAttribute(ENCODING)) {
            builder.append(" encoding=\"");
            builder.append(getEncoding());
            builder.append("\"");
        }

        if (hasAttribute(STANDALONE)) {
            builder.append(" standalone=\"");
            builder.append(isStandalone() ? STANDALONE_YES : STANDALONE_NO);
            builder.append("\"");
        }

        builder.append("?>");
        return builder.toString();
    }
}
