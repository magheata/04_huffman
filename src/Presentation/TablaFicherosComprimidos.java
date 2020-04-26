/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation;

import Application.Controller;
import Presentation.Utils.CellRenderer;
import Utils.Constantes;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

/**
 *
 */
public class TablaFicherosComprimidos {

    private JTable table;
    public JScrollPane getPanel() {
        return panel;
    }
    private JScrollPane panel;
    private Controller controller;

    public TablaFicherosComprimidos() {
        table = new JTable(Constantes.tableModelNewArchivos);
        table.setFillsViewportHeight(true);
        panel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Enumeration<TableColumn> columns = this.getTable().getColumnModel().getColumns();
        for (Iterator i = columns.asIterator(); i.hasNext();){
            TableColumn column = (TableColumn) i.next();
            column.setCellRenderer(new CellRenderer());
        }
    }

    public TablaFicherosComprimidos(Controller controller) {
        this.controller = controller;
        table = new JTable(Constantes.tableModelTotalArchivos);
        table.setFillsViewportHeight(true);
        panel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Enumeration<TableColumn> columns = this.getTable().getColumnModel().getColumns();
        for (Iterator i = columns.asIterator(); i.hasNext();){
            TableColumn column = (TableColumn) i.next();
            column.setCellRenderer(new CellRenderer(controller));
        }
    }

    /**
     *
     * @param data
     */
    public void addRow(Object [] data){
        Constantes.tableModelTotalArchivos.addRowToModel(data);
        Constantes.tableModelNewArchivos.addRowToModel(data);
    }

    public JTable getTable() {
        return table;
    }

}
