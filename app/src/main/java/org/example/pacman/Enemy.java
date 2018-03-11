package org.example.pacman;

import android.util.Log;

/**
 * Created by Michael on 08-03-2018.
 */

public class Enemy {
    private int x = 0;
    private int y = 0;
    private int speed = 1;
    private int maxSpeed = 3;

    public int getEnemyX() {
        return this.x;
    }

    public int getEnemyY() {
        return this.y;
    }

    public void setSpeed(int newSpeed) {
        if (this.speed <= maxSpeed) {
            this.speed = newSpeed;
        }
    }

    public void goToPacMan(int pacX, int pacY) {
        if (this.x > pacX) {
            this.x -= speed;
        }
        if (this.x < pacX) {
            this.x += speed;
        }
        if (this.y > pacY) {
            this.y -= speed;
        }
        if (this.y < pacY) {
            this.y += speed;
        }
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setMaxSpeed(int newMaxSpeed) {
        this.maxSpeed = newMaxSpeed;
    }

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
