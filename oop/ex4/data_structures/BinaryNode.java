package oop.ex4.data_structures;

/**
 * this class represents a node in a directional binary tree - every node has 2 possible sons and one possible father,
 * plus data member.
 *
 * @author lioraryepaz, tal.gliksman
 */

class BinaryNode {

    /**
     * right son semi-boolean value (root, right, left)
     */
    static final int RIGHT_SON = -1;

    /**
     * left son semi-boolean value (root, right, left)
     */
    static final int LEFT_SON = 1;

    /**
     * root semi-boolean value (root, right, left)
     */
    static final int ROOT = 0;

    private BinaryNode father;

    private BinaryNode leftSon;

    private BinaryNode rightSon;

    private int data;

    /**
     * node's height is defined as the length of the longest downward path from the root to any of the leaves.
     */
    private int height;

    /**
     * Constructor
     *
     * @param fatherInput father BinaryNode
     * @param dataInput   required data
     */
    BinaryNode(BinaryNode fatherInput, int dataInput) {
        data = dataInput;
        father = fatherInput;
        sonUpdate();
        height = 0;
    }

    /**
     * while defining new father, it will automatically update the father about his new son
     */
    private void sonUpdate() {
        if (father != null) {
            if (data < father.getData()) {
                father.setLeftSon(this);
            } else {
                father.setRightSon(this);
            }
        }
    }

    /**
     * @return the node's data - integer.
     */
    public int getData() {
        return data;
    }

    /**
     * @param leftSon BinaryNode to be set as left son
     */
    void setLeftSon(BinaryNode leftSon) {
        this.leftSon = leftSon;
    }

    /**
     * @param rightSon BinaryNode to be set as right son
     */
    void setRightSon(BinaryNode rightSon) {
        this.rightSon = rightSon;
    }

    /**
     * @param father BinaryNode to be set as father
     */
    void setFather(BinaryNode father) {
        this.father = father;
        sonUpdate();
    }

    /**
     * @return father node, null if there isn't one
     */
    BinaryNode getFather() {
        return father;
    }

    /**
     * @return right child node, null if there isn't one
     */
    BinaryNode getRightSon() {
        return rightSon;
    }

    /**
     * @return left child node, null if there isn't one
     */
    BinaryNode getLeftSon() {
        return leftSon;
    }

    /**
     * node's height is defined as the length of the longest downward path from the root to any of the leaves.
     *
     * @return height
     */
    int getHeight() {
        return height;
    }

    /**
     * change the height of the node to a given value
     *
     * @param height non-negative integer - new node's height
     */
    void setHeight(int height) {
        this.height = height;
    }

    /**
     * sets node's data
     *
     * @param data node's new data
     */
    void setData(int data) {
        this.data = data;
    }

    /**
     * finds if node is a left son of its father, a right son of his father or a root
     *
     * @return 0 if root, 1 if left son, -1 if right son.
     */
    int whichSon() {
        if (father == null) {
            return ROOT;
        } else if (father.leftSon == this) {
            return LEFT_SON;
        } else {
            return RIGHT_SON;
        }
    }
}
