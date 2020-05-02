/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Infrastructure;

import Domain.Interficies.IReader;
import Domain.Node;
import Infrastructure.Utils.BinaryIn;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Clase encargada de realizar las lecturas de los ficheros
 */
public class Reader implements IReader {

    /**
     * Thread que se lanza cuando se quiere obtener el contenido del fichero
     */
    private ExecutorService executorGetFileContent = Executors.newSingleThreadExecutor();

    /**
     * Thread que se lanza cuando se quiere obtener el árbol Huffman asociado al fichero
     */
    private ExecutorService executorGetTrie = Executors.newSingleThreadExecutor();

    /**
     * Thread que se lanza cuando se quiere obtener los bytes del fichero
     */
    private ExecutorService executorGetBytes = Executors.newSingleThreadExecutor();

    /**
     * Método que retorna los bytes del fichero
     * @param file fichero que se desea leer
     * @return bytes del fichero
     */
    @Override
    public byte[] getBytes(File file){
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método que retorna el contenio del fichero
     * @param path ruta del fichero que se quiere leer
     * @return Stringbuilder con el contenido del fichero
     */
    @Override
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

    /**
     * Metodo que retorna el árbol Huffman correspondiente al fichero
     * @param fileName fichero donde se encuentra el árbol
     * @return nodo raíz del árbol Huffman
     */
    @Override
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

            // right child
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

    /**
     * Método que retorna la información de compresión del archivo dado por parámetro
     * @param path ruta donde se encuentra el archivo en el huffmanCodes/
     * @return lista de objetos que contiene la extensión, los bytes originales y los bytes comprimidos, respectivamente
     */
    @Override
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

    /**
     * Método que retorna la ruta al archivo original
     * @param path ruta del archivo dentro de huffmanCodes/
     * @return ruta al archivo original
     */
    @Override
    public Future<String> getPathArchivoOriginal(String path){
        return executorGetBytes.submit(() -> {
            String rutaArchivoOriginal = null;
            try{
                Scanner scanner = new Scanner(new File(path));
                for (int i = 0; i < 4; i++){
                    if (i == 3){
                        rutaArchivoOriginal = scanner.nextLine().split(";")[1];
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

    /**
     * Método recursivo que retorna el nodo raíz del árbol
     * @param node nodo padre
     * @param bIn instancia de lectura del archivo
     * @return nodo leído
     */
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
}
