package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public class XmlExternalDTD extends XmlDocTypeDeclaration {

    private final String mLocation;

    public XmlExternalDTD(final @Type String type,
                          final @NonNull String rootElement,
                          final @NonNull String location,
                          final @Nullable String internalDefinition) {
        super(type, rootElement, internalDefinition);
        mLocation = location;
    }

    @NonNull
    public String getLocation() {
        return mLocation;
    }

}
