/* Created by andreea on 09/04/2020 */
package Presentation.Utils;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class JLabelTransferable implements Transferable {

    public JLabelTransferable(JLabel label) {
        this.label = label;
    }

    // Implementation of the Transferable interface
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor fl) {
        for (int i = 0; i < flavors.length; i++) {
            if (fl.equals(flavors[i])) {
                return true;
            }
        }

        return false;
    }

    public Object getTransferData(DataFlavor fl) {
        if (!isDataFlavorSupported(fl)) {
            return null;
        }
        if (fl.equals(DataFlavor.stringFlavor)) {
            // String - return the text as a String
            return label.getText() + " (DataFlavor.stringFlavor)";
        } else if (fl.equals(jLabelFlavor)) {
            // The JLabel itself - just return the label.
            return label;
        } else {
            // Plain text - return an InputStream
            try {
                String targetText = label.getText() + " (plain text flavor)";
                int length = targetText.length();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                OutputStreamWriter w = new OutputStreamWriter(os);
                w.write(targetText, 0, length);
                w.flush();
                byte[] bytes = os.toByteArray();
                w.close();
                return new ByteArrayInputStream(bytes);
            } catch (IOException e) {
                return null;
            }
        }
    }

    // A flavor that transfers a copy of the JLabel
    public static final DataFlavor jLabelFlavor = new DataFlavor(JLabel.class,
            "Swing JLabel");

    private JLabel label; // The label being transferred

    private static final DataFlavor[] flavors = new DataFlavor[] {
            DataFlavor.stringFlavor,
            new DataFlavor("text/plain; charset=ascii", "ASCII text"),
            jLabelFlavor };
}



