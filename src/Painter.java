import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class Painter implements Runnable {
    private LinkedList<Data> charBuffer;
    private final Object lockR;

    public Painter(LinkedList<Data> charBuffer, Object lockB2) {
        this.charBuffer = charBuffer;
        this.lockR = lockB2;
    }

    @Override
    public void run() {
        String out = "";
        // TODO Read LinkedList
        synchronized (lockR) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("output.txt"));

                // Crea lista
                ArrayList<ArrayList<Character>> matrix = new ArrayList<>();

                for (int i = 0; i < charBuffer.size(); i++) {

                    int x = charBuffer.get(i).getCoordX();
                    int y = charBuffer.get(i).getCoordY();
                    char c = charBuffer.get(i).getCharacter();

                    // Add row if not exists
                    while (matrix.size() <= x) {
                        matrix.add(new ArrayList<>());
                    }

                    // Add column if not exists
                    while (matrix.get(x).size() <= y) {
                        matrix.get(x).add(' ');
                    }

                    matrix.get(x).set(y, c);
                }

                // Convert to string
                for (int i = 0; i < matrix.size(); i++) {
                    for (int j = 0; j < matrix.get(i).size(); j++) {
                        out += matrix.get(i).get(j);
                    }
                    out += "\n";
                }
                pw.println(out);
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Read data: " + charBuffer);
            lockR.notifyAll();
        }
    }
}