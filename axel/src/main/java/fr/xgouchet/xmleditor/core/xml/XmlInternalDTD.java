package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class XmlInternalDTD extends XmlDocTypeDeclaration {

    private final String mDefinition;

    public XmlInternalDTD(final @NonNull String rootElement,
                          final @NonNull String definition) {
        super(INTERNAL, rootElement);
        mDefinition = definition;
    }

    public String getDefinition() {
        return mDefinition;
    }

    @Override
    public String toString() {
        return "<!DOCTYPE " + getRootElement() +
                " [" + mDefinition + "]>";
    }
}
