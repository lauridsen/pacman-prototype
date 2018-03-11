package org.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.button;
import static android.R.attr.level;

public class MainActivity extends Activity {
    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;

    //Timer
    private Timer myTimer;
    private int counter = 0;
    private Timer levelTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        TextView textViewPoints = findViewById(R.id.points);
        TextView textViewTime = findViewById(R.id.time);


        game = new Game(this, textViewPoints, textViewTime);
        game.setGameView(gameView);
        gameView.setGame(game);

        game.newGame();

        //Timer for pacman
        myTimer = new Timer();
        //should the game be running?
        //We will call the timer 5 times each second
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 16); //0 indicates we start now, 200
        //is the number of milliseconds between each call

        //Timer for level
        levelTimer = new Timer();
        levelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LevelTimerMethod();
            }
        }, 0, 1000);


        Button buttonRight = findViewById(R.id.moveRight);
        //listener of our pacman, when somebody clicks it
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.direction = 2;
                game.running = true;
            }
        });

        Button buttonLeft = findViewById(R.id.moveLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.direction = 4;
                game.running = true;
            }
        });

        Button buttonUp = findViewById(R.id.moveUp);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.direction = 1;
                game.running = true;
            }
        });

        Button buttonDown = findViewById(R.id.moveDown);
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.direction = 3;
                game.running = true;
            }
        });
    }


    private void TimerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private void LevelTimerMethod() {
        this.runOnUiThread(LevelTimer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.
            // so we can draw
            if (game.running) {
                counter++;
                //update the counter - notice this is NOT seconds in this example
                //you need TWO counters - one for the time and one for the pacman
                //textView.setText("Timer value: "+counter);
                //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this
                if (game.direction == 1) {
                    game.movePacmanUp(4);
                } else if (game.direction == 2) {
                    game.movePacmanRight(4);
                } else if (game.direction == 3) {
                    game.movePacmanDown(4);
                } else if (game.direction == 4) {
                    game.movePacmanLeft(4);
                }
            }

        }
    };

    private Runnable LevelTimer_Tick = new Runnable() {
        public void run() {
            if (game.running) {
                game.levelTime--;
                game.setTimerText();
            }
            if (game.gameOver) {
                reset();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_newGame) {
            Toast.makeText(this, "Game has been reset", Toast.LENGTH_LONG).show();
            reset();
            return true;
        } else if (id == R.id.action_pause) {
            game.direction = 0;
            game.running = false;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset() {
        //Recreate the activity to reset
        recreate();
        //Stop the timer
        levelTimer.cancel();
        myTimer.cancel();
    }
}
