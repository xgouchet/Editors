package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public class XmlExternalDTD extends XmlDocTypeDeclaration {

    private final String mLocation;
    private final String mInternal;

    public XmlExternalDTD(final @Type String type,
                          final @NonNull String rootElement,
                          final @NonNull String location) {
        this(type, rootElement, location, null);
    }

    public XmlExternalDTD(final @Type String type,
                          final @NonNull String rootElement,
                          final @NonNull String location,
                          final @Nullable String internal) {
        super(type, rootElement);
        mLocation = location;
        mInternal = internal;
    }

    @NonNull
    public String getLocation() {
        return mLocation;
    }

    @Nullable
    public String getInternal() {
        return mInternal;
    }
}
