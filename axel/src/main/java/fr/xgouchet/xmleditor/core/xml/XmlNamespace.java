package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author Xavier Gouchet
 */
public final class XmlNamespace {

    private static final String TAG = XmlNamespace.class.getSimpleName();

    public static final XmlNamespace XMLNS = new XmlNamespace(XmlUtils.PREFIX_XMLNS, "");

    private final String mPrefix;
    private final String mUri;

    XmlNamespace(final @Nullable String prefix, final @NonNull String uri) {
        mPrefix = prefix;
        mUri = uri;
    }

    @Nullable
    public static XmlNamespace from(final @Nullable String prefix, final @Nullable String uri) {

        if (XmlUtils.PREFIX_XMLNS.equals(prefix)) {
            Log.d(TAG, "XMLNS Namespace : [" + prefix + "]->" + uri);
            return XMLNS;
        } else {
            if ((uri == null)
                    || ("".equals(prefix))
                    || (uri.length() == 0)) {
                Log.d(TAG, "Invalid Namespace : [" + prefix + "]->" + uri);
                return null;
            } else {
                if (prefix == null) {
                    Log.d(TAG, "Default Namespace : [" + prefix + "]->" + uri);
                    return new XmlNamespace(null, uri.intern());
                } else {
                    Log.d(TAG, "Namespace : [" + prefix + "]->" + uri);
                    return new XmlNamespace(prefix.intern(), uri.intern());
                }
            }
        }
    }

    @Nullable
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

    @SuppressWarnings("StringEquality")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlNamespace namespace = (XmlNamespace) o;

        if (mPrefix != namespace.mPrefix) return false;
        return mUri == namespace.mUri;

    }

    @Override
    public int hashCode() {
        int result = mPrefix == null ? 0 : mPrefix.hashCode();
        result = 31 * result + mUri.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "XmlNamespace{ [" + mPrefix + "]->" + mUri + '}';
    }
}
