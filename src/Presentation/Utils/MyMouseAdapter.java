/* Created by andreea on 09/04/2020 */
package Presentation.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseAdapter extends MouseAdapter {
    private JLabel dragLabel = null;
    private int dragLabelWidthDiv2;
    private int dragLabelHeightDiv2;
    private JLayeredPane selectedFilesPanel;
    private JPanel clickedPanel = null;

    @Override
    public void mousePressed(MouseEvent me) {
        clickedPanel = (JPanel) selectedFilesPanel.getComponentAt(me.getPoint());
        Component[] components = clickedPanel.getComponents();
        if (components.length == 0) {
            return;
        }
        // if we click on jpanel that holds a jlabel
        if (components[0] instanceof JLabel) {
            // remove label from panel
            dragLabel = (JLabel) components[0];
            clickedPanel.remove(dragLabel);
            clickedPanel.revalidate();
            clickedPanel.repaint();

            dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
            dragLabelHeightDiv2 = dragLabel.getHeight() / 2;

            int x = me.getPoint().x - dragLabelWidthDiv2;
            int y = me.getPoint().y - dragLabelHeightDiv2;
            dragLabel.setLocation(x, y);
            selectedFilesPanel.add(dragLabel, JLayeredPane.DRAG_LAYER);
            selectedFilesPanel.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (dragLabel == null) {
            return;
        }
        int x = me.getPoint().x - dragLabelWidthDiv2;
        int y = me.getPoint().y - dragLabelHeightDiv2;
        dragLabel.setLocation(x, y);
        selectedFilesPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    public void setSelectedFilesPanel(JLayeredPane selectedFilesPanel) {
        this.selectedFilesPanel = selectedFilesPanel;
    }
}

