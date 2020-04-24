/* Created by andreea on 09/04/2020 */
package Presentation.Panels;

import Presentation.Utils.HighlightButton;
import Presentation.TablaFicherosComprimidos;
import Application.Controller;

import java.io.File;

import Presentation.Window;
import Utils.Constantes;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
;


public class DcomPanel extends JPanel {

    private JPanel deleteFilePanel;
    private HighlightButton descomprimirArchivoButton;
    private Color color;
    private Presentation.Window window;
    private int totalArchivos;
    private Controller controller;
    private JScrollPane selectedFilesScrollPane;
    private DefaultListModel listModel;
    private File ficheroSeleccionado;
    private ArrayList<File> fileArrayList;
    private JList<ArrayList> lista;
    int idx;

    public DcomPanel(Window window, Controller controller) {
        this.window = window;
        this.controller = controller;
        this.color = Color.white;
        initComponents();
    }

    private void initComponents() {
        ToolTipManager.sharedInstance().setInitialDelay(1);
        this.setLayout(new BorderLayout());
        descomprimirArchivoButton = new HighlightButton("Descomprimir archivo");
        descomprimirArchivoButton.addActionListener(e -> {
              controller.descomprimirFicheros(idx,ficheroSeleccionado);
        });
        // Botón que inicia la descompresión
        descomprimirArchivoButton.setHighlight(new Color(231, 29, 54, 64));
        descomprimirArchivoButton.setFocusPainted(false);
        descomprimirArchivoButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        descomprimirArchivoButton.setVisible(false);

        JPanel wrapperFiles = new JPanel();
        wrapperFiles.setLayout(new FlowLayout());

        deleteFilePanel = new JPanel();
        deleteFilePanel.setLayout(new FlowLayout());


        //Creación de la lista de ficheros
        listModel = new DefaultListModel();
        lista = new JList(listModel);
        addArchivosComprimidos();
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent le) {
                 idx = lista.getSelectedIndex();
                if (idx != -1) {
                    ficheroSeleccionado = fileArrayList.get(idx);
                } else
                    System.out.println("Please choose a language.");
            }
        });


        selectedFilesScrollPane = new JScrollPane(lista, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //  selectedFilesPanel.setLayout(new BoxLayout(selectedFilesPanel, BoxLayout.PAGE_AXIS));

        selectedFilesScrollPane.setSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);
        selectedFilesScrollPane.setPreferredSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);


        wrapperFiles.setSize(Constantes.DIM_FILES_PANEL);
        wrapperFiles.setBackground(color);
        wrapperFiles.add(selectedFilesScrollPane);

        JPanel comprimirFicherosPanel = new JPanel();
        comprimirFicherosPanel.setLayout(new BorderLayout());
        comprimirFicherosPanel.add(wrapperFiles, BorderLayout.NORTH);
        comprimirFicherosPanel.add(descomprimirArchivoButton, BorderLayout.CENTER);

        this.add(comprimirFicherosPanel, BorderLayout.NORTH);
        this.setVisible(true);
    }

    public void addArchivosComprimidos() {
        totalArchivos = 0;
        fileArrayList = new ArrayList<File>();
        File folder = new File(Constantes.TITLE_COMPRESSED_FOLDER);
        for (final File fileEntry : folder.listFiles()) {
            listModel.addElement(fileEntry);
            fileArrayList.add(fileEntry);
            totalArchivos++;
        }
        if (totalArchivos > 0) {
            descomprimirArchivoButton.setVisible(true);
            window.repaintOuterPanel();
        } else {
            if (descomprimirArchivoButton.isVisible()) {
                descomprimirArchivoButton.setVisible(false);
                window.repaintOuterPanel();
            }
        }

    }
}
