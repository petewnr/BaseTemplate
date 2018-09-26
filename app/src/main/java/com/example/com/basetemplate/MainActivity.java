package com.example.com.basetemplate;

import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ubtechinc.cruzr.sdk.ros.RosRobotApi;
import com.ubtechinc.cruzr.serverlibutil.interfaces.InitListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public ListView mList;


    public void voiceinputbuttons() {
        mList = (ListView) findViewById(R.id.list);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Code for ROS
        RosRobotApi.get().initializ(getApplicationContext(), new InitListener() {
            @Override
            public void onInit() {

            }
        });

    }




    public void DoYourActionHere(String mp3name) {}

    public void FirstStep(View view) {

        voiceinputbuttons();

        int resId = MainActivity.this.getResources().getIdentifier(
                "firstaudio",
                "raw",
                MainActivity.this.getPackageName()
        );
        RelativeLayout speakingAnimation = (RelativeLayout) this.findViewById(R.id.speakingAnimation);
        speakingAnimation.setVisibility(View.VISIBLE);

        waveArms();

        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, resId);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                SecondStep();
            }
        });

    }





    public void SecondStep() {

        RelativeLayout speakingAnimation = (RelativeLayout) this.findViewById(R.id.smilingAnimation);
        speakingAnimation.setVisibility(View.VISIBLE);
        startVoiceRecognitionActivity();

    }

    public void Reset() {

        RelativeLayout smilingAnimation = (RelativeLayout) this.findViewById(R.id.smilingAnimation);
        smilingAnimation.setVisibility(View.INVISIBLE);
        RelativeLayout speakingAnimation = (RelativeLayout) this.findViewById(R.id.speakingAnimation);
        speakingAnimation.setVisibility(View.INVISIBLE);

    }

    public void ThirdStepOptionOne() {

        int resId = MainActivity.this.getResources().getIdentifier(
                "transcript_bathroom",
                "raw",
                MainActivity.this.getPackageName()
        );
        RelativeLayout speakingAnimation = (RelativeLayout) this.findViewById(R.id.speakingAnimation);
        speakingAnimation.setVisibility(View.VISIBLE);

        waveArms();

        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, resId);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                Reset();
            }
        });

    }

    public void ThirdStepOptionTwo() {

        int resId = MainActivity.this.getResources().getIdentifier(
                "transcript_library",
                "raw",
                MainActivity.this.getPackageName()
        );
        RelativeLayout speakingAnimation = (RelativeLayout) this.findViewById(R.id.speakingAnimation);
        speakingAnimation.setVisibility(View.VISIBLE);

        waveArms();

        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, resId);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                Reset();
            }
        });

    }



    public void waveArms() {
        Random rand = new Random();
        int randomnumber = rand.nextInt(3) + 1;
        RosRobotApi.get().run("talk" + randomnumber);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }



    /**
     * On activity result.
     *
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data the data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {

            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));

            int totalElements = matches.size();
            for(int index=0; index < totalElements; index++) {
                System.out.println(matches.get(index));
                String test = (String) matches.get(0);
                if (test.contains("bathroom")) {
                    ThirdStepOptionOne();
                    break;
                }
            }

            for(int index=0; index < totalElements; index++) {
                System.out.println(matches.get(index));
                String test = (String) matches.get(0);
                if (test.contains("library")) {
                    ThirdStepOptionTwo();
                    break;
                }
            }

        }
    }


}
