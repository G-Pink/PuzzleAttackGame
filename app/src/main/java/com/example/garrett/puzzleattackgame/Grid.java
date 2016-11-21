package com.example.garrett.puzzleattackgame;

import android.app.ActionBar;
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

/**
 * Created by Garrett on 10/13/2016.
 * Need to create a dp background with a mathematical grid of elements which can be populated
 * according to position of the grid.
 * - Display grid.
 * - Get grid properly positioned.
 * - constructor: set rows and columns, draw Grid.
 * +drawGrid:Imageview
 *  -
 * - Two dimensional array, pos[row][col]
 *
 *
 * - Populate grid with elements, ImageViews, randomly.
 *
 *
 */

public class Grid extends GameActivity{


    public Grid() {
    }

    public void popGrid(RelativeLayout gridLayout, ImageView cell) {



        final float scale = gridLayout.getContext().getResources().getDisplayMetrics().density;
        int rlx = (int) (300 * scale + 0.5f);
        int rly = (int) (500 * scale + 0.5f);

        int column = 6;
        int row = 13;
        int totalrc = column * row;

        int r = 0;
        int c = 0;
        int[] colrand = new int[column];
        Tile[] tiles = new Tile[totalrc];

        for(int j=0; j<column; j++)
        {
            colrand[j] = (int) (Math.random() * 4) + 1;
        }

        for(int i=0; i<totalrc; i++) {
            if (c == 6) {
                c = 0;
                r++;
            }

            tiles[i] = new Tile(i);


            if(r<7){
                r++;
                continue;
            }

            if(colrand[c]!=0) {
                colrand[c]-=1;
                c++;
                continue;
            }

            int rand = (int) (Math.random() * 6) + 1;


            switch (rand) {
                case 1: {
                    cell.setId(R.id.tile);
                    tiles[i].setTileNum(cell.getId());
                    cell.setImageResource(R.drawable.blue);
                    break;
                }
                case 2: {
                    cell.setId(R.id.tile);
                    tiles[i].setTileNum(cell.getId());
                    cell.setImageResource(R.drawable.orange);
                    break;
                }
                case 3: {
                    cell.setId(R.id.tile);
                    tiles[i].setTileNum(cell.getId());
                    cell.setImageResource(R.drawable.purple);
                    break;
                }
                case 4: {
                    cell.setId(R.id.tile);
                    tiles[i].setTileNum(cell.getId());
                    cell.setImageResource(R.drawable.green);
                    break;
                }
                case 5: {
                    cell.setId(R.id.tile);
                    tiles[i].setTileNum(cell.getId());
                    cell.setImageResource(R.drawable.red);
                    break;
                }
                case 6: {
                    cell.setId(R.id.tile);
                    tiles[i].setTileNum(cell.getId());
                    cell.setImageResource(R.drawable.yellow);
                    break;
                }

            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            tiles[i].setPosx(c*rlx/column);
            params.leftMargin = c * rlx / column;
            tiles[i].setPosy(r*rly/row);
            params.topMargin = r * rly / row;


            cell.setLayoutParams(params);
            cell.setPadding(6, 6, 6, 6);
            gridLayout.addView(cell, params);

            cell.isClickable();


            c++;
        } //end of for loop for populating Grid




    }
       /* int rowHeight = grids.getLayoutParams().height / NUMROWS;  //1800 / 12 = 150
        int colWidth = grids.getLayoutParams().width / NUMCOLS;  //900 / 6 = 150



    public int getRowHeight(){
        return grids.getLayoutParams().height;
    }
    public int getColWidth(){
        return grids.getLayoutParams().width;
    }
    */
}
