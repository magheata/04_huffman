/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation.Panels;

import Utils.Constantes;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que contiene la región de la ventana en la cual se pueden arrastrar archivos para comprimir
 */
public class DnDPanel extends JPanel {

    private JLabel texto;

    public DnDPanel() {
        initComponents();
    }

    private void initComponents() {
        texto = new JLabel();
        texto.setText(Constantes.TEXT_DND_PANEL);
        this.setBackground(Color.white);
        this.setBorder(Constantes.BORDER_DND_PANEL);
        this.setLayout(new GridBagLayout());
        this.add(texto);
    }
}
