/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Domain;

import javax.swing.table.DefaultTableModel;

/**
 *
 */
public class TableModel extends DefaultTableModel {

    /**
     *
     * @param columns
     */
    public TableModel(String [] columns){
        super();
        for (String column : columns){
            this.addColumn(column);
        }
    }

    /**
     *
     * @param data
     */
    public void addRowToModel(Object [] data){
        this.addRow(data);
    }
}
