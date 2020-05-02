package Infrastructure;

import Application.Controller;
import Domain.Node;
import Infrastructure.Utils.BinaryIn;
import Utils.Constantes;

import java.io.File;

/**
 * Clase encargada de descomprimir un archivo dado.
 */
public class Decompressor {
    private Controller controller;
    private Node root;
    private File file;
    private String extension;

    // Class constructor
    public Decompressor(Controller controller, Node root, File file, String extension) {
        this.controller = controller;
        this.root = root;
        this.file = file;
        this.extension = extension;
    }

    /**
     * Método que se encarga de descomprimir un archivo dado. La descompresión de realiza mirando cada código Huffman
     * del contenido y recorriendo el árbol hasta encontrar el nodo hoja correspondiente al byte que representa
     */
    public void descomprimir() {
        String name = file.getName().substring(0, file.getName().indexOf("_compressed.txt"));
        // Se crea el archivo descomprimido con el nombre y la extensión original
        controller.createOutputFile(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name,
                Constantes.PATH_DECOMPRESSED_FILE + name + "." + extension);
        // Nodo de la raíz del árbol Huffman
        Node tmp = root;
        // Contenido del fichero comprimido
        BinaryIn bIn = new BinaryIn(file.getAbsolutePath());
        boolean code;
        // Para cada byte
        while (!bIn.isEmpty()){
            // Si no es nodo hoja avanzamos en el árbol
            if (!tmp.isLeaf()){
                code = bIn.readBoolean();
                if (!code){
                    tmp = tmp.leftNode;
                } else {
                    tmp = tmp.rightNode;
                }
                //idx++;
            } else {
                // Si es nodo hoja escribimos el byte correspondiente
                controller.write(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name, new byte[] {tmp.byteRepresentado});
                // Volvemos a la raíz del árbol
                tmp = root;
            }
        }
        // Escribimos el último byte
        controller.write(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name, new byte[] {tmp.byteRepresentado});
        // Cerramos el fichero
        controller.closeOutputFile(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name);
    }
}
