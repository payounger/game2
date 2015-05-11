package game2;

import javalib.funworld.World;
import javalib.colors.*;
import javalib.worldimages.*;
import java.util.ArrayList;
import java.util.Random;

public class Game2 extends World {

    public static int n = 13;
    public static int side = 50;
    DataStruct[] worldArray;
    public int ticker = 0;
    int score;
    public static int middleOfWorld = (n * (((n - 1) / 2))) + ((n - 1) / 2);
    public static int middleLeftDoor = ((n - 1) / 2) * n;
    public static int middleRightDoor = middleLeftDoor + n - 1;
    public static int middleTopDoor = (n - 1) / 2;
    public static int middleBottomDoor = middleTopDoor + ((n - 1) * n);
    int currentScr = 0;
    int scrZeroEnemies = 1;
    int scrOneEnemies = 1;
    int gotKeyHuh = 0;

    public Game2(DataStruct[] array, int ticks, int score) {
        super();
        worldArray = array;
        this.score = score;
        ticker = ticks;

    }

    public static void main(String[] args) {

        Game2 world;
        world = new Game2(new DataStruct[n * n], 0, 0);
        world.genScrZero();
        world.spawnPlayerMiddle();
        world.bigBang(n * side, n * side, 0.15);

    }

    public void spawnPlayerMiddle() {
        worldArray[middleOfWorld].setKey(1);
    }

    public World onTick() {

        if (enemiesAliveHuh() == 0) {
            disableCurrentScrSpawn();
        }
        for (int i = 0; i < worldArray.length; i++) {
            if (worldArray[i].getKey() == 4) {
                worldArray[i].setKey(0); //this function clears the attack effect every tick
            }
        }
        return new Game2(worldArray, ticker++, 0);

    }

    public World onKeyEvent(String ke) {
        int playerX = playerLocation().getX();
        int playerY = playerLocation().getY();
        int playerIndex = playerY * (n) + playerX;
        World w;
        w = this;

        if (ke.equals("right")) {
            if (playerX != n - 1
                    && worldArray[playerIndex + 1].getKey() != 2) {
                if (worldArray[playerIndex + 1].getKey() == 3) {
                    return endOfWorld("right collision");
                }
                if (worldArray[playerIndex + 1].getKey() == 5) {
                    gotKeyHuh = 1;
                }
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex + 1].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }

            if (playerIndex == middleRightDoor) {
                //if (currentScr == 0) {
                genScrOne();
                worldArray[middleLeftDoor].setKey(1);
                w = new Game2(worldArray, ticker, 0);
                //}
            }

        }
        if (ke.equals("left")) {
            if (playerX != 0
                    && worldArray[playerIndex - 1].getKey() != 2) {
                if (worldArray[playerIndex - 1].getKey() == 3) {
                    return endOfWorld("left collision");
                }
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex - 1].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }

            if (playerIndex == middleLeftDoor) {
                //if (currentScr == 1) {
                genScrZero();
                worldArray[middleRightDoor].setKey(1);
                w = new Game2(worldArray, ticker, 0);
                //}
            }
        }
        if (ke.equals("up")) {
            if (playerY != 0
                    && worldArray[playerIndex - n].getKey() != 2) {
                if (worldArray[playerIndex - n].getKey() == 3) {
                    return endOfWorld("up collision");
                }
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex - n].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }
        }
        if (ke.equals("down")) {
            if (playerY != n - 1
                    && worldArray[playerIndex + n].getKey() != 2) {
                if (worldArray[playerIndex + n].getKey() == 3) {
                    return endOfWorld("down collision");
                }
                worldArray[playerIndex].setKey(0);
                worldArray[playerIndex + n].setKey(1);
                w = new Game2(worldArray, ticker, 0);
            }
        }

        if (ke.equals(" ")) {
            if (playerIndex != middleLeftDoor
                    && playerIndex != middleRightDoor
                    && playerIndex != middleTopDoor
                    && playerIndex != middleBottomDoor) {

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int currentIndex = DXDYIndex(dx, dy);
                        if (worldArray[currentIndex].getKey() != 1 && worldArray[currentIndex].getKey() != 2) {
                            worldArray[currentIndex].setKey(4);
                        }
                    }
                }
            }
        }

        return w;

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
                scene = new OverlayImages(scene, new RectangleImage(currentPosn, side, side, new Black()));
            } else if (worldArray[i].getKey() == 3) {
                scene = new OverlayImages(scene, new RectangleImage(currentPosn, side, side, new Red()));
            } else if (worldArray[i].getKey() == 4) {
                scene = new OverlayImages(scene, new RectangleImage(currentPosn, side, side, new Green()));
            } else /* if (worldArray[i].getKey() == 5*/ {
                scene = new OverlayImages(scene, new DiskImage(currentPosn, side / 2, new Blue()));
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
                worldArray[i] = output[i];
            }
        }
    }

    public void genScrZero() {
        genEmptyArray();
        currentScr = 0;
        for (int i = 0; i < worldArray.length; i++) {
            if (worldArray[i].getX() == 0) {
                worldArray[i].setKey(2);
            }
            if (worldArray[i].getX() == n - 1) {
                worldArray[i].setKey(2);
            }
            if (worldArray[i].getY() == 0) {
                worldArray[i].setKey(2);
            }
            if (worldArray[i].getY() == n - 1) {
                worldArray[i].setKey(2);
            }
//            if (i == middleLeftDoor) {
//                worldArray[i].setKey(0);
//            }
            if (i == middleRightDoor) {
                worldArray[i].setKey(0);
            }
//            if (i == middleTopDoor) {
//                worldArray[i].setKey(0);
//            }
//            if (i == middleBottomDoor) {
//                worldArray[i].setKey(0);
//            }

        }

        if (scrZeroEnemies == 1) {
            worldArray[middleOfWorld + 3].setKey(3);
        }

    }

    public void genScrOne() {
        genEmptyArray();
        currentScr = 1;
        for (int i = 0; i < worldArray.length; i++) {
            if (worldArray[i].getX() == 0) {
                worldArray[i].setKey(2);
            }
            if (worldArray[i].getX() == n - 1) {
                worldArray[i].setKey(2);
            }
            if (worldArray[i].getY() == 0) {
                worldArray[i].setKey(2);
            }
            if (worldArray[i].getY() == n - 1) {
                worldArray[i].setKey(2);
            }
            if (i == middleLeftDoor) {
                worldArray[i].setKey(0);
            }
//            if (i == middleRightDoor) {
//                worldArray[i].setKey(0);
//            }
//            if (i == middleTopDoor) {
//                worldArray[i].setKey(0);
//            }
//            if (i == middleBottomDoor) {
//                worldArray[i].setKey(0);
//            }

        }
        if (gotKeyHuh == 0) {
            worldArray[middleOfWorld + 2].setKey(5);
        }
    }

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
            if (targetKey == 1) {
                return target;
            } //else should throw an excepttion but this should never be reached
        }
        throw new RuntimeException("player object not found");
    }

    public int enemiesAliveHuh() {
        DataStruct target;
        int targetKey;
        for (int i = 0; i < (n * n); i++) {
            target = worldArray[i];
            targetKey = target.getKey();
            if (targetKey == 1) {
                return 1;
            }
        }
        return 0;
    }

    public int calcIndex(int x, int y) {
        return y * (n) + x;
    }

    public int DXDYIndex(int dx, int dy) {
        int playerX = playerLocation().getX();
        int playerY = playerLocation().getY();
        int playerIndex = calcIndex(playerX, playerY);

        return (playerIndex + (dy * n) + dx);

    }

    public void disableCurrentScrSpawn() {
        if (currentScr == 1) {
            scrZeroEnemies = 0;
        }
        if (currentScr == 2) {
            scrOneEnemies = 0;
        }
    }
}
