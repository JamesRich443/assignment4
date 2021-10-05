package com.codepath.apps.restclienttemplate.models;

import android.provider.ContactsContract;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class tweet {
    @ColumnInfo
    @PrimaryKey
    public long id;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public  String createdAt;
    @ColumnInfo
    public  long userId;

    @Ignore
    public User user;
    public static tweet fromJson(JSONObject jsonObject) throws JSONException {
        tweet Tweet = new tweet();
        Tweet.body= jsonObject.getString("text");
        Tweet.createdAt= jsonObject.getString( "created_at");

        Tweet.id=jsonObject.getLong("id");
        User user= User.fromJson(jsonObject.getJSONObject("user"));
        Tweet.user=User.fromJson( jsonObject.getJSONObject("user"));
        Tweet.user=user;
        Tweet.userId= user.id;

        return Tweet;
    }
    public static List<tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<tweet> tweets=new ArrayList<>();
        for (int i = 0; i < jsonArray.length();i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}

