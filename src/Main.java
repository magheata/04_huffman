/* Created by andreea on 08/04/2020 */

import Application.Controller;
import Application.FileDrop;
import Presentation.Window;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();
        Window window = new Window(controller);
        new FileDrop(controller, null, window.getDragDropComponent(),
                BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(46, 196, 182)),
                true,
                files -> { });
        window.setVisible(true);
        window.pack();
    }
}
