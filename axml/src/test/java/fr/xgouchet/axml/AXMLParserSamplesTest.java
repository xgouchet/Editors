package fr.xgouchet.axml;

import org.junit.Test;

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

        for (File sample : samples){
            System.out.println("---------- " + sample.getName() );
//            parser.parse(new FileInputStream(sample), mListener);
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
        public void startElement(String uri, String localName, String qName, Attribute[] atts) {

        }

        @Override
        public void endElement(String uri, String localName, String qName) {

        }

        @Override
        public void text(String data) {

        }

        @Override
        public void characterData(String data) {

        }

        @Override
        public void processingInstruction(String target, String data) {

        }
    };
}
