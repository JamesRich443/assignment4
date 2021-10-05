package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {
    @Query("SELECT Tweet.body AS tweet_body,Tweet.createdAt , Tweet.id AS Tweet_id" + "FROM Tweet INNER JOIN User ON tweet.userId=User.id ORDER BY tweet.createdAt DESC LIMIT 5")
    List<TweetWithUser> recentItems();

    void insertModel();

    void insertModel(T[] toArray);
}
