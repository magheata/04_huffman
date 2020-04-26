package Infrastructure;

import Application.Controller;
import Domain.Node;
import Infrastructure.Reader;
import Infrastructure.Utils.BinaryIn;
import Infrastructure.Utils.BinaryOut;
import Utils.Constantes;

import java.io.File;
import java.util.Map;

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
        BinaryIn bIn = new BinaryIn(file.getAbsolutePath());
        String name = file.getName().substring(0, file.getName().indexOf("_compressed.txt"));
        controller.createBinaryOutputFile(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name,
                Constantes.PATH_DECOMPRESSED_FILE + name + "." + extension);
        Node tmp = root;
        String code = "";
        char currentChar;
        while(!bIn.isEmpty()){
            currentChar = bIn.readChar();
            if (!tmp.isLeaf()) {
                if (currentChar == '1') {
                    code = code.concat("1");
                    tmp = tmp.rightNode;
                } else {
                    tmp = tmp.leftNode;
                    code = code.concat("0");
                }
            } else {
                System.out.println((char) tmp.byteRepresentado);
                controller.write(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name, tmp.byteRepresentado);
                code = "";
                tmp = root;
            }
        }
        controller.closeBinaryOutputFile(Constantes.OUTPUT_TYPE_DECOMPRESSED_FILE + name);
    }
}
