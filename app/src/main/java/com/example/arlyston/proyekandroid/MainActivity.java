package com.example.arlyston.proyekandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    public static LinkedList<Activity> mActivity = new LinkedList<>();

    private RecyclerView mRecyclerView;
    public static WordListAdapter mAdapter;
    FloatingActionButton x;
    public static FirebaseDatabase database;
    public static DatabaseReference users;
    private String title, priority;
    private Date convdate;
    private int count;
    private int mamen = 1;
    public static String UName;
    public static int countBig;
    private ShakeActivity mShaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("notif");
        UName = extras.getString("Uname");

        count = 1;
        countBig = 0;

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Activity post = postSnapshot.getValue(Activity.class);
                    title = post.getName();
                    convdate = post.getConvdate();
                    priority = post.getPriority();

                    if(post.getId() > countBig){
                        countBig=post.getId();
                    }
                    if(UName.equals(post.getIdUser())){
                        post = new Activity(count, title, convdate, priority,UName);
                        if (mamen == 1) {//untuk supaya fetch data sekali aja

                            mActivity.addLast(post);//ini buat awal masuk

                            // Sort in assending order
                            Collections.sort(mActivity, new Comparator<Activity>() {
                                public int compare(Activity p1, Activity p2) {
                                    return Long.valueOf(p1.getConvdate().getTime()).compareTo(p2.getConvdate().getTime());
                                }
                            });
                        }
                        Log.d("mytag", "masuk");
                        count += 1;
                    }


                }
                callmama(); ////fungsinya disini men
                mamen = 2;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });





        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(this, mActivity);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("mytag", "pak eko");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        x = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        x.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), tiga.class);
                startActivity(i);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                CheckAllActivities();
                handler.postDelayed(this, 1000);
            }
        }, 1000);



        mShaker = new ShakeActivity(this);
        mShaker.setOnShakeListener(new ShakeActivity.OnShakeListener() {
            @Override
            public void onShake() {
                turnOffReminder();
            }
        });

    }

    public void turnOffReminder() {
        for (int i = 0; i < mActivity.size(); i++) {
            if (mActivity.get(i).isReminded() && !mActivity.get(i).isStopped()) {
                mActivity.get(i).setStopped(true);
                database = FirebaseDatabase.getInstance();
                users = database.getReference("notif");
                Activity xx = mActivity.get(i);
                users.child(String.valueOf(xx.getId())).setValue(xx);

                MainActivity.users.child(String.valueOf(xx.getId())).removeValue();
                MainActivity.mActivity.remove(xx);
                MainActivity.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void CheckAllActivities() {
        for (int i = 0; i < mActivity.size(); i++) {
            new CheckNotification().execute(mActivity.get(i));
        }

        for (int i = 0; i < mActivity.size(); i++) {
            new CheckVibrate().execute(mActivity.get(i));
        }
    }

    public class CheckNotification extends AsyncTask<Activity, Void, Activity> {
        @Override
        protected Activity doInBackground(Activity... params) {
            Calendar cal = Calendar.getInstance();
            if (!params[0].isReminded()) {
                DateFormat timeFormat = new SimpleDateFormat("HH:mm"), dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date dateNow = cal.getTime();

                String dataDate = dateFormat.format(params[0].getConvdate());
                String timeDate = timeFormat.format(params[0].getConvdate());

                String timeStr = timeFormat.format(dateNow);
                String dateStr = dateFormat.format(dateNow);

                //Toast.makeText(MainActivity.this, combinedDate +","+ timeNow + "||" + params[0].getDate() + "," + params[0].getTime(), Toast.LENGTH_LONG).show();
                if (dataDate.equals(dateStr)) {
                    if (timeDate.equals(timeStr)) {

                        database = FirebaseDatabase.getInstance();
                        users = database.getReference("notif");
                        params[0].setReminded(true);
                        Activity xx = params[0];
                        users.child(String.valueOf(xx.getId())).setValue(xx);
                        return params[0];
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Activity aActivity) {
            super.onPostExecute(aActivity);
            if (aActivity != null) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    Notification not = new Notification.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.ic_stat_access_alarm)
                            .setContentTitle("Friendly reminder!")
                            .setContentText(aActivity.getName())
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .build();

                    mNotificationManager.notify(aActivity.getId(), not);
                //}

                //Toast.makeText(MainActivity.this, aActivity.getName() + ", " + aActivity.getTime(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CheckVibrate extends AsyncTask<Activity, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (aBoolean) {
// Vibrate for 500 milliseconds
                if (v.hasVibrator()) {
                    v.vibrate(500);
                }
                //else {
                //    v.cancel();
                //}

            }
            //else {
            //    v.cancel();
            //}
        }

        @Override
        protected Boolean doInBackground(Activity... params) {
            if (params[0].isReminded() && !params[0].isStopped())
                return true;
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Collections.sort(mActivity, new Comparator<Activity>() {
            public int compare(Activity p1, Activity p2) {
                return Long.valueOf(p1.getConvdate().getTime()).compareTo(p2.getConvdate().getTime());
            }
        });

        mAdapter.notifyDataSetChanged();

    }

    private void callmama() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(this, mActivity);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Press Log Out to Exit!", Toast.LENGTH_LONG).show();
    }

    //    public void CheckAllActivities(){
//        for(int i=0; i<mActivity.size(); i++){
//            new CheckNotification().execute(mActivity.get(i));
//        }
//
//        for(int i=0; i<mActivity.size(); i++){
//            new CheckVibrate().execute(mActivity.get(i));
//        }
//    }
//    public class CheckNotification extends AsyncTask<Activity, Void, Activity> {
//        @Override
//        protected Activity doInBackground(Activity... params) {
//            Calendar cal = Calendar.getInstance();
//                if (!params[0].isReminded()) {
//                    int hourNow = cal.get(Calendar.HOUR_OF_DAY);
//                    int minuteNow = cal.get(Calendar.MINUTE);
//                    String timeNow = hourNow + ":" + minuteNow;
//                    if (params[0].getTime().equals(timeNow)) {
//                        params[0].setReminded(true);
//                        return params[0];
//                    }
//                }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Activity aActivity) {
//            super.onPostExecute(aActivity);
//            if(aActivity!= null) {
//                NotificationCompat.Builder mBuilder =
//                        (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this)
//                                .setSmallIcon(R.drawable.ic_stat_access_alarm)
//                                .setContentTitle("Proyek Android")
//                                .setContentText(aActivity.getName());
//
//
//                // Gets an instance of the NotificationManager service//
//
//                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                mNotificationManager.notify(aActivity.getId(), mBuilder.build());
//
//                //params[0]
//            }
//        }
//    }
//    public class CheckVibrate extends AsyncTask<Activity, Void, Boolean>{
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            if (aBoolean){
//                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//// Vibrate for 500 milliseconds
//                if(v.hasVibrator()){
//                    v.vibrate(500);
//                }
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.) {
////                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
////                } else {
////                    //deprecated in API 26
////                    v.vibrate(500);
////                }
//            }
//        }
//
//        @Override
//        protected Boolean doInBackground(Activity... params) {
//            if (params[0].isReminded() && !params[0].isStopped())
//                return true;
//            return false;
//        }
//    }
//
//
//    public void sendNotification(View view) {
//
//        //Get an instance of NotificationManager//
//
//        NotificationCompat.Builder mBuilder =
//                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_stat_access_alarm)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");
//
//
//        // Gets an instance of the NotificationManager service//
//
//        NotificationManager mNotificationManager =
//
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        mNotificationManager.notify(001, mBuilder.build());
//
//        // When you issue multiple notifications about the same type of event,
//        // it’s best practice for your app to try to update an existing notification
//        // with this new information, rather than immediately creating a new notification.
//        // If you want to update this notification at a later date, you need to assign it an ID.
//        // You can then use this ID whenever you issue a subsequent notification.
//        // If the previous notification is still visible, the system will update this existing notification,
//        // rather than create a new one. In this example, the notification’s ID is 001//


}

