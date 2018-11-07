package oop.ex4.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * this class implements a data structure of type binary search tree set, in which the nodes of the lest are always
 * smaller, and the nodes on  the right are always bigger.
 *
 * @author lioraryepaz, tal.gliksman
 */

public class BinaryTree implements Iterable<Integer> {

    private static int NOT_EXIST = -1;

    /**
     * tree's root
     */
    BinaryNode root;

    /**
     * the last node that was added to the tree
     */
    BinaryNode lastAddedBinaryNode;

    /**
     * the last node that was deleted from the tree
     */
    BinaryNode lastDeletedBinaryNode;

    /**
     * tree's size
     */
    private int size;


    /**
     * The default constructor.
     */
    public BinaryTree() {
    }

    /**
     * A constructor that builds a new binary search tree containing all unique values in the input array.
     *
     * @param data the values to add to tree.
     */
    public BinaryTree(int[] data) {
        if (data == null) {
            return;
        }
        for (int input : data) {
            add(input);
        }
    }

    /**
     * A constructor that builds a new binary tree containing all unique values in the input array.
     *
     * @param tree The binary tree to be copied
     */
    public BinaryTree(BinaryTree tree) {
        if (tree == null){
            return;
        }
        for (Integer integer : tree) {
            add(integer);
        }
    }

    /**
     * @return number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Add a new node with the given key to the tree.
     *
     * @param newValue the value of the new node to add.
     * @return true if the value to add is not already in the tree and it was successfully added, false otherwise.
     */
    public boolean add(int newValue) {
        if (contains(newValue) != NOT_EXIST) {
            return false;
        } else if (root == null) {
            root = new BinaryNode(null, newValue);
            lastAddedBinaryNode = root;
        } else {
            lastAddedBinaryNode = addHelper(root, newValue);
        }
        size++;
        return true;
    }

    /**
     * Does tree contain a given input value.
     *
     * @param searchVal value to search for
     * @return if val is found in the tree, return the depth of its node (where 0 is the root). Otherwise -- return -1.
     */
    public int contains(int searchVal) {
        BinaryNode result = findElement(root, searchVal);
        if (result == null) {
            return NOT_EXIST;
        } else {
            int depth = 0;
            while (result.getFather() != null) {
                result = result.getFather();
                depth++;
            }
            return depth;
        }
    }

    /**
     * Returns an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an ascending order,
     * and does NOT implement the remove() method.
     *
     * @return an iterator for the Binary Tree.
     */
    public java.util.Iterator<Integer> iterator() {

        return new Iterator<Integer>() {

            BinaryNode current = null;

            int iterationIndex;

            @Override
            public boolean hasNext() {
                return (iterationIndex < size);
            }

            @Override
            public Integer next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                if (current == null) {
                    current = getSmallest(root);
                } else {
                    current = successor(current);
                }
                iterationIndex++;
                return current.getData();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

    }

    /**
     * Removes the node with the given value from the tree, if it exists.
     *
     * @param toDelete the value to remove from the tree
     * @return true if the given value was found and deleted, false otherwise
     */
    public boolean delete(int toDelete) {
        if (contains(toDelete) == NOT_EXIST) {
            return false;
        }
        BinaryNode delete = findElement(root, toDelete);
        BinaryNode deleteRightSon = delete.getRightSon();
        BinaryNode deleteLeftSon = delete.getLeftSon();
        BinaryNode father = delete.getFather();
        if ((deleteRightSon == null) && (deleteLeftSon == null)) {
            deleteLeaf(delete);
            lastDeletedBinaryNode = delete;
        } else if ((deleteRightSon == null) || (deleteLeftSon == null)) {
            deleteWithOneSon(deleteRightSon, deleteLeftSon, father);
            lastDeletedBinaryNode = delete;
        } else {
            int successorData = successor(delete).getData();
            delete(successorData);
            delete.setData(successorData);
        }
        return true;
    }

    /**
     * deals with deleting nodes with one son only.
     *
     * @param deleteRightSon right son of deleted node
     * @param deleteLeftSon  left son of deleted node
     * @param father         father of deleted node
     */
    private void deleteWithOneSon(BinaryNode deleteRightSon, BinaryNode deleteLeftSon, BinaryNode father) {
        BinaryNode newChild;
        if (deleteRightSon == null) {
            newChild = deleteLeftSon;
        } else {
            newChild = deleteRightSon;
        }
        if (father == null) {
            root = newChild;
        }
        newChild.setFather(father);
        size--;
    }

    /**
     * delete a leaf
     */
    private void deleteLeaf(BinaryNode leaf) {
        int whichSon = leaf.whichSon();
        if (whichSon == BinaryNode.ROOT) {
            root = null;
        } else if (whichSon == BinaryNode.RIGHT_SON) {
            leaf.getFather().setRightSon(null);
        } else {
            leaf.getFather().setLeftSon(null);
        }
        size--;
        if (root == leaf) {
            root = null;
        }
    }

    /**
     * find the smallest node in a given sub-tree
     *
     * @param currBinaryNode subTree root
     * @return the smallest node
     */
    private BinaryNode getSmallest(BinaryNode currBinaryNode) {
        while (currBinaryNode.getLeftSon() != null) {
            currBinaryNode = currBinaryNode.getLeftSon();
        }
        return currBinaryNode;
    }

    /**
     * add helper function
     *
     * @param subTreeRoot current subtree to add to
     * @param newValue    the value of the new node to add.
     * @return the node that was added
     */
    private BinaryNode addHelper(BinaryNode subTreeRoot, int newValue) {
        int subTreeRootData = subTreeRoot.getData();
        if (subTreeRootData > newValue) {
            BinaryNode nextBinaryNode = subTreeRoot.getLeftSon();
            return addCurr(subTreeRoot, newValue, nextBinaryNode);
        } else {
            BinaryNode nextBinaryNode = subTreeRoot.getRightSon();
            return addCurr(subTreeRoot, newValue, nextBinaryNode);
        }
    }

    /**
     * part of the add process - to prevent code repetition
     *
     * @param subTreeRoot current subtree to add to
     * @param newValue    the value of the new node to add.
     * @param nextBinaryNode    subTreeRoot relevant son
     * @return the node that was added
     */
    private BinaryNode addCurr(BinaryNode subTreeRoot, int newValue, BinaryNode nextBinaryNode) {
        if (nextBinaryNode == null) {
            return new BinaryNode(subTreeRoot, newValue);
        } else {
            return addHelper(nextBinaryNode, newValue);
        }
    }

    /**
     * find a node with the given data
     *
     * @param root      sub-tree to search in
     * @param searchVal value to search for
     * @return if exist, return the node containing the data. otherwise, return null.
     */
    private BinaryNode findElement(BinaryNode root, int searchVal) {
        if (root == null) {
            return null;
        }
        int rootData = root.getData();
        if (rootData == searchVal) {
            return root;
        } else if (rootData > searchVal) {
            return findElement(root.getLeftSon(), searchVal);
        } else {
            return findElement(root.getRightSon(), searchVal);
        }
    }

    /**
     * Finds the smallest from the bigger integers at the tree
     *
     * @param subTreeRoot a node to find successor to
     * @return the successor node
     */
    private BinaryNode successor(BinaryNode subTreeRoot) {
        if (subTreeRoot.getRightSon() != null) {
            return getSmallest(subTreeRoot.getRightSon());
        } else {
            BinaryNode father = subTreeRoot.getFather();
            if (subTreeRoot.whichSon() == BinaryNode.LEFT_SON) {
                return father;
            } else {
                BinaryNode currBinaryNode = subTreeRoot;
                while (currBinaryNode.whichSon() == BinaryNode.RIGHT_SON) {
                    currBinaryNode = currBinaryNode.getFather();
                }
                return currBinaryNode.getFather();
            }
        }
    }

}

