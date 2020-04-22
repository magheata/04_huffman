/* Created by andreea on 09/04/2020 */
package Application;

import Domain.Interficies.IController;
import Domain.Node;
import Infrastructure.Compressor;
import Infrastructure.Reader;
import Infrastructure.Utils.BinaryOut;
import Presentation.Panels.FilesPanel;
import Presentation.Panels.HuffmanTriePanel;
import Utils.Constantes;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements IController {

    private FilesPanel filesPanel;
    private Reader reader;
    private Compressor compressor;
    private ExecutorService executorService;
    private Map<String, Node> rootNodes = new HashMap<>();
    public HashMap<String, File> getFiles() {
        return files;
    }
    private HashMap<String, File> files;
    private HashMap<String, BinaryOut> binaryOutFiles = new HashMap<>();


    public Controller() {
        reader = new Reader();
    }

    @Override
    public void addFiles(File directory, File[] selectedFiles) {
        filesPanel.setSelectedFiles(selectedFiles);
    }

    @Override
    public void deleteFile(File file) { filesPanel.removeFile(file); }

    @Override
    public void comprimirFicheros(Set<File> files) {
        executorService = Executors.newFixedThreadPool(files.size());
        for (Iterator i = files.iterator(); i.hasNext();){
            executorService.submit(() -> comrimirFichero((File) i.next()));
        }
    }

    @Override
    public StringBuilder readFileContent(String fileName){
        return reader.getFileContent(fileName);
    }

    @Override
    public void addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos){
        filesPanel.addArchivosPorComprimirAPanel(file, bytesOriginales, bytesComprimidos);
    }

    @Override
    public void descomprimirFicheros() {

    }

    @Override
    public JComponent addTrieToPanel(String fileName) {
        HuffmanTriePanel trie = new HuffmanTriePanel(rootNodes.get(fileName));
        return trie.getGraphComponent();
    }

    @Override
    public void addFileRoot(Node node, String fileName){
        rootNodes.put(fileName, node);
    }

    @Override
    public void createBinaryOutputFile(String outputType, String path) {
        binaryOutFiles.put(outputType, new BinaryOut(path));
    }

    @Override
    public void closeBinaryOutputFile(String binaryOutputFile) {
        BinaryOut bOut = binaryOutFiles.get(binaryOutputFile);
        bOut.flush();
        bOut.close();
        binaryOutFiles.remove(binaryOutputFile);
    }

    @Override
    public void write(String outputFile, String string) {
        binaryOutFiles.get(outputFile).write(string);
    }

    @Override
    public void write(String outputFile, boolean bool) {
        binaryOutFiles.get(outputFile).write(bool);
    }

    @Override
    public void write(String outputFile, byte b) {
        binaryOutFiles.get(outputFile).write(b);
    }

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
    //endregion
}
