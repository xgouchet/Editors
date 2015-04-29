package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public final class XmlSystemDTD extends XmlExternalDTD {

    public XmlSystemDTD(final @NonNull String rootElement,
                        final @NonNull String location) {
        super(SYSTEM, rootElement, location);
    }

    public XmlSystemDTD(final @NonNull String rootElement,
                        final @NonNull String location,
                        final @Nullable String internal) {
        super(SYSTEM, rootElement, location, internal);
    }

    @Override
    public String toString() {
        return "<!DOCTYPE " + getRootElement() +
                " " + getDeclarationType() +
                " \"" + getLocation() + "\">";
    }
}
