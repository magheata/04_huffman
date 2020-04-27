/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Infrastructure;

import Domain.Node;
import Infrastructure.Utils.BinaryIn;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Reader {

    private ExecutorService executorGetFileContent = Executors.newSingleThreadExecutor();
    private ExecutorService executorGetTrie = Executors.newSingleThreadExecutor();
    private ExecutorService executorGetBytes = Executors.newSingleThreadExecutor();

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
    public Future<StringBuilder> getFileContent(String path){
        return  executorGetFileContent.submit(() -> {
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
        });
    }

    public Future<Node> readTrieFromFile(String fileName){
        return executorGetTrie.submit(() ->{
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
        });
    }

    private Node readTrieFromFilePrivate(Node node, BinaryIn bIn){
        Node rightNode, leftNode;
        boolean isLeaf;
        char byteRepresentado;
        int frecuencia;

        if (bIn.isEmpty()){
            return node;
        }

        isLeaf = bIn.readBoolean();
        frecuencia = bIn.readInt();

        // is leaf
        if (isLeaf){
            byteRepresentado = bIn.readChar();
            leftNode = new Node((byte) byteRepresentado, frecuencia, null, null);
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
            byteRepresentado = bIn.readChar();
            rightNode = new Node((byte) byteRepresentado, frecuencia, null, null);
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

    public Future<Object[]> getOriginalAndCompressedBytes(String path){
        return executorGetBytes.submit(() -> {
            String bytesOriginalesString = null, bytesComprimidosString = null, extension = null;
            try{
                Scanner scanner = new Scanner(new File(path));
                for (int i = 0; i < 3; i++){
                    if (i == 0){
                        extension = scanner.nextLine().split(":")[1];
                    }
                    if (i == 1){
                        bytesOriginalesString = scanner.nextLine().split(":")[1];
                    }
                    if (i == 2){
                        bytesComprimidosString = scanner.nextLine().split(":")[1];
                    }
                }
                scanner.close();
            } catch (IOException ex){

            } finally{

                return new Object[]{extension.trim(), Integer.parseInt(bytesOriginalesString.trim()), Integer.parseInt(bytesComprimidosString.trim())};
            }
        });
    }

    public Future<String> getPathArchivoOriginal(String path){
        return executorGetBytes.submit(() -> {
            String rutaArchivoOriginal = null;
            try{
                Scanner scanner = new Scanner(new File(path));
                for (int i = 0; i < 4; i++){
                    if (i == 3){
                        rutaArchivoOriginal = scanner.nextLine().split(":")[1];
                    }
                    scanner.nextLine();
                }
                scanner.close();
            } catch (IOException ex){
            } finally{
                return rutaArchivoOriginal.trim();
            }
        });
    }
}
