package Presentation;/* Created by andreea on 08/04/2020 */

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private DnD dragDropComponent;
    private FileChooser fileChooserComponent;

    public Window() {
        initComponents();
    }

    private void initComponents() {
        dragDropComponent = new DnD();
        fileChooserComponent = new FileChooser();
        this.setPreferredSize(new Dimension(500, 600));
        this.setLayout(new BorderLayout());
        this.add(fileChooserComponent, BorderLayout.CENTER);
        this.add(dragDropComponent, BorderLayout.SOUTH);
    }

    public DnD getDragDropComponent(){
        return this.dragDropComponent;
    }
}
