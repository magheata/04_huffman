/* Created by andreea on 09/04/2020 */
package Presentation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class FilesPanel extends JPanel {

    private JPanel deleteFilePanel;
    private JPanel selectedFilesPanel;
    private HashMap<String, JLabel> labels = new HashMap<>();

    private Color color;
    private JButton button;
    private Window window;

    public FilesPanel(Window window) {
        this.window = window;
        this.color = Color.white;
        initComponents();
    }

    private void initComponents() {
        button = new JButton();
        selectedFilesPanel = new JPanel();
        deleteFilePanel = new JPanel();
        deleteFilePanel.setBackground(Color.ORANGE);
        selectedFilesPanel.setLayout(new BoxLayout(selectedFilesPanel, BoxLayout.PAGE_AXIS));

        new DropTargetListener(deleteFilePanel, this);

        button.setText("Bla");
        this.setSize(new Dimension(450, 900));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setPreferredSize(new Dimension(450, 150));
        this.setLayout(new GridBagLayout());
        this.setBackground(color);
        this.add(selectedFilesPanel);
        this.add(deleteFilePanel);
        this.setVisible(true);
    }

    public void setSelectedFiles(File[] selectedFiles){
        for(File file : selectedFiles){
            FileLabel label = new FileLabel(file);
            selectedFilesPanel.add(label.fileLabel);
            labels.put(label.getFileName(), label.fileLabel);
        }
        window.repaintOuterPanel();
    }

    public void removeFile(String fileName){
        JLabel labelToRemove = labels.get(fileName);
        selectedFilesPanel.remove(labelToRemove);
    }
}
