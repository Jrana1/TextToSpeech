package de.fra_uas.fb2.mobiledevices.textToSpeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class MTextToSpeech extends AppCompatActivity {
    private TextView textView;
    private String name;
    private TextView userName;

    private TextToSpeech mTTS;
    private SeekBar seekBarSpeed;
    private SeekBar seekBarPitch;
    private Button speechBtn;
    private EditText editText;
    private ImageView clear;
    private String inputText;
    private String language;
    private Locale selectedLocale;
    private Button playgame;

    private  HashMap<String, String> mp;
    private ImageView goToHome;

    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtext_to_speech);
        Intent intent = getIntent();
        name=intent.getStringExtra("name");
        textView=findViewById(R.id.textView2);
        userName=findViewById(R.id.userName);
        userName.setText(name);
        autoCompleteTextView=findViewById(R.id.autoComplete);
        clear=findViewById(R.id.clear);
        speechBtn=findViewById(R.id.speech);
        seekBarPitch=findViewById(R.id.pitch);
        seekBarSpeed=findViewById(R.id.speed);
        editText = findViewById(R.id.edit);
        playgame = findViewById(R.id.playGame);
        goToHome=findViewById(R.id.goback2);

        mp=new HashMap<>();
        mp.put("english", "en");
        mp.put("german", "de");
        mp.put("french", "fr");
        mp.put("italian", "it");

        userName.setText(name);
        selectedLocale=new Locale("en");
        language="English";
       // String languages[] = {"English","German","French","Japanese"};
        String []languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapterItems;
        adapterItems=new ArrayAdapter<>(this,R.layout.dropdown_items,languages);
        autoCompleteTextView.setAdapter(adapterItems);

        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MTextToSpeech.this,MainActivity.class);
                startActivity(intent);
            }
        });

        playgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MTextToSpeech.this, Game.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString().toLowerCase(); // Convert selected language to lowercase


                String languageCode = mp.get(item);
                if (languageCode != null) {
                    selectedLocale = new Locale(languageCode);
                    int result = mTTS.setLanguage(selectedLocale);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MTextToSpeech.this, "Language not supported!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MTextToSpeech.this, String.format("%s has been selected!", item), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MTextToSpeech.this, "Language not supported!", Toast.LENGTH_SHORT).show();
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
                       Toast.makeText(MTextToSpeech.this, "Language not supported! ", Toast.LENGTH_SHORT).show();
                   }
               }
               else{
                   Toast.makeText(MTextToSpeech.this, "Error while init!!", Toast.LENGTH_SHORT).show();
               }
            }
        });
       clear.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               editText.setText("");
               ///Toast.makeText(MTextToSpeech.this, "clear", Toast.LENGTH_SHORT).show();
           }
       });

        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText=editText.getText().toString();
                if(editText.getText().toString().equals("")){
                    Toast.makeText(MTextToSpeech.this, "You haven't entered any text!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                editText.setText("");
               // Toast.makeText(MTextToSpeech.this, "yoo", Toast.LENGTH_SHORT).show();
                speak(inputText);
            }
        });
    }
    private void speak(String text){
        float pitch = (float)seekBarPitch.getProgress()/50;
        if(pitch<0.1) pitch=0.1f;
        float speed = (float) seekBarSpeed.getProgress()/50;
        if(speed<0.1)speed=0.1f;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH,null);

    }
    @Override
  protected  void onDestroy() {

      if(mTTS!=null){
          mTTS.stop();
          mTTS.shutdown();
      }
      super.onDestroy();
  }
  private void setLanguage(String language){

        this.language=language;
     // Toast.makeText(this, "clicked "+this.language, Toast.LENGTH_SHORT).show();
  }
  private String getLanguage(){
        return this.language;
  }
}