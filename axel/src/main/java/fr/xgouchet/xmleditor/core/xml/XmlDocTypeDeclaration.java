package fr.xgouchet.xmleditor.core.xml;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

/**
 * @author Xavier Gouchet
 */
public abstract class XmlDocTypeDeclaration extends XmlContent {

    @StringDef({INTERNAL, SYSTEM, PUBLIC})
    public @interface Type {
    }

    public static final String SYSTEM = "SYSTEM";
    public static final String PUBLIC = "PUBLIC";
    public static final String INTERNAL = "INTERNAL";

    private final String mDeclarationType;
    private final String mRootElement;


    public XmlDocTypeDeclaration(final @Type String declarationType,
                                 final @NonNull String rootElement) {
        super(XmlUtils.XML_DOCTYPE);
        mDeclarationType = declarationType;
        mRootElement = rootElement;
    }

    @Type
    public String getDeclarationType() {
        return mDeclarationType;
    }

    @NonNull
    public String getRootElement() {
        return mRootElement;
    }
}
