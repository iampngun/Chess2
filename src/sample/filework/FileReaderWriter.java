package sample.filework;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderWriter {

    public static void writeFile(String text, String path) {
        try(FileWriter writer = new FileWriter(path, false))
        {
            writer.write(text);

            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static String readFile(String path) {
        String text = "";

        try(FileReader reader = new FileReader(path))
        {
            int c;

            StringBuilder buildingText = new StringBuilder();

            while((c=reader.read())!=-1){
                buildingText.append((char)c);
            }

            text = buildingText.toString();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        return text;
    }

}
