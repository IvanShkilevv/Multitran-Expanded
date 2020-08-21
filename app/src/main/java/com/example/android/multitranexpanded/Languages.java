package com.example.android.multitranexpanded;

public class Languages {
    public static final String english = "ENG";
    public static final String russian = "RUS";
    public static final String deutch = "DEU";
    public static final String french = "FRA";

    public int getLanguageId (String languageIdentifier) {
        int languageId;
        switch (languageIdentifier) {
            case english: languageId = 1; break;
            case russian: languageId = 2; break;
            case deutch: languageId = 3; break;
            case french: languageId = 4; break;
            default: languageId = -1; break;
        }
        return languageId;
    }

    public int getSpinnerItemNumber(String languageIdentifier) {
        int languageId;
        switch (languageIdentifier) {
            case english: languageId = 0; break;
            case russian: languageId = 1; break;
            case deutch: languageId = 2; break;
            case french: languageId = 3; break;
            default: languageId = -1; break;
        }
        return languageId;
    }

}
