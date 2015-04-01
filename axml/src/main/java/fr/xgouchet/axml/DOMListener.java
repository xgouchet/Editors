package fr.xgouchet.axml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * An AXMLParser.Listener implementation which creates a DOM Document representation
 *
 * @author Xavier Gouchet
 */
public class DOMListener implements AXMLParser.Listener {

    private final DocumentBuilder mBuilder;
    private final Stack<Node> mNodeStack;
    private Document mDocument;


    public DOMListener() throws ParserConfigurationException {
        mBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        mNodeStack = new Stack<>();
    }

    @Override
    public void startDocument() {
        mDocument = mBuilder.newDocument();
        mNodeStack.push(mDocument);
    }

    @Override
    public void endDocument() {
        // Here we don't check that the stack is empty
        // but as the listener is used on compiled XML, we can assume that they are valid
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) {

    }

    @Override
    public void endPrefixMapping(final String prefix, final String uri) {

    }

    @Override
    public void startElement(final String localName,
                             final Attribute[] attributes,
                             final String uri,
                             final String prefix) {
        Element element;

        // create element
        if ((uri == null) || (uri.length() == 0)) {
            element = mDocument.createElement(localName);
        } else {
            element = mDocument.createElementNS(uri, String.format("%s:%s", prefix, localName));
        }

        for (Attribute attribute : attributes) {
            String attributeNamespaceUri = attribute.getNamespaceUri();
            if ((attributeNamespaceUri == null) || (attributeNamespaceUri.length() == 0)) {
                element.setAttribute(attribute.getName(), attribute.getValue());
            } else {
                element.setAttributeNS(attributeNamespaceUri,
                        attribute.getQualifiedName(),
                        attribute.getValue());
            }
        }

        mNodeStack.peek().appendChild(element);
        mNodeStack.push(element);
    }

    @Override
    public void endElement(final String localName,
                           final String uri,
                           final String prefix) {
        // here we pop the last element without any checks,
        // but as the listener is used on compiled XML, we can assume that they are valid
        mNodeStack.pop();
    }

    @Override
    public void text(final String data) {
        Text text = mDocument.createTextNode(data);
        mNodeStack.peek().appendChild(text);
    }

    public Document getDocument() {
        return mDocument;
    }
}
