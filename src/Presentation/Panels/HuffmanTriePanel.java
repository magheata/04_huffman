/* Created by Miruna Andreea Gheata & Rafael Adrián Gil Cañestro */
package Presentation.Panels;

import Domain.Node;
import Presentation.Utils.FoldableTree;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;

import javax.swing.*;

/**
 *
 */
public class HuffmanTriePanel extends JPanel {

    private FoldableTree graph;
    private mxGraphComponent graphComponent;

    public HuffmanTriePanel(Node rootHuffman) {
        graph = new FoldableTree();
        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false);
        layout.setMoveTree(false);
        layout.setUseBoundingBox(false);
        layout.setEdgeRouting(false);
        layout.setLevelDistance(30);
        layout.setNodeDistance(10);

        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            createTree(rootHuffman, parent);
            layout.execute(parent);
        }
        finally {
            graph.getModel().endUpdate();
        }

        graph.addListener(mxEvent.FOLD_CELLS, (sender, evt) -> layout.execute(graph.getDefaultParent()));

        graphComponent = new mxGraphComponent(graph);

        this.setSize(400, 320);
        this.setVisible(true);
    }

    /**
     *
     * @param root
     * @param rootNode
     * @return
     */
    private Object createTree(Node root, Object rootNode){
        Object newNode = graph.insertVertex(rootNode, Integer.toString(root.frecuencia), root.frecuencia, 0, 0, 60, 40);
        graph.insertEdge(rootNode, null,  "0", newNode, createTreeRecursive(root.leftNode, rootNode));
        graph.insertEdge(rootNode, null, "1", newNode, createTreeRecursive(root.rightNode, rootNode));
        return newNode;
    }

    /**
     *
     * @param node
     * @param parent
     * @return
     */
    private Object createTreeRecursive(Node node, Object parent){
        if ((node.rightNode == null) || (node.leftNode == null)){
            Object newLeaf = graph.insertVertex(parent, Integer.toString(node.frecuencia), node.frecuencia + " \n (" + (node.byteRepresentado) + ")", 0, 0, 60, 40);
            return newLeaf;
        }
        Object newTrieNode = graph.insertVertex(parent, Integer.toString(node.frecuencia), node.frecuencia, 0, 0, 60, 40);
        graph.insertEdge(parent, null,  "0", newTrieNode, createTreeRecursive(node.leftNode, parent));
        graph.insertEdge(parent, null, "1", newTrieNode, createTreeRecursive(node.rightNode, parent));
        return newTrieNode;
    }

    /**
     *
     * @return
     */
    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }
}
