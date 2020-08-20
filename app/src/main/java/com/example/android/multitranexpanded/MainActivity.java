package com.example.android.multitranexpanded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.android.multitranexpanded.QueryUtils;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout rootLayout;
    private Spinner inputLanguageSpinner;
    private Spinner outputLanguageSpinner;
    private EditText inputTextView;
    private Button translateButton;
    private QueryUtils queryUtils = new QueryUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.constraint_layout);
        inputLanguageSpinner =  findViewById(R.id.input_language_spinner);
        outputLanguageSpinner =  findViewById(R.id.output_language_spinner);
        inputTextView = findViewById(R.id.input_text_view);
        translateButton = findViewById(R.id.translate_button);

        setupSpinners();

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = inputTextView.getText().toString();
                String inputLanguage = inputLanguageSpinner.getSelectedItem().toString();
                String outputLanguage = outputLanguageSpinner.getSelectedItem().toString();

                if (checkUserInput(inputText) && checkNetworkConnection()) {
                    String url = queryUtils.buildUrl(inputText, inputLanguage, outputLanguage);
//                    webView.loadUrl(url);
                }
            }
        });


    }

    private void setupSpinners () {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputLanguageSpinner.setAdapter(adapter);
        outputLanguageSpinner.setAdapter(adapter);

        //setting default input language (russian)
        inputLanguageSpinner.setSelection(1);
    }

    private boolean checkUserInput (String inputText) {
     boolean userInputPresence = false;

     if (! inputText.isEmpty()) {
         userInputPresence = true;
     }
     else {
         Snackbar.make(rootLayout, "Введите слово, которое хотите перевести", Snackbar.LENGTH_SHORT).show();
     }

     return userInputPresence;
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = false;
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;
        }
        else {
            Snackbar.make(rootLayout, "Нет интернет соединения", Snackbar.LENGTH_SHORT).show();
        }

        return isConnected;
    }




}
