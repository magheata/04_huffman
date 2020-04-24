/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */

package Application;

import Domain.Interficies.IController;
import Domain.Node;
import Infrastructure.Compressor;
import Infrastructure.Reader;
import Infrastructure.Utils.BinaryOut;
import Presentation.Panels.FilesPanel;
import Presentation.Panels.HuffmanTriePanel;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class Controller implements IController {

    private FilesPanel filesPanel;
    private Reader reader;
    private Compressor compressor;
    private ExecutorService executorService;
    private Map<String, Node> rootNodes = new HashMap<>();
    private HashMap<String, File> files;
    private HashMap<String, BinaryOut> binaryOutFiles = new HashMap<>();

    public Controller() {
        reader = new Reader();
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
            executorService.submit(() -> comrimirFichero((File) i.next()));
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

    /**
     *
     */
    @Override
    public void descomprimirFicheros() {

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

    public HashMap<String, File> getFiles() {
        return files;
    }
    //endregion
}
