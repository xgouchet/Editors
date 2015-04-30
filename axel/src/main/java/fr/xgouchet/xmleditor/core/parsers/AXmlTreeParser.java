package fr.xgouchet.xmleditor.core.parsers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.model.XmlNodeFactory;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlNamespace;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

/**
 * This class is the base for any XmlTree parser. It allows parser to construct the tree easily
 * <p/>
 * It is not Thread safe, meaning you should not try to parse two streams with the same parser
 * at the same time.
 *
 * @author Xavier Gouchet
 */
public abstract class AXmlTreeParser {

    private static final String TAG = AXmlTreeParser.class.getSimpleName();

    private Stack<XmlNode> mStack;
    private XmlNode mDocument;
    private boolean mTrimTextWhitespaces;

    private final Map<String, XmlNamespace> mDeclaredNamespaces = new HashMap<>();

    /**
     * @param trimWhiteSpaces if true, text nodes will be trimmed to remove any whitespace at the beggining and then end.
     */
    public void setTrimTextWhiteSpaces(boolean trimWhiteSpaces) {
        mTrimTextWhitespaces = trimWhiteSpaces;
    }

    /**
     * @param inputStream the input stream to parse from
     * @param encoding    the encoding of the stream (if the stream contains text), or null (if the stream is binary data)
     * @return the root XmlNode of the document
     */
    public abstract XmlNode parse(@NonNull InputStream inputStream,
                                  @Nullable String encoding)
            throws XmlParseException, IOException;


    XmlNode getDocument() {
        return mDocument;
    }

    /**
     * Starts the document
     */
    void startDocument() {
        mStack = new Stack<>();
        mDeclaredNamespaces.clear();

        mDocument = XmlNodeFactory.createDocument();
        mStack.push(mDocument);
    }

    /**
     * Ends the document
     */
    void endDocument() {
        // TODO check if we have an emty stack !
    }


    /**
     * Creates an element xml node
     *
     * @param localName  the local name
     * @param attributes the attributes on this element
     * @return the created element
     */
    @NonNull
    XmlNode startElement(final @NonNull String localName, final @NonNull Collection<XmlAttribute> attributes) {
        return startElement(localName, null, null, attributes);
    }

    /**
     * @param localName       the local name
     * @param namespacePrefix the namespace prefix (or null)
     * @param namespaceUrl    the namespace url (or null)
     * @param attributes      the attributes on this element
     * @return the created element
     */
    @NonNull
    XmlNode startElement(final @NonNull String localName,
                         final @Nullable String namespacePrefix,
                         final @Nullable String namespaceUrl,
                         final @NonNull Collection<XmlAttribute> attributes) {


        XmlNode node = XmlNodeFactory
                .createElement(mStack.peek(),
                        localName,
                        namespacePrefix,
                        namespaceUrl,
                        attributes);

        // Store declared namespaces
        List<XmlNamespace> declared = ((XmlElement) node.getData()).getDeclaredNamespaces();
        for (XmlNamespace namespace : declared) {
            Log.d(TAG, "Adding declared namespace : " + namespace);
            mDeclaredNamespaces.put(namespace.getPrefix(), namespace);
        }

        mStack.push(node);
        return node;
    }

    /**
     * Ends the element on top of the stack
     */
    void endElement() {
        XmlNode popped = mStack.pop();

        // Remove declared namespace
        List<XmlNamespace> declared = ((XmlElement) popped.getData()).getDeclaredNamespaces();
        for (XmlNamespace namespace : declared) {
            Log.d(TAG, "Removing declared namespace : " + namespace);
            mDeclaredNamespaces.remove(namespace.getPrefix());
        }
    }

    /**
     * Adds a text node
     *
     * @param text the text content
     */
    void addText(final @NonNull String text) {
        if (mTrimTextWhitespaces) {
            XmlNodeFactory.createText(mStack.peek(), text.trim());
        } else {
            XmlNodeFactory.createText(mStack.peek(), text);
        }
    }

    /**
     * Adds a CData node
     *
     * @param cData the text content
     */
    void addCData(final @NonNull String cData) {
        XmlNodeFactory.createCData(mStack.peek(), cData);
    }

    /**
     * Adds a PI node
     *
     * @param target      the PI target
     * @param instruction the PI instructions
     */
    void addProcessingInstruction(final @NonNull String target,
                                  final @NonNull String instruction) {
        XmlNodeFactory.createProcessingInstruction(mStack.peek(), target, instruction);
    }

    /**
     * Adds a document declaration
     *
     * @param version    the XML version (or null if it's not explicit in the parsed document)
     * @param encoding   the encoding (or null if it's not explicit in the parsed document)
     * @param standalone is this document standalone (or null if it's not explicit in the parsed document)
     */
    void addDocumentDeclaration(final @Nullable String version,
                                final @Nullable String encoding,
                                final @Nullable Boolean standalone) {
        XmlNodeFactory.createDocumentDeclaration(mDocument, version, encoding, standalone);
    }

    /**
     * Adds a Comment node
     *
     * @param comment the text content
     */
    void addComment(final @NonNull String comment) {
        XmlNodeFactory.createComment(mStack.peek(), comment);
    }

    /**
     * Adds a SYSTEM DTD
     *
     * @param root     the root element name
     * @param location the DTD location
     * @param internal the internal DTD if any (or null)
     */
    void addSystemDTD(final @NonNull String root,
                      final @NonNull String location,
                      final @Nullable String internal) {
        if (mStack.peek().findNodeWithType(XmlUtils.XML_DOCTYPE) != null) {
            throw new UnsupportedOperationException("Trying to add a DTD to a document which already has one");
        }
        XmlNodeFactory.createSystemDTD(mStack.peek(), root, location, internal);
    }

    /**
     * Adds a PUBLIC DTD
     *
     * @param root     the root element name
     * @param name     the name (definition) of the DTD
     * @param location the DTD location
     * @param internal the internal DTD if any (or null)
     */
    void addPublicDTD(final @NonNull String root,
                      final @NonNull String name,
                      final @NonNull String location,
                      final @Nullable String internal) {
        if (mStack.peek().findNodeWithType(XmlUtils.XML_DOCTYPE) != null) {
            throw new UnsupportedOperationException("Trying to add a DTD to a document which already has one");
        }
        XmlNodeFactory.createPublicDTD(mStack.peek(), root, name, location, internal);
    }

    /**
     * Adds an internal DTD
     *
     * @param root       the root element name
     * @param definition the internal definition
     */
    void addInternalDTD(final @NonNull String root, final @NonNull String definition) {
        if (mStack.peek().findNodeWithType(XmlUtils.XML_DOCTYPE) != null) {
            throw new UnsupportedOperationException("Trying to add a DTD to a document which already has one");
        }
        XmlNodeFactory.createInternalDTD(mStack.peek(), root, definition);
    }

    @Nullable
    String extractPrefix(final @NonNull String qualifiedName) {
        int index = qualifiedName.indexOf(XmlUtils.PREFIX_SEPARATOR);
        if (index < 0) {
            return null;
        } else {
            return qualifiedName.substring(0, index);
        }
    }

    /**
     * Finds the namespace uri matching the given prefix in the previously declared namespaces
     *
     * @param prefix the current prefix (or null for default namespace)
     * @return the namespace uri or null if no namespace matches the given prefix
     */
    @Nullable
    String getNamespace(final @Nullable String prefix) {
        if (XmlUtils.PREFIX_XMLNS.equals(prefix)) {
            return "";
        } else if (mDeclaredNamespaces.containsKey(prefix)) {
            return mDeclaredNamespaces.get(prefix).getUri();
        } else {
            return null;
        }
    }

    /**
     * Finds the namespace uri matching the given prefix in a namespace declared in the given attributes
     *
     * @param prefix the current prefix (or null for default namespace)
     * @return the namespace uri or null if no namespace matches the given prefix
     */
    String getNamespace(final @Nullable String prefix,
                        final @NonNull Collection<XmlAttribute> attributes) {

        if (prefix == null) {
            // find a default namespace declaration
            for (XmlAttribute attribute : attributes) {
                if (XmlUtils.ATTR_XMLNS.equals(attribute.getQualifiedName())) {
                    return attribute.getValue();
                }
            }
        } else {
            // find a default namespace declaration
            for (XmlAttribute attribute : attributes) {
                if ((attribute.getNamespace() == XmlNamespace.XMLNS)
                        && prefix.equals(attribute.getName())) {
                    return attribute.getValue();
                }
            }
        }

        return null;
    }
}
