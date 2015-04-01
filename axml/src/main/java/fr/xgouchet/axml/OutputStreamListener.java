package fr.xgouchet.axml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * An AXMLParser.Listener implementation which writes a text/xml representation in an output stream.
 *
 * @author Xavier Gouchet
 */
public class OutputStreamListener implements AXMLParser.Listener {

    private final OutputStream mOutputStream;
    private final Writer mWriter;
    private int mDepth = 0;

    private int mPreviousElement = 0;
    private static final int START_TAG = 1;
    private static final int END_TAG = 2;
    private static final int TEXT = 3;
    private final Map<String, String> mNamespaceMap;

    public OutputStreamListener(final OutputStream outputStream) {
        mOutputStream = outputStream;
        mWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

        mNamespaceMap = new HashMap<>();
    }

    @Override
    public void startDocument() throws IOException {
        mWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    }

    @Override
    public void endDocument() throws IOException {
        mWriter.flush();
        mWriter.close();
        mOutputStream.flush();
        mOutputStream.close();
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) {
        mNamespaceMap.put(prefix, uri);
    }

    @Override
    public void endPrefixMapping(final String prefix, final String uri) {
    }

    @Override
    public void startElement(final String localName,
                             final Attribute[] attributes,
                             final String uri,
                             final String prefix) throws IOException {

        if (mPreviousElement == START_TAG) {
            // close previous start tag
            mWriter.write(">\n");
        }

        if (mPreviousElement == TEXT) {
            // add line break
            mWriter.write("\n");
        }

        writeIndentation();

        mWriter.write("<");
        writeTagName(localName, uri, prefix);

        // declare new namespaces
        for (Map.Entry<String , String> namespace : mNamespaceMap.entrySet()){
            mWriter.write(" xmlns:");
            mWriter.write(namespace.getKey());
            mWriter.write("=\"");
            mWriter.write(namespace.getValue());
            mWriter.write("\"");
        }
        mNamespaceMap.clear();

        // declare attributes
        for (Attribute attribute : attributes){
            mWriter.write(" ");
            mWriter.write(attribute.getQualifiedName());
            mWriter.write("=\"");
            mWriter.write(attribute.getValue());
            mWriter.write("\"");
        }

        // update internal state
        mDepth++;
        mPreviousElement = START_TAG;
    }

    @Override
    public void endElement(final String localName,
                           final String uri,
                           final String prefix) throws IOException {

        mDepth--;

        switch (mPreviousElement) {
            case START_TAG:
                // self close
                mWriter.write("/>\n");
                break;
            case END_TAG:
                writeIndentation();
                mWriter.write("</");
                writeTagName(localName, uri, prefix);
                mWriter.write(">\n");
                break;
            case TEXT:
                mWriter.write("</");
                writeTagName(localName, uri, prefix);
                mWriter.write(">\n");
                break;
        }

        mPreviousElement = END_TAG;
    }

    @Override
    public void text(final String data) throws IOException {
        if (mPreviousElement == START_TAG){
            // close previous start tag
            mWriter.write(">");
        }

        mWriter.write(data);
        mPreviousElement = TEXT;
    }


    /**
     * Writes the proper indentation
     *
     * @throws IOException
     */
    private void writeIndentation() throws IOException {

        for (int i = 0; i < mDepth; ++i) {
            mWriter.write("  ");
        }
    }

    /**
     * Writes the tag name (without the brackets) in the proper form
     *
     * @param localName the local name (without prefix)
     * @param uri       the namespace uri
     * @param prefix    the namespace prefix
     * @throws IOException
     */
    private void writeTagName(final String localName,
                              final String uri,
                              final String prefix)
            throws IOException {

        if ((uri != null) && (uri.length() > 0)){
            mWriter.write(prefix);
            mWriter.write(":");
        }
        mWriter.write(localName);
    }


}
