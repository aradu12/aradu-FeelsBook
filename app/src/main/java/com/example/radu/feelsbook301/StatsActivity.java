package com.example.radu.feelsbook301;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** This class handles displaying the feeling counts
 * Improvements: it would be nice to make this into an animated bar graph instead of a list.
*/
public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(StatsActivity.this,MainActivity.class);
                startActivity(backIntent);
            }
        });
        
        // retrieve counts from caller
        Intent intent = getIntent();
        int loveCount = intent.getIntExtra("loveCount",0);
        int joyCount = intent.getIntExtra("joyCount",0);
        int surpriseCount = intent.getIntExtra("surpriseCount",0);
        int sadnessCount = intent.getIntExtra("sadnessCount",0);
        int angerCount = intent.getIntExtra("angerCount",0);
        int fearCount = intent.getIntExtra("fearCount",0);

        // set counts of each feeling
        TextView love = (TextView) findViewById(R.id.loveStat);
        String msg = "Love    ||   " + loveCount + " feelings";
        love.setText(msg);

        TextView joy = (TextView) findViewById(R.id.joyStat);
        msg = "Joy    ||   " + joyCount + " feelings";
        joy.setText(msg);

        TextView surprise = (TextView) findViewById(R.id.surpriseStat);
        msg = "Surprise    ||   " + surpriseCount + " feelings";
        surprise.setText(msg);

        TextView sadness = (TextView) findViewById(R.id.sadnessStat);
        msg = "Sadness    ||   " + sadnessCount + " feelings";
        sadness.setText(msg);

        TextView anger = (TextView) findViewById(R.id.angerStat);
        msg = "Anger    ||   " + angerCount + " feelings";
        anger.setText(msg);

        TextView fear = (TextView) findViewById(R.id.fearStat);
        msg = "Fear    ||   " + fearCount + " feelings";
        fear.setText(msg);

    }
}
