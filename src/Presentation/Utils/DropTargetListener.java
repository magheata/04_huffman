/* Created by andreea on 09/04/2020 */
package Presentation.Utils;

import Application.Controller;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;

import static Presentation.Utils.JLabelTransferable.jLabelFlavor;

/**
 * Clase qiue implementa la funcionalidad de Drag and Drop
 */
public class DropTargetListener extends DropTargetAdapter {

    private final Controller controller;

    public DropTargetListener(JPanel panel, Controller controller) {
        this.controller = controller;
        new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
    }

    @Override
    public void drop(DropTargetDropEvent evt) {
        try {
            Transferable tr = evt.getTransferable();
            evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE);
            // Get a useful list
            JLabel fileList = (JLabel) tr.getTransferData(jLabelFlavor);
            // Elminar fichero de la lista de archivos seleccionados para comprimir
            controller.deleteFile(new File(fileList.getName()));
            // Mark that drop is completed.
            evt.getDropTargetContext().dropComplete(true);
        } catch (Exception e) {
            e.printStackTrace();
            evt.rejectDrop();
        }
    }
}
