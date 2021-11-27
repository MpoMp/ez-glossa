package mbogdanos.ezglossa.files;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileHandlingUtils {

    private FileHandlingUtils() {
        // static util class
    }

    public static Task<String> generateFileLoaderTask(File fileToLoad) {
        //Create a task to load the file asynchronously
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                StringBuilder totalFile;
                try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                    long lineCount;
                    try (Stream<String> stream = Files.lines(fileToLoad.toPath())) {
                        lineCount = stream.count();
                    }

                    String line;
                    totalFile = new StringBuilder();
                    long linesLoaded = 0;
                    while ((line = reader.readLine()) != null) {
                        totalFile.append(line);
                        totalFile.append("\n");
                        updateProgress(++linesLoaded, lineCount);
                    }
                }
                return totalFile.toString();
            }
        };
    }

}
