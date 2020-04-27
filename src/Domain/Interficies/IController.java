/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */

package Domain.Interficies;

import Domain.Node;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public interface IController {
    void addFiles(File directory, File[] selectedFiles);
    void deleteFile(File file);
    void comprimirFicheros(Set<File> files);
    StringBuilder readFileContent(String fileName);
    void addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos);
    JComponent addTrieToPanel(String fileName);
    void addFileRoot(Node node, String fileName);
    void createBinaryOutputFile(String outputType, String path) ;
    void closeBinaryOutputFile(String binaryOutputFile) ;
    void write(String outputFile, String string);
    void write(String outputFile, boolean bool);
    void write(String outputFile, byte b);
    void write(String outputFile, int integer);
    void initRootNodes();
    Object[] getOriginalAndCompressedBytes(String path);
    ArrayList<File> listFilesForFolder(File folder);
    void descomprimirFichero(String nombre, File file);
}
