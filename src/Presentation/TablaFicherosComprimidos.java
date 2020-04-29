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
 * Componente gráfico de la tabla de archivos comprimido.
 */
public class TablaFicherosComprimidos {

    private JTable table;
    private JScrollPane panel;
    private Controller controller;

    /**
     * Constructor vacio que crea una tabla con el modelo de nuevos archivos por comprimir, y se diferencia del otro modelo
     * debido a que se pintan las filas con negrita
     */
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

    /**
     * Constructor que crea una tabla con el modelo de todos los archivos por comprimir, y se diferencia del otro modelo
     * debido a que sólo se pintan las filas de los archivos nuevos en negrita
     * @param controller
     */
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
     * Método que añade una fila al modelo de las tablas
     * @param data nueva fila
     */
    public void addRow(Object [] data){
        // Se añade a ambas tablas ya que una es con los archivos nuevos y la otra es con todos los archivos
        Constantes.tableModelTotalArchivos.addRowToModel(data);
        Constantes.tableModelNewArchivos.addRowToModel(data);
    }

    public JTable getTable() {
        return table;
    }

    public JScrollPane getPanel() {
        return panel;
    }
}
