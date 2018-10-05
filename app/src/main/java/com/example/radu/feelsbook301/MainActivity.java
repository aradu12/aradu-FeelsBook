package com.example.radu.feelsbook301;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/** This class handles the main screen, including the history screen
 *  Feelings are kept in an arrayList
 * My decision to have the adding function and hisorty function on the same screen was inspired by lonelyTwitter
 */

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "FeelLog.sav";
    public ArrayList<Feeling> feelings = new ArrayList<Feeling>();
    private ListView feelingList;
    private ArrayAdapter<Feeling>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feelingList = findViewById(R.id.feel_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<Feeling>(this,
                R.layout.list_item,feelings);
        feelingList.setAdapter(adapter);

        // choose a feeling to edit or view
        feelingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feeling toView = feelings.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("feeling",toView.getFeeling());
                bundle.putString("date",toView.getDate());
                bundle.putString("comment",toView.getComment());
                bundle.putInt("index",feelings.indexOf(toView));
                Intent intent = new Intent(view.getContext(), EditFeelingActivity.class);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });


    }

    // credit to lonelyTwitter

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type feelingListType = new TypeToken<ArrayList<Feeling>>(){}.getType();
            feelings = gson.fromJson(reader,feelingListType);
            Collections.sort(feelings,Feeling.feelingComparator);

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            feelings = new ArrayList<Feeling>();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // returns the number of occurences of a given feeling
    public int countFeeling(String id){
        int count = 0;
        for (Feeling f: feelings){
            if (f.getFeeling().equals(id)){
                count++;
            }
        }
        return count;
    }

    // go to stats page
    public void navStats(View v) {

        Intent intent = new Intent(MainActivity.this, StatsActivity.class);
       
        intent.putExtra("loveCount",countFeeling("Love"));
        intent.putExtra("joyCount",countFeeling("Joy"));
        intent.putExtra("surpriseCount",countFeeling("Surprise"));
        intent.putExtra("sadnessCount",countFeeling("Sadness"));
        intent.putExtra("angerCount",countFeeling("Anger"));
        intent.putExtra("fearCount",countFeeling("Fear"));

        startActivity(intent);


    }
    // save data
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();
            gson.toJson(feelings,writer);
            writer.flush();
            fos.close();
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    // return name of the emotion
    public String getFeelType(View view) {
        String EmotionID = "";
        switch (view.getId()){
            case R.id.button_love:
                EmotionID = "Love";
                break;
            case R.id.button_joy:
                EmotionID = "Joy";
                break;
            case R.id.button_surprise:
                EmotionID = "Surprise";
                break;
            case R.id.button_anger:
                EmotionID = "Anger";
                break;
            case R.id.button_sadness:
                EmotionID = "Sadness";
                break;
            case R.id.button_fear:
                EmotionID = "Fear";
                break;
        }

        return EmotionID;

    }


    // get the emotion and add to our file
    public void createFeeling(View view) {
        String type = getFeelType(view);
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String feel_date = dateFormatter.format(Calendar.getInstance().getTime());
        EditText comment = findViewById(R.id.comment);
        String comment_text = comment.getText().toString();
        Feeling feeling = new Feeling(type,feel_date,comment_text);
        feelings.add(feeling);
        saveInFile();


        // two lines from stackoverflow
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        comment.getText().clear();
        Toast.makeText(this,"Added "+type + " to your feelings", Toast.LENGTH_SHORT).show();


    }

}
