/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Infrastructure;

import Application.Controller;
import Domain.Node;
import Utils.Constantes;


import java.io.File;
import java.util.*;

/**
 * Clase encargada de realizar las operaciones de compresión.
 */
public class Compressor{

    /**
     * Colección de los códigos Huffman con los bytes que representan
     */
    private Map<Byte, String> huffmanCode = new HashMap<>();
    /**
     * Colección de los bytes del fichero con la frecuencia de cada uno
     */
    private Map<Byte, Integer> freq;
    private byte[] bytes;
    private File file;
    private Controller controller;
    private int bytesOriginales;
    private int bytesComprimidos;

    public Compressor(Controller controller, byte[] bytes, File file){
        this.controller = controller;
        this.bytes = bytes;
        this.file = file;
    }
    /**
     *
     */
    // Builds Huffman Tree and huffmanCode and decode given input text
    public void comprimir() {
        // count frequency of appearance of each character
        // and store it in a map
        freq = crearTablaFrecuencias();

        // Create a priority queue to store live nodes of Huffman tree
        // Notice that highest priority item has lowest frequency
        PriorityQueue<Node> pq = crearArbolHuffman(freq);

        // traverse the Huffman tree and store the Huffman codes in a map
        encode(pq.peek(), "", huffmanCode);

        String name = file.getName().split("\\.")[0];

        // Añadimos la raíz a la lista
        controller.addFileRoot(pq.peek(), name);

        // Creamos el fichero que contendrá el árbol Huffman
        controller.createBinaryOutputFile(Constantes.OUTPUT_TYPE_TRIE + name,
                Constantes.PATH_HUFFMAN_TRIE + name + Constantes.EXTENSION_HUFFMAN_TRIE);

        // Creamos el fichero que contendrá la información de la compresión
        controller.createBinaryOutputFile(Constantes.OUTPUT_TYPE_CODES + name,
                Constantes.PATH_HUFFMAN_CODES + name + Constantes.EXTENSION_HUFFMAN_CODES);

        // Creamos el fichero donde se volcarán los bytes comprimidos
        controller.createOutputFile(Constantes.OUTPUT_TYPE_COMPRESSED_FILE + name,
                Constantes.PATH_COMPRESSED_FILE + name + Constantes.EXTENSION_COMPRESSED_FILE);

        // Escribimos el árbol
        writeTrie(Constantes.OUTPUT_TYPE_TRIE + name, pq.peek());

        // Escribimos el fichero comprimido
        writeCompressedFile(Constantes.OUTPUT_TYPE_COMPRESSED_FILE + name);

        // Escribimos el fichero con la información de la compresión
        writeHuffmanCodes(Constantes.OUTPUT_TYPE_CODES + name, file.getName().split("\\.")[1]);

        // Añadimos el archivo a la lista de archivos comprimidos
        controller.addArchivosPorComprimirAPanel(file, bytesOriginales, bytesComprimidos);
    }

    /**
     *
     * @param root
     * @param str
     * @param huffmanCode
     */
    // traverse the Huffman Tree and store Huffman Codes
    // in a map.
    private void encode(Node root, String str, Map<Byte, String> huffmanCode) {
        if (root == null) return;
        // found a leaf node
        if (root.leftNode == null && root.rightNode == null) {
            huffmanCode.put(root.byteRepresentado, str);
        }
        encode(root.leftNode, str + "0", huffmanCode);
        encode(root.rightNode, str + "1", huffmanCode);
    }

    /**
     * Método que sirve para calcular las frecuencias de los bytes en el fichero
     * @return mapa que contiene los bytes y sus respectivas frecuencias
     */
    private Map<Byte, Integer> crearTablaFrecuencias(){
        StringBuilder bytesOrig = new StringBuilder();
        // count frequency of appearance of each character
        // and store it in a map
        Map<Byte, Integer> freq = new HashMap<>();
        for (int i = 0 ; i < bytes.length; i++) {
            if (!freq.containsKey(bytes[i])) {
                freq.put(bytes[i], 0);
            }
            bytesOrig.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
            freq.put(bytes[i], freq.get(bytes[i]) + 1);
        }
        bytesOriginales = bytesOrig.length();
        return freq;
    }

    /**
     * Método que crea el árbol Huffman
     * @param freq mapa de frecuencia
     * @return árbol Huffman
     */
    private PriorityQueue<Node> crearArbolHuffman(Map<Byte, Integer> freq){
        // Create a priority queue to store live nodes of Huffman tree
        // Notice that highest priority item has lowest frequency
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.frecuencia));

        // Create a leaf node for each character and add it
        // to the priority queue.
        for (Map.Entry<Byte, Integer> entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue(), null, null));
        }

        // do till there is more than one node in the queue
        while (pq.size() != 1)
        {
            // Remove the two nodes of highest priority
            // (lowest frequency) from the queue
            Node left = pq.poll();
            Node right = pq.poll();

            // Create a new internal node with these two nodes as children
            // and with frequency equal to the sum of the two nodes
            // frequencies. Add the new node to the priority queue.
            int sum = left.frecuencia + right.frecuencia;
            pq.add(new Node((byte) '\0', sum, left, right));
        }
        return pq;
    }

    /**
     * Método que escribe el árbol Huffman en el fichero
     * @param outputType nombre del BinaryOut que se encarga de escribir el fichero
     * @param x nodo raíz del árbol
     */
    private void writeTrie(String outputType, Node x){
        writeTriePrivate(outputType, x);
        controller.closeBinaryOutputFile(outputType);
    }

    /**
     * Método recursivo que escribe el árbol Huffman en el fichero
     * @param outputType nombre del BinaryOut que se encarga de escribir el fichero
     * @param x nodo raíz del árbol
     */
    private void writeTriePrivate(String outputType, Node x) {
        if (x.isLeaf()) {
            controller.write(outputType, true);
            controller.write(outputType, x.frecuencia);
            controller.write(outputType, x.byteRepresentado);
            return;
        }
        controller.write(outputType, false);
        controller.write(outputType, x.frecuencia);
        writeTriePrivate(outputType, x.leftNode);
        writeTriePrivate(outputType, x.rightNode);
    }

    /**
     * Método que escribe la información de la compresión en un fichero
     * @param outputType nombre del BinaryOut que se encarga de escribir el fichero
     * @param extension extensión del archivo original
     */
    private void writeHuffmanCodes(String outputType, String extension){
        controller.write(outputType, "Extension archivo original: " + extension + "\n");
        controller.write(outputType, "Total bits archivo original: " + bytesOriginales + "\n");
        controller.write(outputType, "Total bits archivo comprimido: " + bytesComprimidos + "\n");
        controller.write(outputType, "Ruta archivo; " + file.getAbsolutePath() + "\n\n");

        StringBuilder outputString;

        // Si es formato txt añadimos los carácteres que representan cada byte
        if (extension.equals("txt")){
            // print the Huffman codes
            controller.write(outputType, "BYTE |    CHARACTER     |     HUFFMAN CODE      |    FREQUENCY     |\n");
            controller.write(outputType, "--------------------------------------------------------------------\n");
            for (Map.Entry<Byte, String> entry : huffmanCode.entrySet()) {
                outputString = new StringBuilder();
                outputString.append(entry.getKey() + "           ");
                if (entry.getKey() == 10){
                    outputString.append("BLANK SPACE           ");
                } else {
                    outputString.append((char) (entry.getKey() & 0xFF) + "                     ");
                }
                outputString.append(entry.getValue() + "                     ");
                outputString.append(freq.get(entry.getKey())+ "\n");

                controller.write(outputType, outputString.toString());
            }
            // Si no sólo escribimos el código Huffman y su frecuencia
        } else {
            // print the Huffman codes
            controller.write(outputType, "BYTE |     HUFFMAN CODE      |    FREQUENCY     |\n");
            controller.write(outputType, "-------------------------------------------------\n");
            for (Map.Entry<Byte, String> entry : huffmanCode.entrySet()) {
                outputString = new StringBuilder();
                outputString.append(entry.getKey() + "           ");
                outputString.append(entry.getValue() + "                     ");
                outputString.append(freq.get(entry.getKey())+ "\n");
                controller.write(outputType, outputString.toString());
            }
        }
        // cerrmos el fichero
        controller.closeBinaryOutputFile(outputType);
    }

    /**
     * Método que sirve para escribir el fichero comprimido resultante
     * @param outputType nombre del FileOutputStream que se encarga de escribir el fichero
     */
    private void writeCompressedFile(String outputType){
        StringBuilder bytesCompr = new StringBuilder();
        // Para cada byte se encuentra su código Huffman
        for (int i = 0 ; i < bytes.length; i++) {
            // Se escribe el código Huffman en el fichero
            String code = huffmanCode.get(bytes[i]);
            controller.write(outputType, code.getBytes());
            bytesCompr.append(huffmanCode.get(bytes[i]));
        }
        bytesComprimidos = bytesCompr.length();
        // Se cierra el fichero
        controller.closeOutputFile(outputType);
    }

}
