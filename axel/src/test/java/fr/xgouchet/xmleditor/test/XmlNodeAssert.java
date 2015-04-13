package fr.xgouchet.xmleditor.test;

import org.assertj.core.api.AbstractAssert;

import fr.xgouchet.xmleditor.core.model.TreeNode;
import fr.xgouchet.xmleditor.core.model.XmlNode;
import fr.xgouchet.xmleditor.core.xml.XmlData;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Xavier Gouchet
 */
public class XmlNodeAssert extends AbstractAssert<XmlNodeAssert, XmlNode> {

    TreeNodeAssert<XmlData> mXmlDataTreeNodeAssert;

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
    public XmlNodeAssert hasData(XmlData data) {
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
    public XmlNodeAssert hasDataExactly(XmlData data) {
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
}
