import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {

        String path = "/home/samuele.brusegan@cz.lan/Documenti/ascii-art-game/data/prova_1";

        LinkedList<String> files = new LinkedList<String>();
        LinkedList<Data> charBuffer = new LinkedList<Data>();

        Object lockB1 = new Object();
        Object lockB2 = new Object();

        Crawler c = new Crawler(path, files, lockB1);

        Worker w = new Worker(files, charBuffer, lockB1, lockB2);

        Painter p = new Painter(charBuffer, lockB2);

        c.run();
        w.run();
        p.run();
    }
}