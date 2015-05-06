package fr.xgouchet.xmleditor.core.model;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * This class describes a tree node visitor, which will visit each node in a tree
 *
 * @param <T> the type of data the TreeNode holds
 * @author Xavier Gouchet
 */
public abstract class TreeNodeVisitor<T> {


    /**
     * Visits the whole tree underneath the given node
     *
     * @param node the root node of the tree to visit
     */
    public void visit(final @NonNull TreeNode<T> node) throws Exception {
        visitNode(node, 0);
    }

    /**
     * Visit a node recursively
     *
     * @param node  the node visited
     * @param depth the depth of the given node
     */
    private void visitNode(final @NonNull TreeNode<T> node, final int depth) throws Exception {
        onVisitNode(node, depth);

        int childrenDepth = depth + 1;
        for (TreeNode<T> child : node.getChildren()) {
            visitNode(child, childrenDepth);
        }

        onNodeVisited(node, depth);
    }

    /**
     * @param node  the node being visited
     * @param depth the depth of the given node (root = 0)
     * @throws Exception
     */
    protected abstract void onVisitNode(final @NonNull TreeNode<T> node, final int depth) throws Exception;

    /**
     * Called when the node and all its children have been visited
     *
     * @param node  the node which was visited
     * @param depth the depth of the given node (root = 0)
     * @throws Exception
     */
    protected abstract void onNodeVisited(final @NonNull TreeNode<T> node, final int depth) throws Exception;
}
