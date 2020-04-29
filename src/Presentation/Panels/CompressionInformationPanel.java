/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation.Panels;

import Application.Controller;
import Presentation.TablaFicherosComprimidos;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Vector;

/**
 * Panel que contiene la información de compresión de un archivo. Contiene:
 * 1. Tabla con todos los archivos comprimidos
 * 2. Panel con 2 pestañas:
 *      2.1. Panel que muestra el contenido del archivo correspondiente dentro de la carpeta huffmanCodes/
 *      2.2. Panel que muestra el árbol correspondiente al archivo seleccionado
 * Para poder visualizar los puntos anteriores se debe seleccionar una fila de la tabla (correspondiente al archivo del
 * cual se desea visualizar la información)
 */
public class CompressionInformationPanel extends JPanel {

    private TablaFicherosComprimidos tablaFicherosComprimidos;
    private Controller controller;
    private JPanel huffmanCodePanel, huffmanTreePanel;

    public CompressionInformationPanel(Controller controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * Método que añade los elementos al panel
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(Constantes.TITLE_HUFFMAN_CODE_PANE, initHuffmanCodesPanel());
        tabbedPane.addTab(Constantes.TITLE_HUFFMAN_TREE_PANE, initHuffmanTriePanel());
        this.add(initTabla(), BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Método que añade el contenido del fichero de huffmanCodes/ correspondiente al panel
     * @param fileName nombre del fichero
     */
    public void addContentToHuffmanCodesPanel(String fileName){
        // se elimina el contenido anterior
        huffmanCodePanel.removeAll();
        // se obtiene el contenido del fichero
        StringBuilder sb = controller.readFileContent("huffmanCodes/" + fileName + Constantes.EXTENSION_HUFFMAN_CODES);
        JTextPane fileContent = new JTextPane();
        fileContent.setFocusable(false);
        fileContent.setRequestFocusEnabled(false);
        fileContent.setText(sb.toString());
        // se añade al panel y se repinta
        huffmanCodePanel.add(fileContent);
        huffmanCodePanel.repaint();
    }

    /**
     * Método que añade el árbol correspondiente al fichero seleccionado
     * @param fileName nombre del fichero
     */
    public void addContentToHuffmanTriePanel(String fileName){
        // se elimina el árbol anterior del panel
        huffmanTreePanel.removeAll();
        // se obtiene el árbol del fichero y se añade al panel
        huffmanTreePanel.add(controller.addTrieToPanel(fileName));
        huffmanTreePanel.repaint();
    }

    /**
     * Método que inicia el panel de códigos Huffman
     * @return JScrollPan resultante
     */
    private JScrollPane initHuffmanCodesPanel(){
        huffmanCodePanel = new JPanel();
        huffmanCodePanel.setBackground(Color.white);
        return new JScrollPane(huffmanCodePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Método que inicia el panel del árbol Huffman
     * @return JScrollPan resultante
     */
    private JScrollPane initHuffmanTriePanel(){
        huffmanTreePanel = new JPanel();
        return new JScrollPane(huffmanTreePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Método que inicia la tabla de archivos comprimidos
     * @return JScrollPan resultante
     */
    private JScrollPane initTabla(){
        // creamos la tabla que contendrá los archivos
        tablaFicherosComprimidos = new TablaFicherosComprimidos(controller);
        tablaFicherosComprimidos.getPanel().setPreferredSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);
        tablaFicherosComprimidos.getPanel().setSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);
        tablaFicherosComprimidos.getTable().getSelectionModel().addListSelectionListener(e -> {
            // cuando se selecciona una fila de la tabla
            if (e.getValueIsAdjusting()){
                // se eliminan los contenidos de los paneles
                huffmanCodePanel.removeAll();
                huffmanTreePanel.removeAll();
                // se obtiene la fila que se ha seleccionado
                Vector values = Constantes.tableModelTotalArchivos.getDataVector().elementAt(tablaFicherosComprimidos.getTable().getSelectedRow());
                // se obtiene el nombre del fichero del que se desea visualizar la información
                String file = (String) values.get(0);
                String fileName = file.split("\\.")[0];
                // se añade el contenido al panel
                addContentToHuffmanCodesPanel(fileName);
                // se añade el árbol al panel
                addContentToHuffmanTriePanel(fileName);
                this.repaint();
            }
        });
        return tablaFicherosComprimidos.getPanel();
    }
}
