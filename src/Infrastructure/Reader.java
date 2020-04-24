/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Infrastructure;

import Domain.Node;
import Infrastructure.Utils.BinaryIn;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class Reader {

    /**
     *
     * @param file
     * @return
     */
    public byte[] getBytes(File file){
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param path
     * @return
     */
    public StringBuilder getFileContent(String path){
        StringBuilder sb = new StringBuilder();
        try{
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                // Print the content on the console
                sb.append(scanner.nextLine());
                sb.append("\n");
            }
            scanner.close();
        } catch (IOException ex){

        }
        return sb;
    }

    public Node readTrieFromFile(String fileName){
        Node root;
        Node rightNode;
        Node leftNode;
        boolean isLeaf;
        byte byteRepresentado;
        BinaryIn bIn = new BinaryIn(fileName);
        bIn.readBoolean();
        int frecuencia = bIn.readInt();

        //root
        root = new Node((byte) '\0', frecuencia, null, null);

        // left child
        isLeaf = bIn.readBoolean();
        frecuencia = bIn.readInt();

        if (isLeaf){
            byteRepresentado = bIn.readByte();
            root.setLeftNode(new Node(byteRepresentado, frecuencia, null, null));
        } else {
            leftNode = new Node((byte) '\0', frecuencia, null, null);
            root.setLeftNode(readTrieFromFilePrivate(leftNode, bIn));
        }

        isLeaf = bIn.readBoolean();
        frecuencia = bIn.readInt();
        if (isLeaf){
            byteRepresentado = bIn.readByte();
            root.setRightNode(new Node(byteRepresentado, frecuencia, null, null));
        } else {
            rightNode = new Node((byte) '\0', frecuencia, null, null);
            root.setRightNode(readTrieFromFilePrivate(rightNode, bIn));
        }

        return root;
    }

    private Node readTrieFromFilePrivate(Node node, BinaryIn bIn){
        Node rightNode, leftNode;
        boolean isLeaf;
        byte byteRepresentado;
        int frecuencia;

        if (bIn.isEmpty()){
            return node;
        }

        isLeaf = bIn.readBoolean();
        frecuencia = bIn.readInt();

        // is leaf
        if (isLeaf){
            byteRepresentado = bIn.readByte();;
            leftNode = new Node(byteRepresentado, frecuencia, null, null);
            node.setLeftNode(leftNode);
            if (bIn.isEmpty()){
                return leftNode;
            }
        } else {
            leftNode = new Node((byte) '\0', frecuencia, null, null);
            node.setLeftNode(readTrieFromFilePrivate(leftNode, bIn));
        }

        isLeaf = bIn.readBoolean();
        frecuencia = bIn.readInt();

        // is leaf
        if (isLeaf){
            byteRepresentado = bIn.readByte();;
            rightNode = new Node(byteRepresentado, frecuencia, null, null);
            node.setRightNode(rightNode);
            if (bIn.isEmpty()){
                return rightNode;
            }
        } else {
            rightNode = new Node((byte) '\0', frecuencia, null, null);
            node.setRightNode(readTrieFromFilePrivate(rightNode, bIn));
        }

        return node;
    }
}
