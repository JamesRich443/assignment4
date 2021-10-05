package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.TweetWithUser;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.models.tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG="TimelineActivity";
    private final int REQUEST_CODE=20;
    TweetDao tweetDao;
    TwitterClient client;
    RecyclerView rvTweets;
    List<tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient(this);
        TweetDao tweetDao = (TweetDao) ((TwitterApp) getApplicationContext()).getMyDatabase().sampleModelDao();
        scrollListener= new EndlessRecyclerViewScrollListener(new LinearLayoutManager(this) {

            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG,"onLoadMore" + page);
                loadMoreData();

            }

            private void loadMoreData() {
                client.getNextPageOfTweets(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG,"onSuccess for load more data"+json.toString());
                                JSONArray jsonArray= json.jsonArray;
                        List<tweet>tweets= null;
                        try {
                            tweets = tweet.fromJsonArray(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.addAll(tweets);
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,"onSuccess for load more data", throwable);
                    }
                    },tweets.get(tweets.size()-1).id);
            }
        }) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");


            }
        });

        rvTweets = findViewById(R.id.rvTweets);

        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        rvTweets.setLayoutManager((new LinearLayoutManager(this)));
        rvTweets.setAdapter(adapter);

    };
    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            Log.i(TAG,"showing data from data base");
            List<TweetWithUser>tweetWithUser=tweetDao.recentItems();
            tweetsfromDb=TweetWithUser.getTweetList(tweetWithUser);
            adapter.addAll(tweetsfromDb);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Compose){
            Toast.makeText(this,"Compose",Toast.LENGTH_SHORT);
            Intent intent = new Intent(this,ComposeActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_CODE&& resultCode==RESULT_OK){
           tweet Tweet= data.getParcelableExtra("tweet");

           tweets.add(0,Tweet);

           adapter.notifyItemInserted(0);
    }
        super.onActivityResult(requestCode, resultCode, data);

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG,"onSuccess");
              JSONArray jsonArray= json.jsonArray;
                try {
                } finally {

                }
                List<tweet> tweetFromNetwork=tweet.fromJsonArray(jsonArray)
            adapter.clear();
                    adapter.addAll(jsonArray));
                    swipeContainer.setRefreshing(false);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG,"saving data from data base");
                            List<User>usersFromNetwork= User.fromJsonTweetArray(tweetFromNetwork);
                            tweetDao.insertModel(usersFromNetwork.toArray(new User [0]));
                            tweetDao.insertModel(tweetFromNetwork.toArray(new tweet[0] ));
                } catch (JSONException e) {
                    Log.e(TAG,"json exception",e);


                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"onFailure",throwable);
            }
        });
    }
}