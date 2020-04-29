/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Domain;

/**
 * Clase que contiene la información de un Nodo del árbol Huffman.
 */
public class Node implements Comparable<Node>{
    public  int frecuencia;
    public  byte byteRepresentado;
    // Nodos hijos
    public  Node rightNode, leftNode;

    public Node(byte byteRepresentado, int frecuencia, Node leftNode, Node rightNode){
        this.byteRepresentado = byteRepresentado;
        this.frecuencia = frecuencia;
        this.rightNode = rightNode;
        this.leftNode = leftNode;
    }

    /**
     * Método que retorna si el nodo es hoja o no (si no tiene hijos es nodo hoja)
     * @return
     */
    public boolean isLeaf(){
        assert  ((leftNode == null) && (rightNode == null)) || ((leftNode != null) && (rightNode != null));
        return (leftNode == null) && (rightNode == null);
    }

    /**
     * Método que sirve para comparar dos nodos por su frecuencia
     * @param node
     * @return
     */
    @Override
    public int compareTo(Node node) {
        return this.frecuencia - node.frecuencia;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

}

