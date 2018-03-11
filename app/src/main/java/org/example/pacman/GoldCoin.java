package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    private boolean Taken = false;
    private int x = 0;
    private int y = 0;

    public boolean isTaken() {
        return Taken;
    }

    public void setTaken(boolean taken) {
        this.Taken = taken;
    }

    public int getCoinY() {
        return this.y;
    }

    public int getCoinX() {
        return this.x;
    }

    public void setCoin(int x, int y) {
        this.y = y;
        this.x = x;
    }

    public GoldCoin(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
