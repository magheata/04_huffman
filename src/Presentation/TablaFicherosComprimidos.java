/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation;

import Utils.Constantes;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 *
 */
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
        ArrayList<File> files = listFilesForFolder(new File("compressed/"));
        if (files.size() > 0){
            for (File fileEntry: files) {

                System.out.println(fileEntry.getName());
            }
        }
    }

    /**
     *
     * @param data
     */
    public void addRow(Object [] data){
        Constantes.tableModel.addRowToModel(data);
    }

    public JTable getTable() {
        return table;
    }

    public ArrayList<File> listFilesForFolder(final File folder) {
        ArrayList<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            files.add(fileEntry);
        }
        return files;
    }

}
