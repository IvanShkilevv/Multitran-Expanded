package com.example.android.multitranexpanded;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String DEFAULT_URL = "https://www.multitran.com/m.exe";
    private ArrayList <String> translationsList = new ArrayList<>();
    private Languages languages = new Languages();

    public ArrayList <String> getTranslationsList () {
        return translationsList;
    }

    public String buildUrl(String inputText, String inputLanguage, String outputLanguage) {
        Uri baseUri = Uri.parse(DEFAULT_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        int inputLanguageId = languages.getLanguageId(inputLanguage);
        int outputLanguageId = languages.getLanguageId(outputLanguage);

        if (inputLanguageId > 0 && outputLanguageId > 0) {
            uriBuilder.appendQueryParameter("l1", String.valueOf(inputLanguageId));
            uriBuilder.appendQueryParameter("l2", String.valueOf(outputLanguageId));
            uriBuilder.appendQueryParameter("s", inputText);
        }

        return uriBuilder.toString();
    }

    public String parseHtml (String url) {
        Log.d(LOG_TAG, "parseHtml started");
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get();
            Elements dataFromHtml = document
                    .select("div.middle_col")
                    .select("table")
                    .select("[width=100%]")
                    .select("td.trans")
                    .select("a[href]");

            for (int i = 0; i < dataFromHtml.size(); i++) {
                Element element = dataFromHtml.get(i);
                if (element.hasAttr("title") || element.text().contains("http")  || element.text().contains("html")) {
                    dataFromHtml.remove(i);
                }
            }

            translationsList.clear();

            for (int i = 0; i < dataFromHtml.size(); i++) {
                int number = i + 1;
                String translation = number + ") " + dataFromHtml.get(i).text();
                translationsList.add(translation);
            }

        } catch (IOException e) {
            Log.d(LOG_TAG, "caused IOException");
        }

        return null;
    }




}
