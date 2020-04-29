/* Created by andreea on 09/04/2020 */
package Presentation.Panels;

import Presentation.Utils.HighlightButton;
import Presentation.TablaFicherosComprimidos;
import Application.Controller;

import java.io.File;

import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecompressPanel extends JPanel {

    private HighlightButton descomprimirArchivoButton;
    private TablaFicherosComprimidos tablaFicherosComprimidos;
    private Controller controller;
    private String nombreArchivoSeleccionado, extensionArchivo;
    private JPanel archivoDescomprimido, archivoOriginal, wrapperTable;
    private JScrollPane scrollPaneArchivoDescomprimido, scrollPaneArchivoOriginal;
    private JProgressBar progressBar;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();;

    public DecompressPanel(Controller controller) {
        this.controller = controller;
        tablaFicherosComprimidos = new TablaFicherosComprimidos(controller);
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setSize(Constantes.DIM_FILES_PANEL);

        initArchivoDescomprimidoPanel();
        initArchivoOriginalPanel();
        initTabla();
        initBotonDescomprimir();

        progressBar = new JProgressBar();

        wrapperTable = new JPanel();
        wrapperTable.setLayout(new BorderLayout());
        wrapperTable.add(tablaFicherosComprimidos.getPanel(), BorderLayout.NORTH);
        wrapperTable.add(descomprimirArchivoButton, BorderLayout.CENTER);

        JPanel wrapperArchivos = new JPanel();
        wrapperArchivos.setLayout(new BorderLayout());
        wrapperArchivos.setVisible(true);

        wrapperArchivos.add(crearPanelContenido(Constantes.TEXT_ARCHIVO_ORIGINAL_PANEL, scrollPaneArchivoOriginal), BorderLayout.WEST);
        wrapperArchivos.add(crearPanelContenido(Constantes.TEXT_ARCHIVO_DESCOMPRIMIDO_PANEL, scrollPaneArchivoDescomprimido));

        this.add(wrapperTable, BorderLayout.NORTH);
        this.add(wrapperArchivos, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Método que añade el contenido del archivo comprimido al panel
     */
    public void addContentToArchivoDescomprimidoPanel(){
        // Si el fichero es de tipo txt se muestra el contenido
        if (extensionArchivo.equals("txt")){
            executorService.submit(() -> {
                archivoDescomprimido.removeAll();
                StringBuilder sb = controller.readFileContent("decompressed/" + nombreArchivoSeleccionado + "." + extensionArchivo);
                JTextPane fileContent = new JTextPane();
                fileContent.setFocusable(false);
                fileContent.setRequestFocusEnabled(false);
                fileContent.setText(sb.toString());
                archivoDescomprimido.add(fileContent);
                archivoDescomprimido.repaint();
                this.revalidate();
            });
            // si no se abre el fichero descomprimido resultante
        } else {
            try {
                Desktop.getDesktop().open(new File("decompressed/" + nombreArchivoSeleccionado + "." + extensionArchivo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que añade el contenido del archivo original al panel
     */
    public void addContentToArchivoOriginalPanel(){
        // Si el fichero es de tipo txt se muestra el contenido
        if (extensionArchivo.equals("txt")){
            executorService.submit(() -> {
                archivoOriginal.removeAll();
                String pathArchivoOriginal = controller.getPathArchivoOriginal(Constantes.PATH_HUFFMAN_CODES + nombreArchivoSeleccionado + Constantes.EXTENSION_HUFFMAN_CODES);
                StringBuilder sb = controller.readFileContent(pathArchivoOriginal);
                JTextPane fileContent = new JTextPane();
                fileContent.setFocusable(false);
                fileContent.setRequestFocusEnabled(false);
                fileContent.setText(sb.toString());
                archivoOriginal.add(fileContent);
                archivoOriginal.repaint();
                this.revalidate();
            });
        }
    }

    /**
     * Método que sirve para redimensionar los paneles cuando la ventana cambia de tamaño
     * @param width anchura de la ventana
     * @param height altura de la ventana
     */
    public void resizePanels(int width, int height){
        scrollPaneArchivoDescomprimido.setPreferredSize(new Dimension(width / 2, height - 150));
        scrollPaneArchivoOriginal.setPreferredSize(new Dimension(width / 2, height - 150));
        scrollPaneArchivoDescomprimido.setSize(new Dimension(width / 2, height - 150));
        scrollPaneArchivoOriginal.setSize(new Dimension(width / 2, height - 150));
    }

    /**
     * Método que elimina el botón de descomprimir y añade la  barra de progreso a la vista
     */
    public void replaceComprimirButton() {
        wrapperTable.remove(descomprimirArchivoButton);
        wrapperTable.add(progressBar);
        wrapperTable.revalidate();
        progressBar.setIndeterminate(true);
    }

    /**
     * Método que elimina la barra de progreso y añade el botón de descomprimir a la vista
     */
    public void replaceProgressBar(){
        wrapperTable.remove(progressBar);
        wrapperTable.add(descomprimirArchivoButton);
        progressBar.setIndeterminate(false);
        descomprimirArchivoButton.setVisible(true);
        wrapperTable.revalidate();
    }

    /**
     * Método que inicializa el panel que contendrá el contenido del archivo original
     */
    private void initArchivoOriginalPanel(){
        archivoOriginal = new JPanel();
        archivoOriginal.setVisible(true);
        archivoOriginal.setBackground(Color.white);
        scrollPaneArchivoOriginal = createScrollPanel(archivoOriginal);
    }

    /**
     * Método que inicializa el panel que contendrá el contenido del archivo descomprimido
     */
    private void initArchivoDescomprimidoPanel(){
        archivoDescomprimido = new JPanel();
        archivoDescomprimido.setVisible(true);
        archivoDescomprimido.setBackground(Color.white);
        scrollPaneArchivoDescomprimido = createScrollPanel(archivoDescomprimido);
    }

    /**
     * Método que inicializa la tabla de los ficheros comprimidos
     */
    private void initTabla(){
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
    }

    /**
     * Método que inicializa el botón de descomprimir
     */
    private void initBotonDescomprimir(){
        descomprimirArchivoButton = new HighlightButton("Descomprimir archivo");
        descomprimirArchivoButton.addActionListener(e -> {
            // añadimos la barra de progreso a la vista
            replaceComprimirButton();
            // descomprimimos el archivo
            controller.descomprimirFichero(nombreArchivoSeleccionado, new File("compressed/"+ nombreArchivoSeleccionado + "_compressed.txt"));
        });

        // Botón que inicia la descompresión
        descomprimirArchivoButton.setHighlight(new Color(231, 29, 54, 64));
        descomprimirArchivoButton.setFocusPainted(false);
        descomprimirArchivoButton.setFont(new Font("Tahoma", Font.BOLD, 12));
    }

    /**
     * Método que crea un JScrollPane que contiene el panel que se pasa por parámetro
     * @param panel panel que contiene el JScrollPane
     * @return JScrollPane resultante
     */
    private JScrollPane createScrollPanel(JPanel panel){
        JScrollPane scrollPanel = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight() - 150));
        scrollPanel.setSize(new Dimension(this.getWidth() / 2, this.getHeight() - 150));
        return scrollPanel;
    }

    /**
     * Método que crea el panel que contendrá el contenido de un archivo
     * @param labelText nombre del panel
     * @param scrollPane JScrollPane que contiene el panel
     * @return panel resultante
     */
    private JPanel crearPanelContenido(String labelText, JScrollPane scrollPane){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel archivoOriginalLabel = new JLabel(labelText);
        archivoOriginalLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(archivoOriginalLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}