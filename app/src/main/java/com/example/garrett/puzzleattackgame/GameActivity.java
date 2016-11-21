package com.example.garrett.puzzleattackgame;


import android.app.Activity;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;



public class GameActivity extends Activity implements List, Chronometer.OnChronometerTickListener{

    int column = 6;
    int row = 13;
    int totalrc = column * row;

    List<ImageView> imgview = new ArrayList<>();

    int rlx;
    int rly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        RelativeLayout gridLayout = (RelativeLayout)findViewById(R.id.playGrid);

        final float scale = gridLayout.getContext().getResources().getDisplayMetrics().density;
        rlx = (int) (300 * scale + 0.5f);
        rly = (int) (500 * scale + 0.5f);

        //Initial population of Grid and seeding of ImageView List
        popGrid();

        //Begin clock at 00:00.
        Chronometer chro = (Chronometer) findViewById(R.id.chronometer2);
        chro.setOnChronometerTickListener(this);
        chro.start();

        //Scan the board, and get rid of initial matches,
        //then set scoring flag so score can be counted
        //scanBoard();

    }

    //TO CHANGE: Make this method scan the board and eliminate initial matches.
    public void scanBoard() {
        RelativeLayout gridLayout = (RelativeLayout)findViewById(R.id.playGrid);

        for(int i=0; i<imgview.size(); i++)
        {
            ImageView cell = imgview.get(i);
            if(cell.getDrawable()==null){
                continue;
            }
            if(cell.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.red).getConstantState()) {

                ImageView second = imgview.get(i - 1);
                if (second.getLayoutParams() != null){
                    ImageView temp = new ImageView(this);
                    temp.setLayoutParams(cell.getLayoutParams());
                    temp.setImageResource(R.drawable.red);
                    temp.setId(cell.getId());

                imgview.set(i, second);
                imgview.set(i - 1, cell);

                cell.setLayoutParams(second.getLayoutParams());
                second.setLayoutParams(temp.getLayoutParams());


                second.setId(temp.getId());
                cell.setId(second.getId());
                }

            }

            gridLayout.requestLayout();
        }

    }

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

            //tiles[i] = new Tile(i);

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
            setListener(cell, i);

            //Set Imageview Params for placement on RelativeLayout
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = c * rlx / column;
            params.topMargin = r * rly / row;
            cell.setLayoutParams(params);
            gridLayout.addView(cell, params);

            c++;
        }  //end of for loop for populating Grid
    }

    public void newBottomRow() {
        TextView level = (TextView) findViewById(R.id.level);
        level.setText(Integer.toString(imgview.size()-6));
        for(int i=0; i<=imgview.size()-6; i++){
            ImageView cell = imgview.get(i);

            if(imgview.get(i).getLayoutParams()!=null){
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)cell.getLayoutParams();
                params.topMargin -= rly / row;
                cell.setLayoutParams(params);

                imgview.set(i - 6, cell);
                ImageView temp = new ImageView(this);
                imgview.set(i, temp);

                //Populate bottom row after moving up.
                if(i==67 || i==68 || i==69 || i==70 || i==71 || i==72) {
                    if (imgview.get(i).getLayoutParams() != null) {

                        ImageView newTile = cell;

                        params.topMargin -= rly / row;
                        cell.setLayoutParams(params);

                        imgview.set(i - 6, cell);
                        selectTile(newTile);
                        imgview.set(i, newTile);
                    }
                }
            }
        }


    }

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

    public void setListener(ImageView cell, int i){

        final ImageView cell1 = cell;

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

        int i = imgview.indexOf(cell);
        ImageView second = imgview.get(i+1);
        //Swap at right edge disallowed
        if(i==12 || i==18 || i==24 || i==30 || i==36 || i==42 || i==48 || i==54 || i==60 || i==66 || i==72 || i==78){

        }
        //Tile to the right.
        else if(second.getLayoutParams()!=null) {
            ImageView temp = new ImageView(this);
            temp.setLayoutParams(cell.getLayoutParams());
            temp.setId(cell.getId());

            imgview.set(i, second);
            imgview.set(i + 1, cell);

            cell.setLayoutParams(second.getLayoutParams());
            second.setLayoutParams(temp.getLayoutParams());

            second.setId(temp.getId());
            cell.setId(second.getId());
        }
        //Blank space to the right.
        else if(second.getLayoutParams()==null){
            imgview.set(i + 1, cell);
            ImageView temp = new ImageView(this);
            imgview.set(i,temp);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = cell.getLeft() + rlx / column;
            params.topMargin = cell.getTop();
            cell.setLayoutParams(params);

        }

        //TextView level = (TextView) findViewById(R.id.level);
        //level.setText(Integer.toString(imgview.indexOf(cell)));
        testGravity();
        //checkTileMatches();

    }

    public void swapLeft(ImageView cell){

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
            params.leftMargin += cell.getLeft() - rlx / column;
            params.topMargin = cell.getTop();
            cell.setLayoutParams(params);

        }

        //TextView level = (TextView) findViewById(R.id.level);
        //level.setText(Integer.toString(imgview.indexOf(cell)));
        testGravity();
        //checkTileMatches();
    }

    public void testGravity(){

        for(int i=imgview.size()-1; i>0; i--) {

            ImageView cell = imgview.get(i);
            if (imgview.get(i).getLayoutParams() != null) {

                if(i<67){
                    int j = i;
                    while(imgview.get(j+6).getLayoutParams()==null) {


                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)cell.getLayoutParams();
                            params.topMargin += rly / row;
                            cell.setLayoutParams(params);

                            imgview.set(j + 6, cell);
                            ImageView temp = new ImageView(this);
                            imgview.set(j, temp);

                            j+=6;

                    }


                }
            }
        }
    }

    public void scoreTiles(int newscore){
        TextView score = (TextView) findViewById(R.id.score);
        //int oldscore = Integer.valueOf(score.getText().toString());
        score.setText("match");
    }

    public void checkTileMatches(){
        //RelativeLayout gridLayout = (RelativeLayout) findViewById(R.id.playGrid);
        //Loop through every tile for matches from bottom.
        //Should be 77 from first iteration.
        for(int i=72; i>0; i--) {
            //ImageView cell = imgview.get(i);
            if(imgview.get(i).getLayoutParams()!=null) {

                testTile(i);


            }

        }

    }

    public void testTile(int i){


        int left = checkTileLeft(i);
        int right = checkTileRight(i);
        int up = checkTileUp(i);
        int down = checkTileDown(i);
        int score = 0;

        //No match Left/Right
        if(left + right < 2){

        }
        //Match Left/Right
        else if(left + right >=2){
            //Match Left/Right and Up/Down
            if(up + down >=2){

            }
            //Only Match Left/Right
            else{
                TextView level = (TextView) findViewById(R.id.level);
                level.setText(Integer.toString(left));
                for(int l=0; l<left; l++){
                    score+=100;
                    ImageView temp = new ImageView(this);
                    imgview.set(i-l,temp);
                    testGravity();
                }

                for(int r=0; r<=right; r++){
                    score+=100;
                    ImageView temp = new ImageView(this);
                    imgview.set(i+r,temp);
                    testGravity();
                }

                //scoreTiles(score);
            }
        }

        //No match Up/Down
        else if(up + down < 2){

        }
        //Match only Up/Down
        else if(up + down >=2){

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

    public int checkTileDown(int i){
        if(i==67 || i==68 || i==69 || i==70 || i==71 || i==72){
            return 0;
        }
        else if(imgview.get(i+6).getLayoutParams()==null){
            return 0;
        }
        else if(imgview.get(i).getDrawable().getConstantState()!=imgview.get(i+6).getDrawable().getConstantState()){
            return 0;
        }
        else{
            return 1 + checkTileDown(i+6);
        }
    }

    public int checkTileUp(int i){
        if(imgview.get(i-6).getLayoutParams()==null){
            return 0;
        }
        else if(imgview.get(i).getDrawable().getConstantState()!=imgview.get(i-6).getDrawable().getConstantState()){
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
