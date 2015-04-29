package fr.xgouchet.xmleditor.core.xml;

/**
 * @author Xavier Gouchet
 */
public class XmlDocument extends XmlContent {


    public XmlDocument() {
        super(XmlUtils.XML_DOCUMENT);
    }

    @Override
    public String toString() {
        return "DOCUMENT";
    }
}
