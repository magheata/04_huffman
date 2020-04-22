package Domain.Interficies;

import Domain.Node;
import Infrastructure.Utils.BinaryOut;

import javax.swing.*;
import java.io.File;
import java.util.Set;
import java.util.function.BinaryOperator;

public interface IController {
    void addFiles(File directory, File[] selectedFiles);
    void deleteFile(File file);
    void comprimirFicheros(Set<File> files);
    void descomprimirFicheros();
    StringBuilder readFileContent(String fileName);
    void addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos);
    JComponent addTrieToPanel(String fileName);
    void addFileRoot(Node node, String fileName);
    void createBinaryOutputFile(String outputType, String path) ;
    void closeBinaryOutputFile(String binaryOutputFile) ;
    void write(String outputFile, String string);
    void write(String outputFile, boolean bool);
    void write(String outputFile, byte b);
}
