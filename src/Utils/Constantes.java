/* Created by andreea on 13/04/2020 */
package Utils;

import Presentation.TableModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

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
    public final static int HEIGHT_WINDOW = 600;

    public final static Border BORDER_DELETE_FILES_PANEL = BorderFactory.createEmptyBorder(25, 10, 10, 10);
    public final static Border BORDER_DND_PANEL = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    public final static JButton BTN_CANCELAR = new JButton("Cancelar");
    public final static JButton BTN_REEMPLAZAR_TODO = new JButton("Reemplazar todo");
    public final static JButton BTN_REEMPLAZAR = new JButton("Reemplazar");

    public final static Dimension DIM_DELETE_FILES_PANEL = new Dimension(WIDTH_DELETE_FILES_PANEL, HEIGHT_DELETE_FILES_PANEL);
    public final static Dimension DIM_SELECTED_FILES_SCROLL_PANEL = new Dimension(WIDTH_SELECTED_FILES_SCROLL_PANEL, HEIGHT_SELECTED_FILES_SCROLL_PANEL);
    public final static Dimension DIM_FILES_PANEL = new Dimension(WIDTH_FILES_PANEL, HEIGHT_FILES_PANEL);
    public final static Dimension DIM_WINDOW = new Dimension(WIDTH_WINDOW, HEIGHT_WINDOW);
    public final static String TEXT_DND_PANEL = "O arrastrar archivos aquí";
    public final static String TEXT_FILECHOOSER_PANEL = "Seleccionar un archivo...";

    public final static String TITLE_COMPRIMIR_TABBED_PANE = "Comprimir";
    public final static String TITLE_DESCOMPRIMIR_TABBED_PANE = "Descomprimir";
    public final static String TITLE_FICHEROS_COMPRIMIDOS_TABBED_PANE = "Archivos comprimidos";
    public final static String TITLE_INFO_TABBED_PANE = "Instrucciones uso";
    public final static String TITLE_HUFFMAN_CODE_PANE = "Códigos Huffman";
    public final static String TITLE_HUFFMAN_TREE_PANE = "Árbol Huffman";

    public final static String PATH_TRASH_ICON = "src/Presentation/Images/delete.png";

    public final static String [] COLUMNAS_TABLA_FICHEROS = {"Fichero", "# Bits (Original)", "# Bits (Comprimido)"};

    public final static TableModel tableModel = new TableModel(COLUMNAS_TABLA_FICHEROS);

    public final static String HUFFMAN_CODES_EXTENSION = "_codes.txt";
    public final static String HUFFMAN_TRIE_EXTENSION = "_trie.txt";
    public final static String COMPRESSED_FILE_EXTENSION = "_compressed.txt";
}
