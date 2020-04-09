/* Created by andreea on 08/04/2020 */
package Presentation;

import javax.swing.*;
import java.awt.*;

public class DnDPanel extends JPanel {
    private JTextField texto;

    public DnDPanel() {
        initComponents();
    }

    private void initComponents() {
        texto = new JTextField();
        texto.setText("O arrastrar archivos aquí");
        this.setLayout(new BorderLayout());
        this.add(texto, BorderLayout.CENTER);
        this.setBackground(Color.GRAY);
    }
}
