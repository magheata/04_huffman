/* Created by andreea on 08/04/2020 */

import Application.FileDrop;
import Application.FileReader;
import Presentation.Window;

import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

    private static PrintStream logger;
    public static void main(String[] args) {
        Presentation.Window window = new Window();
        FileOutputStream fileOutputStream = null;
        File file = new File("logger.txt");
        try {
            fileOutputStream = new FileOutputStream(file);
            logger = new PrintStream(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }
                if(logger!=null){
                    logger.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileDrop fileDrop = new FileDrop(logger, window.getDragDropComponent(), new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

            }

            @Override
            public Insets getBorderInsets(Component c) {
                return null;
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        }, true, files -> {

        });
        FileReader fR = new FileReader();
        fileDrop.setMain(fR);

        window.setVisible(true);
        window.pack();
    }
}
