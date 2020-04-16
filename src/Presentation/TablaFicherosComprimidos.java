/* Created by andreea on 16/04/2020 */
package Presentation;

import Utils.Constantes;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;

public class TablaFicherosComprimidos {

    public JTable getTable() {
        return table;
    }

    private JTable table;
    public JScrollPane getPanel() {
        return panel;
    }

    public void setPanel(JScrollPane panel) {
        this.panel = panel;
    }

    private JScrollPane panel;

    public TablaFicherosComprimidos() {
        table = new JTable(Constantes.tableModel);
        table.setFillsViewportHeight(true);
        panel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public void addRow(Object [] data){
        Constantes.tableModel.addRowToModel(data);
    }
}
