/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation.Panels;

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
 * Panel que contiene los elementos para la compresión de un archivo:
 * 1. El selector de archivos
 * 2. La región en la que se pueden arrastrar los archivos que se desean comprimir
 * 3. Una tabla que muestra los archivos comprimidos
 */
public class FilesPanel extends JPanel {

    private JPanel selectedFilesPanel, deleteFilePanel, comprimirFicherosPanel;
    private TablaFicherosComprimidos archivosComprimidos;
    private HashMap<File, JLabel> labels = new HashMap<>();
    private JProgressBar progressBar;
    private HighlightButton comprimirArchivoButton;
    private Color color;
    private Presentation.Window window;
    private JOptionPane mensajeFicheros;
    private boolean reemplazarArchivo = false;
    private boolean reemplazarArchivos = false;
    private int totalArchivos = 0;
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
        this.setLayout(new BorderLayout());
        progressBar = new JProgressBar();
        archivosComprimidos = new TablaFicherosComprimidos();
        archivosComprimidos.getPanel().setVisible(false);
        // Inicializamos diálogo de mensaje de "archivos repetidos"
        inicializarJOptionPane();
        // Inicializamos el panel que posibilita eliminar un archivo seleccionado
        initDeleteFilePanel();
        // Inicializamos el panel que contendrá la lista de archivos seleccionados para comprimir
        initSelectedFilesPanel();
        // Inicializamos el botón de comprimir
        initComprimirArchivosButton();
        // Inicializamos el panel que contiene la tabla de archivos comprimidos
        initComprimirFicherosPanel();
        this.add(comprimirFicherosPanel, BorderLayout.NORTH);
        this.add(archivosComprimidos.getPanel(), BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void initSelectedFilesPanel(){
        selectedFilesPanel = new JPanel();
        selectedFilesPanel.setLayout(new BoxLayout(selectedFilesPanel, BoxLayout.PAGE_AXIS));
    }

    private void initComprimirFicherosPanel(){
        JScrollPane selectedFilesScrollPane = new JScrollPane(selectedFilesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        selectedFilesScrollPane.setSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);
        selectedFilesScrollPane.setPreferredSize(Constantes.DIM_SELECTED_FILES_SCROLL_PANEL);

        JPanel wrapperFiles = new JPanel();
        wrapperFiles.setLayout(new FlowLayout());
        wrapperFiles.setSize(Constantes.DIM_FILES_PANEL);
        wrapperFiles.setBackground(color);
        wrapperFiles.add(selectedFilesScrollPane);
        wrapperFiles.add(deleteFilePanel);

        comprimirFicherosPanel = new JPanel();
        comprimirFicherosPanel.setLayout(new BorderLayout());
        comprimirFicherosPanel.add(wrapperFiles, BorderLayout.NORTH);
        comprimirFicherosPanel.add(comprimirArchivoButton, BorderLayout.CENTER);
    }

    private void initComprimirArchivosButton(){
        comprimirArchivoButton = new HighlightButton(Constantes.TEXT_COMPRIMIR_BUTTON);
        comprimirArchivoButton.addActionListener(e -> {
            // Se comprime la lista de archivos seleccionados
            controller.comprimirFicheros(labels.keySet());
            comprimirArchivoButton.setVisible(false);
        });

        comprimirArchivoButton.setHighlight(new Color(231, 29, 54, 64));
        comprimirArchivoButton.setFocusPainted(false);
        comprimirArchivoButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        comprimirArchivoButton.setVisible(false);
    }

    private void initDeleteFilePanel(){
        // Seteamos que aparezca el texto tooltip después de 1 milisegundo
        ToolTipManager.sharedInstance().setInitialDelay(1);
        deleteFilePanel = new JPanel();
        deleteFilePanel.setLayout(new FlowLayout());

        ImageIcon trashIcon = new ImageIcon(Constantes.PATH_TRASH_ICON);
        JLabel label = new JLabel(trashIcon, JLabel.CENTER);

        deleteFilePanel.add(label);
        deleteFilePanel.setToolTipText(Constantes.TEXT_DELETE_FILE_TOOLTIP);
        deleteFilePanel.setBackground(new Color(231, 29, 54));
        deleteFilePanel.setBorder(Constantes.BORDER_DELETE_FILES_PANEL);
        deleteFilePanel.setSize(Constantes.DIM_DELETE_FILES_PANEL);
        deleteFilePanel.setPreferredSize(Constantes.DIM_DELETE_FILES_PANEL);

        /* Inicializamos el panel como DropTargetListener, es decir, que espera a que se arrastren archivos encima */
        new DropTargetListener(deleteFilePanel, controller);
    }

    /**
     * Método que añade un nuevo archivo a la tabla de archivos comprimidos
     * @param file archivo comprimido
     * @param totalBytesOriginales total de bits que ocupaba el archivo original
     * @param totalBytesComprimidos total de bits que ocupa el archivo comprimido
     */
    public void addArchivosPorComprimirAPanel(File file, int totalBytesOriginales, int totalBytesComprimidos) {
        // En el caso de que sea el primer archivo comprimido mostramos la tabla
        if(!archivosComprimidos.getPanel().isVisible()){
            archivosComprimidos.getPanel().setVisible(true);
        }
        // Añadimos una nueva fila a la tabla
        archivosComprimidos.addRow(new Object[]{file.getName(), totalBytesOriginales + " bits", totalBytesComprimidos + " bits",
                controller.getPercentageCompression(totalBytesOriginales, totalBytesComprimidos)});
        // Eliminamos el archivo del panel de archivos seleccionados por comprimir
        removeFile(file);
        this.revalidate();
    }

    /**
     * Método que setea los archivos seleccionados para comprimir. Los ficheros vienen en lista. Se comprueba si hay
     * ficheros repetidos, y en caso de que haya se reemplazan o no según lo que elija el usuario. La lista de ficheros
     * es incremental, por lo que se pueden añadir todos los ficheros que se desee.
     * @param selectedFiles lista de ficheros seleccionados
     */
    public void setSelectedFiles(File[] selectedFiles){
        // Se calcula el número de archivos seleccionados
        int archivosRepetidos = contarArchivosRepetidos(selectedFiles);

        // Se calcula el número total de archivos que hay hasta ahora por comprimir
        totalArchivos = totalArchivos + (selectedFiles.length - archivosRepetidos);

        // Para cada fichero seleccionado se crea su JLabel
        for(File file : selectedFiles){
            FileLabel label = new FileLabel(file);
            // Si existe ya este archivo en la lista y no se quieren reemplazar archivos
            if (labels.get(label.file) != null && !reemplazarArchivos){
                Object[] botones;

                // Si hay más de un archivo repetido el botón contendrá el total de ficheros repetidos
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

                // Si se quiere reemplazar un sólo archivo se reemplaza
                if (reemplazarArchivo){
                    replaceFile(label.file, label.file, label.fileLabel);
                    // Se decrementa el total de archivos repetidos
                    archivosRepetidos--;
                }
                // Si se desea reemplazar todos los archivos se reemplazan
            } else if (reemplazarArchivos){
                if (labels.get(label.file) != null){
                    replaceFile(label.file, label.file, label.fileLabel);
                    archivosRepetidos--;
                }
                // Si no existe ya este archivo se añade al panel de archivos seleccionados
            } else {
                selectedFilesPanel.add(label.fileLabel);
                labels.put(file, label.fileLabel);
            }
        }

        // Se resetean las variables de control de reemplazo de archivos
        reemplazarArchivos = false;
        reemplazarArchivo = false;
        if (totalArchivos > 1){
            comprimirArchivoButton.setText("Comprimir archivos (" + totalArchivos + " archivos)");
        }
        comprimirArchivoButton.setVisible(true);
        window.repaintOuterPanel();
    }

    /**
     * Método que sirve para eliminar un archivo de la lista de archivos seleccionados para comprimir
     * @param file archivo que se quiere comprimir
     */
    public void removeFile(File file){
        // Se elimina del panel de archivos seleccionados
        selectedFilesPanel.remove(labels.get(file));
        labels.remove(file);
        totalArchivos--;
        // Si no quedan archivos se esconde el botón
        if (totalArchivos == 0){
            comprimirArchivoButton.setVisible(false);
        // Si quedan más archivos se pone el texto del botón según el total de archivos restantes
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
     * Método que setea los actionListeners de los botones del diálogo cuando hay archivos repetidos
     */
    private void addActionListenerBotonesJOptionPane(){
        Constantes.BTN_CANCELAR.addActionListener(e -> removeDialog(Constantes.BTN_CANCELAR));
        // Botón de reemplazar un sólo archivo
        Constantes.BTN_REEMPLAZAR.addActionListener(e -> {
            reemplazarArchivo = true;
            removeDialog(Constantes.BTN_REEMPLAZAR);
        });
        // Botón de reemplazar todos los archivos
        Constantes.BTN_REEMPLAZAR_TODO.addActionListener(e -> {
            reemplazarArchivos = true;
            removeDialog(Constantes.BTN_REEMPLAZAR_TODO);
        });
    }

    /**
     * Método que elimina el dialogo de la ventana
     * @param button botón que se ha pulsado del JDialog
     */
    private void removeDialog(JButton button){
        JDialog w = (JDialog) SwingUtilities.getWindowAncestor(button);
        if (w != null) {
            w.setVisible(false);
        }
    }

    /**
     * Método que retorna el número de archivos repetidos de la nueva lista de archivos seleccionados
     * @param files archivos seleccionados
     * @return total de archivos repetidos
     */
    private int contarArchivosRepetidos(File[] files){
        int archivosRepetidos = 0;
        for (File file : files){
            // Si ya existe un labek para este archivo es que está repetido
            if (labels.get(file) != null){
                archivosRepetidos++;
            }
        }
        return archivosRepetidos;
    }

    /**
     * Método que reemplaza el archivo antiguo por el nuevo
     * @param oldFile fichero antiguo
     * @param newFile fichero nuevo
     * @param newLabel nuevo label para el archivo
     */
    private void replaceFile(File oldFile, File newFile, JLabel newLabel){
        // Eliminamos el fichero del panel de fichero seleccionados
        selectedFilesPanel.remove(labels.get(oldFile));
        // Eliminamos la constancia del fichero de labels
        labels.remove(oldFile);
        // Añadimos el nuevo fichero con su nuevo label
        labels.put(newFile, newLabel);
        // Añadimos el label del fichero al panel
        selectedFilesPanel.add(newLabel);
    }

    /**
     * Método que sirve para reemplazar el botón de comprimir por el JProgressBar. Se llama cuando se pulsa
     * el botón de comprimir.
     */
    public void replaceComprimirButton() {
        comprimirFicherosPanel.remove(comprimirArchivoButton);
        comprimirFicherosPanel.add(progressBar);
        comprimirFicherosPanel.revalidate();
        progressBar.setIndeterminate(true);
    }

    /**
     * Método que sirve para reemplazar la JProgressBar por el botón de comprimir cuando se acaban de comprimir todos los
     * ficheros.
     */
    public void replaceProgressBar(){
        comprimirFicherosPanel.remove(progressBar);
        comprimirFicherosPanel.add(comprimirArchivoButton);
        progressBar.setIndeterminate(false);
        comprimirArchivoButton.setVisible(false);
        comprimirFicherosPanel.revalidate();
    }
}
