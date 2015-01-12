package com.dartstransit.latetrips;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import android.support.v4.app.NotificationCompat;


public class Messages extends Activity {

    ListView lv;
    SimpleCursorAdapter adapter1;
    //final ArrayList<String> list = new ArrayList<String>();
    private Handler mHandler;
    private int UPDATE_INTERVAL = 30000; // 30 seconds by default.
    public final static String EXTRA_DATA = "com.dartstransit.latetrips.DATA";
    private static Boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        isActive = true;
        lv = (ListView) findViewById(R.id.listView);
        if (mHandler == null) {
            mHandler = new Handler();
        }

        //Check if user credentials are saved
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        String userid = settings.getString("clientId", "-1");
        String userpass = settings.getString("clientPassword", "-1");
        if (!userid.equals("-1") && !userpass.equals("-1")) {
            MsgData.UserId = userid;
            MsgData.UserPassword = userpass;
        } else {
            //Show Login Screen
            OpenSettings();
        }

        MsgData.msgActivity = this;
        MsgData.UpdateMsgs();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(view.getContext(), MsgDetails.class);
                TextView textView = (TextView) view.findViewById(R.id.eventId);
                //Log.d("lolo", "1-" + textView.getText().toString());
                intent.putExtra(EXTRA_DATA, textView.getText());
                startActivity(intent);
            }
        });
        startRepeatingTask();
    }

    public void OpenSettings() {
        //stopRepeatingTask();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            MsgData.UpdateMsgs();
            mHandler.postDelayed(mStatusChecker, UPDATE_INTERVAL);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
        // startRepeatingTask();
        UPDATE_INTERVAL = 30000; // Restore Update frequency  to 30 secs when activated
        MsgData.UpdateMsgs();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
        UPDATE_INTERVAL = 60000; // Update frequency 60 secs when in background
        //stopRepeatingTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void UpdateGui() {
        List<DartsTrip> msgs = MsgData.GetMesages();
        if (msgs != null && isActive) {
            lv.setAdapter(new MessageAdapter(this, msgs));
        } else {
            //Notifications
            int notfs = MsgData.GetNotificationsCount();
            //Log.d("lolo","Working in background");

            String title = "Late/OnBoard issues";
            String msg = "";
            if (notfs == 1) {
                msg = "There is a trip with problems";
            } else {
                if (notfs > 1) {
                    msg = "There are " + String.valueOf(notfs) + " trips with problems";
                } else {
                    msg = "There are no trips with problems at this moment";
                }
            }
            Log.d("lolo", msg);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.late_notification)
                            .setContentTitle(title)
                            .setContentText(msg);
            Intent resultIntent = new Intent(this, Messages.class);
            // Because clicking the notification opens a new ("special") activity, there's
            // no need to create an artificial back stack.
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

        }
    }
}
