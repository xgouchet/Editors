package fr.xgouchet.xmleditor.core.parsers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlDocTypeDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlNamespace;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

/**
 * This parser uses an XmlPullParser to construct a tree based on the input stream, assuming that
 * the input is a well-formed, text based xml document.
 *
 * @author Xavier Gouchet
 */
public class XmlTreePullParser extends AXmlTreeParser {

    public static final String PROPERTY_XML_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";
    public static final String PROPERTY_XML_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";

    private static final String TAG = XmlTreePullParser.class.getSimpleName();

    private final XmlPullParser mPullParser;

    /**
     * @throws XmlPullParserException if the XmlPullParser cannot be created on this device
     */
    public XmlTreePullParser() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // namespace aware means that we miss the attribute declaration of namespaces
        factory.setNamespaceAware(false);
        mPullParser = factory.newPullParser();
    }

    @Override
    public XmlNode parse(final @NonNull InputStream inputStream,
                         final @Nullable String encoding)
            throws XmlParseException, IOException {

        try {
            mPullParser.setInput(inputStream, encoding);
            parse();
        } catch (XmlPullParserException e) {
            throw new XmlParseException(e);
        }

        return getDocument();
    }

    private void parse() throws XmlPullParserException, IOException {
        int eventType = mPullParser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    Log.v(TAG, "START_DOCUMENT");
                    startDocument();
                    break;
                case XmlPullParser.START_TAG:
                    Log.v(TAG, "START_TAG");
                    pullElement();
                    break;
                case XmlPullParser.END_TAG:
                    Log.v(TAG, "END_TAG");
                    endElement();
                    break;
                case XmlPullParser.TEXT:
                    Log.v(TAG, "TEXT");
                    pullText();
                    break;
                case XmlPullParser.CDSECT:
                    Log.v(TAG, "CDSECT");
                    pullCData();
                    break;
                case XmlPullParser.COMMENT:
                    Log.v(TAG, "COMMENT");
                    pullComment();
                    break;
                case XmlPullParser.PROCESSING_INSTRUCTION:
                    Log.v(TAG, "PROCESSING_INSTRUCTION");
                    pullProcessingInstruction();
                    break;
                case XmlPullParser.IGNORABLE_WHITESPACE:
                    Log.v(TAG, "IGNORABLE_WHITESPACE");
                    // ignore, cause it's ignorable :)
                    break;
                case XmlPullParser.DOCDECL:
                    // yeah let's name that doc decl, when it's actually the doctype decl...
                    // Not confusing at all guys !
                    pullDocTypeDeclaration();
                    break;
                default:
                    Log.w(TAG, "Unknown event type : " + eventType);
            }

            eventType = mPullParser.nextToken();
        }

        pullDocumentDeclaration();

        endDocument();
    }

    private void pullDocTypeDeclaration() {
        // raw content
        String dtdContent = mPullParser.getText();
        int start, end;

        // look for the SYSTEM / PUBLIC type
        String[] tokens = dtdContent.trim().split("\\s+", 4);
        String root = tokens[0];
        String internal = null;

        if (XmlDocTypeDeclaration.SYSTEM.equals(tokens[1])) {
            int index = dtdContent.indexOf(XmlDocTypeDeclaration.SYSTEM) + XmlDocTypeDeclaration.SYSTEM.length();
            start = dtdContent.indexOf('"', index) + 1;
            end = dtdContent.indexOf('"', start);
            String location = dtdContent.substring(start, end);

            start = dtdContent.indexOf('[', end);
            if (start >= 0) {
                end = dtdContent.lastIndexOf(']');
                internal = dtdContent.substring(start + 1, end);
            }

            addSystemDTD(root, location, internal);
        } else if (XmlDocTypeDeclaration.PUBLIC.equals(tokens[1])) {
            int index = dtdContent.indexOf(XmlDocTypeDeclaration.PUBLIC) + XmlDocTypeDeclaration.PUBLIC.length();

            start = dtdContent.indexOf('"', index) + 1;
            end = dtdContent.indexOf('"', start);
            String name = dtdContent.substring(start, end);

            start = dtdContent.indexOf('"', end + 1) + 1;
            end = dtdContent.indexOf('"', start);
            String location = dtdContent.substring(start, end);

            start = dtdContent.indexOf('[', end);
            if (start >= 0) {
                end = dtdContent.lastIndexOf(']');
                internal = dtdContent.substring(start + 1, end);
            }

            addPublicDTD(root, name, location, internal);
        } else {
            start = dtdContent.indexOf('[') + 1;
            end = dtdContent.lastIndexOf(']');
            internal = dtdContent.substring(start, end);

            addInternalDTD(root, internal);
        }
    }


    private void pullDocumentDeclaration() {

        // Detect version
        String version = (String) mPullParser.getProperty(PROPERTY_XML_VERSION);
        if (version == null) {
            version = "1.0";
        }

        // Detect encoding (should be the one declared in the xml
        String encoding = mPullParser.getInputEncoding();

        // Detect standalone
        Boolean standalone = (Boolean) mPullParser.getProperty(PROPERTY_XML_STANDALONE);

        addDocumentDeclaration(version, encoding, standalone);
    }

    private void pullProcessingInstruction() {

        // raw content, we need to separate the target by ourselves
        String content = mPullParser.getText();

        String[] tokens = content.split("\\s+");

        if (tokens.length == 0) {
            Log.wtf(TAG, "pullProcessingInstruction : got 0 tokens from string \"" + content + "\"");
            return;
        }

        String target = tokens[0];
        String instruction;
        if (tokens.length >= 2) {
            instruction = content.substring(target.length() + 1);
        } else {
            instruction = "";
        }

        addProcessingInstruction(target, instruction);
    }

    private void pullElement() throws XmlPullParserException {
        String name = mPullParser.getName();
        String prefix = extractPrefix(name);
        if (prefix != null) {
            name = name.substring(prefix.length() + 1);
        }

        // List all attributes
        Collection<XmlAttribute> attributes = pullAttributes();

        // get the actual namespace uri (for that we also need the attributes in this element)
        String uri = getNamespace(prefix);
        if (uri == null) {
            uri = getNamespace(prefix, attributes);
        }

        // TODO boolean isEmptyTag = mPullParser.isEmptyElementTag();

        if (uri == null) {
            Log.d(TAG, "Add element " + name +
                    " with attributes " + Arrays.toString(attributes.toArray()));
            startElement(name, attributes);
        } else {
            Log.d(TAG, "Add element " + name +
                    " with namespace [" + prefix + "]->" + uri +
                    " and attributes " + Arrays.toString(attributes.toArray()));
            startElement(name, prefix, uri, attributes);
        }
    }

    @NonNull
    private Collection<XmlAttribute> pullAttributes() {
        Collection<XmlAttribute> attributes = new HashSet<>();
        SparseIntArray unreadAttributes = new SparseIntArray();
        int attributeCount = mPullParser.getAttributeCount();
        XmlAttribute attribute;

        // First pass !
        for (int i = 0; i < attributeCount; ++i) {
            attribute = pullAttribute(i, attributes);

            if (attribute == null) {
                Log.d(TAG, "Delaying parsing for attribute #" + i);
                unreadAttributes.put(i, i);
            } else {
                attributes.add(attribute);
            }
        }

        // Second pass, if we needed URIs
        attributeCount = unreadAttributes.size();
        for (int i = 0; i < attributeCount; ++i) {
            attribute = pullAttribute(unreadAttributes.keyAt(i), attributes);
            if (attribute == null) {
                Log.w(TAG, "Impossible to read attribute #" + i);
            } else {
                attributes.add(attribute);
            }
        }

        return attributes;
    }

    @Nullable
    private XmlAttribute pullAttribute(final int index,
                                       final @NonNull Collection<XmlAttribute> localAttributes) {
        // get the name and value
        String name = mPullParser.getAttributeName(index);
        String value = mPullParser.getAttributeValue(index);

        // the parser is namespace agnostic, we need to do all the work
        String prefix = extractPrefix(name);
        String uri;

        if (prefix == null) {
            Log.i(TAG, "Found attr  " + name + "=\"" + value + "\"");
            return new XmlAttribute(name, value);
        } else {
            // update name
            name = name.substring(prefix.length() + 1);

            // get the actual namespace uri
            uri = getNamespace(prefix);
            if (uri == null) {
                uri = getNamespace(prefix, localAttributes);
            }

            if (uri == null) {
                // we need an uri that we can't find
                // it's probably declared in the same element, but we haven't read it yet
                Log.d(TAG, "Missing namespace uri for attribute " + prefix + ":" + name + "=\"" + value + "\"");
                return null;
            }

            Log.i(TAG, "Found attr  " + prefix + ":" + name + "=\"" + value + "\"");
            return new XmlAttribute(name, value, prefix, uri);
        }

    }


    private void pullText() throws XmlPullParserException {
        if (mPullParser.isWhitespace()) {
            return;
        }
        String content = mPullParser.getText();
        addText(content);
    }

    private void pullCData() {
        String content = mPullParser.getText();
        addCData(content);
    }


    private void pullComment() {
        String content = mPullParser.getText();
        addComment(content);
    }


}
