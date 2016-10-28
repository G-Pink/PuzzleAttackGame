package com.example.garrett.puzzleattackgame;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.R.attr.id;

public class GameActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //ImageView imageArray[][] = {};


        int blue = getResources().getIdentifier("blue","drawable","com.app");
        int green = getResources().getIdentifier("green","drawable","com.app");
        int orange = getResources().getIdentifier("orange","drawable","com.app");
        int purple = getResources().getIdentifier("purple","drawable","com.app");
        int red = getResources().getIdentifier("red","drawable","com.app");
        int yellow = getResources().getIdentifier("yellow","drawable","com.app");

        popGrid();

        startClock();

    }

    protected void linkTiles() {


    }




    public void popGrid() {

        GridLayout gridLayout = (GridLayout)findViewById(R.id.playGrid);

        int column = 6;
        int row = 13;
        int totalrc = column * row;
        int r = 0;
        int c = 0;
        final Tile[] tiles = new Tile[totalrc];

        for(int i=0; i<totalrc; i++) {
            if (c == 5) {
                c = 0;
                r++;
            }





            tiles[i] = new Tile(i);
            ImageView cell = new ImageView(this);

            if(r<=5){
                tiles[i].setTileNum(R.drawable.none);
                cell.setImageResource(tiles[i].getTileNum());
                cell.setTag(R.drawable.none);
                cell.setPadding(6, 6, 6, 6);
                gridLayout.addView(cell, i);
                c++;
                continue;
            }
            int rand = (int) (Math.random() * 6) + 1;


            switch (rand) {
                case 1: {
                    tiles[i].setTileNum(R.drawable.blue);
                    cell.setImageResource(tiles[i].getTileNum());
                    cell.setTag(R.drawable.blue);
                    break;
                }
                case 2: {
                    cell.setImageResource(R.drawable.green);
                    tiles[i].setTileNum(getResources().getIdentifier("green","drawable","com.app"));
                    break;
                }
                case 3: {
                    cell.setImageResource(R.drawable.orange);
                    tiles[i].setTileNum(getResources().getIdentifier("orange","drawable","com.app"));
                    break;
                }
                case 4: {
                    cell.setImageResource(R.drawable.purple);
                    tiles[i].setTileNum(getResources().getIdentifier("purple","drawable","com.app"));
                    break;
                }
                case 5: {
                    cell.setImageResource(R.drawable.red);
                    tiles[i].setTileNum(getResources().getIdentifier("red","drawable","com.app"));
                    //gridlayout.
                    break;
                }
                case 6: {
                    cell.setImageResource(R.drawable.yellow);
                    tiles[i].setTileNum(getResources().getIdentifier("yellow","drawable","com.app"));
                    break;
                }

            }


            cell.setPadding(6, 6, 6, 6);


            gridLayout.addView(cell, i);
            cell.isClickable();

            /*Integer testi = tiles[i].getTileNum();
            if(testi.equals(R.drawable.blue)) {
                cell.setImageResource(R.drawable.red);
            }*/



            c++;



        } //end of for loop for populating Grid




    }

    public void changeBtoR(){


    }

    public void startClock() {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer2);
        chronometer.start();
    }
}
