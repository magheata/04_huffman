/* Created by andreea on 08/04/2020 */
package Presentation;

import Application.Controller;
import Presentation.Utils.ImagePreview;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileChooserPanel extends JPanel {

    private JFileChooser fileChooser;
    private JButton chooseFileButton;
    private Controller controller;

    public FileChooserPanel(Controller controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        fileChooser = new JFileChooser();
        chooseFileButton = new JButton();
        chooseFileButton.setText(Constantes.TEXT_FILECHOOSER_PANEL);
        fileChooser.setAccessory(new ImagePreview(fileChooser));

        chooseFileButton.addActionListener(e -> {
            switch (fileChooser.showOpenDialog(FileChooserPanel.this))
            {
                case JFileChooser.APPROVE_OPTION:
                    if (fileChooser.getSelectedFiles() != null && fileChooser.getSelectedFiles().length > 1){
                        controller.addFiles(fileChooser.getSelectedFiles());

                    } else {
                        controller.addFiles(new File[]{fileChooser.getSelectedFile()});
                    }
                    break;

                case JFileChooser.CANCEL_OPTION:
                    break;

                case JFileChooser.ERROR_OPTION:
            }
        });

        this.setLayout(new BorderLayout());
        this.setBackground(Color.ORANGE);
        this.add(chooseFileButton, BorderLayout.CENTER);
    }
}
