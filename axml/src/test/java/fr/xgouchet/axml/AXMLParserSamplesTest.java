package fr.xgouchet.axml;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Xavier Gouchet
 */
public class AXMLParserSamplesTest {

    @Test
    public void parseSamples() throws IOException{
        File dir = new File("testres/axml/samples");

        File[] samples = dir.listFiles();
        AXMLParser parser = new AXMLParser();
        parser.setVerbosity(AXMLParser.LOG_EVERYTHING);

        ByteArrayOutputStream output;
        for (File sample : samples){
            System.out.println("---------- " + sample.getName() );
            output = new ByteArrayOutputStream();
            parser.parse(new FileInputStream(sample), output);

            System.out.println(new String(output.toByteArray()));
        }
    }


    private AXMLParser.Listener mListener = new AXMLParser.Listener() {
        @Override
        public void startDocument() {

        }

        @Override
        public void endDocument() {

        }

        @Override
        public void startPrefixMapping(String prefix, String uri) {

        }

        @Override
        public void endPrefixMapping(String prefix, String uri) {

        }

        @Override
        public void startElement(String localName, Attribute[] attributes, String uri, String prefix) {

        }

        @Override
        public void endElement(String localName, String uri, String prefix) {

        }

        @Override
        public void text(String data) {

        }
    };
}
