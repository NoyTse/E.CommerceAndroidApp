package com.example.noytse.loginfacebook.analytics;

import android.content.Context;
import android.os.Bundle;

import com.example.noytse.loginfacebook.MainActivity;
import com.example.noytse.loginfacebook.ProductDetails;
import com.example.noytse.loginfacebook.model.Product;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class AnalyticsManager {
    private static String TAG = "AnalyticsManager";
    private static AnalyticsManager mInstance = null;
    private FirebaseAnalytics mFirebaseAnalytics;


    private AnalyticsManager() { }

    public static AnalyticsManager getInstance() {

        if (mInstance == null) {
            mInstance = new AnalyticsManager();
        }
        return (mInstance);
    }

    public void trackSignupEvent(String signupMethod) {

        String eventName = "signup";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, signupMethod);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP,params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("signup method", signupMethod);
        FlurryAgent.logEvent(eventName,eventParams);

    }

    public void trackPurchase(Product prod) {

        String eventName = "purchase";
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME,prod.getName());
        params.putString(FirebaseAnalytics.Param.PRICE,prod.getPrice());

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE,params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("prod_name", prod.getName());
        eventParams.put("price",prod.getPrice());

        FlurryAgent.logEvent(eventName,eventParams);

    }

    // Called when sending a review
    public void trackTimeBetweenPurchaseAndReview(){
        String eventName = "timeBetweenPurchToRev";
        long diffInMillies = ProductDetails.purchaseTime.getTime() - new Date().getTime();

        //String duration = String.format("%2d:%2d:%2d-%d-days",timeBetweenHours,timeBetweenMinutes,(int)timeBetween,timeBetweenDays)
        String duration = timeToStr(diffInMillies);

        //Firebase
        Bundle params = new Bundle();
        params.putString(eventName, duration);
        mFirebaseAnalytics.logEvent(eventName,params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put(eventName,duration);
        FlurryAgent.logEvent(eventName, eventParams);

    }

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

    public void trackSortParameters(String sortParameter){
        String eventName = "sort";

        //Firebase
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, sortParameter);
        mFirebaseAnalytics.logEvent(eventName,params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("search term", sortParameter);
        FlurryAgent.logEvent(eventName, eventParams);
    }

    public void trackFilterParameters(String filterType,String filterParameter){
        String eventName = "filter";

        //Firebase
        Bundle params = new Bundle();
        params.putString("filter_by", filterParameter);
        params.putString("filter_Type", filterType);
        mFirebaseAnalytics.logEvent(eventName,params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put("filter by", filterParameter);
        eventParams.put("filter_Type", filterType);
        FlurryAgent.logEvent(eventName, eventParams);
    }

    public void trackTimeInsideTheApp(){
        String eventName = "timeInsideApp";
        Date time = new Date();
        long diffInMillies = MainActivity.enterAppTime.getTime() - time.getTime();
        long timeInside = TimeUnit.MILLISECONDS.toSeconds(diffInMillies);


        //Firebase
        Bundle params = new Bundle();
        params.putString(eventName, timeToStr(timeInside));
        mFirebaseAnalytics.logEvent(eventName,params);

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put(eventName,Long.toString(timeInside));
        FlurryAgent.logEvent(eventName, eventParams);
    }

    private String timeToStr(long milli)
    {
        //Period p = Period.between(birthday, today);
        //long p2 = ChronoUnit.DAYS.between(birthday, today);
        //System.out.println("You are " + p.getYears() + " years, " + p.getMonths() +
           //     " months, and " + p.getDays() +
         //       " days old. (" + p2 + " days total)");
       // Duration.between(Instant.now(),Instant.now())
        long second = (milli / 1000) % 60;
        long minute = (milli / (1000 * 60)) % 60;
        long hour = (milli / (1000 * 60 * 60)) % 24;
        long days = milli/(1000*60*60*24);

        String time = String.format("%02d:%02d:%02d %d days", hour, minute, second, days);

       /* long timeBetween = TimeUnit.MILLISECONDS.toSeconds(milli);
        int timeBetweenDays,timeBetweenHours,timeBetweenMinutes;
        timeBetweenDays= (int)timeBetween/60/60/24;
        timeBetween = (int)timeBetween - (int)timeBetween*60*60*24;
        timeBetweenHours = (int)timeBetween/60/60;
        timeBetween = (int)timeBetween - (int)timeBetween*60*60;
        timeBetweenMinutes = (int)timeBetween/60;
        timeBetween = (int)timeBetween - (int)timeBetween*60;
*/
        //return String.format("%2d:%2d:%2d-%d-days",timeBetweenHours,timeBetweenMinutes,(int)timeBetween,timeBetweenDays);
        return time;
    }
    public void trackAppEntrance(){
        String eventName = "entrance";
        Bundle params = new Bundle();
        params.putString(eventName, MainActivity.enterAppTime.toString());
        mFirebaseAnalytics.logEvent(eventName,params  );

        //Flurry
        Map<String, String> eventParams = new HashMap<String, String>();
        eventParams.put(eventName, MainActivity.enterAppTime.toString());
        FlurryAgent.logEvent(eventName, eventParams);
    }

    public void init(Context context) {

        new FlurryAgent.Builder().withLogEnabled(true).build(context, "DXGZNHVWWW7FQNV9244V");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    }





//    public void trackLoginEvent(String loginMethod) {

//        String eventName = "login";
//        Bundle params = new Bundle();
//        params.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, loginMethod);
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN,params);
//
//        //Flurry
//        Map<String, String> eventParams = new HashMap<String, String>();
//        eventParams.put("signup method", loginMethod);
//        FlurryAgent.logEvent(eventName,eventParams);
//
//    }

//    public void trackSongEvent(String event , Song song) {
//        Bundle params = new Bundle();
//
//        params.putString("song_genre", song.getGenre());
//        params.putString("song_name", song.getName());
//        params.putString("song_name", song.getArtist());
//        params.putDouble("song_price",song.getPrice());
//        params.putDouble("song_rating",song.getRating());
//
//        mFirebaseAnalytics.logEvent(event,params);
//
//        //Flurry
//        Map<String, String> eventParams = new HashMap<String, String>();
//        eventParams.put("song_genre", song.getGenre());
//        eventParams.put("song_name", song.getName());
//        eventParams.put("song_name", song.getArtist());
//        eventParams.put("song_price",String.valueOf(song.getPrice()));
//        eventParams.put("song_rating",String.valueOf(song.getRating()));
//
//        FlurryAgent.logEvent(event,eventParams);
//
//    }


//    public void trackSongRating(Song song ,int userRating) {
//
//        String eventName = "song_rating";
//        Bundle params = new Bundle();
//
//        params.putString("song_genre", song.getGenre());
//        params.putString("song_name", song.getName());
//        params.putString("song_artist", song.getArtist());
//        params.putDouble("song_price",song.getPrice());
//        params.putDouble("song_reviews_count",song.getReviewsCount());
//        params.putDouble("song_total_rating",song.getRating());
//        params.putDouble("song_user_rating",userRating);
//
//        mFirebaseAnalytics.logEvent(eventName,params);
//
//
//        //Flurry
//        Map<String, String> eventParams = new HashMap<String, String>();
//        eventParams.put("song_genre", song.getGenre());
//        eventParams.put("song_name", song.getName());
//        eventParams.put("song_name", song.getArtist());
//        eventParams.put("song_price",String.valueOf(song.getPrice()));
//        eventParams.put("song_reviews_count",String.valueOf(song.getReviewsCount()));
//        eventParams.put("song_total_rating",String.valueOf(song.getRating()));
//        eventParams.put("song_user_rating",String.valueOf(userRating));
//
//        FlurryAgent.logEvent(eventName,eventParams);
//
//
//    }

    public void setUserID(String id, boolean newUser) {

        mFirebaseAnalytics.setUserId(id);

        FlurryAgent.setUserId(id);

    }

    public void setUserProperty(String name , String value) {

        mFirebaseAnalytics.setUserProperty(name,value);
    }

}