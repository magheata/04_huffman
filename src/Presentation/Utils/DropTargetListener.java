/* Created by andreea on 09/04/2020 */
package Presentation.Utils;

import Presentation.Panels.FilesPanel;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;

import static Presentation.Utils.JLabelTransferable.jLabelFlavor;

public class DropTargetListener extends DropTargetAdapter {

    private final FilesPanel parentComponent;

    public DropTargetListener(JPanel panel, FilesPanel parentComponent) {
        this.parentComponent = parentComponent;
        new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
    }

    @Override
    public void drop(DropTargetDropEvent evt) {
        try {
            Transferable tr = evt.getTransferable();
            evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE);
            // Get a useful list
            JLabel fileList = (JLabel) tr.getTransferData(jLabelFlavor);
            parentComponent.removeFile(new File(fileList.getName()));
            parentComponent.revalidate();
            // Mark that drop is completed.
            evt.getDropTargetContext().dropComplete(true);
        } catch (Exception e) {
            e.printStackTrace();
            evt.rejectDrop();
        }
    }
}
