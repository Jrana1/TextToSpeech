package de.fra_uas.fb2.mobiledevices.textToSpeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {


    private TextView coundDownText;
    private Locale selectedLocale;
    private  String languageSelectedByMachine;
    private HashMap<String, String> mp;
    private TextToSpeech mTTS;
    private ImageView goBack;
    private TextView score;
    private SeekBar speed;
    private  SeekBar pitch;
    private Button sayWord;
    private RadioGroup options;
    private Button submit;
    private Button reset;
    private Button playAgain;
    private TextView msg1;
    private int count=0;
    private int total=0;
    private TextView heading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        goBack = findViewById(R.id.goBack);
        playAgain=findViewById(R.id.playAgain);
        reset=findViewById(R.id.reset);
        submit=findViewById(R.id.submit);
        options=findViewById(R.id.options);
        sayWord=findViewById(R.id.sayWord);
        pitch=findViewById(R.id.pitch2);
        speed=findViewById(R.id.speed2);
        coundDownText=findViewById(R.id.animatedText);
        msg1=findViewById(R.id.msg4);
        score=findViewById(R.id.score);
        heading=findViewById(R.id.heading);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        heading.setText(String.format("Welcome %s to Accent Guessing Game!!",name));
        mp=new HashMap<>();
        mp.put("english", "en");
        mp.put("german", "de");
        mp.put("french", "fr");
        mp.put("italian", "it");
        selectedLocale=new Locale("en");
        makeInVisible();
        sayWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++count;
                sayWord.setEnabled(false);
                coundDownText.setVisibility(View.VISIBLE);
                handleCountDownAnimation();
               // handleGame();
                score.setText(String.format("%s/%s",total,count));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedButtonId = options.getCheckedRadioButtonId();
                RadioButton selectedBtn = findViewById(selectedButtonId);
                submit.setEnabled(false);
                if(selectedBtn==null){
                    Toast.makeText(Game.this, "No language has been selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                //  Toast.makeText(Game.this, "Test "+ languageSelectedByMachine+" "+selectedBtn.getText().toString().toLowerCase(), Toast.LENGTH_SHORT).show();
                if(selectedBtn.getText().toString().equalsIgnoreCase(languageSelectedByMachine)) {
                    ++total;
                    score.setText(String.format("%s/%s",total,count));
                }

                    for(int i=0;i<options.getChildCount();i++){
                        RadioButton tmp = (RadioButton) options.getChildAt(i);
                        if(tmp.getText().toString().equalsIgnoreCase(languageSelectedByMachine)){
                            tmp.setTextColor(Color.GREEN);
                        }
                        else{
                            tmp.setTextColor(Color.RED);
                        }
                    }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total=0;
                count=0;
                makeInVisible();
                coundDownText.setVisibility(View.GONE);
                score.setText(String.format("%s/%s",total,count));
                sayWord.setEnabled(true);
                options.clearCheck();
                submit.setEnabled(true);
                for(int i=0;i<options.getChildCount();i++){
                    RadioButton tmp = (RadioButton) options.getChildAt(i);
                    tmp.setTextColor(Color.BLACK);
                }
            }
        });

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeInVisible();
                sayWord.setEnabled(true);
                options.clearCheck();
                submit.setEnabled(true);
                for(int i=0;i<options.getChildCount();i++){
                    RadioButton tmp = (RadioButton) options.getChildAt(i);
                    tmp.setTextColor(Color.BLACK);
                }
            }
        });

        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){

                    //Toast.makeText(MTextToSpeech.this, "selected locale "+selectedLocale, Toast.LENGTH_SHORT).show();
                    int result = mTTS.setLanguage(selectedLocale);
                    if(result== TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(Game.this, "Language not supported! ", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Game.this, "Error while init!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Game.this,MTextToSpeech.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    private  void handleCountDownAnimation(){

        new CountDownTimer(4000,1000){

            @Override
            public void onTick(long l) {

                int secondRemaining = (int)(l/1000);
                coundDownText.setText(" "+String.valueOf(secondRemaining)+" ");
                coundDownText.animate().alpha(0f).setDuration(700).start();
                coundDownText.animate().alpha(1f).setDuration(700).start();
            }

            @Override
            public void onFinish() {
                coundDownText.setText("Go!!");
                handleGame();

            }
        }.start();
    }

    @Override
    protected  void onDestroy() {

        if(mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    private void handleGame(){

        List<String> english = Arrays.asList(getResources().getStringArray(R.array.english_words));
        List<String> german = Arrays.asList(getResources().getStringArray(R.array.german_words));
        List<String> french = Arrays.asList(getResources().getStringArray(R.array.french_words));
        List<String> italian = Arrays.asList(getResources().getStringArray(R.array.italian_words));
        HashMap<String,List<String>> wordsMap=new HashMap<>();
        wordsMap.put("german",german);
        wordsMap.put("english",english);
        wordsMap.put("french",french);
        wordsMap.put("italian",italian);
        HashMap<Integer,String>languageMap=new HashMap<>();
        languageMap.put(0,"german");
        languageMap.put(1,"english");
        languageMap.put(2,"french");
        languageMap.put(3,"italian");
        Random random = new Random();
        int randomLang = random.nextInt(4);
        languageSelectedByMachine=languageMap.get(randomLang);
        int randomWord = random.nextInt(10);
        String selectedWord = wordsMap.get(languageSelectedByMachine).get(randomWord);
        selectedLocale=new Locale(mp.get(languageSelectedByMachine));
        /*mTTS.setLanguage(selectedLocale);
        Toast.makeText(this, languageSelectedByMachine+" "+selectedWord+" "+selectedLocale, Toast.LENGTH_SHORT).show();
        speak(selectedWord);*/

        int result = mTTS.setLanguage(selectedLocale);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(this, "Language not supported!", Toast.LENGTH_SHORT).show();
        } else {
          //  Toast.makeText(this, languageSelectedByMachine + " " + selectedWord + " " + selectedLocale, Toast.LENGTH_SHORT).show();
            speak(selectedWord);
        }

    }
    private void makeInVisible(){

          msg1.setVisibility(View.INVISIBLE);
          options.setVisibility(View.INVISIBLE);
          coundDownText.setVisibility(View.INVISIBLE);
          submit.setVisibility(View.INVISIBLE);
          playAgain.setVisibility(View.GONE);
    }

    private void makeVisible(){

        msg1.setVisibility(View.VISIBLE);
        options.setVisibility(View.VISIBLE);
        //coundDownText.setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);
        playAgain.setVisibility(View.VISIBLE);
    }

    private void speak(String text){

        float pitch1 = (float)pitch.getProgress()/50;
        if(pitch1<0.1) pitch1=0.1f;
        float speed1 = (float) speed.getProgress()/50;
        if(speed1<0.1)speed1=0.1f;
        mTTS.setPitch(pitch1);
        mTTS.setSpeechRate(speed1);
         mTTS.speak(text, TextToSpeech.QUEUE_FLUSH,null);
         makeVisible();

    }
}