package fr.xgouchet.xmleditor.test;

import fr.xgouchet.xmleditor.core.model.TreeNode;

/**
 * @author Xavier Gouchet
 */
public final class Assertions {

    public static <T> TreeNodeAssert<T> assertThat(TreeNode<T> actual) {
        return new TreeNodeAssert(actual);
    }

    private Assertions() {
    }
}
