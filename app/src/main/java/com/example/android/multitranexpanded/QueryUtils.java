package com.example.android.multitranexpanded;

import android.net.Uri;

public class QueryUtils {
    private static final String DEFAULT_URL = "https://www.multitran.com/m.exe";

    public String buildUrl(String inputText, String inputLanguage, String outputLanguage) {
        Uri baseUri = Uri.parse(DEFAULT_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        int inputLanguageId = getLanguageId(inputLanguage);
        int outputLanguageId = getLanguageId(outputLanguage);

        if (inputLanguageId > 0 && outputLanguageId > 0) {
            uriBuilder.appendQueryParameter("l1", String.valueOf(inputLanguageId));
            uriBuilder.appendQueryParameter("l2", String.valueOf(outputLanguageId));
            uriBuilder.appendQueryParameter("s", inputText);
        }

        return uriBuilder.toString();
    }

    private int getLanguageId (String languageIdentifier) {
        int languageId;
        switch (languageIdentifier) {
            case "ENG": languageId = 1; break;
            case "RUS": languageId = 2; break;
            case "DEU": languageId = 3; break;
            case "FRA": languageId = 4; break;
            default: languageId = -1; break;
        }

        return languageId;
    }


}
