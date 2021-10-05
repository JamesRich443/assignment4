package com.codepath.apps.restclienttemplate.models;

import androidx.room.Embedded;

import java.util.ArrayList;
import java.util.List;

public class TweetWithUser {
    @Embedded
    User user;
    @Embedded(prefix="tweet")
    tweet Tweet;

    public static Object getTweetList(List<TweetWithUser> tweetWithUser) {
        List<Tweet>tweet= new ArrayList<>();
        for (int i = 0; i<TweetWithUser.size();i++){
            Tweet tweet=new Tweet();
            tweet.user=TweetWithUser.get(i).user;
            tweets.add(tweet);
        }
    }
}
