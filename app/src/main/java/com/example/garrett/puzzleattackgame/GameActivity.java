package com.example.garrett.puzzleattackgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class GameActivity extends Activity implements List, Chronometer.OnChronometerTickListener{

    int column = 6;
    int row = 13;
    int totalrc = column * row;
    int combo=0;

    List<ImageView> imgview = new ArrayList<>();

    int rlx;
    int rly;
    MediaPlayer mediaPlayer;
    MediaPlayer effect;

    SoundPool sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    int whoosh;
    int pip;
    int clack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        RelativeLayout gridLayout = (RelativeLayout)findViewById(R.id.playGrid);
        //initialize scaled grid for gridlayout.
        final float scale = gridLayout.getContext().getResources().getDisplayMetrics().density;
        gridLayout.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //int width = gridLayout.getMeasuredWidth(); For future implementation of tablet.
        //int height = gridLayout.getMeasuredHeight();
        rlx = (int) (300 * scale + 0.5f);
        rly = (int) (500 * scale + 0.5f);

        //Initial population of grid and seeding of ImageView List
        popGrid();

        //Begin clock at 00:00.
        Chronometer chro = (Chronometer) findViewById(R.id.chronometer2);
        chro.setOnChronometerTickListener(this);
        chro.start();

        mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.a9m);

        mediaPlayer.start();
        mediaPlayer.setLooping(true);


        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        pip = sp.load(this, R.raw.pip, 1);
        whoosh = sp.load(this, R.raw.whoosh, 1);
        clack = sp.load(this, R.raw.clack, 1);


        Button newrow = (Button) this.findViewById(R.id.rownew);
        newrow.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  newBottomRow();
              }

        });

        checkTileMatches();
        testGravity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();


    }

    @Override
    protected void onResume() {
        super.onResume();
       mediaPlayer.start();


    }

    @Override
    protected void onStop(){
        sp.release();
        Intent i;
        i = new Intent(this, MainActivity.class);
        startActivity(i);
        super.onStop();
    }

    //If score reaches threshold, game is won.
    public void winState(){
        Context context = getApplicationContext();
        TextView score = (TextView) findViewById(R.id.score);
        int scoretotal = Integer.valueOf(score.getText().toString());
        CharSequence text = "Congratulations, you won with a score of " + scoretotal;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Intent i;
        i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    //If tiles reach top row, game is lost.
    public boolean loseState(){
        for(int i=0; i<7; i++){
            if(imgview.get(i).getLayoutParams()!=null) {
                return true;
            }
        }
        return false;

    }

    //Initial population of the Grid with tiles.
    public void popGrid() {

        RelativeLayout gridLayout = (RelativeLayout) findViewById(R.id.playGrid);
        int r = 0;
        int c = 0;
        int[] colrand = new int[column];

        //Seed initial population of tiles in each column.
        for (int j = 0; j < column; j++) {
            colrand[j] = (int) (Math.random() * 5) + 1;
        }

        //populate ImageView list ArrayList.
        for(int j=0; j < totalrc; j++){
            ImageView temp = new ImageView(this);
            imgview.add(j, temp);
        }

        //for loop for populating ImageView List and Board.
        for (int i = 0; i < totalrc; i++) {
            if (c == 6) {
                c = 0;
                r++;
            }

            //Do not seed above the 7th row.
            if (r < 6) {
                r=7;
                i=36; //Make up for loops not taken in grid.
                continue;
            }

            //Count down to being able to seed in a particular column.
            //After 7th row initial loop above.
            if (colrand[c] != 0) {
                colrand[c] -= 1;
                c++;
                continue;
            }

            //Set list element to position in grid.
            ImageView cell = new ImageView(this);
            imgview.set(i, cell);


            //Generate random number and populate tile.
            selectTile(cell);

            //Set Tag, ID, and implement Listener for Left/Right Swipes.
            cell.setTag(i);
            cell.setId(i);
            setListener(cell);


            //Set Imageview Params for placement on RelativeLayout
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = c * rlx / column;
            params.topMargin = r * rly / row;
            cell.setLayoutParams(params);

            gridLayout.addView(cell, params);

            c++;
        }  //end of for loop for populating Grid
    }

    //Populate the grid with a new row after moving all existing tiles up. (Generates Lose State)
    public void newBottomRow() {
        //loop through existing grid, move each tile up, replace with temp tile.


        sp.play(pip, 1, 1, 1, 0, 1);



        for(int i=0; i<=imgview.size()-6; i++){


            ImageView cell = imgview.get(i);

            ImageView temp = new ImageView(this);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)cell.getLayoutParams();

            if(cell.getLayoutParams()!=null){


                    params.topMargin -= rly / row;
                    cell.setLayoutParams(params);
                    imgview.set(i - 6, cell);
                    imgview.set(i, temp);
            }

        }
        //Populate bottom row after moving up.
        int col = 0;
        RelativeLayout gridLayout = (RelativeLayout) findViewById(R.id.playGrid);
        //gridLayout.setClickable(false);
        for(int j=67; j<73; j++){

            ImageView tempo = new ImageView(this);
            selectTile(tempo);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.leftMargin = col * rlx / column;
            params1.topMargin = 12 * rly / row;
            tempo.setLayoutParams(params1);
            gridLayout.addView(tempo, params1);
            imgview.set(j, tempo);
            setListener(tempo);
            col++;

        }
        for(int i=0; i<=imgview.size()-6; i++){

            ImageView cell = imgview.get(i);
            cell.setClickable(true);
        }

        if(loseState()){
            Context context = getApplicationContext();
            CharSequence text = "Sorry, better luck next time!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            onStop();
        }
        //test top row for tiles
        //gridLayout.setClickable(true);
        checkTileMatches();
        testGravity();
    }

    //Randomly select Drawable, assign to ImageView passed to it.
    public void selectTile(ImageView cell){
        int rand = (int) (Math.random() * 6) + 1;


        switch (rand) {
                case 1: {
                    cell.setImageResource(R.drawable.blue);
                    break;
                }
                case 2: {
                    cell.setImageResource(R.drawable.orange);
                    break;
                }
                case 3: {
                    cell.setImageResource(R.drawable.purple);
                    break;
                }
                case 4: {
                    cell.setImageResource(R.drawable.green);
                    break;
                }
                case 5: {
                    cell.setImageResource(R.drawable.red);
                    break;
                }
                case 6: {
                    cell.setImageResource(R.drawable.yellow);
                    break;
                }
        }
    }

    //Sets Swap listener on ImageView tile.
    public void setListener(ImageView cell){

        final ImageView cell1 = cell;
        cell1.setClickable(true);
        cell1.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeRight() {
                swapRight(cell1);
            }
            public void onSwipeLeft() {
                swapLeft(cell1);
            }
        });
    }

    public void swapRight(ImageView cell){


        sp.play(whoosh, 1, 1, 1, 0, 1);


        int i = imgview.indexOf(cell);
        ImageView second = imgview.get(i+1);
        //Swap at right edge disallowed
        if(i==12 || i==18 || i==24 || i==30 || i==36 || i==42 || i==48 || i==54 || i==60 || i==66 || i==72 || i==78){

        }
        //Tile to the right.
        else if(second.getLayoutParams()!=null) {
            ImageView temp = new ImageView(this);
            temp.setLayoutParams(cell.getLayoutParams());
            temp.setTop(cell.getTop());
            temp.setLeft(cell.getLeft());


            imgview.set(i, second);
            imgview.set(i + 1, cell);


            //cell.animate().x(second.getLeft()).setDuration(50).start();
            //second.animate().x(temp.getLeft()).setDuration(50).start();
            cell.setLayoutParams(second.getLayoutParams());
            second.setLayoutParams(temp.getLayoutParams());

        }
        //Blank space to the right.
        else if(second.getLayoutParams()==null){
            imgview.set(i + 1, cell);
            ImageView temp = new ImageView(this);
            imgview.set(i,temp);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //cell.animate().x(cell.getLeft() + rlx / column).setDuration(50).start();
            params.leftMargin = cell.getLeft() + rlx / column;
            params.topMargin = cell.getTop();
            cell.setLayoutParams(params);

        }


        testGravity();
        checkTileMatches();

    }

    public void swapLeft(ImageView cell){



        sp.play(whoosh, 1, 1, 1, 0, 1);


        int i = imgview.indexOf(cell);
        ImageView second = imgview.get(i-1);

        //Swap at Left edge disallowed.
        if(i==7 || i==13 || i==19 || i==25 || i==31 || i==37 || i==43 || i==49 || i==55 || i==61 || i==67 || i==73){

        }
        //Tile to the left.
        else if(second.getLayoutParams()!=null) {
            ImageView temp = new ImageView(this);
            temp.setLayoutParams(cell.getLayoutParams());
            temp.setId(cell.getId());

            imgview.set(i, second);
            imgview.set(i - 1, cell);

            //cell.animate().x(second.getLeft()).y(second.getTop()).setDuration(50).start();
            //second.animate().x(cell.getLeft()).y(cell.getTop()).setDuration(50).start();
            cell.setLayoutParams(second.getLayoutParams());
            second.setLayoutParams(temp.getLayoutParams());


            second.setId(temp.getId());
            cell.setId(second.getId());
        }
        //Blank space to the left
        else if(second.getLayoutParams()==null){
            imgview.set(i - 1, cell);
            ImageView temp = new ImageView(this);
            imgview.set(i,temp);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //cell.animate().x(cell.getLeft() - rlx / column).y(cell.getTop()).setDuration(50).start();
            params.leftMargin += cell.getLeft() - rlx / column;
            params.topMargin = cell.getTop();
            cell.setLayoutParams(params);

        }


        testGravity();
        checkTileMatches();

    }

    //tests Gravity of tiles, moves any tiles that need to
    // be moved down until they are sitting on bottom or another tile.
    public void testGravity(){
        //Heading through our ArrayList, disregarding the bottom row, as it will not have gravity.
        for(int i=(imgview.size()-12); i>0; i--) {
            ImageView cell = imgview.get(i);
            //Only apply gravity to ImageViews with Layout Params
            if (imgview.get(i).getLayoutParams() != null) {
                int j = i;
                //Apply as many times as necessary.
                while (imgview.get(j + 6).getLayoutParams() == null){
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cell.getLayoutParams();
                    params.topMargin += rly / row;
                    cell.setLayoutParams(params);
                    imgview.set(j + 6, cell);
                    ImageView temp = new ImageView(this);
                    imgview.set(j, temp);
                    if(j<66) {
                        j += 6;
                    }
                }

            }
        }
    }

    //Update and print score on screen.
    public void scoreTiles(int newscore){
        TextView score = (TextView) findViewById(R.id.score);
        int scoretotal = Integer.valueOf(score.getText().toString()) + newscore;
        score.setText(scoretotal+"");
        if(scoretotal>=10000){winState();}
    }

    //Check all tiles for matches, move from bottom right to top left.
    public void checkTileMatches(){
        //Loop through every tile for matches from bottom.
        //Should be 77 from first iteration.
        for(int i=72; i>0; i--) {
            //ImageView cell = imgview.get(i);
            if(imgview.get(i).getLayoutParams()!=null) {
                testTile(i);
            }

        }

        testGravity();

        if(combo>1){
            combo=1;
            checkTileMatches();
        }



    }

    //Subsequent method for checking each Tile.
    public void testTile(int i){

        //assign ints to variables recursively based on number of adjacent same tiles.
        int left = checkTileLeft(i);
        int up = checkTileUp(i);
        //int down = checkTileDown(i);
        int right;
        int score = 0;
        //Both iterations of left/right & up/down matches taken care of here, sole left/right and up/down matches taken care of after that.
        if(left>=2){
            for(int h = 0; h<=left; h++){

                up = checkTileUp(i-h);
                if(up<2){up=0;}

                else if(up>=2){
                    //Get rid of vertical tiles.
                    for(int u=0; u<=up; u++){
                        score+=100 * combo;
                        ImageView cell = imgview.get(i-(u*6));
                        cell.setImageDrawable(null);
                        ImageView temp = new ImageView(this);
                        imgview.set(i-(u*6), temp);
                    }
                }
            }
            //Now get rid of horizontal tiles.
            for (int l = 0; l < left + 1; l++) {
                score += 100 * combo;
                ImageView cell = imgview.get(i - l);
                cell.setImageDrawable(null);
                ImageView temp = new ImageView(this);
                imgview.set(i - l, temp);
            }
            combo++;
            scoreTiles(score);
        }
        else if(up>=2){
            for(int h = up; h>=0; h--){
                //check left and right for this tile, if no left/right, up is resolved below.
                int iter = i-(h*6);
                left = checkTileLeft(iter);
                right = checkTileRight(iter);

                //Match Left & Right in Up Match
                if(left + right >=2){
                    for (int l = left; l >0; l--) {
                        score += 100 * combo;
                        ImageView cell = imgview.get(iter - l);
                        cell.setImageDrawable(null);
                        ImageView temp = new ImageView(this);
                        imgview.set(iter - l, temp);

                    }
                    for (int r = right; r >0; r--) {
                        score += 100 * combo;
                        ImageView cell = imgview.get(iter + r);
                        cell.setImageDrawable(null);
                        ImageView temp = new ImageView(this);
                        imgview.set(iter + r, temp);

                    }
                }
            }
            //Match up
            for(int u=0; u<=up; u++){
                score += 100 * combo;
                ImageView cell = imgview.get(i-(u*6));
                cell.setImageDrawable(null);
                ImageView temp = new ImageView(this);
                imgview.set(i-(u*6), temp);
            }
            combo++;
            scoreTiles(score);
        }

    }

    public int checkTileLeft(int i){


        if(i==7 || i==13 || i==19 || i==25 || i==31 || i==37 || i==43 || i==49 || i==55 || i==61 || i==67){
            return 0;
        }
        else if(imgview.get(i-1).getLayoutParams()==null){
            return 0;
        }
        else if(imgview.get(i).getDrawable().getConstantState()!=imgview.get(i-1).getDrawable().getConstantState()){
            return 0;
        }
        else{
            return 1 + checkTileLeft(i-1);
        }


    }

    public int checkTileRight(int i) {


        if (i == 12 || i == 18 || i == 24 || i == 30 || i == 36 || i == 42 || i == 48 || i == 54 || i == 60 || i == 66 || i == 72) {
            return 0;
        }
        else if (imgview.get(i + 1).getLayoutParams() == null) {
            return 0;
        }
        else if (imgview.get(i).getDrawable().getConstantState() != imgview.get(i + 1).getDrawable().getConstantState()) {
            return 0;
        }
        else {
            return 1 + checkTileRight(i + 1);
        }

    }

    public int checkTileUp(int i){
        if (i <= 7) {
            return 0;
        }
        else if(imgview.get(i-6).getLayoutParams()==null){
            return 0;
        }
        else if(imgview.get(i).getDrawable().getConstantState() != imgview.get(i-6).getDrawable().getConstantState()){
            return 0;
        }
        else{
            return 1 + checkTileUp(i-6);
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @NonNull
    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public void onChronometerTick(Chronometer chrono) {

        long milliseconds = SystemClock.elapsedRealtime() - chrono.getBase();
        long seconds = milliseconds/1000;

        if (seconds%10==0 && seconds!=0) {

            newBottomRow();


        }
    }
}
