package com.pictionary;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Game")
public class Game extends ParseObject {

    public static final String KEY_ID = "objectId";
    public static final String KEY_TEAM_ONE_NAME = "teamOneName";
    public static final String KEY_TEAM_TWO_NAME = "teamTwoName";
    public static final String KEY_TEAM_ONE_SCORE = "teamOneScore";
    public static final String KEY_TEAM_TWO_SCORE = "teamTwoScore";
    public static final String KEY_CREATED_BY = "createdBy";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getId() { return getString(KEY_ID); }

    public void setTeamOneName(String name) { put(KEY_TEAM_ONE_NAME, name); }

    public void setTeamTwoName(String name) { put(KEY_TEAM_TWO_NAME, name); }

    public String getTeamOneName() { return getString(KEY_TEAM_ONE_NAME); }

    public String getTeamTwoName() { return getString(KEY_TEAM_TWO_NAME); }

    public void setTeamOneScore(int score) { put(KEY_TEAM_ONE_SCORE, score); }

    public void setTeamTwoScore(int score) { put(KEY_TEAM_TWO_SCORE, score); }

    public int getTeamOneScore() { return getInt(KEY_TEAM_ONE_SCORE); }

    public int getTeamTwoScore() { return getInt(KEY_TEAM_TWO_SCORE); }

    public void setCreatedBy(ParseUser user) { put(KEY_CREATED_BY, user); }

    public ParseUser getCreatedBy() { return getParseUser(KEY_CREATED_BY); }

    public Date getCreatedAt() { return getDate(KEY_CREATED_AT); }
}
