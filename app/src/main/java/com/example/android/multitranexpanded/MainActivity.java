package com.example.android.multitranexpanded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements AsyncHtmlParserDelegate {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ConstraintLayout rootLayout;
    private Spinner inputLanguageSpinner;
    private Spinner outputLanguageSpinner;
    private EditText inputTextView;
    private Button translateButton;
    private QueryUtils queryUtils = new QueryUtils();
    private AsyncHtmlParser htmlParser;
    private ListView translationsListView;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

        rootLayout = findViewById(R.id.constraint_layout);
        inputLanguageSpinner = findViewById(R.id.input_language_spinner);
        outputLanguageSpinner = findViewById(R.id.output_language_spinner);
        inputTextView = findViewById(R.id.input_text_view);
        translateButton = findViewById(R.id.translate_button);
        translationsListView = findViewById(R.id.translations_list_view);

        setupSpinners();

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate();
            }
        });

    }

    private void setupSpinners() {
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

    private boolean checkUserInput(String inputText) {
        boolean userInputPresence = false;

        if (!inputText.isEmpty()) {
            userInputPresence = true;
        } else {
            Snackbar.make(rootLayout, R.string.snackbar_input_request, Snackbar.LENGTH_SHORT).show();
        }

        return userInputPresence;
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }
        boolean isConnected = false;
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;
        } else {
            Snackbar.make(rootLayout, R.string.snackbar_no_internet_connection, Snackbar.LENGTH_SHORT).show();
        }

        return isConnected;
    }

    @Override
    public void adapterDidSet(ArrayAdapter<String> adapter) {
        translationsListView.setAdapter(adapter);
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    private void translate() {
        String inputText = inputTextView.getText().toString();
        String inputLanguage = inputLanguageSpinner.getSelectedItem().toString();
        String outputLanguage = outputLanguageSpinner.getSelectedItem().toString();

        if (checkUserInput(inputText) && checkNetworkConnection()) {
            String url = queryUtils.buildUrl(inputText, inputLanguage, outputLanguage);
            htmlParser = new AsyncHtmlParser();
            htmlParser.delegateInstance = this;
            htmlParser.execute(url);
        }
    }

}
