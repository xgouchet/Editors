package fr.xgouchet.xmleditor.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.xmleditor.core.xml.XmlCharData;
import fr.xgouchet.xmleditor.core.xml.XmlCommentData;
import fr.xgouchet.xmleditor.core.xml.XmlDocDeclData;
import fr.xgouchet.xmleditor.core.xml.XmlDocTypeData;
import fr.xgouchet.xmleditor.core.xml.XmlDocumentData;
import fr.xgouchet.xmleditor.core.xml.XmlElementData;
import fr.xgouchet.xmleditor.core.xml.XmlProcInstrData;
import fr.xgouchet.xmleditor.core.xml.XmlTextData;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

/**
 * An XmlNode factory for easier construction, with a few assumption on tree structure
 *
 * @author Xavier Gouchet
 */
public class XmlNodeFactory {

    @NonNull
    public static XmlNode createDocument() {
        return new XmlNode(new XmlDocumentData());
    }

    @NonNull
    public static XmlNode createDocumentDeclaration(final @NonNull XmlNode parent) {
        return createDocumentDeclaration(parent, null, null, null);
    }

    @NonNull
    public static XmlNode createDocumentDeclaration(final @NonNull XmlNode parent,
                                                    final @Nullable String version) {
        return createDocumentDeclaration(parent, version, null, null);
    }

    @NonNull
    public static XmlNode createDocumentDeclaration(final @NonNull XmlNode parent,
                                                    final @Nullable String version,
                                                    final @Nullable String encoding) {
        return createDocumentDeclaration(parent, version, encoding, null);
    }

    @NonNull
    public static XmlNode createDocumentDeclaration(final @NonNull XmlNode parent,
                                                    final @Nullable String version,
                                                    final @Nullable String encoding,
                                                    final @Nullable Boolean standalone) {
        if (parent.getData().getType() != XmlUtils.XML_DOCUMENT) {
            throw new UnsupportedOperationException("Cannot create DocumentDeclaration on non Document node");
        }

        XmlDocDeclData data = new XmlDocDeclData();
        if (version != null) {
            data.setVersion(version);
        }

        if (encoding != null) {
            data.setEncoding(encoding);
        }

        if (standalone != null) {
            data.setStandalone(standalone);
        }

        return new XmlNode(parent, data);
    }

    @NonNull
    public static XmlNode createDocType(final @NonNull XmlNode parent,
                                        final @NonNull String content) {
        if (parent.getData().getType() != XmlUtils.XML_DOCUMENT) {
            throw new UnsupportedOperationException("Cannot create DocType on non Document node");
        }

        return new XmlNode(parent, new XmlDocTypeData(content));
    }

    @NonNull
    public static XmlNode createText(final @NonNull XmlNode parent,
                                     final @NonNull String text) {
        if (parent.getData().getType() != XmlUtils.XML_ELEMENT) {
            throw new UnsupportedOperationException("Cannot create Text on non Element node");
        }

        return new XmlNode(parent, new XmlTextData(text));
    }

    @NonNull
    public static XmlNode createCData(final @NonNull XmlNode parent,
                                      final @NonNull String text) {
        if (parent.getData().getType() != XmlUtils.XML_ELEMENT) {
            throw new UnsupportedOperationException("Cannot create CData on non Element node");
        }

        return new XmlNode(parent, new XmlCharData(text));
    }

    @NonNull
    public static XmlNode createComment(final @NonNull XmlNode parent,
                                        final @NonNull String comment) {
        if ((parent.getData().getType() != XmlUtils.XML_ELEMENT)
                && (parent.getData().getType() != XmlUtils.XML_DOCUMENT)) {
            throw new UnsupportedOperationException("Cannot create Comment on non Document or Element node");
        }

        return new XmlNode(parent, new XmlCommentData(comment));
    }

    @NonNull
    public static XmlNode createElement(final @NonNull XmlNode parent,
                                        final @NonNull String localName) {
        return createElement(parent, localName, null, null);
    }

    @NonNull
    public static XmlNode createElement(final @NonNull XmlNode parent,
                                        final @NonNull String localName,
                                        final @Nullable String namespacePrefix,
                                        final @Nullable String namespaceUri) {
        if ((parent.getData().getType() != XmlUtils.XML_ELEMENT)
                && (parent.getData().getType() != XmlUtils.XML_DOCUMENT)) {
            throw new UnsupportedOperationException("Cannot create Comment on non Document or Element node");
        }

        return new XmlNode(parent, new XmlElementData(localName, namespacePrefix, namespaceUri));
    }

    @NonNull
    public static XmlNode createProcessingInstruction(final @NonNull XmlNode parent,
                                                      final @NonNull String target){
        return createProcessingInstruction(parent, target, "");
    }

    @NonNull
    public static XmlNode createProcessingInstruction(final @NonNull XmlNode parent,
                                                   final @NonNull String target,
                                                   final @NonNull String instruction){
        if ((parent.getData().getType() != XmlUtils.XML_ELEMENT)
                && (parent.getData().getType() != XmlUtils.XML_DOCUMENT)) {
            throw new UnsupportedOperationException("Cannot create Comment on non Document or Element node");
        }

        return new XmlNode(new XmlProcInstrData(target, instruction));
    }
}
