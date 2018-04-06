package Huffman;

public class Node implements Comparable<Node>
{
    Node parent;
    Node right;
    Node left;
    public char chars;
    public int counter;
    public Node(){
        parent = null;
        right = null;
        left = null;
        counter = 0;
    }

    @Override
    public int compareTo(Node node)
    {
        return counter - node.counter;
    }
}