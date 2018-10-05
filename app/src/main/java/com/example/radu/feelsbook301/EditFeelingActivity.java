package com.example.radu.feelsbook301;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/** This class handles delete or edit of a logged feeling
 * Feelings are stored in an ArrayList
 *
 *
 * @note
 *       Users can change the type of emotion using a radio button. If no radio button is selected
 *       the emotion type does not change.
 *       Improvements:
 *           - it would be better to have the radio button checked to the current emotion on open
*/
public class EditFeelingActivity extends AppCompatActivity {
    private ArrayList<Feeling> feelings;
    private static final String FILENAME = "FeelLog.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_feeling);
        
        // get relevant feeling data from click
        EditText feelComment = findViewById(R.id.feel_comment);
        Intent intent = this.getIntent();
        String FEEL = intent.getStringExtra("feeling");
        String DATE = intent.getStringExtra("date");
        String COMMENT = intent.getStringExtra("comment");
        setTitle(FEEL+"  ||  "+DATE);
        feelComment.setText(COMMENT);
        
        // get date using positions in DATE
        int dateYear = Integer.valueOf(DATE.substring(0,4));
        int dateMonth = Integer.valueOf(DATE.substring(5,7))-1;
        int dateDay = Integer.valueOf(DATE.substring(8,10));
        int dateHour = Integer.valueOf(DATE.substring(11,13));
        int dateMin = Integer.valueOf(DATE.substring(14,16));

        // configure time picker to current time and date
        DatePicker displayDate = findViewById(R.id.date_hint);
        displayDate.updateDate(dateYear, dateMonth, dateDay);
        TimePicker time = findViewById(R.id.time_hint);
        time.setIs24HourView(true);
        time.setCurrentHour(dateHour);
        time.setCurrentMinute(dateMin);

    }



    // choose to cancel, save, or delete edits by clicking a button
    public void navigateChoice(View view) {

        int id = view.getId();
        Button button = (Button) findViewById(id);
        DatePicker date = findViewById(R.id.date_hint);

        TimePicker time = findViewById(R.id.time_hint);
        time.setIs24HourView(true);

        // cancel editing
        if (id == R.id.back_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        // delete emotion from list
        if (id == R.id.delete_button) {
            deleteEntry(view);
        }

        Date newDate = new Date(date.getYear()-1900, date.getMonth(), date.getDayOfMonth(),time.getHour(),time.getMinute());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        
        // edit an emotion
        String editDate = dateFormatter.format(newDate);
        EditText comment = findViewById(R.id.feel_comment);
        
        // change feeling type
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rad_group);
        String editMood = null;
        int checkedMood = radioGroup.getCheckedRadioButtonId();
        
	// find out which feeling the user wants to change to
        switch (checkedMood) {
            case R.id.love:
                editMood = "Love";
                break;
            case R.id.joy:
                editMood = "Joy";
                break;
            case R.id.sadness:
                editMood = "Sadness";
                break;
            case R.id.surprise:
                editMood = "Surprise";
                break;
            case R.id.anger:
                editMood = "Anger";
                break;
            case R.id.fear:
                editMood = "Fear";
                break;

        }


        String editComment = comment.getText().toString();
        Bundle bundle = this.getIntent().getExtras();

        int index = bundle.getInt("index");
        feelings = loadFromFile();
        Feeling editFeeling = feelings.get(index);

        // save the editied feeling
        if (id == R.id.save_button) {
            if (editMood != null) {
                editFeeling.setFeeling(editMood);
            }

            editFeeling.setDate(editDate);
            editFeeling.setComment(editComment);
            Collections.sort(feelings,Feeling.RecDateComparator);
            saveInFile();
            Toast.makeText(this, "Feeling Saved", Toast.LENGTH_SHORT).show();
	    
            // return home
            startActivity(new Intent(EditFeelingActivity.this,MainActivity.class));
        }


    }

    // load data from file
    private ArrayList<Feeling> loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listfeelingsType = new TypeToken<ArrayList<Feeling>>(){}.getType();
            feelings = gson.fromJson(reader,listfeelingsType);
            return feelings;

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            feelings = new ArrayList<Feeling>();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // save new data to file
    private void saveInFile(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();
            gson.toJson(feelings,writer);
            writer.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // delete one emotion
    public void deleteEntry(View view) {
        Bundle bundle = this.getIntent().getExtras();
        int index = bundle.getInt("index");
        feelings = loadFromFile();

        // get the feeling to delete
        Feeling toEdit = feelings.get(index);
        feelings.remove(toEdit);
        saveInFile();

        // notify user of delete
        Toast.makeText(this, toEdit.getFeeling()+" Deleted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditFeelingActivity.this,MainActivity.class));


    }
}

