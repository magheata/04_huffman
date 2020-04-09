/* Created by andreea on 09/04/2020 */
package Presentation;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;

public class DraggableComponent implements DragGestureListener, DragSourceListener {

    private DragSource dragSource;
    private JLabel label;

    public DraggableComponent() {
        dragSource = new DragSource();
    }

    public void dragGestureRecognized(DragGestureEvent evt) {
        Transferable t = new JLabelTransferable(label);
        evt.startDrag(null, t, this);
    }

    //region UNUSED DRAG METHODS
    public void dragEnter(DragSourceDragEvent evt) { }

    public void dragOver(DragSourceDragEvent evt) {}

    public void dragExit(DragSourceEvent evt) {}

    public void dropActionChanged(DragSourceDragEvent evt) {}

    public void dragDropEnd(DragSourceDropEvent evt) { }
    //endregion

    public void setLabel(JLabel label) {
        this.label = label;
        dragSource.createDefaultDragGestureRecognizer(label, DnDConstants.ACTION_COPY_OR_MOVE, this);
    }
}