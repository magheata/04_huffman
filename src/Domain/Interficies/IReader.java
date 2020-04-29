package Domain.Interficies;

import Domain.Node;
import Infrastructure.Utils.BinaryIn;

import java.io.File;
import java.util.concurrent.Future;

public interface IReader {
    byte[] getBytes(File file);
    Future<StringBuilder> getFileContent(String path);
    Future<Node> readTrieFromFile(String fileName);
    Future<Object[]> getOriginalAndCompressedBytes(String path);
    Future<String> getPathArchivoOriginal(String path);
}
