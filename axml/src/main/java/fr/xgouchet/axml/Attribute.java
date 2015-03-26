package fr.xgouchet.axml;

/**
 * Represents an Attribute in an xml tag.
 * <br/>
 * For instance, if the xml tag is <code>&lt;element foo="42"/&gt;</code>, then the Attribute will
 * have a name "foo", value "42" and null prefix and namespace.
 * <br/>
 * If the xml tag is <code>&lt;element xmlns:bar="spam" foo="42"/&gt;</code>, then the Attribute will
 * have a name "foo", value "42", prefix "bar" and namespace "spam"
 */
public final class Attribute {

    private String mName, mPrefix, mNamespaceUri, mValue;

    public Attribute() {
        this(null, null);
    }

    public Attribute(String name, String value) {
        this(name, value, null, null);
    }

    public Attribute(String name, String value, String prefix, String namespaceUri) {
        mName = name;
        mPrefix = prefix;
        mNamespaceUri = namespaceUri;
        mValue = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return mPrefix;
    }

    /**
     * @return the namespace
     */
    public String getNamespaceUri() {
        return mNamespaceUri;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return mValue;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        mName = name;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(final String prefix) {
        mPrefix = prefix;
    }

    /**
     * @param namespaceUri the namespaceUri to set
     */
    public void setNamespaceUri(final String namespaceUri) {
        mNamespaceUri = namespaceUri;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final String value) {
        mValue = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (!mName.equals(attribute.mName)) return false;
        if (mNamespaceUri != null ? !mNamespaceUri.equals(attribute.mNamespaceUri) : attribute.mNamespaceUri != null)
            return false;
        if (mPrefix != null ? !mPrefix.equals(attribute.mPrefix) : attribute.mPrefix != null)
            return false;
        if (!mValue.equals(attribute.mValue)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + (mPrefix != null ? mPrefix.hashCode() : 0);
        result = 31 * result + (mNamespaceUri != null ? mNamespaceUri.hashCode() : 0);
        result = 31 * result + mValue.hashCode();
        return result;
    }

    @Override
    public String toString() {

        return "Attribute{"
                + ((mPrefix == null) ? "" : mPrefix + ':')
                + mName + "=\"" + mValue + "\""
                + ((mNamespaceUri == null) ? "" : " [" + mNamespaceUri + "]")
                + "}";
    }
}