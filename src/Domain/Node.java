/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Domain;

public class Node implements Comparable<Node>{
    public  int frecuencia;
    public  byte byteRepresentado;

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public  Node rightNode, leftNode;


    public Node(byte byteRepresentado, int frecuencia, Node leftNode, Node rightNode){
        this.byteRepresentado = byteRepresentado;
        this.frecuencia = frecuencia;
        this.rightNode = rightNode;
        this.leftNode = leftNode;
    }

    public Node() {
    }

    /**
     *
     * @return
     */
    public boolean isLeaf(){
        assert  ((leftNode == null) && (rightNode == null)) || ((leftNode != null) && (rightNode != null));
        return (leftNode == null) && (rightNode == null);
    }

    /**
     *
     * @param node
     * @return
     */
    @Override
    public int compareTo(Node node) {
        return this.frecuencia - node.frecuencia;
    }


}

