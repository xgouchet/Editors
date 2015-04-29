package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public class XmlAttribute {

    private String mName;
    private String mValue;
    private XmlNamespace mNamespace;


    public XmlAttribute(String name, String value) {
        mName = name;
        mValue = value;
    }

    public XmlAttribute(final @NonNull String name,
                        final @NonNull String value,
                        final @Nullable String namespacePrefix,
                        final @Nullable String namespaceUri) {
        mName = name;
        mValue = value;
        mNamespace = XmlNamespace.from(namespacePrefix, namespaceUri);
    }

    public XmlAttribute(final @NonNull XmlAttribute attribute) {
        this(attribute.getName(),
                attribute.getValue(),
                attribute.getNamespacePrefix(),
                attribute.getNamespaceUri());
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public String getValue() {
        return mValue;
    }


    @Nullable
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
        return (prefix == null) ? mName : (prefix + XmlUtils.PREFIX_SEPARATOR + mName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlAttribute that = (XmlAttribute) o;

        if (!mName.equals(that.mName)) return false;
        if (mNamespace != null ? !mNamespace.equals(that.mNamespace) : that.mNamespace != null)
            return false;
        if (!mValue.equals(that.mValue)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mValue.hashCode();
        result = 31 * result + (mNamespace != null ? mNamespace.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "XmlAttribute{" + getQualifiedName() + "=\"" + mValue + "\"}";
    }
}
