package Infrastructure;

import Application.Controller;
import Domain.Node;
import Infrastructure.Reader;
import Infrastructure.Utils.BinaryOut;
import Utils.Constantes;

import java.io.File;
import java.util.Map;

public class Decompressor implements Runnable {
    private Controller controller;
    private Thread worker;
    private File file;
    private byte[] bytes;
    private BinaryOut binaryOutCompressedFile;
    private String nombre;
    // Class constructor
    public Decompressor(Controller controller, File file, String nombre ) {
    this.controller = controller;
    this.file=file;
    this.nombre = nombre;

    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }


    @Override
    public void run() {
    Reader reader = new Reader();
    descomprimir(reader);
    }

   private void descomprimir(Reader reader) {
       Node root = controller.getFileRoot(file.getName());
       binaryOutCompressedFile = new BinaryOut(Constantes.PATH_DECOMPRESSED_FILE+nombre);
       bytes = reader.getBytes(file);
       for (int i = 0; i < bytes.length; i++) {
           Node tmp =root;
             if (!tmp.isLeaf()) {
                 if (bytes[i] == 0) {
                    tmp=tmp.leftNode;
                 }else{
                     tmp=tmp.rightNode;
                 }


             }else{
                binaryOutCompressedFile.write(tmp.byteRepresentado);
             }
        }
    }


}
