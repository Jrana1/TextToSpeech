package de.fra_uas.fb2.mobiledevices.textToSpeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    private TextInputLayout textInputLayout;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.button);
        textInputLayout=findViewById(R.id.textInputLayout);
        editText=findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText.getText().toString().length()>3){
                    startBtn.setEnabled(true);
                }
                else{
                    startBtn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MTextToSpeech.class);
                intent.putExtra("name",editText.getText().toString());
                startActivity(intent);
            }
        });
    }
}