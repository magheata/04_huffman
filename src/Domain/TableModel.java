/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Domain;

import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa el modelo de una tabla (es decir, la información que contiene la tabla).
 */
public class TableModel extends DefaultTableModel {

    /**
     *
     * @param columns columnas que contendrá la tabla
     */
    public TableModel(String [] columns){
        super();
        for (String column : columns){
            this.addColumn(column);
        }
    }

    /**
     * Método que sirve para añadir una nueva fila a la tabla
     * @param data datos que contiene la fila
     */
    public void addRowToModel(Object [] data){
        this.addRow(data);
    }
}
