package game2;

public class DataStruct {

    private final int x;
    private final int y;
    private int key;

    public DataStruct(int x, int y, int key) {
        this.x = x;
        this.y = y;
        this.key = key;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int i) {
        key = i;
    }

}
