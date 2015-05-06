package fr.xgouchet.xmleditor.core.actions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import fr.xgouchet.xmleditor.core.model.TreeNode;
import fr.xgouchet.xmleditor.core.model.TreeNodeVisitor;
import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlCData;
import fr.xgouchet.xmleditor.core.xml.XmlComment;
import fr.xgouchet.xmleditor.core.xml.XmlContent;
import fr.xgouchet.xmleditor.core.xml.XmlDocTypeDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlDocumentDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlExternalDTD;
import fr.xgouchet.xmleditor.core.xml.XmlProcessingInstruction;
import fr.xgouchet.xmleditor.core.xml.XmlPublicDTD;
import fr.xgouchet.xmleditor.core.xml.XmlText;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

/**
 * TODO ensure that attributes " / ' are properly (un)escaped as needed on read/write.
 * TODO ensure ref entities (&, ", ...) are properly (un)escaped as needed on read/write.
 * TODO allow setting tabs / spaces as indentation
 *
 * @author Xavier Gouchet
 */
public class SaveXmlAction implements AsyncAction<Pair<XmlNode, ? extends OutputStream>, Void> {

    public static final String INDENTATION = "  ";

    @Nullable
    @Override
    public Void performAction(@Nullable Pair<XmlNode, ? extends OutputStream> input) throws Exception {
        if (input == null){
            return null;
        }

        XmlNode document = input.first;
        OutputStream output = input.second;

        WriterXmlVisitor visitor = new WriterXmlVisitor(output);

        visitor.visit(document);
        visitor.close();

        return null;
    }

    /**
     * An XmlNode visitor which writes a text output representing the content of the XML document
     */
    static class WriterXmlVisitor extends TreeNodeVisitor<XmlContent> {

        private final OutputStreamWriter mWriter;

        public WriterXmlVisitor(final @NonNull OutputStream output) {
            mWriter = new OutputStreamWriter(output);
        }

        @Override
        protected void onVisitNode(final @NonNull TreeNode<XmlContent> node, final int depth) throws Exception {
            writeIndentation(depth);
            writeNodeContent(node);
        }

        @Override
        public void onNodeVisited(final @NonNull TreeNode<XmlContent> node, final int depth) throws Exception {
            if ((node.getData().getType() == XmlUtils.XML_ELEMENT) && (!node.isLeaf())) {
                writeIndentation(depth);
                writeElementClose((XmlElement) node.getData());
            }
            mWriter.flush();
        }

        /**
         * @param node the node being written
         * @throws IOException
         */
        private void writeNodeContent(final @NonNull TreeNode<XmlContent> node) throws IOException {
            XmlContent content = node.getData();
            switch (content.getType()) {
                case XmlUtils.XML_DOCUMENT_DECLARATION:
                    writeDocDecl((XmlDocumentDeclaration) content);
                    break;
                case XmlUtils.XML_DOCTYPE:
                    writeDocType((XmlDocTypeDeclaration) content);
                    break;
                case XmlUtils.XML_ELEMENT:
                    writeElementOpen((XmlElement) content, node.isLeaf());
                    break;
                case XmlUtils.XML_COMMENT:
                    writeComment((XmlComment) content);
                    break;
                case XmlUtils.XML_TEXT:
                    writeText((XmlText) content);
                    break;
                case XmlUtils.XML_CDATA:
                    writeCData((XmlCData) content);
                    break;
                case XmlUtils.XML_PROCESSING_INSTRUCTION:
                    writeProcessingInstruction((XmlProcessingInstruction) content);
                    break;
                case XmlUtils.XML_DOCUMENT:
                    // Ignore
                    break;
            }
        }

        /**
         * @param pi the processing instruction to write
         * @throws IOException
         */
        private void writeProcessingInstruction(XmlProcessingInstruction pi) throws IOException{
            mWriter.write("<?");
            mWriter.write(pi.getTarget());
            mWriter.write(" ");
            mWriter.write(pi.getInstruction());
            mWriter.write("?>\n");
        }

        /**
         * @param text the text to write
         * @throws IOException
         */
        private void writeText(XmlText text) throws IOException {
            mWriter.write(text.getText());
            mWriter.write("\n");
        }

        /**
         * @param cData the CData to write
         * @throws IOException
         */
        private void writeCData(XmlCData cData) throws IOException {
            mWriter.write("<![CDATA[");
            mWriter.write(cData.getText());
            mWriter.write("]]>\n");
        }

        /**
         * @param comment the comment to write
         * @throws IOException
         */
        private void writeComment(XmlComment comment) throws IOException {
            mWriter.write("<!--");
            mWriter.write(comment.getText());
            mWriter.write("-->\n");
        }

        /**
         * @param docDecl the document declaration to write
         * @throws IOException
         */
        private void writeDocDecl(final @NonNull XmlDocumentDeclaration docDecl) throws IOException {
            mWriter.write("<?xml");
            mWriter.write(" version=\"");
            mWriter.write(docDecl.getVersion());
            mWriter.write("\"");

            String encoding = docDecl.getEncoding();
            if (encoding != null) {
                mWriter.write(" encoding=\"");
                mWriter.write(docDecl.getEncoding());
                mWriter.write("\"");
            }

            if (docDecl.hasAttribute(XmlDocumentDeclaration.STANDALONE)) {
                mWriter.write(" standalone=\"");
                mWriter.write(docDecl.isStandalone() ? XmlDocumentDeclaration.STANDALONE_YES : XmlDocumentDeclaration.STANDALONE_NO);
                mWriter.write("\"");
            }
            mWriter.write("?>\n");
        }

        /**
         * @param element the element to write
         * @throws IOException
         */
        private void writeElementOpen(final @NonNull XmlElement element, final boolean isLeaf) throws IOException {
            mWriter.write("<");
            mWriter.write(element.getQualifiedName());

            for (XmlAttribute attribute : element.getAttributes()) {
                mWriter.write(" ");
                mWriter.write(attribute.getQualifiedName());
                mWriter.write("=\"");
                mWriter.write(attribute.getValue());
                mWriter.write("\"");
            }

            if (isLeaf) {
                mWriter.write("/>\n");
            } else {
                mWriter.write(">\n");
            }

        }

        /**
         * @param element the element to write
         * @throws IOException
         */
        private void writeElementClose(final @NonNull XmlElement element) throws IOException {
            mWriter.write("</");
            mWriter.write(element.getQualifiedName());
            mWriter.write(">\n");
        }

        /**
         * @param docTypeDecl the DTD to write
         * @throws IOException
         */
        private void writeDocType(final @NonNull XmlDocTypeDeclaration docTypeDecl) throws IOException {
            mWriter.write("<!DOCTYPE ");
            mWriter.write(docTypeDecl.getRootElement());

            // External doctype reference
            if (docTypeDecl instanceof XmlExternalDTD) {
                mWriter.write(" ");
                mWriter.write(docTypeDecl.getDeclarationType());

                if (docTypeDecl instanceof XmlPublicDTD) {
                    mWriter.write(" \"");
                    mWriter.write(((XmlPublicDTD) docTypeDecl).getName());
                    mWriter.write("\"");
                }

                mWriter.write(" \"");
                mWriter.write(((XmlExternalDTD) docTypeDecl).getLocation());
                mWriter.write("\"");
            }

            // Internal definition / override
            String internalDef = docTypeDecl.getInternalDefinition();
            if (internalDef != null) {
                mWriter.write(" [");
                mWriter.write(internalDef);
                mWriter.write("]");
            }

            mWriter.write(">\n");
        }

        /**
         * @param depth the node depth
         * @throws IOException
         */
        private void writeIndentation(final int depth) throws IOException {

            if (depth > 1) {
                for (int i = 1; i < depth; ++i) {
                    mWriter.write(INDENTATION);
                }
            }
        }

        public void close() throws IOException {
            mWriter.flush();
            mWriter.close();
        }
    }
}
