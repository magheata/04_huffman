/* Created by andreea on 16/04/2020 */
package Presentation;

import Application.Controller;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class CompressionInformationPanel extends JPanel {

    private TablaFicherosComprimidos tablaFicherosComprimidos;
    private Controller controller;
    private JPanel huffmanCodePanel, huffmanTreePanel;

    public CompressionInformationPanel(Controller controller) {
        this.controller = controller;
        tablaFicherosComprimidos = new TablaFicherosComprimidos();
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        tablaFicherosComprimidos.getPanel().setPreferredSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);
        tablaFicherosComprimidos.getPanel().setSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);

        this.add(tablaFicherosComprimidos.getPanel());
        huffmanCodePanel = new JPanel();
        huffmanCodePanel.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(huffmanCodePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        huffmanTreePanel = new JPanel();
        tablaFicherosComprimidos.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()){
                huffmanCodePanel.removeAll();
                Vector values = Constantes.tableModel.getDataVector().elementAt(tablaFicherosComprimidos.getTable().getSelectedRow());
                String file = (String) values.get(0);
                String fileName = file.split("\\.")[0];
                StringBuilder sb = controller.readFileContent("huffmanCodes/" + fileName + Constantes.EXTENSION_HUFFMAN_CODES);
                JTextPane fileContent = new JTextPane();
                fileContent.setFocusable(false);
                fileContent.setRequestFocusEnabled(false);
                fileContent.setText(sb.toString());
                huffmanCodePanel.add(fileContent);
                huffmanCodePanel.repaint();
                this.repaint();
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(Constantes.TITLE_HUFFMAN_CODE_PANE, scrollPane);
        tabbedPane.addTab(Constantes.TITLE_HUFFMAN_TREE_PANE, huffmanTreePanel);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.add(tablaFicherosComprimidos.getPanel(), BorderLayout.NORTH);
        wrapper.add(tabbedPane, BorderLayout.CENTER);
        this.add(wrapper);
    }
}
