/* Created by andreea on 09/04/2020 */
package Presentation;

import Presentation.Utils.DropTargetListener;
import Presentation.Utils.FileLabel;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class FilesPanel extends JPanel {

    private JPanel selectedFilesPanel, deleteFilePanel;
    private HashMap<String, JLabel> labels = new HashMap<>();
    private JButton comprimirArchivoButton;
    private Color color;
    private Window window;
    private JOptionPane mensajeFicheros;
    private boolean reemplazarArchivo = false;
    private boolean reemplazarArchivos = false;
    private int totalArchivos;

    public FilesPanel(Window window) {
        this.window = window;
        this.color = Color.white;
        initComponents();
    }

    private void initComponents() {
        totalArchivos = 0;
        comprimirArchivoButton = new JButton("Comprimir archivo");
        comprimirArchivoButton.setVisible(false);
        JPanel wrapperFiles = new JPanel();
        wrapperFiles.setLayout(new FlowLayout());
        this.setLayout(new BorderLayout());
        selectedFilesPanel = new JPanel();
        deleteFilePanel = new JPanel();

        deleteFilePanel.setLayout(new FlowLayout());
        inicializarJOptionPane();

        ImageIcon trashIcon = new ImageIcon(Constantes.PATH_TRASH_ICON);
        JLabel label = new JLabel(trashIcon, JLabel.CENTER);
        deleteFilePanel.add(label);

        JScrollPane selectedFilesScrollPane = new JScrollPane(selectedFilesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        deleteFilePanel.setBackground(Color.ORANGE);
        deleteFilePanel.setBorder(Constantes.BORDER_DELETE_FILES_PANEL);
        selectedFilesPanel.setLayout(new BoxLayout(selectedFilesPanel, BoxLayout.PAGE_AXIS));

        selectedFilesScrollPane.setSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);
        selectedFilesScrollPane.setPreferredSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);

        deleteFilePanel.setSize(Constantes.DIM_DELETE_FILES_PANEL);
        deleteFilePanel.setPreferredSize(Constantes.DIM_DELETE_FILES_PANEL);

        new DropTargetListener(deleteFilePanel, this);

        wrapperFiles.setSize(Constantes.DIM_FILES_PANEL);
        wrapperFiles.setBackground(color);
        wrapperFiles.add(selectedFilesScrollPane);
        wrapperFiles.add(deleteFilePanel);

        this.add(wrapperFiles, BorderLayout.CENTER);
        this.add(comprimirArchivoButton, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void setSelectedFiles(File[] selectedFiles){
        int archivosRepetidos = contarArchivosRepetidos(selectedFiles);
        totalArchivos = totalArchivos - archivosRepetidos;
        for(File file : selectedFiles){
            FileLabel label = new FileLabel(file);
            if (labels.get(label.fileLabel.getText()) != null && !reemplazarArchivos){
                Object[] botones;
                if (archivosRepetidos > 1){
                    botones = new Object[]{Constantes.BTN_REEMPLAZAR, Constantes.BTN_REEMPLAZAR_TODO, Constantes.BTN_CANCELAR};
                    Constantes.BTN_REEMPLAZAR_TODO.setText("Reemplazar todo (" + (archivosRepetidos - 1) +" más)");
                } else {
                    botones = new Object[]{Constantes.BTN_REEMPLAZAR, Constantes.BTN_CANCELAR};
                }
                mensajeFicheros.showOptionDialog(
                        this,
                        "El fichero " + label.getFileName() + " ya existe. Desea reemplazarlo?",
                        "Mensaje",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        botones,
                        Constantes.BTN_REEMPLAZAR);
                if (reemplazarArchivo){
                    archivosRepetidos--;
                    removeFile(label.getFileName(), false);
                    labels.put(label.getFileName(), label.fileLabel);
                    selectedFilesPanel.add(label.fileLabel);
                    totalArchivos++;
                }
            } else {
                if (labels.get(label.fileLabel.getText()) != null){
                    removeFile(label.getFileName(), false);
                    archivosRepetidos--;
                }
                totalArchivos++;
                labels.put(label.getFileName(), label.fileLabel);
                selectedFilesPanel.add(label.fileLabel);
            }
        }
        selectedFilesPanel.repaint();
        this.repaint();
        reemplazarArchivos = false;
        reemplazarArchivo = false;
        if (selectedFiles.length > 1){
            comprimirArchivoButton.setText("Comprimir archivos (" + totalArchivos + " archivos)");
        }
        comprimirArchivoButton.setVisible(true);
        window.repaintOuterPanel();
    }

    public void removeFile(String fileName, boolean repaint){
        selectedFilesPanel.remove(labels.get(fileName));
        labels.remove(fileName);
        if (repaint){
            selectedFilesPanel.repaint();
        }
    }

    private void inicializarJOptionPane(){
        mensajeFicheros = new JOptionPane();
        addActionListenerBotonesJOptionPane();
    }

    private void addActionListenerBotonesJOptionPane(){
        Constantes.BTN_CANCELAR.addActionListener(e -> removeDialog(Constantes.BTN_CANCELAR));
        Constantes.BTN_REEMPLAZAR.addActionListener(e -> {
            reemplazarArchivo = true;
            removeDialog(Constantes.BTN_REEMPLAZAR);
        });
        Constantes.BTN_REEMPLAZAR_TODO.addActionListener(e -> {
            reemplazarArchivos = true;
            removeDialog(Constantes.BTN_REEMPLAZAR_TODO);
        });
    }

    private void removeDialog(JButton button){
        JDialog w = (JDialog) SwingUtilities.getWindowAncestor(button);
        if (w != null) {
            w.setVisible(false);
        }
    }

    private int contarArchivosRepetidos(File[] files){
        int archivosRepetidos = 0;
        for (File file : files){
            FileLabel label = new FileLabel(file);
            if (labels.get(label.fileLabel.getText()) != null){
                archivosRepetidos++;
            }
        }
        return archivosRepetidos;
    }
}
