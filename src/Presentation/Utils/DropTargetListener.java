/* Created by andreea on 09/04/2020 */
package Presentation.Utils;

import Presentation.FilesPanel;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

import static Presentation.Utils.JLabelTransferable.jLabelFlavor;

public class DropTargetListener extends DropTargetAdapter {

    private final FilesPanel parentComponent;
    private final DropTarget dropTarget;
    private final JPanel panel;

    public DropTargetListener(JPanel panel, FilesPanel parentComponent) {
        this.panel = panel;
        this.parentComponent = parentComponent;

        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
    }

    @Override
    public void drop(DropTargetDropEvent evt) {
        try {
            Transferable tr = evt.getTransferable();
            evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE);
            // Get a useful list
            JLabel fileList = (JLabel) tr.getTransferData(jLabelFlavor);
            parentComponent.removeFile(fileList.getName(), true);
            parentComponent.revalidate();
            // Mark that drop is completed.
            evt.getDropTargetContext().dropComplete(true);
        } catch (Exception e) {
            e.printStackTrace();
            evt.rejectDrop();
        }
    }
}
