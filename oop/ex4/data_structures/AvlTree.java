package oop.ex4.data_structures;

import java.lang.Math;

/**
 * This class represents an AVL type of binary tree, with the unique quality of keeping the tree relatively balanced, to
 * keep the running time around O(log(n)).
 *
 * @author lioraryepaz, tal.gliksman
 */

public class AvlTree extends BinaryTree {

    /**
     * The default constructor.
     */
    public AvlTree() {
        super();
    }

    /**
     * A constructor that builds the tree by adding the elements in the input array one-by-one If the same values
     * appears twice (or more) in the list, it is ignored.
     *
     * @param data the values to add to tree.
     */
    public AvlTree(int[] data) {
        super(data);
    }

    /**
     * A copy-constructor that builds the tree from existing tree.
     *
     * @param tree The binary tree to be copied
     */
    public AvlTree(AvlTree tree) {
        super(tree);
    }

    /**
     * Add a new node with key newValue into the tree.
     *
     * @param newValue new value to add to the tree.
     * @return false iff newValue already exist in the tree
     */
    @Override
    public boolean add(int newValue) {
        if (super.add(newValue)) {
            addingAdjustHeight(this.lastAddedBinaryNode);
            return true;
        }
        return false;
    }

    /**
     * Remove a node from the tree, if it exists.
     *
     * @param toDelete value to delete
     * @return true iff toDelete found and deleted
     */
    @Override
    public boolean delete(int toDelete) {
        if (super.delete(toDelete)) {
            deleteAdjustHeight(this.lastDeletedBinaryNode);
            return true;
        }
        return false;
    }

    /**
     * fix the height post a binaryNode deletion
     *
     * @param binaryNode last deleted binaryNode
     */
    private void deleteAdjustHeight(BinaryNode binaryNode) {
        BinaryNode father = binaryNode.getFather();
        while (father != null) {
            checkAvl(father);
            fixHeight(father);
            father = father.getFather();
        }
    }

    /**
     * fix the height post a binaryNode addition
     *
     * @param binaryNode last added binaryNode
     */
    private void addingAdjustHeight(BinaryNode binaryNode) {
        if ((binaryNode.getFather() == null) || (binaryNode.getFather().getHeight() == 1)) {
            return;
        }
        BinaryNode father = binaryNode.getFather();
        while (father != root) {
            fixHeight(father);
            BinaryNode newFather = father.getFather();
            if (checkAvl(newFather)) {
                return;
            }
            father = newFather;
        }
        fixHeight(root);
    }

    /**
     * check's if give binaryNode's subtree stands in criteria of Avl, and fix accordingly if needed
     *
     * @param binaryNode root of subtree to check
     * @return true if a fix was made, false otherwise
     */
    private boolean checkAvl(BinaryNode binaryNode) {
        int balanceFactor = getBalanceFactor(binaryNode);
        if (balanceFactor == 2) {
            if (getBalanceFactor(binaryNode.getLeftSon()) == -1) {
                rotateLeft(binaryNode.getLeftSon());
                rotateRight(binaryNode);
            } else {
                rotateRight(binaryNode);
            }
            return true;
        } else if (balanceFactor == -2) {
            if (getBalanceFactor(binaryNode.getRightSon()) == 1) {
                rotateRight(binaryNode.getRightSon());
                rotateLeft(binaryNode);
            } else {
                rotateLeft(binaryNode);
            }
            return true;
        }
        return false;
    }

    /**
     * return the AVL balance factor
     *
     * @param binaryNode a given avl subtree root to check
     * @return the error rate
     */
    private int getBalanceFactor(BinaryNode binaryNode) {
        int rightHeight = -1;
        int leftHeight = -1;
        BinaryNode leftSon = binaryNode.getLeftSon();
        BinaryNode rightSon = binaryNode.getRightSon();
        if (rightSon != null) {
            rightHeight = rightSon.getHeight();
        }
        if (leftSon != null) {
            leftHeight = leftSon.getHeight();
        }
        return leftHeight - rightHeight;
    }

    /**
     * performs an AVL right rotation
     *
     * @param subRoot a subRoot to rotate
     */
    private void rotateRight(BinaryNode subRoot) {
        BinaryNode newSubRoot = subRoot.getLeftSon();
        BinaryNode newRootRightSon = newSubRoot.getRightSon();
        subRoot.setLeftSon(null);
        rotateHelper(subRoot, newSubRoot, newRootRightSon);
    }

    /**
     * performs an AVL left rotation
     *
     * @param subRoot a subRoot to rotate
     */
    private void rotateLeft(BinaryNode subRoot) {
        BinaryNode newSubRoot = subRoot.getRightSon();
        BinaryNode newRootLeftSon = newSubRoot.getLeftSon();
        subRoot.setRightSon(null);
        rotateHelper(subRoot, newSubRoot, newRootLeftSon);
    }

    /**
     * rotation process helper
     *
     * @param subRoot    subRoot to rotate
     * @param newSubRoot new subRoot post rotation
     * @param newRootSon the son of the new root
     */
    private void rotateHelper(BinaryNode subRoot, BinaryNode newSubRoot, BinaryNode newRootSon) {
        BinaryNode rootFather = subRoot.getFather();
        if (newRootSon != null) {
            newRootSon.setFather(subRoot);
        }
        newSubRoot.setFather(rootFather);
        subRoot.setFather(newSubRoot);
        if (subRoot == this.root) {
            this.root = newSubRoot;
        }
        fixHeight(subRoot);
        fixHeight(newSubRoot);
    }

    /**
     * A method that calculates the minimum numbers of nodes in an AVL tree of height h,
     *
     * @param h height of the tree (a non-negative number).
     * @return minimum number of nodes of height h
     */
    public static int findMinNodes(int h) {
        return findFibonacci(h + 3) - 1;
    }

    /**
     * A method that calculates the maximum number of nodes in an AVL tree of height h,
     *
     * @param h height of the tree (a non-negative number).
     * @return maximum number of nodes of height h
     */
    public static int findMaxNodes(int h) {
        return (int) (Math.pow(2, h + 1) - 1);
    }

    /**
     * finds the K fibonacci number
     *
     * @param k desired index of fibonacci number
     * @return the desired fibonacci number
     */
    private static int findFibonacci(int k) {
        int a1 = 0;
        int a2 = 1;
        for (int i = 1; i < k - 1; i++) {
            int temp = a1;
            a1 = a2;
            a2 += temp;
        }
        return a1 + a2;
    }

    /**
     * fix the height of specific binaryNode
     *
     * @param binaryNode a binaryNode which need height fix
     */
    private void fixHeight(BinaryNode binaryNode) {
        int rightHeight = -1;
        int leftHeight = -1;
        BinaryNode leftSon = binaryNode.getLeftSon();
        BinaryNode rightSon = binaryNode.getRightSon();
        if (rightSon != null) {
            rightHeight = rightSon.getHeight();
        }
        if (leftSon != null) {
            leftHeight = leftSon.getHeight();
        }
        binaryNode.setHeight(Math.max(rightHeight, leftHeight) + 1);
    }

}
