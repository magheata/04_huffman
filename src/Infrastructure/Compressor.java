/* Created by andreea on 12/04/2020 */
package Infrastructure;

import Application.Controller;
import Domain.Interficies.ICompressor;
import Domain.Node;
import Infrastructure.Utils.BinaryOut;
import Utils.Constantes;


import java.io.File;
import java.util.*;

public class Compressor implements ICompressor {

    private Thread worker;
    private Map<Byte, String> huffmanCode = new HashMap<>();
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
    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    // traverse the Huffman Tree and store Huffman Codes
    // in a map.
    public void encode(Node root, String str, Map<Byte, String> huffmanCode) {
        if (root == null) return;

        // found a leaf node
        if (root.leftNode == null && root.rightNode == null) {
            huffmanCode.put(root.byteRepresentado, str);
        }

        encode(root.leftNode, str + "0", huffmanCode);
        encode(root.rightNode, str + "1", huffmanCode);
    }

    // traverse the Huffman Tree and decode the encoded string
    public int decode(Node root, int index, StringBuilder sb) {
        if (root == null)
            return index;

        // found a leaf node
        if (root.leftNode == null && root.rightNode == null)
        {
            System.out.print(root.byteRepresentado);
            return index;
        }

        index++;

        if (sb.charAt(index) == '0')
            index = decode(root.leftNode, index, sb);
        else
            index = decode(root.rightNode, index, sb);

        return index;
    }

    // Builds Huffman Tree and huffmanCode and decode given input text
    public void comprimir() {
        // count frequency of appearance of each character
        // and store it in a map
        Map<Byte, Integer> freq = crearTablaFrecuencias(bytes);

        // Create a priority queue to store live nodes of Huffman tree
        // Notice that highest priority item has lowest frequency
        PriorityQueue<Node> pq = crearArbolHuffman(freq);

        // traverse the Huffman tree and store the Huffman codes in a map
        encode(pq.peek(), "", huffmanCode);

        String name = file.getName().split("\\.")[0];

        controller.addFileRoot(pq.peek(), name);

        controller.createBinaryOutputFile(Constantes.OUTPUT_TYPE_TRIE + name,
                Constantes.PATH_HUFFMAN_TRIE + name + Constantes.EXTENSION_HUFFMAN_TRIE);
        controller.createBinaryOutputFile(Constantes.OUTPUT_TYPE_CODES + name,
                Constantes.PATH_HUFFMAN_CODES + name + Constantes.EXTENSION_HUFFMAN_CODES);
        controller.createBinaryOutputFile(Constantes.OUTPUT_TYPE_COMPRESSED_FILE + name,
                Constantes.PATH_COMPRESSED_FILE + name + Constantes.EXTENSION_COMPRESSED_FILE);

        writeTrie(Constantes.OUTPUT_TYPE_TRIE + name, pq.peek());
        writeHuffmanCodes(Constantes.OUTPUT_TYPE_CODES + name, file.getName().split("\\.")[1]);
        writeCompressedFile(Constantes.OUTPUT_TYPE_COMPRESSED_FILE + name, bytes);
        controller.addArchivosPorComprimirAPanel(file, bytesOriginales, bytesComprimidos);
    }

    private Map<Byte, Integer> crearTablaFrecuencias(byte[] bytes){
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

    private void writeTrie(String outputType, Node x){
        writeTriePrivate(outputType, x);
        controller.closeBinaryOutputFile(outputType);
    }

    private void writeTriePrivate(String outputType, Node x) {
        if (x.isLeaf()) {
            controller.write(outputType, true);
            controller.write(outputType, x.byteRepresentado);
            return;
        }
        controller.write(outputType, false);
        writeTriePrivate(outputType, x.leftNode);
        writeTriePrivate(outputType, x.rightNode);
    }

    private void writeHuffmanCodes(String outputType, String extension){
        controller.write(outputType, "Extension archivo original: " + extension + "\n\n");
        StringBuilder outputString;
        if (extension.equals("txt")){
            // print the Huffman codes
            controller.write(outputType, "BYTE |    CHARACTER     |     HUFFMAN CODE\n");
            controller.write(outputType, "------------------------------------------\n");
            for (Map.Entry<Byte, String> entry : huffmanCode.entrySet()) {
                outputString = new StringBuilder();
                outputString.append(entry.getKey() + "           ");
                if (entry.getKey() == 10){
                    outputString.append("BLANK SPACE           ");
                } else {
                    outputString.append((char) (entry.getKey() & 0xFF) + "                     ");
                }
                outputString.append(entry.getValue() + "\n");
                controller.write(outputType, outputString.toString());
            }
        } else {
            // print the Huffman codes
            controller.write(outputType, "BYTE |     HUFFMAN CODE\n");
            controller.write(outputType, "-----------------------\n");
            for (Map.Entry<Byte, String> entry : huffmanCode.entrySet()) {
                outputString = new StringBuilder();
                outputString.append(entry.getKey() + "           ");
                outputString.append(entry.getValue() + "\n");
                controller.write(outputType, outputString.toString());
            }
        }
        controller.closeBinaryOutputFile(outputType);
    }

    private void writeCompressedFile(String outputType, byte [] bytes){
        StringBuilder bytesCompr = new StringBuilder();
        // print encoded string
        for (int i = 0 ; i < bytes.length; i++) {
            String code = huffmanCode.get(bytes[i]);
            for (char byteChar : code.toCharArray()){
                if (byteChar == '0'){
                    controller.write(outputType, false);
                } else {
                    controller.write(outputType, true);
                }
            }
            bytesCompr.append(huffmanCode.get(bytes[i]));
        }
        bytesComprimidos = bytesCompr.length();
        controller.closeBinaryOutputFile(outputType);
    }

    @Override
    public void run() {
        comprimir();
    }
}
