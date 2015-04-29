package fr.xgouchet.xmleditor.test;

import org.assertj.core.api.AbstractAssert;

import fr.xgouchet.xmleditor.core.model.TreeNode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Xavier Gouchet
 */
@SuppressWarnings("unchecked")
public class TreeNodeAssert<T> extends AbstractAssert<TreeNodeAssert<T>, TreeNode<T>> {

    public TreeNodeAssert(TreeNode<T> actual) {
        super(actual, TreeNodeAssert.class);
    }

    /**
     * Verify that the actual parent of the node is equal to the given node
     *
     * @param parent the node to compare the actual parent to
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasParent(TreeNode<T> parent) {
        isNotNull();

        assertThat(actual.getParent())
                .overridingErrorMessage("Expected parent to be <%s> but was <%s>", parent, actual.getParent())
                .isEqualTo(parent);

        return this;
    }

    /**
     * Verify that the actual node has a null parent
     *
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasNullParent() {
        isNotNull();

        assertThat(actual.getParent())
                .overridingErrorMessage("Expected parent to be <null> but was <%s>", actual.getParent())
                .isNull();

        return this;
    }


    /**
     * Verifies that the data held by the actual node is equal to the given value
     *
     * @param data the given data to compare the actual data to.
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasData(T data) {
        isNotNull();

        assertThat(actual.getData())
                .overridingErrorMessage("Expected data to be <%s> but was <%s>", data, actual.getData())
                .isEqualTo(data);

        return this;
    }

    /**
     * Verifies that the data held by the actual node is the same instance as the given value
     *
     * @param data the given data to compare the actual data to.
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasDataExactly(T data) {
        isNotNull();

        assertThat(actual.getData())
                .overridingErrorMessage("Expected data to be <%s> but was <%s>", data, actual.getData())
                .isSameAs(data);

        return this;
    }

    /**
     * Verifies that the actual node contains only the given children and nothing else, <b>in order</b>.
     *
     * @param children the expected children in order
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasChildrenExactly(TreeNode<T>... children) {
        isNotNull();

        assertThat(actual.getChildren())
                .containsExactly(children);

        return this;
    }


    /**
     * Verifies that the actual node contains at least the given children, <b>in any order</b>.
     *
     * @param children the expected children, in any order
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasChildren(TreeNode<T>... children) {
        isNotNull();

        assertThat(actual.getChildren())
                .contains(children);

        return this;
    }


    /**
     * Verifies that the actual node does not contain the given children
     *
     * @param nodes the nodes we don't want to find has children
     * @return this assertion object
     */
    public TreeNodeAssert<T> doesNotHaveChildren(TreeNode<T>... nodes) {
        isNotNull();

        assertThat(actual.getChildren())
                .doesNotContain(nodes);

        return this;
    }

    /**
     * Verify that the actual node has a the given depth
     *
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasDepth(int depth) {
        isNotNull();

        assertThat(actual.getDepth())
                .overridingErrorMessage("Expected depth to be <%d> but was <%d>", depth, actual.getDepth())
                .isEqualTo(depth);

        return this;
    }


    /**
     * Verify that the actual node has the given number of children
     *
     * @return this assertion object
     */
    public TreeNodeAssert<T> hasChildrenCount(int count) {
        isNotNull();

        assertThat(actual.getChildrenCount())
                .overridingErrorMessage("Expected children count to be <%d> but was <%d>", count, actual.getChildrenCount())
                .isEqualTo(count);

        return this;
    }
}
