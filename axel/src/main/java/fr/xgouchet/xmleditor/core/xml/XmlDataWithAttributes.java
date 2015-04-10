package fr.xgouchet.xmleditor.core.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An Attributed xml data (Element, DocDecl)
 * <p/>
 *
 * @author Xavier Gouchet
 */
abstract class XmlDataWithAttributes extends XmlData {

    final Map<String, XmlAttribute> mAttributes = new HashMap<>();

    /**
     * Create an  XmlAttributedData entity with the given type
     *
     * @param type
     */
    public XmlDataWithAttributes(final @XmlUtils.XmlNodeType int type) {
        super(type);
    }

    /**
     * Adds an attribute to this node. If an attribute with the same qualified name already exists,
     * it will be overwritten
     *
     * @param attribute the attribute to add
     */
    public void addAttribute(XmlAttribute attribute) {
        String key = attribute.getQualifiedName();
        mAttributes.put(key, attribute);
    }

    public void removeAttribute(XmlAttribute attribute) {
        String key = attribute.getQualifiedName();
        mAttributes.remove(key);
    }

    public XmlAttribute getAttribute(String qualifiedName) {
        return null;
    }

    public Collection<XmlAttribute> getAttributes() {
        return mAttributes.values();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("XmlDataWithAttributes{");

        for (XmlAttribute attr : mAttributes.values()) {
            builder.append(attr.toString());
            builder.append(", ");
        }

        builder.append("}");

        return builder.toString();
    }
}
