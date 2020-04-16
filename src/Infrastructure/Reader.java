/* Created by andreea on 12/04/2020 */
package Infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Reader {

    public byte[] getBytes(File file){
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
