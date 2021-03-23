package com.pictionary;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Phrase")
public class Phrase extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_DIFFICULTY = "difficulty";

    public String getName() { return getString(KEY_NAME); }

    public void setName(String name) { put(KEY_NAME, name); }

    public String getDifficulty() {
        String difficulty;
        switch(getInt(KEY_DIFFICULTY)) {
            case 1:
                difficulty = "easy";
                break;
            case 2:
                difficulty = "medium";
                break;
            case 3:
                difficulty = "hard";
                break;
            case 4:
                difficulty = "expert";
                break;
            default:
                difficulty = "unknown";
        }
        return difficulty;
    }

    public void setDifficulty(int difficulty) { put(KEY_DIFFICULTY, difficulty); }
}
