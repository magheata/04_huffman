/* Created by andreea on 09/04/2020 */
package Presentation.Panels;

import Presentation.Utils.HighlightButton;
import Presentation.TablaFicherosComprimidos;
import Application.Controller;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import Presentation.Window;
import Utils.Constantes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Flow;
;


public class DecompressPanel extends JPanel {

    private HighlightButton descomprimirArchivoButton;
    private TablaFicherosComprimidos tablaFicherosComprimidos;
    private Controller controller;
    private String nombreArchivoSeleccionado, extensionArchivo;
    private JPanel archivoDescomprimido, archivoOriginal;
    private JScrollPane scrollPaneArchivoDescomprimido, scrollPaneArchivoOriginal;

    public DecompressPanel(Controller controller) {
        this.controller = controller;
        tablaFicherosComprimidos = new TablaFicherosComprimidos(controller);
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setSize(Constantes.DIM_FILES_PANEL);

        JPanel wrapperTable = new JPanel();
        wrapperTable.setLayout(new BorderLayout());

        archivoDescomprimido = new JPanel();
        archivoDescomprimido.setVisible(true);
        archivoDescomprimido.setBackground(Color.white);
        scrollPaneArchivoDescomprimido = new JScrollPane(archivoDescomprimido, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneArchivoDescomprimido.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight()/2));
        scrollPaneArchivoDescomprimido.setSize(new Dimension(this.getWidth() / 2, this.getHeight()/2));

        JPanel wrapperArchivos = new JPanel();
        wrapperArchivos.setLayout(new BorderLayout());
        wrapperArchivos.setVisible(true);

        archivoOriginal = new JPanel();
        archivoOriginal.setVisible(true);
        archivoOriginal.setBackground(Color.white);
        scrollPaneArchivoOriginal = new JScrollPane(archivoOriginal, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneArchivoOriginal.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight()/2));
        scrollPaneArchivoOriginal.setSize(new Dimension(this.getWidth() / 2, this.getHeight()/2));

        descomprimirArchivoButton = new HighlightButton("Descomprimir archivo");
        descomprimirArchivoButton.addActionListener(e -> {
              controller.descomprimirFicheros(nombreArchivoSeleccionado, new File("compressed/"+ nombreArchivoSeleccionado + "_compressed.txt"));
        });
        descomprimirArchivoButton.setPreferredSize(new Dimension(300, 5));
        tablaFicherosComprimidos.getPanel().setPreferredSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);
        tablaFicherosComprimidos.getPanel().setSize(Constantes.DIM_TABLA_FICHEROS_COMPRIMIDOS);
        tablaFicherosComprimidos.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()){
                Vector values = Constantes.tableModelTotalArchivos.getDataVector().elementAt(tablaFicherosComprimidos.getTable().getSelectedRow());
                String file = (String) values.get(0);
                nombreArchivoSeleccionado = file.split("\\.")[0];
                extensionArchivo = file.split("\\.")[1];
            }
        });

        // Botón que inicia la descompresión
        descomprimirArchivoButton.setHighlight(new Color(231, 29, 54, 64));
        descomprimirArchivoButton.setFocusPainted(false);
        descomprimirArchivoButton.setFont(new Font("Tahoma", Font.BOLD, 12));

        wrapperTable.add(tablaFicherosComprimidos.getPanel(), BorderLayout.NORTH);
        wrapperTable.add(descomprimirArchivoButton, BorderLayout.SOUTH);

        wrapperArchivos.add(scrollPaneArchivoOriginal, BorderLayout.WEST);
        wrapperArchivos.add(scrollPaneArchivoDescomprimido);

        this.add(wrapperTable, BorderLayout.NORTH);
        this.add(wrapperArchivos, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     *
     */
    public void addContentToArchivoDescomprimidoPanel(){
        archivoDescomprimido.removeAll();
        StringBuilder sb = controller.readFileContent("decompressed/" + nombreArchivoSeleccionado + "." + extensionArchivo);
        JTextPane fileContent = new JTextPane();
        fileContent.setFocusable(false);
        fileContent.setRequestFocusEnabled(false);
        fileContent.setText(sb.toString());
        archivoDescomprimido.add(fileContent);
        archivoDescomprimido.repaint();
    }

    /**
     *
     */
    public void addContentToArchivoOriginalPanel(){
        archivoOriginal.removeAll();
        String pathArchivoOriginal = controller.getPathArchivoOriginal(Constantes.PATH_HUFFMAN_CODES + nombreArchivoSeleccionado + Constantes.EXTENSION_HUFFMAN_CODES);
        StringBuilder sb = controller.readFileContent(pathArchivoOriginal);
        JTextPane fileContent = new JTextPane();
        fileContent.setFocusable(false);
        fileContent.setRequestFocusEnabled(false);
        fileContent.setText(sb.toString());
        archivoOriginal.add(fileContent);
        archivoOriginal.repaint();
    }

    public void resizePanels(int width, int height){
        scrollPaneArchivoDescomprimido.setPreferredSize(new Dimension(width / 2, height/2));
        scrollPaneArchivoOriginal.setPreferredSize(new Dimension(width / 2, height/2));
        scrollPaneArchivoDescomprimido.setSize(new Dimension(width / 2, height/2));
        scrollPaneArchivoOriginal.setSize(new Dimension(width / 2, height/2));
    }
}
