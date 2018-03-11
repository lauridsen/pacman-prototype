package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have
    //bitmap of the pacman
    private Bitmap pacBitmapRight;
    private Bitmap pacBitmapDown;
    private Bitmap pacBitmapLeft;
    private Bitmap pacBitmapUp;
    //textview reference to points
    private TextView pointsView;
    private TextView timerView;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    //The list of enemies
    private ArrayList<Enemy> enemies = new ArrayList<>();

    //a reference to the gameview
    private GameView gameView;
    private int h, w; //height and width of screen

    private boolean coinsInitialized = false;
    private boolean enemiesInitialized = false;

    // Timer
    public boolean running = false;
    public boolean gameOver = false;
    public int direction = 0;
    public int levelTime = 60;


    public Game(Context context, TextView view, TextView textViewTime) {
        this.context = context;
        this.pointsView = view;
        this.timerView = textViewTime;
        pacBitmapRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacmanright);
        pacBitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacmandown);
        pacBitmapLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacmanleft);
        pacBitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacmanup);

    }

    public void setGameView(GameView view) {
        this.gameView = view;
    }

    public boolean getCoinsInitialized() {
        return coinsInitialized;
    }

    public boolean getEnemiesInitialized() {
        return enemiesInitialized;
    }

    public void initCoins() {
        for (int i = 0; i < 10; i++) {
            int xRandom = (int) Math.floor(Math.random() * w);
            int yRandom = (int) Math.floor(Math.random() * h);
            coins.add(new GoldCoin(xRandom, yRandom));
        }
        coinsInitialized = true;
    }

    public void initEnemies() {
        for (int i = 0; i < 1; i++) {
            int xRandom = (int) Math.floor(Math.random() * w);
            int yRandom = (int) Math.floor(Math.random() * h);
            enemies.add(new Enemy(xRandom, yRandom));
        }
        enemiesInitialized = true;
    }

    // New game
    public void newGame() {
        pacx = 50;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;
        setPointText();
        setTimerText();
        gameView.invalidate(); //redraw screen
    }

    public void setSize(int h, int w) {
        this.h = h;
        this.w = w;
    }

    public void movePacmanRight(int pixels) {
        //still within our boundaries?
        doCollisionCheck();
        if (pacx + pixels + pacBitmapRight.getWidth() < w) {
            pacx = pacx + pixels;
            gameView.invalidate();
        }
    }

    public void movePacmanLeft(int pixels) {
        //still within our boundaries?
        doCollisionCheck();
        if (pacx - pixels + pacBitmapRight.getWidth() < w && pacx > 0) {
            pacx = pacx - pixels;
            gameView.invalidate();
        }
    }

    public void movePacmanUp(int pixels) {
        //still within our boundaries?
        doCollisionCheck();
        if (pacy - pixels + pacBitmapRight.getHeight() > 0 && pacy > 0) {
            pacy = pacy - pixels;
            gameView.invalidate();
        }
    }

    public void movePacmanDown(int pixels) {
        //still within our boundaries?
        doCollisionCheck();
        if (pacy + pixels + pacBitmapRight.getHeight() < h) {
            pacy = pacy + pixels;
            gameView.invalidate();
        }
    }


    //Check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    public void doCollisionCheck() {
        int pacRadius = 80;
        int enemyRadius = 40;
        int middlePacX = pacx + getPacBitmapRight().getWidth() / 2;
        int middlePacY = pacy + getPacBitmapRight().getHeight() / 2;
        // check if gold coin is hit
        for (GoldCoin coin : getCoins()) {
            int a = Math.abs(middlePacX - coin.getCoinX());
            int b = Math.abs(middlePacY - coin.getCoinY());
            if (Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)) < pacRadius && !coin.isTaken()) {
                // 1. set coin to taken
                coin.setTaken(true);
                // 2. update points
                points++;
                // 3. refresh view
                gameView.invalidate();
                // update point text
                setPointText();
                // set speed of enemy depending on points taken
                for (Enemy enemy : getEnemies()) {
                    if (getPoints() == 3) {
                        enemy.setSpeed(2);
                    } else if (getPoints() == 6) {
                        enemy.setSpeed(3);
                    }
                }
            }
        }
        // Check if enemy is hit
        for (Enemy enemy : getEnemies()) {
            enemy.goToPacMan(middlePacX, middlePacY);
            gameView.invalidate();
            int a = Math.abs(middlePacX - enemy.getEnemyX());
            int b = Math.abs(middlePacY - enemy.getEnemyY());
            if (Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)) < enemyRadius) {
                running = false;
                gameOver = true;
                Toast toast = Toast.makeText(context.getApplicationContext(), "Du tabte", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public int getPacx() {
        return pacx;
    }

    public int getPacy() {
        return pacy;
    }

    public int getPoints() {
        return points;
    }

    public int getDirection() {
        return direction;
    }

    public void resetPoints() {
        this.points = 0;
    }

    public ArrayList<GoldCoin> getCoins() {
        return coins;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Bitmap getPacBitmapRight() {
        return pacBitmapRight;
    }

    public Bitmap getPacBitmapDown() {
        return pacBitmapDown;
    }

    public Bitmap getPacBitmapLeft() {
        return pacBitmapLeft;
    }

    public Bitmap getPacBitmapUp() {
        return pacBitmapUp;
    }


    public void setPointText() {
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        if (points == 10) {
            Toast toast = Toast.makeText(context.getApplicationContext(), "Du vandt", Toast.LENGTH_SHORT);
            toast.show();
            running = false;
        }
    }

    public void setTimerText() {
        timerView.setText(context.getResources().getString(R.string.time) + " " + levelTime);
        if (levelTime == 0) {
            direction = 0;
            running = false;
            gameOver = true;
            Toast toast = Toast.makeText(context.getApplicationContext(), "Du tabte", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
