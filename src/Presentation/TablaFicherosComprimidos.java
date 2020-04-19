/* Created by andreea on 16/04/2020 */
package Presentation;

import Utils.Constantes;

import javax.swing.*;

public class TablaFicherosComprimidos {
    private JTable table;
    public JScrollPane getPanel() {
        return panel;
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

    public JTable getTable() {
        return table;
    }
}
