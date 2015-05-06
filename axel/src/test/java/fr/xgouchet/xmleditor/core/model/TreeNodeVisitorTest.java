package fr.xgouchet.xmleditor.core.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 * @author Xavier Gouchet
 */
@SuppressWarnings("unchecked")
public class TreeNodeVisitorTest {

    private TreeNodeVisitor mMockVisitor;
    private TreeNodeVisitor<String> mVisitor;

    @Before
    public void setUp() throws Exception {
        mMockVisitor = mock(TreeNodeVisitor.class);
        doNothing().when(mMockVisitor).onVisitNode(Matchers.<TreeNode>any(), anyInt());

        mVisitor = new TreeNodeVisitor<String>() {

            @Override
            protected void onVisitNode(TreeNode node, int depth) throws Exception {
                System.out.println("Visiting " + node.getData() + " at depth " + depth + " : " + node);
                mMockVisitor.onVisitNode(node, depth);
            }

            @Override
            public void onNodeVisited(TreeNode<String> node, int depth) throws Exception {
                System.out.println("Visited  " + node.getData() + " at depth " + depth + " : " + node);
                mMockVisitor.onNodeVisited(node, depth);
            }
        };
    }

    @Test
    public void shouldVisitTree() throws Exception {
        TreeNode _0 = new TreeNode("0");
        TreeNode _00 = new TreeNode(_0, "00");
        TreeNode _01 = new TreeNode(_0, "01");
        TreeNode _000 = new TreeNode(_00, "000");
        TreeNode _001 = new TreeNode(_00, "001");
        TreeNode _002 = new TreeNode(_00, "002");
        TreeNode _010 = new TreeNode(_01, "010");

        mVisitor.visit(_0);

        InOrder inOrder = inOrder(mMockVisitor);

        inOrder.verify(mMockVisitor).onVisitNode(eq(_0), eq(0));
        inOrder.verify(mMockVisitor).onVisitNode(eq(_00), eq(1));
        inOrder.verify(mMockVisitor).onVisitNode(eq(_000), eq(2));
        inOrder.verify(mMockVisitor).onNodeVisited(eq(_000), eq(2));
        inOrder.verify(mMockVisitor).onVisitNode(eq(_001), eq(2));
        inOrder.verify(mMockVisitor).onNodeVisited(eq(_001), eq(2));
        inOrder.verify(mMockVisitor).onVisitNode(eq(_002), eq(2));
        inOrder.verify(mMockVisitor).onNodeVisited(eq(_002), eq(2));
        inOrder.verify(mMockVisitor).onNodeVisited(eq(_00), eq(1));
        inOrder.verify(mMockVisitor).onVisitNode(eq(_01), eq(1));
        inOrder.verify(mMockVisitor).onVisitNode(eq(_010), eq(2));
        inOrder.verify(mMockVisitor).onNodeVisited(eq(_010), eq(2));
        inOrder.verify(mMockVisitor).onNodeVisited(eq(_01), eq(1));
        inOrder.verify(mMockVisitor).onNodeVisited(eq(_0), eq(0));
    }

}