package fr.xgouchet.xmleditor.test;

import fr.xgouchet.xmleditor.core.model.TreeNode;
import fr.xgouchet.xmleditor.core.model.XmlNode;

/**
 * @author Xavier Gouchet
 */
public final class Assertions {

    public static <T> TreeNodeAssert<T> assertThat(TreeNode<T> actual) {
        return new TreeNodeAssert<>(actual);
    }

    public static XmlNodeAssert assertThat(XmlNode actual) {
        return new XmlNodeAssert(actual);
    }

    private Assertions() {
    }
}
