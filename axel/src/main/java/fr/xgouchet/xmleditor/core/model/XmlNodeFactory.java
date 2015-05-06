package fr.xgouchet.xmleditor.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlCData;
import fr.xgouchet.xmleditor.core.xml.XmlComment;
import fr.xgouchet.xmleditor.core.xml.XmlDocument;
import fr.xgouchet.xmleditor.core.xml.XmlDocumentDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlInternalDTD;
import fr.xgouchet.xmleditor.core.xml.XmlProcessingInstruction;
import fr.xgouchet.xmleditor.core.xml.XmlPublicDTD;
import fr.xgouchet.xmleditor.core.xml.XmlSystemDTD;
import fr.xgouchet.xmleditor.core.xml.XmlText;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

/**
 * An XmlNode factory for easier construction, with a few assumption on tree structure
 *
 * @author Xavier Gouchet
 */
public class XmlNodeFactory {

    @NonNull
    public static XmlNode createDocument() {
        return new XmlNode(new XmlDocument());
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
        if (parent.getDataType() != XmlUtils.XML_DOCUMENT) {
            throw new UnsupportedOperationException("Cannot create DocumentDeclaration on non Document node");
        }

        XmlDocumentDeclaration data = new XmlDocumentDeclaration();
        if (version != null) {
            data.setVersion(version);
        }

        if (encoding != null) {
            data.setEncoding(encoding);
        }

        if (standalone != null) {
            data.setStandalone(standalone);
        }

        XmlNode docDecl = new XmlNode(data);
        parent.insertChild(docDecl, 0);
        return docDecl;
    }

    @NonNull
    public static XmlNode createSystemDTD(final @NonNull XmlNode parent,
                                          final @NonNull String root,
                                          final @NonNull String location,
                                          final @Nullable String internalDefinition) {
        if (parent.getDataType() != XmlUtils.XML_DOCUMENT) {
            throw new UnsupportedOperationException("Cannot create DTD on non Document node");
        }

        return new XmlNode(parent, new XmlSystemDTD(root, location, internalDefinition));
    }

    @NonNull
    public static XmlNode createPublicDTD(final @NonNull XmlNode parent,
                                          final @NonNull String root,
                                          final @NonNull String name,
                                          final @NonNull String location,
                                          final @Nullable String internalDefinition) {
        if (parent.getDataType() != XmlUtils.XML_DOCUMENT) {
            throw new UnsupportedOperationException("Cannot create DTD on non Document node");
        }

        return new XmlNode(parent, new XmlPublicDTD(root, name, location, internalDefinition));
    }

    @NonNull
    public static XmlNode createInternalDTD(final @NonNull XmlNode parent,
                                            final @NonNull String root,
                                            final @NonNull String definition) {
        if (parent.getDataType() != XmlUtils.XML_DOCUMENT) {
            throw new UnsupportedOperationException("Cannot create DTD on non Document node");
        }

        return new XmlNode(parent, new XmlInternalDTD(root, definition));
    }

    @NonNull
    public static XmlNode createText(final @NonNull XmlNode parent,
                                     final @NonNull String text) {
        if (parent.getDataType() != XmlUtils.XML_ELEMENT) {
            throw new UnsupportedOperationException("Cannot create Text on non Element node");
        }

        return new XmlNode(parent, new XmlText(text));
    }

    @NonNull
    public static XmlNode createCData(final @NonNull XmlNode parent,
                                      final @NonNull String text) {
        if (parent.getDataType() != XmlUtils.XML_ELEMENT) {
            throw new UnsupportedOperationException("Cannot create CData on non Element node");
        }

        return new XmlNode(parent, new XmlCData(text));
    }

    @NonNull
    public static XmlNode createComment(final @NonNull XmlNode parent,
                                        final @NonNull String comment) {
        if ((parent.getDataType() != XmlUtils.XML_ELEMENT)
                && (parent.getDataType() != XmlUtils.XML_DOCUMENT)) {
            throw new UnsupportedOperationException("Cannot create Comment on non Document or Element node");
        }

        return new XmlNode(parent, new XmlComment(comment));
    }

    @NonNull
    public static XmlNode createElement(final @NonNull XmlNode parent,
                                        final @NonNull String localName) {
        return createElement(parent, localName, null, null, null);
    }

    @NonNull
    public static XmlNode createElement(final @NonNull XmlNode parent,
                                        final @NonNull String localName,
                                        final @Nullable Collection<XmlAttribute> attributes) {
        return createElement(parent, localName, null, null, attributes);
    }

    @NonNull
    public static XmlNode createElement(final @NonNull XmlNode parent,
                                        final @NonNull String localName,
                                        final @Nullable String namespacePrefix,
                                        final @Nullable String namespaceUri) {

        return createElement(parent, localName, namespacePrefix, namespaceUri, null);
    }

    @NonNull
    public static XmlNode createElement(final @NonNull XmlNode parent,
                                        final @NonNull String localName,
                                        final @Nullable String namespacePrefix,
                                        final @Nullable String namespaceUri,
                                        final @Nullable Collection<XmlAttribute> attributes) {
        if ((parent.getDataType() != XmlUtils.XML_ELEMENT)
                && (parent.getDataType() != XmlUtils.XML_DOCUMENT)) {
            throw new UnsupportedOperationException("Cannot create Comment on non Document or Element node");
        }

        XmlElement element = new XmlElement(localName, namespacePrefix, namespaceUri);
        if (attributes != null) {
            element.addAttributes(attributes);
        }
        return new XmlNode(parent, element);
    }

    @NonNull
    public static XmlNode createProcessingInstruction(final @NonNull XmlNode parent,
                                                      final @NonNull String target) {
        return createProcessingInstruction(parent, target, "");
    }

    @NonNull
    public static XmlNode createProcessingInstruction(final @NonNull XmlNode parent,
                                                      final @NonNull String target,
                                                      final @NonNull String instruction) {
        if ((parent.getDataType() != XmlUtils.XML_ELEMENT)
                && (parent.getDataType() != XmlUtils.XML_DOCUMENT)) {
            throw new UnsupportedOperationException("Cannot create Comment on non Document or Element node");
        }

        return new XmlNode(parent, new XmlProcessingInstruction(target, instruction));
    }
}
