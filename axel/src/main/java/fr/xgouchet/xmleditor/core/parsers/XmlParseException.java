package fr.xgouchet.xmleditor.core.parsers;

import org.xmlpull.v1.XmlPullParserException;

/**
 * @author Xavier Gouchet
 */
public class XmlParseException extends Exception {

    public XmlParseException(XmlPullParserException cause) {
        super(cause);
    }

}
