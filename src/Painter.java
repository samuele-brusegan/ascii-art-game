import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Painter implements Runnable{
    private LinkedList<Data> charBuffer;
    private final Object lockR;

    public Painter(LinkedList<Data> charBuffer, Object lockB2){
        this.charBuffer = charBuffer;
        this.lockR = lockB2;
    }

    @Override
    public void run() {
        String out = "";
        // TODO Read LinkedList
        synchronized(lockR){
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("output.txt"));
                for (int i = 0; i < charBuffer.size(); i++) {
                    
                    int x = charBuffer.get(i).getCoordX();
                    int y = charBuffer.get(i).getCoordY();
                    char c = charBuffer.get(i).getCharacter();
                }
                
                pw.println("testo da scrivere");
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
}