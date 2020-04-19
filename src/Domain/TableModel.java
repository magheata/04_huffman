/* Created by andreea on 16/04/2020 */
package Domain;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    public TableModel(String [] columns){
        super();
        for (String column : columns){
            this.addColumn(column);
        }
    }

    public void addRowToModel(Object [] data){
        this.addRow(data);
    }
}
