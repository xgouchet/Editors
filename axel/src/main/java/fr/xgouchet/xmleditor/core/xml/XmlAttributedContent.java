package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An Attributed xml data (Element, DocDecl)
 * <p/>
 *
 * @author Xavier Gouchet
 */
abstract class XmlAttributedContent extends XmlContent {

    final Map<String, XmlAttribute> mAttributes = new HashMap<>();

    /**
     * Create an  XmlAttributedData entity with the given type
     *
     * @param type
     */
    public XmlAttributedContent(final @XmlUtils.XmlNodeType int type) {
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

    /**
     * Removes an attribute from this node.
     *
     * @param attribute the attribute to remove
     */
    public void removeAttribute(XmlAttribute attribute) {
        String key = attribute.getQualifiedName();
        mAttributes.remove(key);
    }

    /**
     * @param qualifiedName the qualified name of the attribute
     * @return the attribute object, or null if no matching attribute is found
     */
    @Nullable
    public XmlAttribute getAttribute(final @NonNull String qualifiedName) {
        return mAttributes.get(qualifiedName);
    }

    /**
     * @param qualifiedName the qualified name of the attribute
     * @return if a matching attribute is present on this node
     */
    public boolean hasAttribute(final @NonNull String qualifiedName) {
        return mAttributes.containsKey(qualifiedName);
    }

    /**
     * @return the collection of attributes on this node
     */
    @NonNull
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

    public void addAttributes(final @NonNull Collection<XmlAttribute> attributes) {
        for (XmlAttribute attribute : attributes) {
            addAttribute(attribute);
        }
    }
}
