import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scansiona la cartella alla ricerca di file
 * Per ogni file trovato, inserisce il path in una `LinkedList` condivisa
 * Quando ha finito, deve comunicare agli altri che non ci sono più file da
 * elaborare
 */
public class Crawler implements Runnable {
    private final String path;
    private final LinkedList<String> files;
    private final Object lockB1;

    public Crawler(String path, LinkedList<String> files, Object lockB1) {
        this.path = path;
        this.files = files;
        this.lockB1 = lockB1;
    }

    @Override
    public /* #synchronized */ void run() {

        try {
            // Scan folder & List filenames

            Set<String> filesSet = listFilesUsingFilesList(path);

            // (synchronized) Add filenames to LinkedList

            synchronized (lockB1) {
                this.files.addAll(filesSet);
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        // Notify other threads
        lockB1.notifyAll();
    }

    public Set<String> listFilesUsingFilesList(String dir) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }
}
