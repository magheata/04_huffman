/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */

package Application;

import Domain.Interficies.IController;
import Domain.Node;
import Infrastructure.Compressor;
import Infrastructure.Decompressor;
import Infrastructure.Reader;
import Infrastructure.Utils.BinaryOut;
import Presentation.Panels.FilesPanel;
import Presentation.Panels.HuffmanTriePanel;
import Utils.Constantes;

import javax.swing.*;
import java.util.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */

public class Controller implements IController {

    private FilesPanel filesPanel;
    private Reader reader;
    private Compressor compressor;
    private Decompressor decompressor;
    private ExecutorService executorService;
    private Map<String, Node> rootNodes = new HashMap<>();
    private HashMap<String, File> files;
    private HashMap<String, BinaryOut> binaryOutFiles = new HashMap<>();
    private HashMap<String, Boolean> fileIsNew = new HashMap<>();

    public Controller() {
        reader = new Reader();
        initRootNodes();
    }

    /**
     *
     * @param directory
     * @param selectedFiles
     */
    @Override
    public void addFiles(File directory, File[] selectedFiles) {
        filesPanel.setSelectedFiles(selectedFiles);
    }

    public boolean isFileNew(String fileName){
        return fileIsNew.get(fileName);
    }

    /**
     *
     * @param file
     */
    @Override
    public void deleteFile(File file) { filesPanel.removeFile(file); }

    /**
     *
     * @param files
     */
    @Override
    public void comprimirFicheros(Set<File> files) {
        executorService = Executors.newFixedThreadPool(files.size());
        for (Iterator i = files.iterator(); i.hasNext();){
            executorService.submit(() -> comprimirFichero((File) i.next()));
        }
    }



    /**
     *
     * @param fileName
     * @return
     */
    @Override
    public StringBuilder readFileContent(String fileName){
        return reader.getFileContent(fileName);
    }


    private void comprimirFichero(File file){
        compressor = new Compressor(this, reader.getBytes(file), file);
        compressor.start();
    }

    /**
     *
     * @param file
     * @param bytesOriginales
     * @param bytesComprimidos
     */
    @Override
    public void addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos){
        filesPanel.addArchivosPorComprimirAPanel(file, bytesOriginales, bytesComprimidos);
    }

    @Override
    public void descomprimirFicheros(String nombre, File file) {
        decompressor = new Decompressor(this, rootNodes.get(nombre), file, "txt");
        decompressor.run();
    }

    /**
     *
     * @param fileName
     * @return
     */
    @Override
    public JComponent addTrieToPanel(String fileName) {
        HuffmanTriePanel trie = new HuffmanTriePanel(rootNodes.get(fileName));
        return trie.getGraphComponent();
    }

    /**
     *
     * @param node
     * @param fileName
     */
    @Override
    public void addFileRoot(Node node, String fileName){
        rootNodes.put(fileName, node);
        fileIsNew.put(fileName, true);
    }

    /**
     *
     * @param outputType
     * @param path
     */
    @Override
    public void createBinaryOutputFile(String outputType, String path) {
        binaryOutFiles.put(outputType, new BinaryOut(path));
    }

    /**
     *
     * @param binaryOutputFile
     */
    @Override
    public void closeBinaryOutputFile(String binaryOutputFile) {
        BinaryOut bOut = binaryOutFiles.get(binaryOutputFile);
        bOut.flush();
        bOut.close();
        binaryOutFiles.remove(binaryOutputFile);
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

    @Override
    public void write(String outputFile, int integer) {
        binaryOutFiles.get(outputFile).write(integer);
    }

    @Override
    public void initRootNodes() {
        ArrayList<File> files = listFilesForFolder(new File("compressed/"));
        if (files.size() > 0){
            for (File fileEntry: files) {
                if (!fileEntry.getName().equals(".DS_Store")){
                    String name = fileEntry.getName().substring(0, fileEntry.getName().length() - Constantes.EXTENSION_COMPRESSED_FILE.length());
                    Object [] bytes = getOriginalAndCompressedBytes(Constantes.PATH_HUFFMAN_CODES + name + Constantes.EXTENSION_HUFFMAN_CODES);
                    Constantes.tableModelTotalArchivos.addRow(new Object[]{name + "." + bytes[0], bytes[1] + " bits", bytes[2] + " bits"});
                    Node rootNode = reader.readTrieFromFile(Constantes.PATH_HUFFMAN_TRIE + name + Constantes.EXTENSION_HUFFMAN_TRIE);
                    rootNodes.put(name, rootNode);
                    fileIsNew.put(name, false);
                }
            }
        }
    }

    @Override
    public Object[] getOriginalAndCompressedBytes(String path) {
        return reader.getOriginalAndCompressedBytes(path);
    }

    @Override
    public ArrayList<File> listFilesForFolder(File folder) {
        ArrayList<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            files.add(fileEntry);
        }
        return files;
    }

    /**
     *
     * @param file
     */
    private void comrimirFichero(File file){
        compressor = new Compressor(this, reader.getBytes(file), file);
        compressor.start();
    }

    //region SETTERS Y GETTERS
    public void setFilesPanel(FilesPanel filesPanel) {
        this.filesPanel = filesPanel;
    }

    public void setFiles(HashMap<String, File> files) {
        this.files = files;
    }

    public Node getFileRoot(String fileName){return rootNodes.get(fileName);}

    public HashMap<String, File> getFiles() {
        return files;
    }
    //endregion
}
