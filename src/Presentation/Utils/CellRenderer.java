/* Created by andreea on 25/04/2020 */
package Presentation.Utils;

import Application.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Clase que contiene el Renderer de las celdas de la tabla
 */
public class CellRenderer extends DefaultTableCellRenderer {

    private Controller controller;
    /* Sirve para saber si se trata de una tabla de archivos nuevos o no */
    private boolean newTableModel;

    public CellRenderer(Controller controller) {
        this.controller = controller;
        this.newTableModel = false;
    }

    public CellRenderer() {
        this.newTableModel = true;
    }


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String file = (String) table.getValueAt(row, 0);
        // Si es tabla con archivos nuevos se pone el texto en negrita
        if (newTableModel){
            this.setValue(table.getValueAt(row, column));
            this.setFont(this.getFont().deriveFont(Font.BOLD));
        } else {
            // Si no, se comprueba si el archivo que se encuentra en la fila es un archivo nuevo
            boolean isNew = controller.isFileNew(file.split("\\.")[0]);
            if (isNew){
                // Si lo es se pone el texto en negrita
                this.setValue(table.getValueAt(row, column));
                this.setFont(this.getFont().deriveFont(Font.BOLD));
            }
        }
        return this;
    }
}
