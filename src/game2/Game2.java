package game2;

import javalib.funworld.World;
import javalib.colors.*;
import javalib.worldimages.*;
import java.util.ArrayList;
import java.util.Random;

public class Game2 extends World {

    public static int n = 10;
    public static int side = 50;
    DataStruct[] worldArray;
    public int ticker = 0;

    public Game2(DataStruct[] array, int ticks, int score) {
        super();
        array = worldArray;

    }

    public static void main(String[] args) {
        Game2 world;
        world = new Game2(new DataStruct[n * n], 0, 0);
        world.genEmptyArray();
        world.bigBang(n * side, n * side, 0.15);

    }

    public World onTick() {

        return new Game2(worldArray, ticker++, 0);

    }

    public World onKeyEvent(String ke) {
        int playerX = playerLocation().getX();
        int playerY = playerLocation().getY();
        int playerIndex = playerY * (n) + playerX;
        World w;
        
        if(ke.equals("right")){
            if(playerX != n-1){
                //worldArray[playerIndex+1].getKey()
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex+1].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }
            
        }
        if(ke.equals("left")){
            if(playerX != 0){
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex-1].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }
        }
        if(ke.equals("up")){
            if(playerY != 0){
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex-n].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }
        }
        if(ke.equals("down")){
            if(playerY != n-1){
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex+n].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }
        }
        
    }

    public WorldImage makeImage() {
        Posn currentPosn;
        int height = side * n;
        int width = height;
        WorldImage scene = new RectangleImage(new Posn(width / 2, height / 2), width, height, new White());
        for (int i = 0; i < worldArray.length; i++) {
            currentPosn = calcPin(worldArray[i]);
            if (worldArray[i].getKey() == 0) {
                scene = new OverlayImages(scene, new FrameImage(currentPosn, side, side, new Black()));
            } else if (worldArray[i].getKey() == 1) {
                scene = new OverlayImages(scene, new RectangleImage(currentPosn, side, side, new Blue()));
            } else if (worldArray[i].getKey() == 2) {
                scene = new OverlayImages(scene, new RectangleImage(currentPosn, side, side, new Red()));
            }
        }
        return scene;
    }

    public void genEmptyArray() {
        DataStruct[] output = new DataStruct[n * n];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                int i = (y * n) + x;
                output[i] = new DataStruct(x, y, 0);
            }
        }
        worldArray = output;
    }

//    public void genScrOne(){
//        DataStruct[] array;
//        array = genEmptyArray();
//        
//    }
    public Posn calcPin(DataStruct Struct) {
        int x = Struct.getX();
        int y = Struct.getY();
        return new Posn(x * side + side / 2, y * side + side / 2);
    }

    public DataStruct playerLocation() {
        DataStruct target;
        int targetKey;
        for (int i = 0; i < (n * n); i++) {
            target = worldArray[i];
            targetKey = target.getKey();
            if (targetKey == 2) {
                return target;
            } //else should throw an excepttion but this should never be reached
        }
        throw new RuntimeException("player object not found");
    }
}
