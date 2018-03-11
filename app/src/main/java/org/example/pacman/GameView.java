package org.example.pacman;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    Game game;
    int h, w; //used for storing our height and width of the view

    public void setGame(Game game) {
        this.game = game;
    }


    /* The next 3 constructors are needed for the Android view system,
    when we have a custom view.
     */
    public GameView(Context context) {
        super(context);

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //In the onDraw we put all our code that should be
    //drawn whenever we update the screen.
    @Override
    protected void onDraw(Canvas canvas) {
        //Here we get the height and weight
        h = canvas.getHeight();
        w = canvas.getWidth();
        //update the size for the canvas to the game.
        game.setSize(h, w);
        //Making a new paint object
        Paint paint = new Paint();
        canvas.drawColor(Color.BLACK); //clear entire canvas to white color

        //loop through the list of goldcoins and enemies and draw them.
        //Check if w and h are initialized.
        if (!game.getCoinsInitialized() && !game.getEnemiesInitialized()) {
            game.initCoins();
            game.initEnemies();
        }
        // Draw only available coins
        for (GoldCoin coin : game.getCoins()) {
            if (!coin.isTaken()) {
                paint.setColor(Color.YELLOW);
                canvas.drawCircle(coin.getCoinX(), coin.getCoinY(), 20, paint);
            }
        }
        // Loop through enemy to set color depending on speed
        for (Enemy enemy : game.getEnemies()) {
            if (enemy.getSpeed() == 1) {
                paint.setColor(Color.CYAN);
            } else if (enemy.getSpeed() == 2) {
                paint.setColor(Color.MAGENTA);
            } else {
                paint.setColor(Color.RED);
            }
            canvas.drawCircle(enemy.getEnemyX(), enemy.getEnemyY(), 40, paint);
        }
        //draw the pacman depending on direction
        if (game.getDirection() == 1) {
            canvas.drawBitmap(game.getPacBitmapUp(), game.getPacx(), game.getPacy(), paint);
        } else if (game.getDirection() == 2) {
            canvas.drawBitmap(game.getPacBitmapRight(), game.getPacx(), game.getPacy(), paint);
        } else if (game.getDirection() == 3) {
            canvas.drawBitmap(game.getPacBitmapDown(), game.getPacx(), game.getPacy(), paint);
        } else if (game.getDirection() == 4) {
            canvas.drawBitmap(game.getPacBitmapLeft(), game.getPacx(), game.getPacy(), paint);
        } else {
            canvas.drawBitmap(game.getPacBitmapRight(), game.getPacx(), game.getPacy(), paint);
        }

        super.onDraw(canvas);
    }

}
