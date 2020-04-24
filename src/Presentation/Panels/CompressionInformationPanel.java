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
 *
 */
public class CompressionInformationPanel extends JPanel {

    private TablaFicherosComprimidos tablaFicherosComprimidos;
    private Controller controller;
    private JPanel huffmanCodePanel, huffmanTreePanel;

    public CompressionInformationPanel(Controller controller) {
        this.controller = controller;
        tablaFicherosComprimidos = new TablaFicherosComprimidos();
        initComponents();
    }

    /**
     *
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());

        huffmanCodePanel = new JPanel();
        huffmanCodePanel.setBackground(Color.white);

        JScrollPane scrollPaneHuffmanCode = new JScrollPane(huffmanCodePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        huffmanTreePanel = new JPanel();

        JScrollPane scrollPaneHuffmanTrie = new JScrollPane(huffmanTreePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(Constantes.TITLE_HUFFMAN_CODE_PANE, scrollPaneHuffmanCode);
        tabbedPane.addTab(Constantes.TITLE_HUFFMAN_TREE_PANE, scrollPaneHuffmanTrie);

        tablaFicherosComprimidos.getPanel().setPreferredSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);
        tablaFicherosComprimidos.getPanel().setSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);
        tablaFicherosComprimidos.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()){
                huffmanCodePanel.removeAll();
                huffmanTreePanel.removeAll();
                Vector values = Constantes.tableModel.getDataVector().elementAt(tablaFicherosComprimidos.getTable().getSelectedRow());
                String file = (String) values.get(0);
                String fileName = file.split("\\.")[0];
                addContentToHuffmanCodesPanel(fileName);
                addContentToHuffmanTriePanel(fileName);
                this.repaint();
            }
        });

        this.add(tablaFicherosComprimidos.getPanel(), BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     *
     * @param fileName
     */
    public void addContentToHuffmanCodesPanel(String fileName){
        huffmanCodePanel.removeAll();
        StringBuilder sb = controller.readFileContent("huffmanCodes/" + fileName + Constantes.EXTENSION_HUFFMAN_CODES);
        JTextPane fileContent = new JTextPane();
        fileContent.setFocusable(false);
        fileContent.setRequestFocusEnabled(false);
        fileContent.setText(sb.toString());
        huffmanCodePanel.add(fileContent);
        huffmanCodePanel.repaint();
    }

    /**
     *
     * @param fileName
     */
    public void addContentToHuffmanTriePanel(String fileName){
        huffmanTreePanel.removeAll();
        huffmanTreePanel.add(controller.addTrieToPanel(fileName));
        huffmanTreePanel.repaint();
    }
}
