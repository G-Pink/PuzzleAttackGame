package com.example.garrett.puzzleattackgame;


import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by Garrett on 10/15/2016.
 */

public class Tile extends GameActivity{

  /*  int blue = getResources().getIdentifier("blue","drawable","com.app");
    int green = getResources().getIdentifier("green","drawable","com.app");
    int orange = getResources().getIdentifier("orange","drawable","com.app");
    int purple = getResources().getIdentifier("purple","drawable","com.app");
    int red = getResources().getIdentifier("red","drawable","com.app");
    int yellow = getResources().getIdentifier("yellow","drawable","com.app");*/

    ImageView tileimage;
    int position;
    int tileNum;


    public Tile(int orientation) {

        position = orientation;
    }

    public void setTileimage(ImageView tileimage) {
        this.tileimage = tileimage;
    }

    public int getTileNum() {
        return tileNum;
    }

    public void setTileNum(int tilenum) {
        this.tileNum = tilenum;
    }

    public ImageView getTileimage() {
        return tileimage;
    }


}
