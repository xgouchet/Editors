package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;

/**
 * @author Xavier Gouchet
 */
public class XmlInternalDTD extends XmlDocTypeDeclaration {


    public XmlInternalDTD(final @NonNull String rootElement,
                          final @NonNull String internalDefinition) {
        super(INTERNAL, rootElement, internalDefinition);
    }

    @Override
    public String toString() {
        return "<!DOCTYPE " + getRootElement() +
                " [" + getInternalDefinition() + "]>";
    }
}
