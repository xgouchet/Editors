package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier Gouchet
 */
public final class XmlElement extends XmlAttributedContent {

    private String mLocalName;
    private XmlNamespace mNamespace;

    public XmlElement(final @NonNull String localName) {
        this(localName, null, null);
    }

    public XmlElement(final @NonNull String localName,
                      final @Nullable String namespacePrefix,
                      final @Nullable String namespaceUri) {
        super(XmlUtils.XML_ELEMENT);
        mLocalName = localName;
        mNamespace = XmlNamespace.from(namespacePrefix, namespaceUri);
    }

    public String getLocalName() {
        return mLocalName;
    }

    public XmlNamespace getNamespace() {
        return mNamespace;
    }

    @Nullable
    public String getNamespacePrefix() {
        return mNamespace == null ? null : mNamespace.getPrefix();
    }

    @Nullable
    public String getNamespaceUri() {
        return mNamespace == null ? null : mNamespace.getUri();
    }

    @NonNull
    public String getQualifiedName() {

        String prefix = getNamespacePrefix();

        return (prefix == null) ? mLocalName : (prefix + XmlUtils.PREFIX_SEPARATOR + mLocalName);
    }


    public void setLocalName(final @NonNull String localName) {
        mLocalName = localName;
    }

    public void setNamespace(final @Nullable XmlNamespace namespace) {
        mNamespace = namespace;
    }

    public void setNamespace(final @Nullable String namespacePrefix, final @Nullable String namespaceUri) {
        mNamespace = XmlNamespace.from(namespacePrefix, namespaceUri);
    }


    @NonNull
    public List<XmlNamespace> getDeclaredNamespaces() {

        List<XmlNamespace> declaredNamespaces = new ArrayList<>();

        XmlNamespace ns;
        for (XmlAttribute attribute : getAttributes()) {
            ns = attribute.getNamespace();

            if (XmlUtils.ATTR_XMLNS.equals(attribute.getQualifiedName())) {
                declaredNamespaces.add(XmlNamespace.from(null, attribute.getValue()));
            } else if ((ns != null) && (ns.isXmlns())) {
                declaredNamespaces.add(XmlNamespace.from(attribute.getName(), attribute.getValue()));
            }
        }

        return declaredNamespaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlElement that = (XmlElement) o;

        if (!mAttributes.equals(that.mAttributes)) return false;
        if (!mLocalName.equals(that.mLocalName)) return false;
        if (mNamespace != null ? !mNamespace.equals(that.mNamespace) : that.mNamespace != null)
            return false;


        return true;
    }

    @Override
    public int hashCode() {
        int result = mLocalName.hashCode();
        result = 31 * result + (mNamespace != null ? mNamespace.hashCode() : 0);
        result = 31 * result + mAttributes.hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "<" + getQualifiedName() + "> [" + mAttributes.size() + " attributes]";
    }
}
