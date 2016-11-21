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
    int posx;
    int row;
    int posy;
    int col;
    int position;
    int tileNum;


    public Tile(int position) {


        this.position = position;
    }

    public void setTileimage(ImageView tileimage) {
        this.tileimage = tileimage;
    }

    public ImageView getTileimage() {
        return tileimage;
    }

    public int getTileNum() {
        return tileNum;
    }

    public void setTileNum(int tilenum) {
        this.tileNum = tilenum;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
