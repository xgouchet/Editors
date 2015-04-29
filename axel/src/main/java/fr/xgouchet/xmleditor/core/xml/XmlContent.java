package fr.xgouchet.xmleditor.core.xml;

/**
 * This class represents the data contained in an Xml node, without knowledge of the surrouding nodes.
 * <p/>
 * For instance, the xml data for each element foo in the following xml would be identical :
 * <p/>
 * <pre><code>
 *     &lt;root>
 *       &lt;foo>
 *         &lt;spam/>
 *         &lt;foo/>
 *       &lt;/foo>
 *     &lt;/root>
 * </code></pre>
 * <p/>
 *
 * @author Xavier Gouchet
 */
public abstract class XmlContent {

    @XmlUtils.XmlNodeType
    final int mType;

    /**
     * Create an XmlData entity with the given type
     *
     * @param type
     */
    XmlContent(final @XmlUtils.XmlNodeType int type) {
        mType = type;
    }

    @XmlUtils.XmlNodeType
    public int getType() {
        return mType;
    }
}
