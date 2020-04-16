package Presentation;/* Created by andreea on 08/04/2020 */

import Application.Controller;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private DnDPanel dragDropComponent;
    private FileChooserPanel fileChooserPanelComponent;
    public FilesPanel filesPanel;
    private Controller controller;

    JPanel outerPanel;
    public Window(Controller controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        //region GridBagConstraints
        GridBagConstraints fileChooserConstraints = new GridBagConstraints();
        fileChooserConstraints.fill = GridBagConstraints.HORIZONTAL;
        fileChooserConstraints.gridwidth = 3;
        fileChooserConstraints.gridheight = 2;
        fileChooserConstraints.gridx = 0;
        fileChooserConstraints.gridy = 0;

        GridBagConstraints dragDropConstraints = new GridBagConstraints();
        dragDropConstraints.fill = GridBagConstraints.HORIZONTAL;
        dragDropConstraints.gridwidth = 3;
        dragDropConstraints.gridheight = 1;
        dragDropConstraints.ipady = 40;      //make this component tall
        dragDropConstraints.gridx = 0;
        dragDropConstraints.gridy = 2;
        dragDropConstraints.weightx = 1;
        //endregion

        dragDropComponent = new DnDPanel();
        fileChooserPanelComponent = new FileChooserPanel(controller);

        filesPanel = new FilesPanel(Window.this, controller);
        controller.setFilesPanel(filesPanel);

        panel.add(fileChooserPanelComponent, fileChooserConstraints);
        panel.add(dragDropComponent, dragDropConstraints);

        outerPanel.add(panel, BorderLayout.NORTH);
        outerPanel.add(filesPanel, BorderLayout.CENTER);

        JScrollPane pane = new JScrollPane(outerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel panelDescomprimir = new JPanel();
        panelDescomprimir.setVisible(true);

        JPanel panelInformacion = new JPanel();
        panelInformacion.setVisible(true);

        JPanel panelArchivosComprimidos = new JPanel();
        panelArchivosComprimidos.setVisible(true);

        tabbedPane.addTab(Constantes.TITLE_INFO_TABBED_PANE, panelInformacion);
        tabbedPane.addTab(Constantes.TITLE_COMPRIMIR_TABBED_PANE, outerPanel);
        tabbedPane.addTab(Constantes.TITLE_DESCOMPRIMIR_TABBED_PANE, panelDescomprimir);
        tabbedPane.addTab(Constantes.TITLE_FICHEROS_COMPRIMIDOS_TABBED_PANE, panelArchivosComprimidos);

        this.setPreferredSize(Constantes.DIM_WINDOW);
        this.setLayout(new BorderLayout());
        this.setResizable(true);
        this.setMinimumSize(Constantes.DIM_WINDOW);
        this.add(tabbedPane);
    }

    public DnDPanel getDragDropComponent(){
        return this.dragDropComponent;
    }

    public void repaintOuterPanel(){
        outerPanel.revalidate();
    }
}
