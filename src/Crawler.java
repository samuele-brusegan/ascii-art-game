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

                // Notify other threads
                lockB1.notifyAll();
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    /**
     * Restituisce la lista di file nella cartella `dir`
     */
    public Set<String> listFilesUsingFilesList(String dir) throws IOException {

        Stream<Path> stream = Files.list(Paths.get(dir));

        // Convert Stream path 2 Set String
        Set<String> resultSet = stream
                .map(path -> "" + path.toString()) // Converte Path in String
                .collect(Collectors.toSet()); // Raccoglie in un Set (rimuove i duplicati)

        return resultSet;
    }
}
