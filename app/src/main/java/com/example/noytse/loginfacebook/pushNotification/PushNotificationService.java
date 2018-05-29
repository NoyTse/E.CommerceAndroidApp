package com.example.noytse.loginfacebook.pushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.noytse.loginfacebook.ProductDetails;
import com.example.noytse.loginfacebook.ProductListActivity;
import com.example.noytse.loginfacebook.R;
import com.example.noytse.loginfacebook.model.Product;
import com.example.noytse.loginfacebook.model.ProductWithKey;
import com.example.noytse.loginfacebook.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG ="PushNotificationService";
    private User m_user;
    private Product m_product;
    private NotificationCompat.Builder m_builder;
    private String m_bigText;
    private String m_disc;
    private int m_smallIcon;
    private ProductWithKey m_productWithKey;
    private Uri m_soundUri;
    private Intent m_intentToInvoke;
    private NotificationCompat.Style m_style;

    public PushNotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        m_builder =
                new NotificationCompat.Builder(this, "notify_001");
        m_smallIcon = R.drawable.ic_notifications_black_24dp;
        m_soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Map<String,String> data;
        String title = "title";
        String body = "body";
        RemoteMessage.Notification notification;

        if (remoteMessage.getNotification() == null) {
            Log.e(TAG, "onMessageReceived() >> Notification is empty");
        } else {
            notification = remoteMessage.getNotification();
            title = notification.getTitle();
            body = notification.getBody();
            Log.e(TAG, "onMessageReceived() >> title: " + title + " , body=" + body);
        }

        data = remoteMessage.getData();
        String value = data.get("title");
        if (value != null) {
            m_bigText = value;
        }

        value = data.get("discription");
        if (value != null) {
            m_disc = value;
        }

        value = data.get("small_icon");
        if(value != null){
            if(value.equals("summer")){
                m_smallIcon = R.mipmap.ic_sun;
            }
            else if(value.equals("sale")){
                m_smallIcon = R.drawable.ic_alarm_black_24dp;
            }
            else if(value.equals("special")){
                m_smallIcon = R.drawable.ic_shopping_cart_black_24dp;
            }
        }

        value = data.get("image");
        if(value != null){
            if(value.equals("true")){
                NotificationCompat.BigPictureStyle bigImage = new NotificationCompat.BigPictureStyle();
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sunset);
                bigImage.bigPicture(bm);
                m_style = bigImage;
            }
        }else{
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(m_bigText);
            bigText.setBigContentTitle(m_disc);
            bigText.setSummaryText("Text in detail");
            m_style = bigText;
        }

        value = data.get("item");
        if(value != null){
            if(value.equals("true")){
                final DatabaseReference myUserRef = FirebaseDatabase.getInstance().getReference();
                myUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        m_user = dataSnapshot.child("Users/" + FirebaseAuth.getInstance().getUid()).getValue(User.class);
                        m_product = dataSnapshot.child("products/" + 0).getValue(Product.class);
                        Product newProduct = new Product(
                                m_product.getName(),
                                m_product.getCategory(),
                                m_product.getColor(),
                                m_product.getAvailableInStock(),
                                m_product.getSize(),
                                m_product.getMaterial(),
                                m_product.getPhotoURL(),
                                m_product.getPrice(),
                                m_product.getReviewList());
                        m_productWithKey = new ProductWithKey(newProduct, "0");

                        m_intentToInvoke = new Intent(getApplicationContext(), ProductDetails.class);
                        m_intentToInvoke.putExtra("Product", m_productWithKey);
                        m_intentToInvoke.putExtra("FromService", "hello world");
                        m_intentToInvoke.putExtra("user_email",m_user.getEmail());
                        m_intentToInvoke.putExtra("user_ParchesedList",m_user.getMyBags());
                        m_intentToInvoke.putExtra("user_total",m_user.getTotalPurchase());

                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, m_intentToInvoke, 0);

                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                        bigText.bigText(m_bigText);
                        bigText.setBigContentTitle(m_disc);

                        m_builder.setContentIntent(pendingIntent);
                        m_builder.setSmallIcon(m_smallIcon);
                        m_builder.setContentTitle("Your Title");
                        m_builder.setContentText("Your text");
                        m_builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bigsunset));
                        m_builder.setPriority(Notification.PRIORITY_MAX);
                        m_builder.setStyle(bigText);
                        m_builder.setAutoCancel(true);

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("notify_001",
                                    "Channel human readable title",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            mNotificationManager.createNotificationChannel(channel);
                        }

                        mNotificationManager.notify(0, m_builder.build());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else{
                m_intentToInvoke = new Intent(this, ProductListActivity.class);
                value = data.get("filterBy");
                if(value != null){
                    m_intentToInvoke.putExtra("filterBy", value);
                }

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, m_intentToInvoke, 0);

                m_builder.setContentIntent(pendingIntent);
                m_builder.setSmallIcon(m_smallIcon);
                m_builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bigsunset));
                m_builder.setContentTitle(m_bigText);
                m_builder.setContentText(m_disc);
                m_builder.setPriority(Notification.PRIORITY_MAX);
                m_builder.setStyle(m_style);
                m_builder.setAutoCancel(true);

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("notify_001",
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    mNotificationManager.createNotificationChannel(channel);
                }

                mNotificationManager.notify(0, m_builder.build());
            }
        }
    }
}
