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
import java.util.Vector;
;


public class DcomPanel extends JPanel {

    private HighlightButton descomprimirArchivoButton;
    private TablaFicherosComprimidos tablaFicherosComprimidos;
    private Controller controller;
    private String nombreArchivoSeleccionado;


    public DcomPanel(Controller controller) {
        this.controller = controller;
        tablaFicherosComprimidos = new TablaFicherosComprimidos(controller);
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        JPanel archivoDescomprimido = new JPanel();
        archivoDescomprimido.setVisible(true);
        archivoDescomprimido.setBackground(Color.ORANGE);
        archivoDescomprimido.setPreferredSize(new Dimension(300, 200));
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
            }
        });
        // Botón que inicia la descompresión
        descomprimirArchivoButton.setHighlight(new Color(231, 29, 54, 64));
        descomprimirArchivoButton.setFocusPainted(false);
        descomprimirArchivoButton.setFont(new Font("Tahoma", Font.BOLD, 12));

        this.add(tablaFicherosComprimidos.getPanel(), BorderLayout.NORTH);

        this.add(descomprimirArchivoButton, BorderLayout.CENTER);
        this.add(archivoDescomprimido, BorderLayout.SOUTH);
        this.setVisible(true);
    }
}
