/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */

package Presentation;

import Application.Controller;
import Presentation.Panels.*;
import Utils.Constantes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 */
public class Window extends JFrame {

    private DnDPanel dragDropComponent;
    private FileChooserPanel fileChooserPanelComponent;
    public FilesPanel filesPanel;
    public DecompressPanel decompressPanel;
    private IntroductionPanel introductionPanel;
    private Controller controller;
    private JPanel outerPanel;

    public Window(Controller controller) {
        super(Constantes.TITLE_WINDOW);
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

        decompressPanel = new DecompressPanel(controller);
        decompressPanel.setVisible(true);
        controller.setDecompressPanel(decompressPanel);

        introductionPanel = new IntroductionPanel();
        introductionPanel.setVisible(true);

        CompressionInformationPanel panelArchivosComprimidos = new CompressionInformationPanel(controller);
        panelArchivosComprimidos.setVisible(true);

        JScrollPane introductionScrollPane = new JScrollPane(introductionPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tabbedPane.addTab(Constantes.TITLE_INFO_TABBED_PANE, introductionScrollPane);
        tabbedPane.addTab(Constantes.TITLE_COMPRIMIR_TABBED_PANE, outerPanel);
        tabbedPane.addTab(Constantes.TITLE_DESCOMPRIMIR_TABBED_PANE, decompressPanel);
        tabbedPane.addTab(Constantes.TITLE_FICHEROS_COMPRIMIDOS_TABBED_PANE, panelArchivosComprimidos);

        this.getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                controller.resizePanels(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
        this.setPreferredSize(Constantes.DIM_WINDOW);
        this.setLayout(new BorderLayout());
        this.setResizable(true);
        this.setMinimumSize(Constantes.DIM_WINDOW);
        this.add(tabbedPane);
    }

    /**
     *
     * @return
     */
    public DnDPanel getDragDropComponent(){
        return this.dragDropComponent;
    }

    /**
     *
     */
    public void repaintOuterPanel(){
        outerPanel.revalidate();
    }
}
