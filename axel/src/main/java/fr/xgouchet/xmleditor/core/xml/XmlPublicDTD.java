package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Xavier Gouchet
 */
public final class XmlPublicDTD extends XmlExternalDTD {

    public static final String SEPARATOR = "//";

    public static final String PREFIX_ISO = "ISO";
    public static final String PREFIX_APPROVED = "+";
    public static final String PREFIX_UNAPPROVED = "-";

    private String mName;

    public XmlPublicDTD(final @NonNull String rootElement,
                        final @NonNull String name,
                        final @NonNull String location) {
        this(rootElement, name, location, null);
    }


    public XmlPublicDTD(final @NonNull String rootElement,
                        final @NonNull String name,
                        final @NonNull String location,
                        final @Nullable String internalDefinition) {
        super(PUBLIC, rootElement, location, internalDefinition);
        mName = name;
        // TODO parse name : "PREFIX//OWNER//DESCRIPTION WITH SPACES//ISO_639_LANG"
        // eg : "-//W3C//DTD HTML 4.0 Transitional//EN"
    }

    @NonNull
    public String getName() {
        return mName;
    }


    @Override
    public String toString() {
        return "<!DOCTYPE " + getRootElement() +
                " " + getDeclarationType() +
                " \"" + getName() + "\">" +
                " \"" + getLocation() + "\">";
    }
}
