package com.example.android.multitranexpanded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ConstraintLayout rootLayout;
    private Spinner inputLanguageSpinner;
    private Spinner outputLanguageSpinner;
    private EditText inputTextView;
    private Button translateButton;
    private QueryUtils queryUtils = new QueryUtils();
    private ListView translationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.constraint_layout);
        inputLanguageSpinner =  findViewById(R.id.input_language_spinner);
        outputLanguageSpinner =  findViewById(R.id.output_language_spinner);
        inputTextView = findViewById(R.id.input_text_view);
        translateButton = findViewById(R.id.translate_button);
        translationsListView = findViewById(R.id.translations_list_view);

        setupSpinners();

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = inputTextView.getText().toString();
                String inputLanguage = inputLanguageSpinner.getSelectedItem().toString();
                String outputLanguage = outputLanguageSpinner.getSelectedItem().toString();

                if (checkUserInput(inputText) && checkNetworkConnection()) {
                    String url = queryUtils.buildUrl(inputText, inputLanguage, outputLanguage);

                    new AsyncHtmlParser().execute(url);

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
        int spinnerItemNumber = new Languages()
                .getSpinnerItemNumber(Languages.russian);
        inputLanguageSpinner.setSelection(spinnerItemNumber);
    }

    private boolean checkUserInput (String inputText) {
     boolean userInputPresence = false;

     if (! inputText.isEmpty()) {
         userInputPresence = true;
     }
     else {
         Snackbar.make(rootLayout, R.string.snackbar_input_request, Snackbar.LENGTH_SHORT).show();
     }

     return userInputPresence;
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = false;
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;
        }
        else {
            Snackbar.make(rootLayout, R.string.snackbar_no_internet_connection, Snackbar.LENGTH_SHORT).show();
        }

        return isConnected;
    }

    public class AsyncHtmlParser  extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            queryUtils.parseHtml(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //updating ListView
            ArrayList<String> translationsList = queryUtils.getTranslationsList();
            ArrayAdapter<String> adapter
                    = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, translationsList);
            translationsListView.setAdapter(adapter);
        }
    }




}
