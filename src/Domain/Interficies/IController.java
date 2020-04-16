package Domain.Interficies;

import java.io.File;
import java.util.Set;

public interface IController {
    void addFiles(File directory, File[] selectedFiles);
    void deleteFile(File file);
    void comprimirFicheros(Set<File> files);
    void descomprimirFicheros();
}
