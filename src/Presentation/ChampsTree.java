/* Created by andreea on 17/04/2020 */
package Presentation;

import Domain.Node;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;

import javax.swing.*;
import java.util.Iterator;

public class ChampsTree extends JFrame
{
    private FoldableTree graph;
    public ChampsTree(Node rootHuffman)
    {
        super("Hello, World!");

        graph = new FoldableTree();

        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false);
        layout.setMoveTree(false);
        layout.setUseBoundingBox(false);
        layout.setEdgeRouting(false);
        layout.setLevelDistance(30);
        layout.setNodeDistance(10);

        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {


            /*Object root = graph.insertVertex(parent, "treeRoot", "Root", 0, 0, 60, 40);

            Object v1 = graph.insertVertex(parent, "v1", "Child 1", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", root, v1);

            Object v2 = graph.insertVertex(parent, "v2", "Child 2", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", root, v2);

            Object v3 = graph.insertVertex(parent, "v3", "Child 3", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", root, v3);

            Object v11 = graph.insertVertex(parent, "v11", "Child 1.1", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", v1, v11);

            Object v12 = graph.insertVertex(parent, "v12", "Child 1.2", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", v1, v12);

            Object v21 = graph.insertVertex(parent, "v21", "Child 2.1", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", v2, v21);

            Object v22 = graph.insertVertex(parent, "v22", "Child 2.2", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", v2, v22);

            Object v221 = graph.insertVertex(parent, "v221", "Child 2.2.1", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", v22, v221);

            Object v222 = graph.insertVertex(parent, "v222", "Child 2.2.2", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", v22, v222);

            Object v31 = graph.insertVertex(parent, "v31", "Child 3.1", 0, 0, 60, 40);
            graph.insertEdge(parent, null, "", v3, v31);*/
            createTree(rootHuffman, parent);

            layout.execute(parent);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        graph.addListener(mxEvent.FOLD_CELLS, (sender, evt) -> layout.execute(graph.getDefaultParent()));

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        getContentPane().add(graphComponent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 320);
        this.setVisible(true);
    }

    private Object createTree(Node root, Object rootNode){
        Object newNode = graph.insertVertex(rootNode, Integer.toString(root.frecuencia), root.frecuencia, 0, 0, 60, 40);
        graph.insertEdge(rootNode, null,  root.leftNode.byteRepresentado, newNode, createTreeRecursive(root.leftNode, rootNode, newNode));
        graph.insertEdge(rootNode, null, root.rightNode.byteRepresentado, newNode, createTreeRecursive(root.rightNode, rootNode, newNode));
        return newNode;
    }

    private Object createTreeRecursive(Node node, Object parent, Object root){
        if ((node.rightNode == null) || (node.leftNode == null)){
            return root;
        }
        Object newNode = graph.insertVertex(parent, Integer.toString(node.frecuencia), node.frecuencia, 0, 0, 60, 40);
        graph.insertEdge(parent, null,  node.leftNode.byteRepresentado, root, createTreeRecursive(node.leftNode, parent, newNode));
        graph.insertEdge(parent, null, node.rightNode.byteRepresentado, root, createTreeRecursive(node.rightNode, parent, newNode));
        return newNode;
    }
}
