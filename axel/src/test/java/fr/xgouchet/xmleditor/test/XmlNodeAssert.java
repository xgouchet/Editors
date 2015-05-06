package fr.xgouchet.xmleditor.test;

import org.assertj.core.api.AbstractAssert;

import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.xml.XmlAttribute;
import fr.xgouchet.xmleditor.core.xml.XmlCData;
import fr.xgouchet.xmleditor.core.xml.XmlComment;
import fr.xgouchet.xmleditor.core.xml.XmlContent;
import fr.xgouchet.xmleditor.core.xml.XmlDocTypeDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlDocumentDeclaration;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlInternalDTD;
import fr.xgouchet.xmleditor.core.xml.XmlProcessingInstruction;
import fr.xgouchet.xmleditor.core.xml.XmlPublicDTD;
import fr.xgouchet.xmleditor.core.xml.XmlSystemDTD;
import fr.xgouchet.xmleditor.core.xml.XmlText;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Xavier Gouchet
 */
public class XmlNodeAssert extends AbstractAssert<XmlNodeAssert, XmlNode> {

    TreeNodeAssert<XmlContent> mXmlDataTreeNodeAssert;

    public XmlNodeAssert(XmlNode actual) {
        super(actual, XmlNodeAssert.class);

        mXmlDataTreeNodeAssert = new TreeNodeAssert<>(actual);
    }

    /**
     * Verify that the actual parent of the node is equal to the given node
     *
     * @param parent the node to compare the actual parent to
     * @return this assertion object
     */
    public XmlNodeAssert hasParent(XmlNode parent) {
        isNotNull();

        mXmlDataTreeNodeAssert.hasParent(parent);

        return this;
    }

    /**
     * Verify that the actual node has a null parent
     *
     * @return this assertion object
     */
    public XmlNodeAssert hasNullParent() {
        isNotNull();

        mXmlDataTreeNodeAssert.hasNullParent();

        return this;
    }


    /**
     * Verifies that the data held by the actual node is equal to the given value
     *
     * @param data the given data to compare the actual data to.
     * @return this assertion object
     */
    public XmlNodeAssert hasData(XmlContent data) {
        isNotNull();

        mXmlDataTreeNodeAssert.hasData(data);

        return this;
    }

    /**
     * Verifies that the data held by the actual node is the same instance as the given value
     *
     * @param data the given data to compare the actual data to.
     * @return this assertion object
     */
    public XmlNodeAssert hasDataExactly(XmlContent data) {
        isNotNull();

        mXmlDataTreeNodeAssert.hasDataExactly(data);

        return this;
    }

    /**
     * Verifies that the actual node contains only the given children and nothing else, <b>in order</b>.
     *
     * @param children the expected children in order
     * @return this assertion object
     */
    public XmlNodeAssert hasChildrenExactly(XmlNode... children) {
        isNotNull();

        mXmlDataTreeNodeAssert.hasChildrenExactly(children);

        return this;
    }


    /**
     * Verifies that the actual node contains at least the given children, <b>in any order</b>.
     *
     * @param children the expected children, in any order
     * @return this assertion object
     */
    public XmlNodeAssert hasChildren(XmlNode... children) {
        isNotNull();

        mXmlDataTreeNodeAssert.hasChildren(children);

        return this;
    }


    /**
     * Verifies that the actual node does not contain the given children
     *
     * @param nodes the nodes we don't want to find has children
     * @return this assertion object
     */
    public XmlNodeAssert doesNotHaveChildren(XmlNode... nodes) {
        isNotNull();

        mXmlDataTreeNodeAssert.doesNotHaveChildren(nodes);

        return this;
    }

    /**
     * Verify that the actual node has the given number of children
     *
     * @return this assertion object
     */
    public XmlNodeAssert hasChildrenCount(int count) {
        isNotNull();

        mXmlDataTreeNodeAssert.hasChildrenCount(count);

        return this;
    }

    /**
     * Verify that the actual node has the given depth
     *
     * @return this assertion object
     */
    public XmlNodeAssert hasDepth(int depth) {
        isNotNull();

        mXmlDataTreeNodeAssert.hasDepth(depth);

        return this;
    }

    /**
     * Verify that the actual node as the given XPath
     *
     * @return this assertion object
     */
    public XmlNodeAssert hasXPath(String xPath) {
        isNotNull();

        assertThat(actual.getXPath())
                .overridingErrorMessage("Expecting XPath to be <%s> but was <%s>", xPath, actual.getXPath())
                .isEqualTo(xPath);

        return this;
    }

    /**
     * Verify that the actual node as the given type
     *
     * @return this assertion object
     */
    public XmlNodeAssert hasType(@XmlUtils.XmlNodeType int type) {
        isNotNull();

        assertThat(actual.getDataType())
                .overridingErrorMessage("Expecting node type to be <%s> but was <%s>",
                        XmlUtils.getTypeName(type),
                        XmlUtils.getTypeName(actual.getDataType()))
                .isEqualTo(type);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Element
     * - has the given qualified name
     *
     * @return this assertion object
     */
    public XmlNodeAssert isDocument() {
        isNotNull();

        hasType(XmlUtils.XML_DOCUMENT);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Element
     * - has the given qualified name
     *
     * @return this assertion object
     */
    public XmlNodeAssert isElement(String qualifiedName) {
        isNotNull();

        hasType(XmlUtils.XML_ELEMENT);

        assertThat(((XmlElement) actual.getData()).getQualifiedName())
                .overridingErrorMessage(
                        "Expected qualified name to be <%s> but was <%s>",
                        qualifiedName,
                        ((XmlElement) actual.getData()).getQualifiedName()
                )
                .isEqualTo(qualifiedName);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Element
     * - has the given qualified name
     *
     * @return this assertion object
     */
    public XmlNodeAssert isElement(String qualifiedName, String namespaceUri) {
        isNotNull();

        hasType(XmlUtils.XML_ELEMENT);

        XmlElement element = (XmlElement) actual.getData();

        assertThat(element.getQualifiedName())
                .overridingErrorMessage(
                        "Expected qualified name to be <%s> but was <%s>",
                        qualifiedName,
                        element.getQualifiedName())
                .isEqualTo(qualifiedName);

        if (namespaceUri == null) {
            assertThat(element.getNamespace())
                    .overridingErrorMessage(
                            "Expected null namespace but was <%s>",
                            element.getNamespace())
                    .isNull();
        } else {
            assertThat(element.getNamespace().getUri())
                    .overridingErrorMessage(
                            "Expected namespace URI to be <%s> but was <%s>",
                            namespaceUri,
                            element.getNamespace().getUri())
                    .isEqualTo(namespaceUri);
        }

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Text
     * - has the given text content
     *
     * @return this assertion object
     */
    public XmlNodeAssert isText(String text) {
        isNotNull();

        hasType(XmlUtils.XML_TEXT);

        assertThat(((XmlText) actual.getData()).getText())
                .overridingErrorMessage(
                        "Expected text to be <%s> but was <%s>",
                        text,
                        ((XmlText) actual.getData()).getText()
                )
                .isEqualTo(text);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type CData
     * - has the given text content
     *
     * @return this assertion object
     */
    public XmlNodeAssert isCData(String text) {
        isNotNull();

        hasType(XmlUtils.XML_CDATA);

        assertThat(((XmlCData) actual.getData()).getText())
                .overridingErrorMessage(
                        "Expected CData to be <%s> but was <%s>",
                        text,
                        ((XmlCData) actual.getData()).getText()
                )
                .isEqualTo(text);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Comment
     * - has the given text content
     *
     * @return this assertion object
     */
    public XmlNodeAssert isComment(String comment) {
        isNotNull();

        hasType(XmlUtils.XML_COMMENT);

        assertThat(((XmlComment) actual.getData()).getText())
                .overridingErrorMessage(
                        "Expected Comment to be <%s> but was <%s>",
                        comment,
                        ((XmlComment) actual.getData()).getText()
                )
                .isEqualTo(comment);

        return this;
    }


    /**
     * Verify that the actual node :
     * - is of type DocDecl
     * - has the given version, encoding and standalone values (null params are ignored)
     *
     * @return this assertion object
     */
    public XmlNodeAssert isDocDecl(String version, String encoding, Boolean standalone) {
        isNotNull();

        hasType(XmlUtils.XML_DOCUMENT_DECLARATION);

        XmlDocDeclDataAssert dataAssert = new XmlDocDeclDataAssert((XmlDocumentDeclaration) actual.getData());

        if (version != null) {
            dataAssert.hasVersion(version);
        }

        if (encoding != null) {
            dataAssert.hasEncoding(encoding);
        }

        if (standalone != null) {
            dataAssert.isStandalone(standalone);
        }

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Processing Instruction
     * - has the given target and isntructions
     *
     * @return this assertion object
     */
    public XmlNodeAssert isProcessingInstruction(String target, String instruction) {
        isNotNull();

        hasType(XmlUtils.XML_PROCESSING_INSTRUCTION);

        XmlProcInstrDataAssert procInstrAssert = new XmlProcInstrDataAssert((XmlProcessingInstruction) actual.getData());

        procInstrAssert
                .hasTarget(target)
                .hasInstruction(instruction);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Doctype (with DTD type SYSTEM)
     * - has the given root and location valus
     *
     * @return this assertion object
     */
    public XmlNodeAssert isSystemDoctype(String root, String location, String internal) {
        isNotNull();

        hasType(XmlUtils.XML_DOCTYPE);

        XmlDocTypeDeclaration dtd = (XmlDocTypeDeclaration) actual.getData();
        assertThat(dtd.getDeclarationType())
                .isEqualTo(XmlDocTypeDeclaration.SYSTEM);

        XmlSystemDTD systemDTD = (XmlSystemDTD) dtd;

        assertThat(systemDTD.getRootElement())
                .overridingErrorMessage("Expected DTD root to be <%s> but was <%s>", root, systemDTD.getRootElement())
                .isEqualTo(root);

        assertThat(systemDTD.getLocation())
                .overridingErrorMessage("Expected DTD location to be <%s> but was <%s>", location, systemDTD.getLocation())
                .isEqualTo(location);

        assertThat(systemDTD.getInternalDefinition())
                .overridingErrorMessage("Expected DTD internal definition to be <%s> but was <%s>", internal, systemDTD.getInternalDefinition())
                .isEqualTo(internal);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Doctype (with DTD type PUBLIC)
     * - has the given root, dtd name and location values
     *
     * @return this assertion object
     */
    public XmlNodeAssert isPublicDoctype(String root, String name, String location, String internal) {
        isNotNull();

        hasType(XmlUtils.XML_DOCTYPE);

        XmlDocTypeDeclaration dtd = (XmlDocTypeDeclaration) actual.getData();
        assertThat(dtd.getDeclarationType())
                .isEqualTo(XmlDocTypeDeclaration.PUBLIC);

        XmlPublicDTD publicDTD = (XmlPublicDTD) dtd;

        assertThat(publicDTD.getRootElement())
                .overridingErrorMessage("Expected DTD root to be <%s> but was <%s>", root, publicDTD.getRootElement())
                .isEqualTo(root);

        assertThat(publicDTD.getName())
                .overridingErrorMessage("Expected DTD name to be <%s> but was <%s>", name, publicDTD.getName())
                .isEqualTo(name);

        assertThat(publicDTD.getLocation())
                .overridingErrorMessage("Expected DTD location to be <%s> but was <%s>", location, publicDTD.getLocation())
                .isEqualTo(location);

        assertThat(publicDTD.getInternalDefinition())
                .overridingErrorMessage("Expected DTD internal definition to be <%s> but was <%s>", internal, publicDTD.getInternalDefinition())
                .isEqualTo(internal);

        return this;
    }

    /**
     * Verify that the actual node :
     * - is of type Doctype (with DTD type INTERNAL)
     * - has the given root, and definition values
     *
     * @return this assertion object
     */
    public XmlNodeAssert isInternalDoctype(String root, String definition) {
        isNotNull();

        hasType(XmlUtils.XML_DOCTYPE);

        XmlDocTypeDeclaration dtd = (XmlDocTypeDeclaration) actual.getData();
        assertThat(dtd.getDeclarationType())
                .isEqualTo(XmlDocTypeDeclaration.INTERNAL);

        XmlInternalDTD internalDTD = (XmlInternalDTD) dtd;

        assertThat(internalDTD.getRootElement())
                .overridingErrorMessage("Expected DTD root to be <%s> but was <%s>", root, internalDTD.getRootElement())
                .isEqualTo(root);

        assertThat(internalDTD.getInternalDefinition())
                .overridingErrorMessage("Expected DTD name to be <%s> but was <%s>", definition, internalDTD.getInternalDefinition())
                .isEqualTo(definition);

        return this;
    }

    /**
     * Verifies this node holds the given attributes.
     * This will only work for Element typed nodes.
     *
     * @return this assertion object
     */
    public XmlNodeAssert hasAttributes(XmlAttribute... attrs) {
        isNotNull();

        hasType(XmlUtils.XML_ELEMENT);

        XmlElement data = (XmlElement) actual.getData();

        assertThat(data.getAttributes())
                .contains(attrs);

        return this;
    }
}
