package com.example.android.multitranexpanded;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class AsyncHtmlParser  extends AsyncTask<String, Void, Void> {
    AsyncHtmlParserDelegate delegateInstance;
    private QueryUtils queryUtils = new QueryUtils();

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getAppContext(), android.R.layout.simple_list_item_1, translationsList);
        delegateInstance.adapterDidSet(adapter);
    }
}
