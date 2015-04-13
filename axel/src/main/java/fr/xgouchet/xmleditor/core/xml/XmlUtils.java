package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.IntDef;

/**
 * @author Xavier Gouchet
 */
public class XmlUtils {

    /**
     * XML default Namespace attribute
     */
    public static final String ATTR_XMLNS= "xmlns";

    /**
     * XML Namespace declaration prefix
     */
    public static final String PREFIX_XMLNS= "xmlns";

    /**
     * XML Schema Instance Namespace default prefix
     */
    public static final String PREFIX_XSI = "xsi";


    /**
     * XML Schema Instance Namespace URI
     */
    public static final String URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * The xml document property : Version
     */
    public static final String PROPERTY_XML_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";
    /**
     * The xml document property : Standalone
     */
    public static final String PROPERTY_XML_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";

    @IntDef(value = {XML_DOCUMENT, XML_DOCTYPE, XML_PROCESSING_INSTRUCTION, XML_ELEMENT, XML_CDATA, XML_TEXT, XML_COMMENT, XML_DOCUMENT_DECLARATION})
    public @interface XmlNodeType {


    }


    public static final int XML_DOCUMENT = 1;
    public static final int XML_DOCTYPE = 2;
    public static final int XML_PROCESSING_INSTRUCTION = 3;
    public static final int XML_ELEMENT = 4;
    public static final int XML_CDATA = 5;
    public static final int XML_TEXT = 6;
    public static final int XML_COMMENT = 7;
    public static final int XML_DOCUMENT_DECLARATION = 8;


    /**
     * Names of the different data types
     */
    protected static final String[] TYPES = new String[]{
            "INVALID",
            "Document",
            "DocType",
            "Processing Instruction",
            "Element",
            "CData",
            "Text",
            "Comment",
            "Header"
    };

    public static String getTypeName(@XmlNodeType int type) {
        return TYPES[type];
    }
}
