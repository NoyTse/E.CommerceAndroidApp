package com.example.noytse.loginfacebook.model;

/**
 * Created by user on 28/05/2018.
 */

        import android.content.Context;
        import android.os.Bundle;
        import android.util.Log;

       // import com.appsee.Appsee;
        import com.flurry.android.FlurryAgent;
        import com.google.firebase.analytics.FirebaseAnalytics;


        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

       // import com.mixpanel.android.mpmetrics.MixpanelAPI;


public class AnalyticsManager {
    private static String TAG = "AnalyticsManager";
    private static AnalyticsManager mInstance = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    //private  MixpanelAPI mMixpanel;

    private AnalyticsManager() {


    }

    public static AnalyticsManager getInstance() {

        if (mInstance == null) {
            mInstance = new AnalyticsManager();
        }
        return (mInstance);
    }

    public void init(Context context) {

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(context, "DXGZNHVWWW7FQNV9244V");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void EnterApp
    public void trackSearchEvent(String searchString) {

        String eventName = "search";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchString);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("search term", searchString);
        FlurryAgent.logEvent(eventName, eventParams);

    }

    public void trackSignupEvent(String signupMethod) {

        String eventName = "signup";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, signupMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("signup method", signupMethod);
        FlurryAgent.logEvent(eventName, eventParams);

    }


    public void trackLoginEvent(String loginMethod) {

        String eventName = "login";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, loginMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("signup method", loginMethod);
        FlurryAgent.logEvent(eventName, eventParams);

    }

    public void trackSongEvent(String event , Song song) {
        Bundle params = new Bundle();

        params.putString("song_genre", song.getGenre());
        params.putString("song_name", song.getName());
        params.putString("song_name", song.getArtist());
        params.putDouble("song_price", song.getPrice());
        params.putDouble("song_rating", song.getRating());

        mFirebaseAnalytics.logEvent(event, params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("song_genre", song.getGenre());
        eventParams.put("song_name", song.getName());
        eventParams.put("song_name", song.getArtist());
        eventParams.put("song_price", String.valueOf(song.getPrice()));
        eventParams.put("song_rating", String.valueOf(song.getRating()));

        FlurryAgent.logEvent(event, eventParams);

    }
    public void trackPurchase(Song song) {

        String eventName = "purchase";
        Bundle params = new Bundle();
        params.putDouble(FirebaseAnalytics.Param.PRICE, song.getPrice());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, params);


        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("song_genre", song.getGenre());
        eventParams.put("song_name", song.getName());
        eventParams.put("song_name", song.getArtist());
        eventParams.put("song_price", String.valueOf(song.getPrice()));
        eventParams.put("song_rating", String.valueOf(song.getRating()));

        FlurryAgent.logEvent(eventName, eventParams);

    }
    public void trackSongRating(Song song ,int userRating) {

        String eventName = "song_rating";
        Bundle params = new Bundle();

        params.putString("song_genre", song.getGenre());
        params.putString("song_name", song.getName());
        params.putString("song_artist", song.getArtist());
        params.putDouble("song_price", song.getPrice());
        params.putDouble("song_reviews_count", song.getReviewsCount());
        params.putDouble("song_total_rating", song.getRating());
        params.putDouble("song_user_rating", userRating);

        mFirebaseAnalytics.logEvent(eventName, params);


        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("song_genre", song.getGenre());
        eventParams.put("song_name", song.getName());
        eventParams.put("song_name", song.getArtist());
        eventParams.put("song_price", String.valueOf(song.getPrice()));
        eventParams.put("song_reviews_count", String.valueOf(song.getReviewsCount()));
        eventParams.put("song_total_rating", String.valueOf(song.getRating()));
        eventParams.put("song_user_rating", String.valueOf(userRating));

        FlurryAgent.logEvent(eventName, eventParams);

    }
    public void setUserID(String id, boolean newUser) {

        mFirebaseAnalytics.setUserId(id);

        FlurryAgent.setUserId(id);

    }

    public void setUserProperty(String name , String value) {

        mFirebaseAnalytics.setUserProperty(name,value);

    }

}
