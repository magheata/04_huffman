package Infrastructure;

import Application.Controller;
import Domain.Node;
import Utils.Constantes;

import java.io.File;

public class Decompressor implements Runnable {
    private Controller controller;
    private Thread worker;
    private Node root;
    private File file;
    private String extension;

    // Class constructor
    public Decompressor(Controller controller, Node root, File file, String extension) {
        this.controller = controller;
        this.root = root;
        this.file = file;
        this.extension = extension;
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }


    @Override
    public void run() {
        descomprimir();
    }

    private void descomprimir() {
        String name = file.getName().substring(0, file.getName().indexOf("_compressed.txt"));
        controller.createOutputFile(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name,
                Constantes.PATH_DECOMPRESSED_FILE + name + "." + extension);
        Node tmp = root;
        byte[] fileBytes = controller.getBytes(file.getAbsolutePath());
        int idx = 0;
        while (idx < fileBytes.length){
            if (!tmp.isLeaf()){
                byte code = fileBytes[idx];
                if ((char) code == '0'){
                    tmp = tmp.leftNode;
                } else {
                    tmp = tmp.rightNode;
                }
                idx++;
            } else {
                controller.write(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name, new byte[] {tmp.byteRepresentado});
                tmp = root;
            }
        }
        controller.write(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name, new byte[] {tmp.byteRepresentado});
        controller.closeOutputFile(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name);
    }
}
