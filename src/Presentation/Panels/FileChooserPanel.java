/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation.Panels;

import Application.Controller;
import Presentation.Utils.ImagePreview;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Panel que contiene el selector de ficheros
 */
public class FileChooserPanel extends JPanel {

    private JFileChooser fileChooser;
    private JButton chooseFileButton;
    private Controller controller;

    public FileChooserPanel(Controller controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     *
     */
    private void initComponents() {
        fileChooser = new JFileChooser();
        chooseFileButton = new JButton();
        chooseFileButton.setText(Constantes.TEXT_FILECHOOSER_PANEL);
        fileChooser.setAccessory(new ImagePreview(fileChooser));

        chooseFileButton.addActionListener(e -> {
            switch (fileChooser.showOpenDialog(FileChooserPanel.this))
            {
                case JFileChooser.APPROVE_OPTION:
                    // Si se ha seleccionado más de un archivo se retorna la lista
                    if (fileChooser.getSelectedFiles() != null && fileChooser.getSelectedFiles().length > 1){
                        controller.addFiles(fileChooser.getSelectedFiles());
                    } else {
                        // Si no retorna sólo el fichero seleccionado
                        controller.addFiles(new File[]{fileChooser.getSelectedFile()});
                    }
                    break;
            }
        });
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(1, 35, 63));
        this.add(chooseFileButton, BorderLayout.CENTER);
    }
}
