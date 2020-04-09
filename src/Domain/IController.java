package Domain;

import java.io.File;

public interface IController {
    void addFiles(File[] selectedFiles);
    void deleteFile(String fileName);
}
