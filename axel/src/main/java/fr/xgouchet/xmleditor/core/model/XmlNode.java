package fr.xgouchet.xmleditor.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.xmleditor.core.xml.XmlContent;
import fr.xgouchet.xmleditor.core.xml.XmlElement;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

/**
 * @author Xavier Gouchet
 */
public class XmlNode extends TreeNode<XmlContent> {

    /**
     * @param data any non null XML data
     */
    XmlNode(final @NonNull XmlContent data) {
        this(null, data);
    }

    /**
     * @param parent the parent XMLNode
     * @param data   any non null XML data
     */
    XmlNode(final @Nullable XmlNode parent, final @NonNull XmlContent data) {
        super(parent, data);
    }


    @Nullable
    @Override
    public XmlNode getParent() {
        return (XmlNode) super.getParent();
    }

    @NonNull
    @Override
    public XmlNode getChild(int position) {
        return (XmlNode) super.getChild(position);
    }

    /**
     * @return the canonical XPath leading to this node.
     * If this node is node an element, an XPath like string will be returned (eg : "/foo {comment}")
     */
    @NonNull
    public String getXPath() {

        if (getParent() == null) {
            return getLocalXPath();
        } else {
            return getParent().getXPath() + getLocalXPath();
        }
    }

    /**
     * @return an XPath value from this node's parent to this node
     */
    @NonNull
    private String getLocalXPath() {
        XmlContent data = getData();

        switch (data.getType()) {
            case XmlUtils.XML_ELEMENT:
                String local = "/" + ((XmlElement) data).getQualifiedName();
                if (getParent() == null) {
                    return local;
                } else {
                    int count = 0;
                    int index = 0;

                    for (TreeNode<XmlContent> sibling : getParent().getChildren()) {
                        if ((sibling == null)) {
                            continue;
                        }
                        XmlContent siblingData = sibling.getData();
                        if (siblingData.getType() != XmlUtils.XML_ELEMENT) {
                            continue;
                        }
                        if (sibling == this) {
                            index = count;
                        }
                        if (((XmlElement) siblingData).getQualifiedName().equals(((XmlElement) data).getQualifiedName())) {
                            count++;
                        }
                    }

                    if (count > 1) {
                        // Xpath index starts at 1
                        local += "[" + (index + 1) + "]";
                    }
                    return local;
                }
            case XmlUtils.XML_DOCUMENT:
                return "";
            default:
                return " {" + XmlUtils.getTypeName(data.getType()) + "}";
        }

    }

    /**
     * Search in this node and its children for a node with the given type. The first node to match
     * the type is returned.
     *
     * @param type the type to look for
     * @return the top and first node with the given XML type, or null if none is found.
     */
    @Nullable
    public XmlNode findNodeWithType(final @XmlUtils.XmlNodeType int type) {
        // easy
        if (type == getDataType()) {
            return this;
        }

        XmlNode node, best = null;
        for (TreeNode<XmlContent> child : getChildren()) {

            node = ((XmlNode) child).findNodeWithType(type);

            if (node == null) {
                continue;
            }

            if ((best == null) || (node.getDepth() < best.getDepth())) {
                best = node;
                if (best.getDepth() == (getDepth() + 1)) {
                    break;
                }
            }
        }

        return best;
    }

    /**
     * @return the Xml data type of this node
     */
    public int getDataType() {
        return getData().getType();
    }

    @Override
    public String toString() {
        return "XmlNode{" + getData() + "}";
    }
}
