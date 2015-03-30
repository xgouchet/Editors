package fr.xgouchet.axml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Xavier Gouchet
 */
public class DOMListener implements AXMLParser.Listener {

    private final DocumentBuilder mBuilder;
    private final Stack<Node> mNodeStack;
    private Document mDocument;


    public DOMListener() throws ParserConfigurationException {
        mBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        mNodeStack = new Stack<>();
    }

    @Override
    public void startDocument() {
        mDocument = mBuilder.newDocument();
        mNodeStack.push(mDocument);
    }

    @Override
    public void endDocument() {
        // TODO check that the stack is empty ?
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {

    }

    @Override
    public void endPrefixMapping(String prefix, String uri) {

    }

    @Override
    public void startElement(String localName, Attribute[] attributes, String uri, String prefix) {
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
    public void endElement(String localName, String uri, String prefix) {
        mNodeStack.pop();
        // TODO ? check that the popped element matches the one which is ending
    }

    @Override
    public void text(String data) {
        Text text = mDocument.createTextNode(data);
        mNodeStack.peek().appendChild(text);
    }

    public Document getDocument() {
        return mDocument;
    }
}
