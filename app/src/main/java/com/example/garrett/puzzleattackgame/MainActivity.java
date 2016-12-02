package com.example.garrett.puzzleattackgame;/*Main Activity
Will act as main title screen, with link to begin the game, and possible links to High Score Leaderboards
onCreate
onClick
Will need resume and other functions from Game Activity perhaps.
Garrett Pinkney
Sept-Dec 2016 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button listener and import
        ImageView buttonPlay = (ImageView)findViewById(R.id.imageButton);
        //Button buttonPlay = (Button)findViewById(R.id.button1);
        buttonPlay.setOnClickListener(this);
    }

    @Override //Upon clicking the Play Button, goes to Game Activity screen.
    public void onClick(View view) {

        Intent i;
        i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

}
