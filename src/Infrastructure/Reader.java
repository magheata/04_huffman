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
        Node root = new Node();
        Node rightChild;
        Node leftChild;
        Node leaf;
        BinaryIn bIn = new BinaryIn(fileName);
        byte byteRepresentado;
        int frecuencia;
        while(!bIn.isEmpty()){
            rightChild = new Node();
            leftChild = new Node();
            byteRepresentado = bIn.readByte();
            frecuencia = bIn.readInt();
            //leaf
            if (bIn.readBoolean()){
                leaf = new Node(byteRepresentado, frecuencia, null, null);
            }

            if (!bIn.isEmpty()){
                byteRepresentado = bIn.readByte();
                frecuencia = bIn.readInt();

                //leftChild = new Node(byteRepresentado, frecuencia)
            }
        }
        return null;
    }

    private Node readTrieFromFilePrivate(Node node){
        return null;
    }

}
