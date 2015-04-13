package fr.xgouchet.xmleditor.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fr.xgouchet.xmleditor.core.xml.XmlData;
import fr.xgouchet.xmleditor.core.xml.XmlElementData;
import fr.xgouchet.xmleditor.core.xml.XmlUtils;

/**
 * @author Xavier Gouchet
 */
public class XmlNode extends TreeNode<XmlData> {

    /**
     * @param data any non null XML data
     */
    public XmlNode(final @NonNull XmlData data) {
        this(null, data);
    }

    /**
     * @param parent the parent XMLNode
     * @param data   any non null XML data
     */
    public XmlNode(final @Nullable XmlNode parent, final @NonNull XmlData data) {
        super(parent, data);
    }

    @Nullable
    @Override
    public XmlNode getParent() {
        return (XmlNode) super.getParent();
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
        XmlData data = getData();

        switch (data.getType()) {
            case XmlUtils.XML_ELEMENT:
                String local = "/" + ((XmlElementData) data).getQualifiedName();
                if (getParent() == null) {
                    return local;
                } else {
                    int count = 0;
                    int index = 0;

                    for (TreeNode<XmlData> sibling : getParent().getChildren()) {
                        if ((sibling == null)) {
                            continue;
                        }
                        XmlData siblingData = sibling.getData();
                        if (siblingData.getType() != XmlUtils.XML_ELEMENT) {
                            continue;
                        }
                        if (sibling == this) {
                            index = count;
                        }
                        if (((XmlElementData) siblingData).getQualifiedName().equals(((XmlElementData) data).getQualifiedName())) {
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
        if (type == getData().getType()) {
            return this;
        }

        XmlNode node, best = null;
        for (TreeNode<XmlData> child : getChildren()) {

            node = ((XmlNode) child).findNodeWithType(type);

            if (node == null){
                continue;
            }

            if ((best == null) || (node.getDepth() < best.getDepth())){
                best = node;
                if (best.getDepth() == (getDepth() + 1)){
                    break;
                }
            }
        }

        return best;
    }
}
