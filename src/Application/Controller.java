/* Created by andreea on 09/04/2020 */
package Application;

import Domain.Interficies.IController;
import Domain.Node;
import Infrastructure.Compressor;
import Infrastructure.Decompressor;
import Infrastructure.Reader;
import Presentation.Panels.FilesPanel;
import Presentation.Panels.HuffmanTriePanel;
import Utils.Constantes;


import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller implements IController {

    private FilesPanel filesPanel;
    private FileDrop fileDropService;
    private Reader reader;
    private Compressor compressor;
    private Decompressor decompressor;
    private ExecutorService executorService;
    private Map<String, Node> rootNodes = new HashMap<>();


    public void setFiles(HashMap<String, File> files) {
        this.files = files;
    }

    public HashMap<String, File> getFiles() {
        return files;
    }

    private HashMap<String, File> files;

    public Controller() {
        reader = new Reader();
    }

    @Override
    public void addFiles(File directory, File[] selectedFiles) {
        filesPanel.setSelectedFiles(selectedFiles);
    }

    @Override
    public void deleteFile(File file) {
        filesPanel.removeFile(file);
    }

    @Override
    public void comprimirFicheros(Set<File> files) {
        executorService = Executors.newFixedThreadPool(files.size());
        for (Iterator i = files.iterator(); i.hasNext();){
            executorService.submit(() -> comprimirFichero((File) i.next()));
        }
    }



    public StringBuilder readFileContent(String fileName){
        return reader.getFileContent(fileName);
    }


    private void comprimirFichero(File file){
        compressor = new Compressor(this, reader.getBytes(file), file);
        compressor.start();
    }

    public void addArchivosPorComprimirAPanel(File file, int bytesOriginales, int bytesComprimidos){
        filesPanel.addArchivosPorComprimirAPanel(file, bytesOriginales, bytesComprimidos);
    }

    public void descomprimirFicheros(int idx,File file)  {
        String s = file.getName();
        String nombreFichero= null;

        try {
            nombreFichero = getName(idx,s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        decompressor = new Decompressor(this,file,nombreFichero);
        decompressor.start();
    }

    public JComponent addTrieToPanel(String fileName) {
        HuffmanTriePanel trie = new HuffmanTriePanel(rootNodes.get(fileName));
        return trie.getGraphComponent();
    }

    public void addFileRoot(Node node, String fileName){
        rootNodes.put(fileName, node);
    }
    //region SETTERS Y GETTERS
    public void setFilesPanel(FilesPanel filesPanel) {
        this.filesPanel = filesPanel;
    }

    public void setFileDropService(FileDrop fileDropService) {
        this.fileDropService = fileDropService;
    }

    public Node getFileRoot(String fileName){return rootNodes.get(fileName);}

    private String getName(int idx, String s) throws IOException {
        String s1;
        String s2 ="";
        BufferedReader br;
      s1= s.substring(0, s.lastIndexOf('.'));
        File folder = new File("huffmanCodes");
        int i =0;
        for (final File fileEntry : folder.listFiles()) {
            if(i==idx){
                br = new BufferedReader(new FileReader(fileEntry));
               s2= br.readLine();

               s2 = s2.substring(28);
                System.out.println(s2);
                br.close();
            }

             i++;
        }
        s=s1+"."+s2;

        return s;
    }

    //endregion
}
