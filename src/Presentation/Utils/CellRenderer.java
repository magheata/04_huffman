/* Created by andreea on 25/04/2020 */
package Presentation.Utils;

import Application.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CellRenderer extends DefaultTableCellRenderer {

    private Controller controller;
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
        if (newTableModel){
            this.setValue(table.getValueAt(row, column));
            this.setFont(this.getFont().deriveFont(Font.BOLD));
        } else {
            boolean isNew = controller.isFileNew(file.split("\\.")[0]);
            if (isNew){
                this.setValue(table.getValueAt(row, column));
                this.setFont(this.getFont().deriveFont(Font.BOLD));
            }
        }
        return this;
    }
}
