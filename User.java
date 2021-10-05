package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class User {
    @ColumnInfo
    @PrimaryKey
    public Long id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String screeName;
    @ColumnInfo
    public String profileImageUrl;


    public User() {}

    public  static User  fromJson(JSONObject jsonObject) throws JSONException {
        User user =new User();
        user.id= jsonObject.getLong("");
        user.name= jsonObject.getString("name");
        user.screeName= jsonObject.getString("screen_name");
        user.profileImageUrl= jsonObject.getString("profile_image_url_https");
        return user;
    }

    public static List<User> fromJsonTweetArray(List<tweet> tweetFromNetwork) {
        List<User>users = new ArrayList<>();
        for (int i = 0; i <tweetFromNetwork.size();i++);
        users.add(tweetFromNetwork.get(i.user));
            return users;
    }
}
