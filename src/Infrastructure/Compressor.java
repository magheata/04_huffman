/* Created by andreea on 12/04/2020 */
package Infrastructure;

import Application.Controller;
import Domain.Interficies.ICompressor;
import Domain.Node;
import Infrastructure.Utils.BinaryOut;
import Presentation.HuffmanTrie;
import Utils.Constantes;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;

import java.io.File;
import java.util.*;

public class Compressor implements ICompressor {

    private Thread worker;
    private Map<Byte, String> huffmanCode = new HashMap<>();

    private BinaryOut binaryOutTrie, binaryOutCodes, binaryOutCompressedFile;
    private byte[] bytes;
    private File file;
    private Controller controller;
    private int bytesOriginales;
    private int bytesComprimidos;
    private TreeNode<Node> trie;


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

        binaryOutTrie = new BinaryOut(Constantes.PATH_HUFFMAN_TRIE + name + Constantes.EXTENSION_HUFFMAN_TRIE);
        binaryOutCodes = new BinaryOut(Constantes.PATH_HUFFMAN_CODES + name + Constantes.EXTENSION_HUFFMAN_CODES);
        binaryOutCompressedFile = new BinaryOut(Constantes.PATH_COMPRESSED_FILE + name + Constantes.EXTENSION_COMPRESSED_FILE);

        writeTrie(pq.peek());
        writeHuffmanCodes(file.getName().split("\\.")[1]);
        writeCompressedFile(bytes);
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

    private TreeNode<Node> createTreeRecursive(Node root){
        if ((root.leftNode == null) || (root.rightNode == null)){
            return new ArrayMultiTreeNode<>(root);
        }
        TreeNode<Node> newNode = new ArrayMultiTreeNode<>(root);
        TreeNode<Node> newNodeLeft = createTreeRecursive(root.leftNode);
        TreeNode<Node> newNodeRight = createTreeRecursive(root.rightNode);
        newNode.add(newNodeLeft);
        newNode.add(newNodeRight);
        return newNode;
    }

    private void writeTrie(Node x){
        writeTrie(x, binaryOutTrie);
        binaryOutTrie.flush();
        binaryOutTrie.close();
    }

    private void writeTrie(Node x, BinaryOut binaryOut) {
        if (x.isLeaf()) {
            binaryOut.write(true);
            binaryOut.write(x.byteRepresentado);
            return;
        }
        binaryOut.write(false);
        writeTrie(x.leftNode, binaryOut);
        writeTrie(x.rightNode, binaryOut);
    }

    private void writeHuffmanCodes(String extension){
        binaryOutCodes.write("Extension archivo original: " + extension + "\n\n");
        StringBuilder outputString;
        if (extension.equals("txt")){
            // print the Huffman codes
            binaryOutCodes.write("BYTE |    CHARACTER     |     HUFFMAN CODE\n");
            binaryOutCodes.write("------------------------------------------\n");
            for (Map.Entry<Byte, String> entry : huffmanCode.entrySet()) {
                outputString = new StringBuilder();
                outputString.append(entry.getKey() + "           ");
                if (entry.getKey() == 10){
                    outputString.append("BLANK SPACE           ");
                } else {
                    outputString.append((char) (entry.getKey() & 0xFF) + "                     ");
                }
                outputString.append(entry.getValue() + "\n");
                binaryOutCodes.write(outputString.toString());
            }
        } else {
            // print the Huffman codes
            binaryOutCodes.write("BYTE |     HUFFMAN CODE\n");
            binaryOutCodes.write("-----------------------\n");
            for (Map.Entry<Byte, String> entry : huffmanCode.entrySet()) {
                outputString = new StringBuilder();
                outputString.append(entry.getKey() + "           ");
                outputString.append(entry.getValue() + "\n");
                binaryOutCodes.write(outputString.toString());
            }
        }

        binaryOutCodes.flush();
        binaryOutCodes.close();
    }

    private void writeCompressedFile(byte [] bytes){
        StringBuilder bytesCompr = new StringBuilder();
        // print encoded string
        for (int i = 0 ; i < bytes.length; i++) {
            String code = huffmanCode.get(bytes[i]);
            for (char byteChar : code.toCharArray()){
                if (byteChar == '0'){
                    binaryOutCompressedFile.write(false);
                } else {
                    binaryOutCompressedFile.write(true);
                }
            }
            bytesCompr.append(huffmanCode.get(bytes[i]));
        }
        bytesComprimidos = bytesCompr.length();
        binaryOutCompressedFile.flush();
        binaryOutCompressedFile.close();
    }

    @Override
    public void run() {
        comprimir();
    }
}
