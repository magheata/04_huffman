/* Created by andreea on 13/04/2020 */
package Domain;

public class Node implements Comparable<Node>{
    public final int frecuencia;
    public final byte byteRepresentado;
    public final Node rightNode, leftNode;


    public Node(byte byteRepresentado, int frecuencia, Node leftNode, Node rightNode){
        this.byteRepresentado = byteRepresentado;
        this.frecuencia = frecuencia;
        this.rightNode = rightNode;
        this.leftNode = leftNode;
    }

    public boolean isLeaf(){
        assert  ((leftNode == null) && (rightNode == null)) || ((leftNode != null) && (rightNode != null));
        return (leftNode == null) && (rightNode == null);
    }

    @Override
    public int compareTo(Node node) {
        return this.frecuencia - node.frecuencia;
    }
}
