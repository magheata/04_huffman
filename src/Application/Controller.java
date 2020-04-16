/* Created by andreea on 09/04/2020 */
package Application;

import Domain.Interficies.IController;
import Infrastructure.Compressor;
import Infrastructure.Reader;
import Presentation.FilesPanel;

import java.io.File;
import java.util.Set;

public class Controller implements IController {

    private FilesPanel filesPanel;
    private FileDrop fileDropService;
    private Reader reader;
    private Compressor compressor;

    public Controller() {
        reader = new Reader();
        compressor = new Compressor();
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
        for (File file : files){
            compressor.buildHuffmanTree(reader.getBytes(file), file.getName());
        }
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
