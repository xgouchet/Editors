package fr.xgouchet.axml;

import android.util.Xml;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * The AXML Parser can parse a file or InputStream as a compressed Android XML file
 * <p/>
 * <p/>
 * TODO add convert method (inputStream -> outputStream)
 * TODO make the whole thing concurrent safe
 *
 * @author Xavier Gouchet
 */
public class AXMLParser {

    public static final int WORD_SIZE = 4;

    public static final int WORD_START_DOCUMENT = 0x00080003;

    public static final int WORD_RES_TABLE = 0x00080180;
    public static final int WORD_STRING_TABLE = 0x001C0001;

    public static final int WORD_START_NAMESPACE = 0x00100100;
    public static final int WORD_END_NAMESPACE = 0x00100101;
    public static final int WORD_START_TAG = 0x00100102;
    public static final int WORD_END_TAG = 0x00100103;
    public static final int WORD_TEXT = 0x00100104;

    public static final int DEFAULT_NAMESPACE = 0xFFFFFFFF;
    public static final int ATTR_TYPED_VALUE = 0xFFFFFFFF;

    public static final int TYPE_ID_REF = 0x01000008;
    public static final int TYPE_ATTR_REF = 0x02000008;
    public static final int TYPE_STRING = 0x03000008;
    public static final int TYPE_FLOAT = 0x04000008;
    public static final int TYPE_DIMEN = 0x05000008;
    public static final int TYPE_PERCENT = 0x06000008;
    public static final int TYPE_INT = 0x10000008;
    public static final int TYPE_FLAGS = 0x11000008;
    public static final int TYPE_BOOL = 0x12000008;
    public static final int TYPE_COLOR_AARRGGBB = 0x1C000008;
    public static final int TYPE_COLOR_RRGGBB = 0x1D000008;
    public static final int TYPE_COLOR_ARGB = 0x1E000008;
    public static final int TYPE_COLOR_RGB = 0x1F000008;

    public static final int ENCODING_UTF8 = 0X00000100;
    public static final int ENCODING_UTF16_LE = 0X00000000;

    public static final String[] DIMENSION_UNIT = new String[]{"px", "dp", "sp", "pt", "in", "mm"};

    // equivalent to 2Ko ints
    private static final int BUFFER_SIZE = 4 * 2048;

    private InputStream mInputStream;
    private Listener mListener;

    private final byte[] mBuffer = new byte[BUFFER_SIZE];
    private int mBufferStartPosition = BUFFER_SIZE;
    private int mBufferEndPosition = BUFFER_SIZE;

    private boolean mParsingComplete = false;
    private boolean mEndOfStreamReached = false;


    private int mStringsCount;
    private String[] mStringsTable;

    private int mResourcesCount;
    private int[] mResourcesTable;

    private Map<String, String> mNamespaces = new HashMap<>();

    private int mStylesCount;


    /**
     * A SAX like listener. Events will be triggered whenever
     */
    public interface Listener {
        /**
         * Receive notification of the beginning of a document.
         */
        void startDocument();

        /**
         * Receive notification of the end of a document.
         */
        void endDocument();

        /**
         * Begin the scope of a prefix-URI Namespace mapping. This event is triggered when the parser
         * encounters a <code>&lt;tag xmlns:prefix="uri" ...&gt;</code> tag.
         *
         * @param prefix the Namespace prefix being declared. An empty string is used
         *               for the default element namespace, which has no prefix.
         * @param uri    the Namespace URI the prefix is mapped to
         */
        void startPrefixMapping(String prefix, String uri);

        /**
         * End the scope of a prefix-URI mapping. This event is triggered when the parser finds
         * the closing tag of the element which declared a XML namespace.
         *
         * @param prefix the prefix that was being mapped. This is the empty string
         *               when a default mapping scope ends.
         * @param uri    the Namespace URI the prefix is mapped to
         */
        void endPrefixMapping(String prefix, String uri);

        /**
         * Receive notification of the beginning of an element.
         *
         * @param uri       the Namespace URI, or the empty string if the element has no
         *                  Namespace URI or if Namespace processing is not being
         *                  performed
         * @param localName the local name (without prefix), or the empty string if
         *                  Namespace processing is not being performed
         * @param qName     the qualified name (with prefix), or the empty string if
         *                  qualified names are not available
         * @param atts      the attributes attached to the element. If there are no
         *                  attributes, it shall be an empty Attributes object. The value
         *                  of this object after startElement returns is undefined
         */
        void startElement(String uri, String localName, String qName,
                          Attribute[] atts);

        /**
         * Receive notification of the end of an element.
         *
         * @param uri       the Namespace URI, or the empty string if the element has no
         *                  Namespace URI or if Namespace processing is not being
         *                  performed
         * @param localName the local name (without prefix), or the empty string if
         *                  Namespace processing is not being performed
         * @param qName     the qualified XML name (with prefix), or the empty string if
         *                  qualified names are not available
         */
        void endElement(String uri, String localName, String qName);

        /**
         * Receive notification of text.
         *
         * @param data the text data
         */
        void text(String data);

        /**
         * Receive notification of character data (in a <![CDATA[ ]]> block).
         *
         * @param data the text data
         */
        void characterData(String data);

        /**
         * Receive notification of a processing instruction.
         *
         * @param target the processing instruction target
         * @param data   the processing instruction data, or null if none was supplied.
         *               The data does not include any whitespace separating it from
         *               the target
         * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
         */
        void processingInstruction(String target, String data);
    }

    /**
     * Parses the given input stream and build a DOM representation of the XML document
     *
     * @param inputStream the input stream to parse
     * @return the DOM representation of the document
     */
    public Document parse(final InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException();
        }


        return null;
    }

    /**
     * Parses the given input stream and trigger events on the given listener
     *
     * @param inputStream the input stream to parse (it will automatically be closed at the end of the parsing)
     * @param listener    the listener to trigger on each parsing event
     */
    public void parse(final InputStream inputStream, final AXMLParser.Listener listener)
            throws IOException {
        if ((inputStream == null) || (listener == null)) {
            throw new NullPointerException();
        }

        resetInternalState();

        mListener = listener;
        mInputStream = inputStream;

        processBuffer();

        try {
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void resetInternalState() {
        mParsingComplete = false;
        mEndOfStreamReached = false;
        mBufferStartPosition = BUFFER_SIZE;
        mBufferEndPosition = BUFFER_SIZE;
        mStringsCount = 0;
        mStringsTable = new String[]{};
        mResourcesCount = 0;
        mResourcesTable = new int[]{};
    }

    /**
     * The parse loop will move block by block through the input stream
     */
    private void processBuffer() throws IOException {

        int id;


        while (!mParsingComplete) {
            // if possible, update the buffer
            updateBuffer();

            // read the next block id
            id = readWord(mBufferStartPosition, 0);

            // switch on block type
            switch (id) {
                case WORD_START_DOCUMENT:
                    parseStartDocument();
                    break;
                case WORD_RES_TABLE:
                    parseResourceTable();
                    break;
                case WORD_STRING_TABLE:
                    parseStringTable();
                    break;
                case WORD_START_NAMESPACE:
                    parseNamespace(true);
                    break;
                case WORD_END_NAMESPACE:
                    parseNamespace(false);
                    break;
                case WORD_START_TAG:
                    parseStartTag();
                    break;
                case WORD_END_TAG:
                    parseEndTag();
                    break;
                case WORD_TEXT:
                    parseText();
                    break;
                default:
                    moveBufferPositionByWords(1);
                    System.err.println(String.format("Unknown block id : 0x%x at position 0x%x", id, mBufferStartPosition));
                    break;
            }

            // Check the end of document
            if (mBufferStartPosition >= mBufferEndPosition) {
                mListener.endDocument();
                mParsingComplete = true;
            }
        }

    }

    /**
     * A document starts with the following words :
     * <ul>
     * <li>0 : 0x00080003</li>
     * <li>1 : document size (bytes) including this start block</li>
     * </ul>
     */
    private void parseStartDocument() {
        mListener.startDocument();
        int docSize = readWord(mBufferStartPosition, 1);

        moveBufferPositionByWords(2);
    }

    /**
     * The resource ids table starts with the following words :
     * <ul>
     * <li>0 : 0x00080180</li>
     * <li>1 : block size (resource ids + these 2 first words)</li>
     * </ul>
     * The block is then followed by a list of resource ids
     * <p>
     * The resources are used by android to match attributes to framework values
     * (or so it seems).
     * </p>
     */
    private void parseResourceTable() {

        int blockSize = readWord(mBufferStartPosition, 1);
        mResourcesCount = (blockSize / WORD_SIZE) - 2; // remove the first 2 words (id, size)

        moveBufferPositionByWords(2);

        // read resources ids
        mResourcesTable = new int[mResourcesCount];
        for (int i = 0; i < mResourcesCount; ++i) {
            mResourcesTable[i] = readWord(mBufferStartPosition, i);
        }

        moveBufferPositionByWords(mResourcesCount);
    }

    /**
     * A string table starts with the following words :
     * <ul>
     * <li>0 : 0x001C0001</li>
     * <li>1 : string table block size</li>
     * <li>2 : number of string in the string table</li>
     * <li>3 : number of styles in the string table</li>
     * <li>4 : encoding (0x00 = UTF-16 Little Endian / Ox100 = UTF-8)</li>
     * <li>5 : Offset from the start of this block to String table data</li>
     * <li>6 : Offset to style data</li>
     * </ul>
     * The block is then followed by a list of offset in the string table, one for each string
     */
    private void parseStringTable() {

        // Read block data
        int blockSize = readWord(mBufferStartPosition, 1);
        mStringsCount = readWord(mBufferStartPosition, 2);
        mStylesCount = readWord(mBufferStartPosition, 3);
        int encoding = readWord(mBufferStartPosition, 4);
        int stringTableOffset = mBufferStartPosition + readWord(mBufferStartPosition, 5);
        int styleOffset = readWord(mBufferStartPosition, 6);

        // read Strings table
        mStringsTable = new String[mStringsCount];

        // Each string is prefixed by a
        int offset;
        for (int i = 0; i < mStringsCount; ++i) {
            offset = stringTableOffset + readWord(mBufferStartPosition, i + 7);
            mStringsTable[i] = readString(offset, encoding);
            System.out.println("STRING : " + mStringsTable[i]);
        }

        // TODO read the styles ???

        moveBufferPositionByBytes(blockSize);
    }

    /**
     * A namespace tag block contains the following words :
     * <ul>
     * <li>0 : 0x00100100 (start) / 0x00100101 (end)</li>
     * <li>1 : block size</li>
     * <li>2 : line number of this tag in the original file</li>
     * <li>3 : ??? (always 0xFFFFFFFF)</li>
     * <li>5 : index of the namespace prefix in String Table</li>
     * <li>5 : index of the namespace uri in String Table</li>
     * </ul>
     */
    private void parseNamespace(boolean isStartBlock) {

        int blockSize = readWord(mBufferStartPosition, 1);
        int lineNumber = readWord(mBufferStartPosition, 2);
        int unknown3 = readWord(mBufferStartPosition, 3);
        int namespacePrefixIndex = readWord(mBufferStartPosition, 4);
        int namespaceUriIndex = readWord(mBufferStartPosition, 5);

        System.out.println(String.format("Unknown value in namespace block : 0x%x", unknown3));

        final String namespacePrefix = getString(namespacePrefixIndex);
        final String namespaceUri = getString(namespaceUriIndex);

        if (isStartBlock) {
            mListener.startPrefixMapping(namespacePrefix, namespaceUri);
            mNamespaces.put(namespaceUri, namespacePrefix);
        } else {
            mListener.endPrefixMapping(namespacePrefix, namespaceUri);
            mNamespaces.remove(namespaceUri);
        }

        moveBufferPositionByWords(6);
    }


    /**
     * A start tag will start with the following words :
     * <ul>
     * <li>0 : 0x00100102</li>
     * <li>1 : block size</li>
     * <li>2 : line number of this tag in the original file</li>
     * <li>3 : ??? (seems to always be  0xFFFFFFFF)</li>
     * <li>4 : index of the namespace uri in String Table, or 0xFFFFFFFF for default namespace</li>
     * <li>5 : index of element name in String Table</li>
     * <li>6 : ???</li>
     * <li>7 : number of attributes following the start tag block</li>
     * <li>8 : ??? (seems to always be 0x0)</li>
     * </ul>
     */
    private void parseStartTag() {

        int blockSize = readWord(mBufferStartPosition, 1);
        int lineNumber = readWord(mBufferStartPosition, 2);
        int unknown3 = readWord(mBufferStartPosition, 3);
        int namespaceUriIndex = readWord(mBufferStartPosition, 4);
        int tagNameIndex = readWord(mBufferStartPosition, 5);
        int unknown6 = readWord(mBufferStartPosition, 6);
        int attributesCount = readWord(mBufferStartPosition, 7);
        int unknown8 = readWord(mBufferStartPosition, 8);

        System.out.println(String.format("Unknown values in start tag block : 0x%x, 0x%x, 0x%x", unknown3, unknown6, unknown8));


        String localName = getString(tagNameIndex);
        String uri, qualifiedName;
        if (namespaceUriIndex == DEFAULT_NAMESPACE) {
            uri = "";
            qualifiedName = localName;
        } else {
            uri = getString(namespaceUriIndex);
            if (mNamespaces.containsKey(uri)) {
                qualifiedName = mNamespaces.get(uri) + ':' + localName;
            } else {
                qualifiedName = localName;
            }
        }

        // offset to start of attributes
        moveBufferPositionByWords(9);

        // read attributes
        final Attribute[] attrs = new Attribute[attributesCount];
        for (int a = 0; a < attributesCount; a++) {
            attrs[a] = parseAttribute();
            System.out.println(attrs[a].toString());
        }

        mListener.startElement(uri, localName, qualifiedName, attrs);
    }

    /**
     * An attribute block will have the following words :
     * <ul>
     * <li>0 : index of namespace uri in StringIndexTable, or 0xFFFFFFFF for default NS</li>
     * <li>1 : index of attribute name in StringIndexTable</li>
     * <li>2 : index of attribute value, or 0xFFFFFFFF if value is a typed value</li>
     * <li>3 : the value type</li>
     * <li>4 : the value data or id</li>
     * </ul>
     */
    private Attribute parseAttribute() {

        int namespaceUriIndex = readWord(mBufferStartPosition, 0);
        int attrNameIndex = readWord(mBufferStartPosition, 1);
        int attrValueIndex = readWord(mBufferStartPosition, 2);
        int attrValueType = readWord(mBufferStartPosition, 3);
        int attrValueData = readWord(mBufferStartPosition, 4);

        String name = getString(attrNameIndex);

        // Read Namespace
        String uri, prefix;
        if (namespaceUriIndex == DEFAULT_NAMESPACE) {
            uri = null;
            prefix = null;
        } else {
            uri = getString(namespaceUriIndex);
            prefix = mNamespaces.get(uri);
        }

        // Read value
        String value;
        if (attrValueIndex == ATTR_TYPED_VALUE) {
            value = getTypedAttributeValue(attrValueType, attrValueData);
        } else {
            value = getString(attrValueIndex);
        }

        moveBufferPositionByWords(5);

        return new Attribute(name, value, prefix, uri);
    }

    /**
     * @param type the type
     * @param data the data word
     * @return the typed value as a String (in most cases, identitcal to the way it appeared in the
     * original xml)
     */
    private String getTypedAttributeValue(final int type, final int data) {
        String value;

        switch (type) {
            case TYPE_ID_REF:
                value = "@" + getIdReference(data);
                break;
            case TYPE_ATTR_REF:
                value = "?" + getIdReference(data);
                break;

//          TODO  case TYPE_STRING:
//                res = getString(data);
//                break;
            case TYPE_FLOAT:
                // keep the same bits, read them as float
                value = Float.toString(Float.intBitsToFloat(data));
                break;
            case TYPE_DIMEN:
                int dimenUnit = data & 0xFF;
                int dimenValue = data >> 8;
                value = String.format("%d%s", dimenValue, DIMENSION_UNIT[dimenUnit]);
                break;
            case TYPE_PERCENT:
                // multiple of 100%, [100% .. 838860000%]
                if ((data & 0xFF) == 0) {
                    int multiple = (data >> 8);
                    value = String.format("%d00%%", multiple);
                } else
                    // [100.1%...25599.99%]
                    if ((data & 0xFF) == 0x20) {
                        double fracValue = (((double) (data >> 8)) / ((double) 0x7FFF));
                        value = new DecimalFormat("#%").format(fracValue);
                    } else
                    // between -99% & 99%
                    {
                        double fracValue = (((double) data) / ((double) 0x7FFFFFFF));
                        value = new DecimalFormat("#.##%").format(fracValue);
                    }
                System.out.println("Percentage : " + data + " -> " + value);
                break;
            case TYPE_INT:
            case TYPE_FLAGS:
                value = Integer.toString(data);
                break;
            case TYPE_BOOL:
                value = (data == 0) ? "false" : "true";
                break;
            case TYPE_COLOR_AARRGGBB:
                value = String.format("#%08X", data);
                break;
            case TYPE_COLOR_RRGGBB:
                value = String.format("#%06X", (data & 0x00FFFFFF));
                break;
            case TYPE_COLOR_ARGB:
                value = String.format(
                        "#%X%X%X%X",
                        (data >> 24) & 0xF,
                        (data >> 16) & 0xF,
                        (data >> 8) & 0xF,
                        data & 0xF);
                break;
            case TYPE_COLOR_RGB:
                value = String.format(
                        "#%X%X%X",
                        (data >> 16) & 0xF,
                        (data >> 8) & 0xF,
                        data & 0xF);
                break;
            default:
                System.out.println(
                        String.format("Attribute type unknown : 0x%x (data = 0x%X) @0x%x",
                                type, data, mBufferStartPosition));
                value = String.format("%08X/0x%08X", type, data);
                break;
        }

        return value;
    }

    // In the android.R class, this is the order the ids are set... apparently
    // TODO test those values with files compiled for older versions of Android...
    private static final String[] ANDROID_REF_TYPES = new String[]{
            null, "attr", "id", "style", "string", "dimen", "color", "array", "drawable",
            "layout", "anim", "animator", "interpolator", "mipmap", "integer", "transition"
    };

    private String getIdReference(int value) {
        String prefix, type;

        int typeIndex = (value & 0x00FF0000) >> 16;

        // get prefix
        if ((value >= 0x01010000) && (value < 0x01100000)) {
            prefix = "android:";
            if ((typeIndex > 0) && (typeIndex < ANDROID_REF_TYPES.length)) {
                type = ANDROID_REF_TYPES[typeIndex];
            } else {
                type = "id";
            }
        } else {
            prefix = "";
            type = "id";
        }


        return String.format("%s%s/0x%08X", prefix, type, value);
    }


    /**
     * A block will start with the following word :
     * <ul>
     * <li>0 : 0x00100104</li>
     * <li>1 : block size</li>
     * <li>2 : line number of this text in the original file</li>
     * <li>3 : ??? (seems to always be 0xFFFFFFF)</li>
     * <li>4 : string index in string table</li>
     * <li>5 : ??? (seems to always be 0x8)</li>
     * <li>6 : ??? (seems to always be 0x0)</li>
     * </ul>
     */
    private void parseText() {

        int blockSize = readWord(mBufferStartPosition, 1);
        int lineNumber = readWord(mBufferStartPosition, 2);
        int unknown3 = readWord(mBufferStartPosition, 3);
        int textIndex = readWord(mBufferStartPosition, 4);
        int unknown5 = readWord(mBufferStartPosition, 5);
        int unknown6 = readWord(mBufferStartPosition, 6);

        System.out.println(String.format("Unknown values in text block : 0x%x, 0x%x, 0x%x", unknown3, unknown5, unknown6));


        String data = getString(textIndex);
        mListener.text(data);


        moveBufferPositionByWords(7);
    }

    /**
     * A closing tag contains the following words :
     * <ul>
     * <li>0 : 0x00100103</li>
     * <li>1 : block size</li>
     * <li>2 : line number of this tag in the original file</li>
     * <li>3 : ??? (seems to always be  0xFFFFFFFF)</li>
     * <li>4 : index of the namespace uri in the String Table, or 0xFFFFFFFF for default namespace</li>
     * <li>5 : index of element name in String Table</li>
     * </ul>
     */
    private void parseEndTag() {

        int blockSize = readWord(mBufferStartPosition, 1);
        int lineNumber = readWord(mBufferStartPosition, 2);
        int unknown3 = readWord(mBufferStartPosition, 3);
        int namespaceUriIndex = readWord(mBufferStartPosition, 4);
        int tagNameIndex = readWord(mBufferStartPosition, 5);

        System.out.println(String.format("Unknown value in end tag block : 0x%x", unknown3));


        String localName = getString(tagNameIndex);
        String uri, qualifiedName;
        if (namespaceUriIndex == DEFAULT_NAMESPACE) {
            uri = "";
            qualifiedName = localName;
        } else {
            uri = getString(namespaceUriIndex);
            if (mNamespaces.containsKey(uri)) {
                qualifiedName = mNamespaces.get(uri) + ':' + localName;
            } else {
                qualifiedName = localName;
            }
        }

        mListener.endElement(uri, localName, qualifiedName);

        moveBufferPositionByWords(6);
    }


    /**
     * Get a string from the string table
     *
     * @param stringIndex the string index
     * @return the string at the given index
     */
    private String getString(final int stringIndex) {
        String result;
        if ((stringIndex >= 0) && (stringIndex < mStringsCount)) {
            result = mStringsTable[stringIndex];
        } else {
            result = null;
        }
        return result;
    }


    /**
     * @param offset offset of the beginning of the string inside the StringTable (and not the whole
     *               data array)
     * @return the String
     */
    private String readString(final int offset, final int encoding) {
        int bytesCount, charOffset, charsCount;
        byte data[];
        String charsetName, result;

        // Each string is prefix by 2 bytes indicating the length of the string
        charOffset = offset + 2;

        switch (encoding) {
            case ENCODING_UTF8:
                charsCount = mBuffer[offset];
                bytesCount = mBuffer[offset + 1];
                data = new byte[bytesCount];
                charsetName = "UTF-8";
                break;
            case ENCODING_UTF16_LE:
                charsCount = ((mBuffer[offset + 1] << 8) & 0xFF00) | (mBuffer[offset] & 0xFF);
                bytesCount = charsCount * 2;
                data = new byte[bytesCount];
                charsetName = "UTF-16LE";
                break;
            default:
                System.err.println(String.format("Unsupporting chars type %x", encoding));
                return "";
        }

        // copy the string bytes
        System.arraycopy(mBuffer, charOffset, data, 0, bytesCount);

        // Convert to string
        try {
            result = new String(data, charsetName);
            if (result.length() != charsCount){
                System.err.println("Decoding seems off, expecting " + charsCount + " characters, final String has " + result.length());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result = "";
        }
        return result;
    }

    private void moveBufferPositionByWords(int wordCount) {
        mBufferStartPosition += wordCount * WORD_SIZE;
    }

    private void moveBufferPositionByBytes(int byteCount) {
        mBufferStartPosition += byteCount;
    }

    /**
     * Reads more data into the buffer
     */
    private void updateBuffer() throws IOException {
        if (mEndOfStreamReached) {
            return;
        }

        if (mBufferStartPosition == 0) {
            return;
        }

        // first decal the end of the buffer
        int remainingLength = mBufferEndPosition - mBufferStartPosition;
        System.arraycopy(mBuffer, mBufferStartPosition, mBuffer, 0, remainingLength);

        // then read from input stream into our buffer
        int sizeToRead = BUFFER_SIZE - remainingLength;
        int sizeRead = mInputStream.read(mBuffer, remainingLength, sizeToRead);

        if (sizeRead == -1) {
            mEndOfStreamReached = true;
            sizeRead = 0;
        }

        mBufferEndPosition = remainingLength + sizeRead;
        mBufferStartPosition = 0;
    }

    /**
     * Read a 4 byte word at the given offset and index. Words are read as Little Endian 32 bit word
     *
     * @param bufferStartOffset the byte offset in the buffer
     * @param wordIndex         the index of the word after the offset
     * @return the word as an int
     */
    private int readWord(int bufferStartOffset, int wordIndex) {

        int offset = bufferStartOffset + (wordIndex * WORD_SIZE);

        return ((mBuffer[offset + 3] << 24) & 0xff000000)
                | ((mBuffer[offset + 2] << 16) & 0x00ff0000)
                | ((mBuffer[offset + 1] << 8) & 0x0000ff00)
                | ((mBuffer[offset]) & 0x000000ff);
    }


}
