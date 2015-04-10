package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public final class XmlNamespace {

    private static final XmlNamespace XMLNS = new XmlNamespace(XmlUtils.PREFIX_XMLNS, "");

    private final String mPrefix;
    private final String mUri;

    XmlNamespace(final @NonNull String prefix, final @NonNull String uri) {
        mPrefix = prefix;
        mUri = uri;
    }

    @Nullable
    public static XmlNamespace from(final @Nullable String prefix, final @Nullable String uri) {

        if (XmlUtils.PREFIX_XMLNS.equals(prefix)) {
            return XMLNS;
        } else {
            if ((prefix == null)
                    || (uri == null)
                    || (prefix.length() == 0)
                    || (uri.length() == 0)) {
                return null;
            } else {
                return new XmlNamespace(prefix.intern(), uri.intern());
            }
        }
    }

    @NonNull
    public String getPrefix() {
        return mPrefix;
    }

    @NonNull
    public String getUri() {
        return mUri;
    }

    public boolean isXmlns() {
        return this == XMLNS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlNamespace that = (XmlNamespace) o;

        // Strings are interned, so use == instead of equals
        if (mPrefix != that.mPrefix) return false;
        if (mUri != that.mUri) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mPrefix.hashCode();
        result = 31 * result + mUri.hashCode();
        return result;
    }
}
