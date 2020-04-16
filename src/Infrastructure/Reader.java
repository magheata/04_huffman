/* Created by andreea on 12/04/2020 */
package Infrastructure;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class Reader {

    public byte[] getBytes(File file){
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StringBuilder getFileContent(String path){
        StringBuilder sb = new StringBuilder();
        try{
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                // Print the content on the console
                sb.append(scanner.nextLine());
                sb.append("\n");
            }
            scanner.close();
        } catch (IOException ex){

        }
        return sb;
    }
}
