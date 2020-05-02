/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */

package Application;

import Domain.Interficies.IController;
import Domain.Node;
import Infrastructure.Compressor;
import Infrastructure.Decompressor;
import Infrastructure.Reader;
import Infrastructure.Utils.BinaryOut;
import Presentation.Panels.DecompressPanel;
import Presentation.Panels.FilesPanel;
import Presentation.Panels.HuffmanTriePanel;
import Utils.Constantes;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase del programa que se encarga de realizar la comuniación entre los distintos elementos del proyecto
 */
public class Controller implements IController {

    private FilesPanel filesPanel;
    private DecompressPanel decompressPanel;
    private Reader reader;
    private Compressor compressor;
    private Decompressor decompressor;

    private ExecutorService executorService;

    /**
     * Nodos raíz de los árboles Huffman de todos los ficheros comprimidos del proyecto, tanto comprimidos en una misma
     * ejecución como los archivos comprimidos con anterioridad.
     */
    private Map<String, Node> rootNodes = new HashMap<>();

    /**
     * Todos los ficheros que se están creando con su respectiva instancia de la clase BinaryOut.
     */
    private HashMap<String, BinaryOut> binaryOutFiles = new HashMap<>();

    /**
     * Todos los ficheros que se están creando con su respectiva instancia de la clase FileOutputStream.
     */
    private HashMap<String, FileOutputStream> outFiles = new HashMap<>();

    /**
     * Todos los ficheros existentes en el proyecto, tanto ya comprimidos como nuevos comprimidos. Cuando se comprime
     * un fichero en esta ejecución se marca como true con el fin de resaltarlo en la tabla de los ficheros comprimidos.
     */
    private HashMap<String, Boolean> fileIsNew = new HashMap<>();

    private AtomicInteger filesCompressed = new AtomicInteger(0);

    private int totalFiles = 0;

    public Controller() {
        reader = new Reader();
        // Comprobamos si hay ficheros ya comprimidos de otra ejecución
        initRootNodes();
    }

    /**
     * Una vez comprimido un archivo se añade a la tabla de los archivos comprimidos y se elimina la barra de progreso
     * @param file Fichero que se acaba de comprimir
     * @param bytesOriginales número de bits que ocupaba el fichero original
     * @param bytesComprimidos número de bits que ocupa el fichero comprimido
     */
    @Override
    public void addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos){
        filesPanel.addArchivosPorComprimirAPanel(file, bytesOriginales, bytesComprimidos);
        // Se elimina la barra de progreso de la vista
        replaceProgressBarFilesPanel();
    }

    /**
     * Añadimos el nodo raíz del árbol Huffman equivalente al fichero a la lista
     * @param node nodo raíz del árbiol Huffman
     * @param fileName nombre del fichero comprimido
     */
    @Override
    public void addFileRoot(Node node, String fileName){
        // Si ya existía un nodo para este fichero se elimina para que se pueda reemplazar
        if (rootNodes.get(fileName) != null){
            rootNodes.remove(fileName);
        }
        rootNodes.put(fileName, node);
        // este fichero se acaba de crear por lo tanto se marca como nuevo
        fileIsNew.put(fileName, true);
    }

    /**
     * Método que guarda una lista con los archivos que se quieren comprimir. Los archivos pueden proceder de
     * arrastrar x archivos o de seleccionar a través de la ventana de selección de ficheros.
     * @param selectedFiles archivos seleccionados para su posterior compresión
     */
    @Override
    public void addFiles(File[] selectedFiles) {
        filesPanel.setSelectedFiles(selectedFiles);
    }

    /**
     * Método que sirve para añadir el árbol Huffman al panel de CompressionInformationPanel
     * @param fileName nombre del fichero del cual se quiere mostrar el árbol
     * @return
     */
    @Override
    public JComponent addTrieToPanel(String fileName) {
        // Creamos el componente del árbol
        HuffmanTriePanel trie = new HuffmanTriePanel(rootNodes.get(fileName));
        return trie.getGraphComponent();
    }

    /**
     * Método que sirve para cerrar el fichero tras su escritura
     * @param binaryOutputFile nombre que identifica a la instancia de BinaryOut
     */
    @Override
    public void closeBinaryOutputFile(String binaryOutputFile) {
        BinaryOut bOut = binaryOutFiles.get(binaryOutputFile);
        bOut.flush();
        bOut.close();
        // Eliminamos de la lista la instancia de BinaryOut
        binaryOutFiles.remove(binaryOutputFile);
    }

    /**
     * Método que sirve para cerrar el fichero tras su escritura
     * @param outputFile nombre que identifica a la instancia de FileOutputStream
     */
    @Override
    public void closeOutputFile(String outputFile) {
        FileOutputStream bOut = outFiles.get(outputFile);
        try {
            bOut.flush();
            bOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outFiles.remove(outputFile);
    }

    /**
     * Método que sirve para comprimir todos los ficheros seleccionados por el usuario. Por cada fichero se levanta un
     * hilo de manera que todos los ficheros se comprimen en paralelo.
     * @param files lista de ficheros por comprimir
     */
    @Override
    public void comprimirFicheros(Set<File> files) {
        //se guarda el tamaño de la lista
        totalFiles = files.size();
        for (Iterator i = files.iterator(); i.hasNext();){
            // creamos el Thread
            executorService = Executors.newSingleThreadExecutor();
            // ejecutamos el Thread
            executorService.submit(() -> comprimirFichero((File) i.next()));
        }
    }

    /**
     * Método que sirve para crear una instancia de la clase BinaryOut
     * @param outputType string con el que se identificará la instancia, representa el nombre del fichero que se está
     *                   escribiendo
     * @param path ruta del archivo que se desea crear y escribir
     */
    @Override
    public void createBinaryOutputFile(String outputType, String path) {
        // se añade la nueva instancia a la lista
        binaryOutFiles.put(outputType, new BinaryOut(path));
    }

    /**
     * Método que sirve para crear una instancia de la clase FileOutputStream
     * @param outputType string con el que se identificará la instancia, representa el nombre del fichero que se está
     *                   escribiendo
     * @param path ruta del archivo que se desea crear y escribir
     */
    @Override
    public void createOutputFile(String outputType, String path) {
        try {
            outFiles.put(outputType, new FileOutputStream(path, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que sirve para eliminar un archivo de la lista de archivos que se desea comprimir
     * @param file fichero que se desea eliminar
     */
    @Override
    public void deleteFile(File file) { filesPanel.removeFile(file); }

    /**
     * Método que sirve para descomprimir un fichero. Sólo se puede descomprimir un fichero cada vez. Se lanza un hilo
     * para que se realice la descompresión.
     * @param nombre nombre del fichero que se desea descomprimir.
     * @param fileOriginal fichero original
     */
    @Override
    public void descomprimirFichero(String nombre, File fileOriginal) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> descomprimirFicheroPrivate(nombre, fileOriginal));
    }

    /**
     * Método que retorna ínformación del fichero comprimido (bits originales, bits después de la compresión, y extensión
     * del archivo original).
     * @param path ruta al fichero dentro de la carpeta huffmanCodes/
     * @return
     */
    @Override
    public Object[] getOriginalAndCompressedBytes(String path) {
        try {
            Future<Object[]> objects = reader.getOriginalAndCompressedBytes(path);
            while (!objects.isDone()){}
            return objects.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método que retorna la ruta del fichero original que se encuentra dentro del fichero equivalente en la carpeta
     * huffmanCodes/
     * @param path ruta al fichero dentro de la carpeta huffmanCodes/
     * @return
     */
    @Override
    public String getPathArchivoOriginal(String path){
        try {
            Future <String> pathOriginal = reader.getPathArchivoOriginal(path);
            while (!pathOriginal.isDone()){}
            return pathOriginal.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Método que sirve para inicializar la información correspondiente a los ficheros comprimidos en ejecuciones
     * anteriores.
     */
    @Override
    public void initRootNodes() {
        ArrayList<File> files = listFilesForFolder(new File(Constantes.PATH_COMPRESSED_FILE));
        // Si hay ficheros dentro de la carpeta significa que hay ficheros ya comprimidos
        if (files.size() > 0){
            for (File fileEntry: files) {
                if (!fileEntry.getName().equals(".DS_Store")){
                    String name = fileEntry.getName().substring(0, fileEntry.getName().length() - Constantes.EXTENSION_COMPRESSED_FILE.length());
                    // Obtenemos la información que se debe introducir en la tabla de archivos comprimidos
                    Object [] bytes = getOriginalAndCompressedBytes(Constantes.PATH_HUFFMAN_CODES + name + Constantes.EXTENSION_HUFFMAN_CODES);
                    // Añadimos la información del archivo en la tabla
                    Constantes.tableModelTotalArchivos.addRow(new Object[]{name + "." + bytes[0],
                            bytes[1] + " bits",
                            bytes[2] + " bits",
                            getPercentageCompression((int) bytes[1], (int) bytes[2])});
                    // Creamos el nodo raíz del árbol Huffman correspondiente
                    Future<Node> rootNode = reader.readTrieFromFile(Constantes.PATH_HUFFMAN_TRIE + name + Constantes.EXTENSION_HUFFMAN_TRIE);
                    while(!rootNode.isDone()){}
                    try{
                        // Añadimos la raíz a la lista
                        rootNodes.put(name, rootNode.get());
                        // Como no se acaba de crear lo marcamos falso (no es nuevo)
                        fileIsNew.put(name, false);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Retorna todos los ficheros de una carpeta
     * @param folder carpeta que se quiere comprobar
     * @return
     */
    @Override
    public ArrayList<File> listFilesForFolder(File folder) {
        ArrayList<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            files.add(fileEntry);
        }
        return files;
    }

    /**
     * Método que retorna un StringBuilder que contiene el contenido de un fichero
     * @param fileName nombre del fichero que se quiere leer
     * @return
     */
    @Override
    public StringBuilder readFileContent(String fileName){
        try {
            Future<StringBuilder> fileContent = reader.getFileContent(fileName);
            while (!fileContent.isDone()){}
            return  fileContent.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Método que sirve para eliminar la barra de progreso de FilesPanel una vez se hayan comprimido todos los archivos
     */
    @Override
    public void replaceProgressBarFilesPanel() {
        if (filesCompressed.incrementAndGet() == totalFiles){
            filesPanel.replaceProgressBar();
            filesCompressed.set(0);
        }
    }

    /**
     * Método que sirve para modificar el tamaño de los paneles que contienen el contenido del archivo original y del
     * comprimido tras descomprimirlo a medida que se modifique la ventana de tamaño.
     * @param width anchura de la ventana
     * @param height altura de la ventana
     */
    @Override
    public void resizePanels(int width, int height){
        decompressPanel.resizePanels(width, height);
    }

    /**
     *
     * @param outputFile
     * @param bool
     */
    @Override
    public void write(String outputFile, boolean bool) {
        binaryOutFiles.get(outputFile).write(bool);
    }

    /**
     *
     * @param outputFile
     * @param b
     */
    @Override
    public void write(String outputFile, byte b) {
        binaryOutFiles.get(outputFile).write(b);
    }

    /**
     *
     * @param outputFile
     * @param bytes
     */
    @Override
    public void write(String outputFile, byte[] bytes) {
        try {
            outFiles.get(outputFile).write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param outputFile
     * @param integer
     */
    @Override
    public void write(String outputFile, int integer) {
        binaryOutFiles.get(outputFile).write(integer);
    }

    /**
     *
     * @param outputFile
     * @param string
     */
    @Override
    public void write(String outputFile, String string) {
        binaryOutFiles.get(outputFile).write(string);
    }

    //region PRIVATE METHODS

    /**
     * Método que crea una instancia de la clase Compresor y ejecuta el Thread del mismo para iniciar la compresión del
     * archivo
     * @param file fichero que se desea comprimir
     */
    private void comprimirFichero(File file){
        filesPanel.replaceComprimirButton();
        compressor = new Compressor(this, reader.getBytes(file), file);
        compressor.comprimir();
    }

    /**
     * Método que sirve para descomprimir un fichero
     * @param nombre nombre del fichero que se desea descomprimir
     * @param file fichero original
     */
    private void descomprimirFicheroPrivate(String nombre, File file){
        try {
            // obtenemos la extensión del archivo original
            Future<Object[]> objects = reader.getOriginalAndCompressedBytes(Constantes.PATH_HUFFMAN_CODES + nombre + Constantes.EXTENSION_HUFFMAN_CODES);
            while (!objects.isDone()){}
            decompressor = new Decompressor(this, rootNodes.get(nombre), file, (String) objects.get()[0]);
            decompressor.descomprimir();
            // añadimos el contenido del archivo original a la ventana
            decompressPanel.addContentToArchivoOriginalPanel();
            // añadimos el contenido del archivo descomprimido a la ventana
            decompressPanel.addContentToArchivoDescomprimidoPanel();
            // eliminamos la barra de progreso
            decompressPanel.replaceProgressBar();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region SETTERS Y GETTERS

    /**
     *
     * @param filesPanel
     */
    public void setFilesPanel(FilesPanel filesPanel) {
        this.filesPanel = filesPanel;
    }

    /**
     *
     * @param original
     * @param comprimido
     * @return
     */
    public int getPercentageCompression(int original, int comprimido) {
        return 100 - ((100 * comprimido) / original);
    }

    /**
     *
     * @param decompressPanel
     */
    public void setDecompressPanel(DecompressPanel decompressPanel) {
        this.decompressPanel = decompressPanel;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public boolean isFileNew(String fileName){
        return fileIsNew.get(fileName);
    }
    //endregion
}
