package fr.xgouchet.xmleditor.core.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import fr.xgouchet.xmleditor.test.Assertions;

import static fr.xgouchet.xmleditor.test.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class TreeNodeTest {

    private TreeNode<Object> mTreeNode;
    private Object mData;

    @Before
    public void setUp() {
        mData = mock(Object.class);
        mTreeNode = new TreeNode<>(mData);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void shouldHoldUserData() {
        assertThat(mTreeNode)
                .hasDataExactly(mData);
    }

    @Test
    public void shouldAppendChildrenNodes() {
        TreeNode<Object> child0 = mock(TreeNode.class);
        TreeNode<Object> child1 = mock(TreeNode.class);

        doNothing().when(child0).updateDepthRecursive();
        doNothing().when(child1).updateDepthRecursive();

        mTreeNode.appendChild(child0);
        mTreeNode.appendChild(child1);

        assertThat(mTreeNode)
                .hasChildrenExactly(child0, child1);
    }

    @Test
    public void shouldInsertChildrenNodes() {
        TreeNode<Object> child0 = mock(TreeNode.class);
        TreeNode<Object> child1 = mock(TreeNode.class);
        TreeNode<Object> child2 = mock(TreeNode.class);

        doNothing().when(child0).updateDepthRecursive();
        doNothing().when(child1).updateDepthRecursive();
        doNothing().when(child2).updateDepthRecursive();

        mTreeNode.appendChild(child0);
        mTreeNode.insertChild(child1, 3);
        mTreeNode.insertChild(child2, 1);

        assertThat(mTreeNode)
                .hasChildrenExactly(child0, child2, child1);
    }

    @Test
    public void shouldHaveChildrenAccess() {
        TreeNode<Object> child0 = mock(TreeNode.class);
        TreeNode<Object> child1 = mock(TreeNode.class);
        TreeNode<Object> child2 = mock(TreeNode.class);

        doNothing().when(child0).updateDepthRecursive();
        doNothing().when(child1).updateDepthRecursive();
        doNothing().when(child2).updateDepthRecursive();

        mTreeNode.appendChild(child0);
        mTreeNode.appendChild(child1);
        mTreeNode.appendChild(child2);

        assertThat(mTreeNode.getChildrenCount())
                .isEqualTo(3);

        assertThat(mTreeNode.getChild(0))
                .isSameAs(child0);
        assertThat(mTreeNode.getChild(1))
                .isSameAs(child1);
        assertThat(mTreeNode.getChild(2))
                .isSameAs(child2);
        try {
            mTreeNode.getChild(3);
            fail("Expected an IooB exception... ");
        } catch (IndexOutOfBoundsException ignored) {
        }


        assertThat(mTreeNode.getChildPosition(child0))
                .isEqualTo(0);
        assertThat(mTreeNode.getChildPosition(child1))
                .isEqualTo(1);
        assertThat(mTreeNode.getChildPosition(child2))
                .isEqualTo(2);

        assertThat(mTreeNode.getChildPosition(mock(TreeNode.class)))
                .isEqualTo(-1);
    }

    @Test
    public void shouldRemoveChildren() {
        TreeNode<Object> child0 = mock(TreeNode.class);
        TreeNode<Object> child1 = mock(TreeNode.class);
        TreeNode<Object> child2 = mock(TreeNode.class);

        doNothing().when(child0).updateDepthRecursive();
        doNothing().when(child1).updateDepthRecursive();
        doNothing().when(child2).updateDepthRecursive();

        mTreeNode.appendChild(child0);
        mTreeNode.appendChild(child1);
        mTreeNode.appendChild(child2);

        // remove object
        mTreeNode.removeChild(child1);
        assertThat(mTreeNode)
                .hasChildrenExactly(child0, child2);

        // remove at position
        mTreeNode.removeChild(0);
        assertThat(mTreeNode)
                .hasChildrenExactly(child2);

        // remove at wrong position (IooB)
        try {
            mTreeNode.removeChild(42);
            fail("Should throw IooB");
        } catch (IndexOutOfBoundsException ignored) {
        }

        try {
            mTreeNode.removeChild(-3);
            fail("Should throw IooB");
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test // make sure that the link is coherent in both ways, whatever the operation is
    public void shouldEnsureParentChildLink() {

        // link by constructor
        TreeNode a = new TreeNode(null);
        TreeNode b = new TreeNode(a, null);
        assertThat(a).hasChildren(b);
        assertThat(b).hasParent(a);

        // link by child addition
        TreeNode c = new TreeNode(null);
        a.appendChild(c);
        assertThat(a).hasChildren(c);
        assertThat(c).hasParent(a);

        // link by child insertion
        TreeNode d = new TreeNode(null);
        a.insertChild(d, 0);
        assertThat(a).hasChildren(d);
        assertThat(d).hasParent(a);

        // unlink by child removal (object)
        a.removeChild(b);
        assertThat(a).doesNotHaveChildren(b);
        assertThat(b).hasNullParent();

        // unlink by child removal (position)
        a.removeChild(0);
        assertThat(a).doesNotHaveChildren(d);
        assertThat(d).hasNullParent();

        // make sure unlinking doesn't remove a link that should not be removed
        b.appendChild(d);
        a.removeChild(d);
        assertThat(b).hasChildren(d);
        assertThat(d).hasParent(b);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldPreventAddingChildWithParent() {
        TreeNode a, b, c;

        try {
            a = new TreeNode(null);
            b = new TreeNode(a, null);
            c = new TreeNode(null);
        } catch (UnsupportedOperationException e) {
            fail("Unexpected exception " + e);
            return;
        }

        c.appendChild(b);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldPreventInsertingChildWithParent() {
        TreeNode a, b, c;

        try {
            a = new TreeNode(null);
            b = new TreeNode(a, null);
            c = new TreeNode(null);
        } catch (UnsupportedOperationException e) {
            fail("Unexpected exception " + e);
            return;
        }

        c.insertChild(b, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldPreventAddingTheSameChildTwice() {
        TreeNode a = new TreeNode(null);
        TreeNode b = new TreeNode(a, null);

        assertThat(a).hasChildrenExactly(b);

        a.appendChild(b);
        assertThat(a).hasChildrenExactly(b);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void shouldPreventInsertingTheSameChildTwice() {
        TreeNode a = new TreeNode(null);
        TreeNode b = new TreeNode(a, null);

        assertThat(a).hasChildrenExactly(b);

        a.insertChild(b, 0);
        assertThat(a).hasChildrenExactly(b);
    }

    @Test
    public void shouldComputeDepth() {
        // root node
        TreeNode a = new TreeNode(null);
        assertThat(a).hasDepth(0);

        // constructor child
        TreeNode b = new TreeNode(a, null);
        assertThat(b).hasDepth(1);

        // node addition
        TreeNode c = new TreeNode(null);
        a.appendChild(c);
        assertThat(c).hasDepth(1);

        // node insertion
        TreeNode d = new TreeNode(null);
        c.insertChild(d, 0);
        assertThat(d).hasDepth(2);


        // Recursive addition
        TreeNode e = new TreeNode(null);
        e.appendChild(a);
        assertThat(e).hasDepth(0);
        assertThat(a).hasDepth(1);
        assertThat(b).hasDepth(2);
        assertThat(c).hasDepth(2);
        assertThat(d).hasDepth(3);

        // Recursive insertion
        TreeNode f = new TreeNode(null);
        f.insertChild(e, 0);
        assertThat(f).hasDepth(0);
        assertThat(e).hasDepth(1);
        assertThat(a).hasDepth(2);
        assertThat(b).hasDepth(3);
        assertThat(c).hasDepth(3);
        assertThat(d).hasDepth(4);

        // remove child reset the depth
        c.removeChild(0);
        assertThat(d).hasDepth(0);

        a.removeChild(c);
        assertThat(c).hasDepth(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotAddAncestorAsChild(){
        TreeNode a = new TreeNode(null);
        TreeNode b = new TreeNode(a, null);
        TreeNode c = new TreeNode(b, null);

        c.appendChild(a);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotInsertAncestorAsChild(){
        TreeNode a = new TreeNode(null);
        TreeNode b = new TreeNode(a, null);
        TreeNode c = new TreeNode(b, null);

        c.insertChild(a, 0);
    }

}