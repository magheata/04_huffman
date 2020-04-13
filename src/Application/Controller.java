/* Created by andreea on 09/04/2020 */
package Application;

import Domain.IController;
import Infrastructure.Reader;
import Presentation.FilesPanel;

import java.io.File;

public class Controller implements IController {

    private FilesPanel filesPanel;
    private FileDrop fileDropService;
    private Reader reader;

    public Controller() {

    }

    @Override
    public void addFiles(File[] selectedFiles) {
        filesPanel.setSelectedFiles(selectedFiles);
    }

    @Override
    public void deleteFile(String fileName) {
        filesPanel.removeFile(fileName, true);
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
