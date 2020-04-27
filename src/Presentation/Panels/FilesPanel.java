/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation.Panels;

import Presentation.Utils.CellRenderer;
import Presentation.Utils.HighlightButton;
import Presentation.TablaFicherosComprimidos;
import Presentation.Utils.DropTargetListener;
import Application.Controller;

import Presentation.Utils.FileLabel;
import Presentation.Window;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

/**
 *
 */
public class FilesPanel extends JPanel {

    private JPanel selectedFilesPanel, deleteFilePanel, comprimirFicherosPanel;
    private TablaFicherosComprimidos archivosComprimidos;
    private HashMap<File, JLabel> labels = new HashMap<>();
    private HashMap<String, File> files = new HashMap<>();
    private JProgressBar progressBar;
    private HighlightButton comprimirArchivoButton;
    private Color color;
    private Presentation.Window window;
    private JOptionPane mensajeFicheros;
    private boolean reemplazarArchivo = false;
    private boolean reemplazarArchivos = false;
    private int totalArchivos;
    private Controller controller;

    public FilesPanel(Window window, Controller controller) {
        this.window = window;
        this.controller = controller;
        this.color = Color.white;
        initComponents();
    }

    /**
     *
     */
    private void initComponents() {
        ToolTipManager.sharedInstance().setInitialDelay(1);
        controller.setFiles(files);
        progressBar = new JProgressBar();

        this.setLayout(new BorderLayout());
        archivosComprimidos = new TablaFicherosComprimidos();
        archivosComprimidos.getPanel().setVisible(false);
        totalArchivos = 0;
        comprimirArchivoButton = new HighlightButton("Comprimir archivo");
        comprimirArchivoButton.addActionListener(e -> {
            controller.comprimirFicheros(labels.keySet());
            comprimirArchivoButton.setVisible(false);
        });

        comprimirArchivoButton.setHighlight(new Color(231, 29, 54, 64));
        comprimirArchivoButton.setFocusPainted(false);
        comprimirArchivoButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        comprimirArchivoButton.setVisible(false);

        JPanel wrapperFiles = new JPanel();
        wrapperFiles.setLayout(new FlowLayout());

        selectedFilesPanel = new JPanel();
        deleteFilePanel = new JPanel();

        deleteFilePanel.setLayout(new FlowLayout());
        inicializarJOptionPane();

        ImageIcon trashIcon = new ImageIcon(Constantes.PATH_TRASH_ICON);
        JLabel label = new JLabel(trashIcon, JLabel.CENTER);
        deleteFilePanel.add(label);
        deleteFilePanel.setToolTipText("Arrastrar archivo aquí para eliminar");
        JScrollPane selectedFilesScrollPane = new JScrollPane(selectedFilesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        deleteFilePanel.setBackground(new Color(231, 29, 54));
        deleteFilePanel.setBorder(Constantes.BORDER_DELETE_FILES_PANEL);
        selectedFilesPanel.setLayout(new BoxLayout(selectedFilesPanel, BoxLayout.PAGE_AXIS));

        selectedFilesScrollPane.setSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);
        selectedFilesScrollPane.setPreferredSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);

        deleteFilePanel.setSize(Constantes.DIM_DELETE_FILES_PANEL);
        deleteFilePanel.setPreferredSize(Constantes.DIM_DELETE_FILES_PANEL);

        new DropTargetListener(deleteFilePanel, controller);

        wrapperFiles.setSize(Constantes.DIM_FILES_PANEL);
        wrapperFiles.setBackground(color);
        wrapperFiles.add(selectedFilesScrollPane);
        wrapperFiles.add(deleteFilePanel);

        comprimirFicherosPanel = new JPanel();
        comprimirFicherosPanel.setLayout(new BorderLayout());
        comprimirFicherosPanel.add(wrapperFiles, BorderLayout.NORTH);
        comprimirFicherosPanel.add(comprimirArchivoButton, BorderLayout.CENTER);

        this.add(comprimirFicherosPanel, BorderLayout.NORTH);
        this.add(archivosComprimidos.getPanel(), BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     *
     * @param file
     * @param totalBytesOriginales
     * @param totalBytesComprimidos
     */
    public void addArchivosPorComprimirAPanel(File file, int totalBytesOriginales, int totalBytesComprimidos) {
        if(!archivosComprimidos.getPanel().isVisible()){
            archivosComprimidos.getPanel().setVisible(true);
        }
        archivosComprimidos.addRow(new Object[]{file.getName(), totalBytesOriginales + " bytes", totalBytesComprimidos + " bytes",
                controller.getPercentageCompression(totalBytesOriginales, totalBytesComprimidos)});
        removeFile(file);
        this.revalidate();
    }

    /**
     *
     * @param selectedFiles
     */
    public void setSelectedFiles(File[] selectedFiles){
        int archivosRepetidos = contarArchivosRepetidos(selectedFiles);
        totalArchivos = totalArchivos + (selectedFiles.length - archivosRepetidos);

        for(File file : selectedFiles){
            FileLabel label = new FileLabel(file);
            if (labels.get(label.file) != null && !reemplazarArchivos){
                Object[] botones;

                //region JOPTIONPANE
                if (archivosRepetidos > 1){
                    botones = new Object[]{Constantes.BTN_REEMPLAZAR, Constantes.BTN_REEMPLAZAR_TODO, Constantes.BTN_CANCELAR};
                    Constantes.BTN_REEMPLAZAR_TODO.setText("Reemplazar todo (" + (archivosRepetidos - 1) +" más)");
                } else {
                    botones = new Object[]{Constantes.BTN_REEMPLAZAR, Constantes.BTN_CANCELAR};
                }

                String[] fileName = label.getFileName().split("/");
                mensajeFicheros.showOptionDialog(
                        this,
                        "El fichero " + fileName[fileName.length - 1] + " ya existe. Desea reemplazarlo?",
                        "Mensaje",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        botones,
                        Constantes.BTN_REEMPLAZAR);
                //endregion

                if (reemplazarArchivo){
                    replaceFile(label.file, label.file, label.fileLabel);
                    archivosRepetidos--;
                }
            } else if (reemplazarArchivos){
                if (labels.get(label.file) != null){
                    replaceFile(label.file, label.file, label.fileLabel);
                    archivosRepetidos--;
                }
            } else {
                selectedFilesPanel.add(label.fileLabel);
                labels.put(file, label.fileLabel);
                files.put(file.getName(), file);
            }
        }
        reemplazarArchivos = false;
        reemplazarArchivo = false;
        if (totalArchivos > 1){
            comprimirArchivoButton.setText("Comprimir archivos (" + totalArchivos + " archivos)");
        }
        comprimirArchivoButton.setVisible(true);
        window.repaintOuterPanel();
    }

    /**
     *
     * @param file
     */
    public void removeFile(File file){
        selectedFilesPanel.remove(labels.get(file));
        labels.remove(file);
        totalArchivos--;
        if (totalArchivos == 0){
            comprimirArchivoButton.setVisible(false);
        } else if (totalArchivos == 1){
            comprimirArchivoButton.setText("Comprimir archivo");
        } else {
            comprimirArchivoButton.setText("Comprimir archivos (" + totalArchivos + " archivos)");
        }
        selectedFilesPanel.repaint();
        this.revalidate();
    }

    /**
     *
     */
    private void inicializarJOptionPane(){
        mensajeFicheros = new JOptionPane();
        addActionListenerBotonesJOptionPane();
    }

    /**
     *
     */
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

    /**
     *
     * @param button
     */
    private void removeDialog(JButton button){
        JDialog w = (JDialog) SwingUtilities.getWindowAncestor(button);
        if (w != null) {
            w.setVisible(false);
        }
    }

    /**
     *
     * @param files
     * @return
     */
    private int contarArchivosRepetidos(File[] files){
        int archivosRepetidos = 0;
        for (File file : files){
            if (labels.get(file) != null){
                archivosRepetidos++;
            }
        }
        return archivosRepetidos;
    }

    /**
     *
     * @param oldFile
     * @param newFile
     * @param newButton
     */
    private void replaceFile(File oldFile, File newFile, JLabel newButton){
        selectedFilesPanel.remove(labels.get(oldFile));
        files.remove(oldFile.getName());
        labels.remove(oldFile);
        labels.put(newFile, newButton);
        files.remove(newFile.getName(), newFile);
        selectedFilesPanel.add(newButton);
    }

    public void replaceComprimirButton() {
        comprimirFicherosPanel.remove(comprimirArchivoButton);
        comprimirFicherosPanel.add(progressBar);
        comprimirFicherosPanel.revalidate();
        progressBar.setIndeterminate(true);
    }

    public void replaceProgressBar(){
        comprimirFicherosPanel.remove(progressBar);
        comprimirFicherosPanel.add(comprimirArchivoButton);
        progressBar.setIndeterminate(false);
        comprimirArchivoButton.setVisible(false);
        comprimirFicherosPanel.revalidate();
    }
}
