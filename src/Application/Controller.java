/* Created by andreea on 09/04/2020 */
package Application;

import Domain.Interficies.IController;
import Domain.Node;
import Infrastructure.Compressor;
import Infrastructure.Reader;
import Presentation.Panels.FilesPanel;
import Presentation.Panels.HuffmanTriePanel;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements IController {

    private FilesPanel filesPanel;
    private FileDrop fileDropService;
    private Reader reader;
    private Compressor compressor;
    private ExecutorService executorService;
    private Map<String, Node> rootNodes = new HashMap<>();


    public void setFiles(HashMap<String, File> files) {
        this.files = files;
    }

    public HashMap<String, File> getFiles() {
        return files;
    }

    private HashMap<String, File> files;

    public Controller() {
        reader = new Reader();
    }

    @Override
    public void addFiles(File directory, File[] selectedFiles) {
        filesPanel.setSelectedFiles(selectedFiles);
    }

    @Override
    public void deleteFile(File file) {
        filesPanel.removeFile(file);
    }

    @Override
    public void comprimirFicheros(Set<File> files) {
        executorService = Executors.newFixedThreadPool(files.size());
        for (Iterator i = files.iterator(); i.hasNext();){
            executorService.submit(() -> comrimirFichero((File) i.next()));
        }
    }

    public StringBuilder readFileContent(String fileName){
        return reader.getFileContent(fileName);
    }

    private void comrimirFichero(File file){
        compressor = new Compressor(this, reader.getBytes(file), file);
        compressor.start();
    }

    public void addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos){
        filesPanel.addArchivosPorComprimirAPanel(file, bytesOriginales, bytesComprimidos);
    }
    @Override
    public void descomprimirFicheros() {

    }

    public JComponent addTrieToPanel(String fileName) {
        HuffmanTriePanel trie = new HuffmanTriePanel(rootNodes.get(fileName));
        return trie.getGraphComponent();
    }

    public void addFileRoot(Node node, String fileName){
        rootNodes.put(fileName, node);
    }
    //region SETTERS Y GETTERS
    public void setFilesPanel(FilesPanel filesPanel) {
        this.filesPanel = filesPanel;
    }

    public void setFileDropService(FileDrop fileDropService) {
        this.fileDropService = fileDropService;
    }

    //endregion
}
