/* Created by andreea on 09/04/2020 */
package Presentation.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

public class FileLabel implements Serializable, DragGestureListener, DragSourceListener {

    public File file;
    private String fileName;
    private Icon fileIcon;
    public JLabel fileLabel;
    private DragSource dragSource;
    private JLabel label;

    public FileLabel(File file) {
        super();
        dragSource = new DragSource();
        this.file = file;
        this.fileName = file.getPath();
        this.fileIcon = FileSystemView.getFileSystemView().getSystemIcon(file);
        this.fileLabel = new JLabel(fileName, getImageIcon(), JLabel.LEFT);
        this.fileLabel.setName(fileName);
        this.fileLabel.setText(file.getName());
        setLabel(fileLabel);
    }

    protected ImageIcon getImageIcon() {
        ImageIcon icon = new ImageIcon(file.getPath(), fileName);
        return new ImageIcon(getScaledImage(
                icon.getImage(),
                10,
                10));
    }

    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public String getFileName() {
        return fileName;
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
