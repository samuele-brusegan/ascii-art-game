import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Prelevano un path alla volta dalla `LinkedList` condivisa
 * Leggono e analizzano il contenuto del file
 * Estraggono i dati nascosti
 * Inseriscono il risultato in una seconda `LinkedList` condivisa
 */
public class Worker implements Runnable {
    private final LinkedList<String> files;
    private final LinkedList<Data> charBuffer;
    private final Object lockB1;
    private final Object lockB2;

    public Worker(LinkedList<String> files, LinkedList<Data> charBuffer, Object lockB1, Object lockB2) {
        this.files = files;
        this.charBuffer = charBuffer;
        this.lockB1 = lockB1;
        this.lockB2 = lockB2;
    }

    @Override
    public void run() {
        // Get a path from the LinkedList files
        while (!files.isEmpty()) {
            synchronized (lockB1) {
                while (files.isEmpty()) {
                    try {
                        lockB1.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Read file
                String path = files.removeFirst();

                // Extract hidden data
                Data hiddenData = extractHiddenData(path);

                addToResults(hiddenData);

                lockB1.notifyAll();
            }
        }
    }

    public Data extractHiddenData(String path) {
        int coordX = 0;
        int coordY = 0;
        char character = 0;

        ArrayList<ArrayList<Character>> matrix = readFile(path);

        // #0###
        // #0#/#
        // #0###
        // #####
        // 004##

        // Find vertically stacked numbers
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                // Se trovo un numero, controllo se c'è un altro numero sotto

                if (Character.isDigit(matrix.get(i).get(j))) {
                    try {
                        Character c1 = matrix.get(i).get(j);
                        Character c2 = matrix.get(i).get(j + 1);
                        Character c3 = matrix.get(i).get(j + 2);

                        if (Character.isDigit(c1) && Character.isDigit(c2) && Character.isDigit(c3)) {
                            // Somma i 3 numeri
                            int sum = Character.getNumericValue(c1) + Character.getNumericValue(c2)
                                    + Character.getNumericValue(c3);
                            coordY = sum;
                        }

                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("(notice) IndexOutOfBoundsException in Worker.extractHiddenData");
                    }
                }
            }
        }

        // Find horizontally stacked numbers
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if (Character.isDigit(matrix.get(i).get(j))) {
                    try {
                        Character c1 = matrix.get(i).get(j);
                        Character c2 = matrix.get(i).get(j + 1);
                        Character c3 = matrix.get(i).get(j + 2);

                        if (Character.isDigit(c1) && Character.isDigit(c2) && Character.isDigit(c3)) {
                            // Somma i 3 numeri
                            int sum = Character.getNumericValue(c1) + Character.getNumericValue(c2)
                                    + Character.getNumericValue(c3);
                            coordX = sum;
                        }

                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("(notice) IndexOutOfBoundsException in Worker.extractHiddenData");
                    }
                }
            }
        }

        // Find character differ
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if (Character.isDigit(matrix.get(i).get(j))) {
                    coordX = i;
                    coordY = j;
                    character = matrix.get(i).get(j);
                }
            }
        }

        Data data = new Data(coordX, coordY, character);

        System.out.println("Data: " + data);
        return data;
    }

    public void addToResults(Data hiddenData) {
        // (synchronized) Add hidden data to LinkedList charBuffer
        synchronized (lockB2) {
            charBuffer.add(hiddenData);
            System.out.println("Added data: " + hiddenData);
            lockB2.notifyAll();
        }
    }

    private ArrayList<ArrayList<Character>> readFile(String path) {
        ArrayList<ArrayList<Character>> matrix = new ArrayList<>();
        BufferedReader br;
        try {
            System.out.println("Reading file: " + path);
            br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Character> row = new ArrayList<>();
                for (char c : line.toCharArray()) {
                    row.add(c);
                }
                matrix.add(row);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }
}
