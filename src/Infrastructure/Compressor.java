/* Created by andreea on 12/04/2020 */
package Infrastructure;

import Domain.Interficies.ICompressor;
import Domain.Node;
import Infrastructure.Utils.BinaryOut;

import java.util.*;

public class Compressor implements ICompressor {

    private Map<Byte, String> huffmanCode = new HashMap<>();
    private BinaryOut binaryOutTrie, binaryOutCodes, binaryOutCompressedFile;
    // traverse the Huffman Tree and store Huffman Codes
    // in a map.
    public void encode(Node root, String str, Map<Byte, String> huffmanCode)
    {
        if (root == null) return;

        // found a leaf node
        if (root.leftNode == null && root.rightNode == null) {
            huffmanCode.put(root.byteRepresentado, str);
        }

        encode(root.leftNode, str + "0", huffmanCode);
        encode(root.rightNode, str + "1", huffmanCode);
    }

    // traverse the Huffman Tree and decode the encoded string
    public int decode(Node root, int index, StringBuilder sb)
    {
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
    public void buildHuffmanTree(byte[] bytes, String fileName)
    {
        StringBuilder bytesOriginales = new StringBuilder();

        // count frequency of appearance of each character
        // and store it in a map
        Map<Byte, Integer> freq = new HashMap<>();
        for (int i = 0 ; i < bytes.length; i++) {
            if (!freq.containsKey(bytes[i])) {
                freq.put(bytes[i], 0);
            }
            bytesOriginales.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
            freq.put(bytes[i], freq.get(bytes[i]) + 1);
        }

        System.out.println("Bytes originales (bits): " + bytesOriginales.length());

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

        // root stores pointer to root of Huffman Tree
        Node root = pq.peek();

        // traverse the Huffman tree and store the Huffman codes in a map
        encode(root, "", huffmanCode);

        String name = fileName.split("\\.")[0];

        binaryOutTrie = new BinaryOut("huffmanTrees/" + name + "_trie.txt");
        binaryOutCodes = new BinaryOut("huffmanCodes/" + name + "_codes.txt");
        binaryOutCompressedFile = new BinaryOut("compressed/" + name + "_compressed.txt");

        writeTrie(root);
        writeHuffmanCodes(fileName.split("\\.")[1]);
        writeCompressedFile(bytes);
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
        StringBuilder bytesComprimidos = new StringBuilder();
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
            bytesComprimidos.append(huffmanCode.get(bytes[i]));
        }
        System.out.println("Bytes comprimidos (bits): " + bytesComprimidos.length());
        binaryOutCompressedFile.flush();
        binaryOutCompressedFile.close();
    }
}
