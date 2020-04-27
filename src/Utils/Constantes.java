/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Utils;

import Domain.TableModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 *
 */
public class Constantes {
    public final static int IMAGE_PREVIEW_WIDTH = 150;
    public final static int IMAGE_PREVIEW_HEIGHT = 100;

    public final static int WIDTH_DELETE_FILES_PANEL = 45;
    public final static int HEIGHT_DELETE_FILES_PANEL = 100;
    public final static int WIDTH_SELECTED_FILES_SCROLL_PANEL = 395;
    public final static int HEIGHT_SELECTED_FILES_SCROLL_PANEL = 100;
    public final static int WIDTH_FILES_PANEL = 450;
    public final static int HEIGHT_FILES_PANEL = 900;
    public final static int WIDTH_WINDOW = 500;
    public final static int HEIGHT_WINDOW = 750;
    public final static int WIDTH_TABLA_FICHEROS_COMPRIMIDOS = 300;
    public final static int HEIGHT_TABLA_FICHEROS_COMPRIMIDOS = 100;

    public final static Border BORDER_DELETE_FILES_PANEL = BorderFactory.createEmptyBorder(25, 10, 10, 10);
    public final static Border BORDER_DND_PANEL = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    public final static JButton BTN_CANCELAR = new JButton("Cancelar");
    public final static JButton BTN_REEMPLAZAR_TODO = new JButton("Reemplazar todo");
    public final static JButton BTN_REEMPLAZAR = new JButton("Reemplazar");

    public final static Dimension DIM_DELETE_FILES_PANEL = new Dimension(WIDTH_DELETE_FILES_PANEL, HEIGHT_DELETE_FILES_PANEL);
    public final static Dimension DIM_SELECTED_FILES_SCROLL_PANEL = new Dimension(WIDTH_SELECTED_FILES_SCROLL_PANEL, HEIGHT_SELECTED_FILES_SCROLL_PANEL);
    public final static Dimension DIM_FILES_PANEL = new Dimension(WIDTH_FILES_PANEL, HEIGHT_FILES_PANEL);
    public final static Dimension DIM_WINDOW = new Dimension(WIDTH_WINDOW, HEIGHT_WINDOW);
    public final static Dimension DIM_TABLA_FICHEROS_COMPRIMIDOS = new Dimension(WIDTH_TABLA_FICHEROS_COMPRIMIDOS, HEIGHT_TABLA_FICHEROS_COMPRIMIDOS);

    public final static String TEXT_DND_PANEL = "O arrastrar archivos aquí";
    public final static String TEXT_FILECHOOSER_PANEL = "Seleccionar un archivo...";

    public final static String TITLE_WINDOW = "Compresor Huffman";
    public final static String TITLE_COMPRIMIR_TABBED_PANE = "Comprimir";
    public final static String TITLE_DESCOMPRIMIR_TABBED_PANE = "Descomprimir";
    public final static String TITLE_FICHEROS_COMPRIMIDOS_TABBED_PANE = "Archivos comprimidos";
    public final static String TITLE_INFO_TABBED_PANE = "Instrucciones uso";
    public final static String TITLE_HUFFMAN_CODE_PANE = "Códigos Huffman";
    public final static String TITLE_HUFFMAN_TREE_PANE = "Árbol Huffman";
    public final static String TITLE_COMPRESSED_FOLDER = "compressed";

    public final static String PATH_TRASH_ICON = "src/Presentation/Images/delete.png";

    public final static String [] COLUMNAS_TABLA_FICHEROS = {"Fichero", "# Bits (Original)", "# Bits (Comprimido)"};

    public final static TableModel tableModelNewArchivos = new TableModel(COLUMNAS_TABLA_FICHEROS);
    public final static TableModel tableModelTotalArchivos = new TableModel(COLUMNAS_TABLA_FICHEROS);

    public final static String EXTENSION_HUFFMAN_CODES = "_codes.txt";
    public final static String EXTENSION_HUFFMAN_TRIE = "_trie.txt";
    public final static String EXTENSION_COMPRESSED_FILE = "_compressed.txt";

    public final static String PATH_HUFFMAN_CODES = "huffmanCodes/";
    public final static String PATH_HUFFMAN_TRIE = "huffmanTrees/";
    public final static String PATH_COMPRESSED_FILE = "compressed/";
    public final static String PATH_DECOMPRESSED_FILE = "decompressed/";

    public final static String OUTPUT_TYPE_TRIE = "trie_";
    public final static String OUTPUT_TYPE_CODES = "codes_";
    public final static String OUTPUT_TYPE_COMPRESSED_FILE = "compressedFile_";
    public final static String OUTPUT_TYPE_DECOMPRESSED_FILE = "decompressedFile_";


}
