/* Created by andreea on 09/04/2020 */
package Presentation;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

public class FileLabel extends DraggableComponent implements Serializable {

    private File file;

    public String getFileName() {
        return fileName;
    }

    private String fileName;
    private Icon fileIcon;
    public JLabel fileLabel;

    public FileLabel(File file) {
        super();
        this.file = file;
        this.fileName = file.getName();
        this.fileIcon = FileSystemView.getFileSystemView().getSystemIcon(file);
        this.fileLabel = new JLabel(fileName, getImageIcon(), JLabel.LEFT);
        this.fileLabel.setName(fileName);
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
}
