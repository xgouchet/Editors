package fr.xgouchet.xmleditor.core.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Node in a Tree, holding user data. A tree node's parent and children must have the
 * same data type.
 *
 * @param <T> the type of the user data held in the node
 * @author Xavier Gouchet
 */
public class TreeNode<T> {

    private int mDepth;
    private TreeNode<T> mParent;
    private final List<TreeNode<T>> mChildren = new LinkedList<>();
    private final T mData;

    /**
     * Constructs a new root TreeNode instance (a node without parent)
     *
     * @param data the data held by this node (note that it <b>can't</b> be changed afterwards)
     */
    public TreeNode(final @NonNull T data) {
        this(null, data);
    }

    /**
     * Constructs a new TreeNode instance
     *
     * @param parent the parent of this node (note that it can be changed afterwards if this node is
     *               attached or removed from other nodes )
     * @param data   the data held by this node (note that it <b>can't</b> be changed afterwards)
     */
    public TreeNode(final @Nullable TreeNode<T> parent, final @NonNull T data) {
        mData = data;

        if (parent != null) {
            parent.appendChild(this);
        }
    }

    /**
     * @param parent the new parent for this node
     */
    private void setParent(final @Nullable TreeNode<T> parent) {
        mParent = parent;
        updateDepthRecursive();
    }

    /**
     * @return the parent of this node, or null if this node is a root
     */
    @Nullable
    public TreeNode<T> getParent() {
        return mParent;
    }

    /**
     * @return the children of this node
     */
    @NonNull
    public List<TreeNode<T>> getChildren() {
        return mChildren;
    }

    /**
     * @return the number of children attached to this node
     */
    public int getChildrenCount() {
        return mChildren.size();
    }

    /**
     * @param node a node to look for in the direct children of this node
     * @return the position of this node in the children, or -1 if the given node is not a child of this node
     */
    public int getChildPosition(final @NonNull TreeNode<T> node) {
        return mChildren.indexOf(node);
    }

    /**
     * @param position the position (might throw an IooB exception if the position is out of bound)
     * @return the child node at the given position
     */
    @NonNull
    public TreeNode<T> getChild(final int position) {
        return mChildren.get(position);
    }

    /**
     * @return the user data in this node
     */
    @NonNull
    public T getData() {
        return mData;
    }


    /**
     * @return the depth of this node from the root of this tree (the highest parent possible)
     */
    public int getDepth() {
        return mDepth;
    }

    /**
     * @return if this node is a leaf (it has no children)
     */
    public boolean isLeaf() {
        return mChildren.isEmpty();
    }

    /**
     * @return if this node is a root (it has no parent)
     */
    public boolean isRoot() {
        return mParent == null;
    }

    /**
     * @param node a node to test
     * @return if the given node is an ancestor of this node
     */
    public boolean isAncestor(final @NonNull TreeNode<T> node) {

        TreeNode<T> ancestor = getParent();

        while (ancestor != null) {
            if (ancestor == node) {
                return true;
            }

            ancestor = ancestor.getParent();
        }

        return false;
    }

    /**
     * Attach a child to this node. The child will be ordered after all existing children of this node.
     *
     * @param child the child to attach to this node (must not be null)
     */
    public void appendChild(final @NonNull TreeNode<T> child) {

        // child already has a parent
        if (child.getParent() != null) {
            throw new UnsupportedOperationException("Cannot append child, it already has a parent");
        }

        // child is an ancestor (avoid weird families)
        if (isAncestor(child)) {
            throw new UnsupportedOperationException("Cannot append child, it is an ancestor of this node");
        }

        if (!mChildren.contains(child)) {
            if (mChildren.add(child)) {
                child.setParent(this);
            }
        }
    }

    /**
     * Attach a child to this node. The child will be ordered at the given position, if possible.
     * When inserting the given child, other children after the given position will be shifted to
     * make room for the new child.
     * <p/>
     * If there are not enough children, the child will be ordered after all existing children of
     * this node.
     *
     * @param child    the child to attach to this node (must not be null)
     * @param position the position to give the child in the children list.
     */
    public void insertChild(final @NonNull TreeNode<T> child, final int position) {

        // child already has a parent
        if (child.getParent() != null) {
            throw new UnsupportedOperationException("Cannot append child, it already has a parent");
        }

        // child is an ancestor (avoid weird families)
        if (isAncestor(child)) {
            throw new UnsupportedOperationException("Cannot append child, it is an ancestor of this node");
        }

        if (!mChildren.contains(child)) {
            boolean added;
            if (position >= mChildren.size()) {
                added = mChildren.add(child);
            } else {
                mChildren.add(position, child);
                added = true;
            }

            if (added) {
                child.setParent(this);
            }
        }
    }

    /**
     * Remove the given child from the children list
     *
     * @param child the child to remove
     */
    public void removeChild(final TreeNode<T> child) {
        if (mChildren.contains(child)) {
            mChildren.remove(child);
            child.setParent(null);
        }
    }

    /**
     * Removes the child at the given position
     *
     * @param position the position to remove a child from
     */
    public void removeChild(final int position) {
        TreeNode<T> child = mChildren.get(position);
        mChildren.remove(position);
        child.setParent(null);
    }

    /**
     * Updates (recursively) the depth of this node and its children. This should only be triggered
     * when the parent of this node changes
     */
    void updateDepthRecursive() {
        int oldDepth = mDepth;

        if (mParent == null) {
            mDepth = 0;
        } else {
            mDepth = mParent.getDepth() + 1;
        }

        if (oldDepth != mDepth) {
            for (TreeNode<T> child : mChildren) {
                child.updateDepthRecursive();
            }
        }
    }
}
