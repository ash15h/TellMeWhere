package com.example.tellmewhere;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SPEAK_REQUEST = 1000;

    //UI components
    private TextView mTextView;
    private Button mButton;

    public static  CountryDataSource sCountryDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI components
        mTextView = findViewById(R.id.text_view);
        mButton = findViewById(R.id.speak_button);

        //setting on-click listeners
        mButton.setOnClickListener(MainActivity.this);

        //inserting the country names to be detected.

        Hashtable<String,String> country_and_messages = new Hashtable<>();
        country_and_messages.put("Madagascar","Welcome to Madagascar");
        country_and_messages.put("India","Welcome to India");
        country_and_messages.put("China","Welcome to China");
        country_and_messages.put("United States of America","Welcome to the USA");
        country_and_messages.put("Patna","Welcome to Patna");
        country_and_messages.put("Delhi Airport","Delhi Airport");
        sCountryDataSource = new CountryDataSource(country_and_messages);

        PackageManager packageManager = this.getPackageManager();

        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities
                (new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);

        if(resolveInfoList.size()>0)
        {
            Toast.makeText(MainActivity.this,
                    "Your device supports speech recognition.",Toast.LENGTH_LONG).show();
        }

        else
        {
            Toast.makeText(MainActivity.this,
                    "Your device does not support speech recognition.",Toast.LENGTH_LONG).show();
        }
    }


    private void voiceListener()
    {
        Intent voice_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        voice_intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Talk to me");
        voice_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice_intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);

        startActivityForResult(voice_intent,SPEAK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SPEAK_REQUEST && resultCode==RESULT_OK) {

            ArrayList<String> voice_words =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            float[] confidence_levels = data.getFloatArrayExtra
                    (RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

//            int index = 0;
//            for(String string:voice_words)
//            {
//                if(confidence_levels!=null&&index<confidence_levels.length)
//                {
//                    mTextView.setText("You said : "+string+" and the probablity that we heard that right is : "+confidence_levels[index]);
//                }
//            }

            String country_matched =
                    sCountryDataSource.minimumConfidenceLevelOfUserWords(voice_words, confidence_levels);

            //switching to maps activity and passing the country name that the user said
            Intent my_map_intent = new Intent(MainActivity.this,MapsActivity.class);
            my_map_intent.putExtra(sCountryDataSource.COUNTRY_KEY,country_matched);
            startActivity(my_map_intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.speak_button:
                voiceListener();
                break;
        }
    }
}