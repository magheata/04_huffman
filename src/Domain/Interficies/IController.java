/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */

package Domain.Interficies;

import Domain.Node;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public interface IController {
    void            addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos);
    void            addFileRoot(Node node, String fileName);
    void            addFiles(File[] selectedFiles);
    JComponent      addTrieToPanel(String fileName);
    void            closeBinaryOutputFile(String binaryOutputFile);
    void            closeOutputFile(String outputFile);
    void            comprimirFicheros(Set<File> files);
    void            createBinaryOutputFile(String outputType, String path);
    void            createOutputFile(String outputType, String path);
    void            deleteFile(File file);
    void            descomprimirFichero(String nombre, File file);
    byte[]          getFileBytes(String name);
    Object[]        getOriginalAndCompressedBytes(String path);
    String          getPathArchivoOriginal(String path);
    void            initRootNodes();
    ArrayList<File> listFilesForFolder(File folder);
    StringBuilder   readFileContent(String fileName);
    void            replaceProgressBarFilesPanel();
    void            resizePanels(int width, int height);
    void            write(String outputFile, boolean bool);
    void            write(String outputFile, byte b);
    void            write(String outputFile, byte[] bytes);
    void            write(String outputFile, int integer);
    void            write(String outputFile, String string);
}
