package Domain.Interficies;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Set;

public interface IController {
    void addFiles(File directory, File[] selectedFiles);
    void deleteFile(File file);
    void comprimirFicheros(Set<File> files);
    void descomprimirFicheros();
}
