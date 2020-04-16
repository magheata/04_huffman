/* Created by andreea on 09/04/2020 */
package Application;

import Domain.Interficies.IController;
import Infrastructure.Compressor;
import Infrastructure.Reader;
import Presentation.FilesPanel;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements IController {

    private FilesPanel filesPanel;
    private FileDrop fileDropService;
    private Reader reader;
    private Compressor compressor;
    private ExecutorService executorService;

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
        for (File file : files){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.submit(() -> comrimirFichero(file));
        }
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

    //region SETTERS Y GETTERS
    public void setFilesPanel(FilesPanel filesPanel) {
        this.filesPanel = filesPanel;
    }

    public void setFileDropService(FileDrop fileDropService) {
        this.fileDropService = fileDropService;
    }
    //endregion
}
