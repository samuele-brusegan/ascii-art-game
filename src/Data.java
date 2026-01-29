public class Data {
    private int coordX;
    private int coordY;
    private char character;

    public Data(int coordX, int coordY, char character) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.character = character;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return "Data [coordX=" + coordX + ", coordY=" + coordY + ", character=" + character + "]";
    }
}
