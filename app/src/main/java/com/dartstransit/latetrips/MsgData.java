package com.dartstransit.latetrips;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MsgData {
    private static final String SERVICE_URL = "http://192.168.10.61/latetrips/getData";
    private static final String POST_SERVICE_URL = "https://www.dartstransit.com:8093/inprueba/latetrips/getData";//https://www.dartstransit.com:8093/transit/latetrips/getlatetrips";
    private static String AJAX_MESSAGE = "NADA";
    public static Messages msgActivity;
    public static List<DartsTrip> msgs = new ArrayList<DartsTrip>();
    private static int NotificationCounter;
    public static String UserId;
    public static String UserPassword;

    private static String Get() {
        InputStream inputStream;
        String result;
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(SERVICE_URL);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else
                result = "Did not work!";
        } catch (IOException e) {
            e.printStackTrace();
            result = Arrays.toString(e.getStackTrace());
        }
        return result;
    }

    private static String Post() {
        InputStream inputStream;
        String result;
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(POST_SERVICE_URL);

            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            postParams.add(new BasicNameValuePair("Username", UserId));
            postParams.add(new BasicNameValuePair("Password", UserPassword));
            postParams.add(new BasicNameValuePair("Id", "0"));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
            entity.setContentEncoding(HTTP.UTF_8);
            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else
                result = "NADA";
        } catch (Exception e) {
            //e.printStackTrace();
            //Log.e("late", e.getMessage());
            result = "NADA";
        }
        //Log.d("lolo", result);
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private static class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return MsgData.Post();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            AJAX_MESSAGE = result;
            if (msgActivity != null) {
                NotificationCounter = 0;
                msgActivity.UpdateGui();
            }
        }
    }

    public static void UpdateMsgs() {
        new HttpAsyncTask().execute(SERVICE_URL);
    }

    public static List<DartsTrip> GetMesages() {
        //Log.d("lolo", AJAX_MESSAGE);
        msgs.clear();
        if (AJAX_MESSAGE.contains("Invalid Access")) {
            Toast.makeText(msgActivity.getBaseContext(), "There was a problem connecting to the server. Check your UserId and Password", Toast.LENGTH_LONG).show();
            //msgActivity.OpenSettings();
            //UpdateMsgs();
            return null;
        } else {
            final JSONArray objs;
            try {
                objs = new JSONArray(AJAX_MESSAGE);
                int index = 0;
                for (int i = 0; i < objs.length(); i++) {
                    DartsTrip msg = new DartsTrip();
                    JSONObject obj = objs.getJSONObject(i);
                    Log.d("lolo", obj.toString());
                    msg.ClientID = obj.getString("ClientId");
                    msg.Late_PU = new Date();
                    msg.Vehicle = obj.getString("Vehicle");
                    msg.setBell(obj.getBoolean("Bell"));
                    msg.ClientName = obj.getString("ClientName");
                    msg.Driver = obj.getString("Driver");
                    msg.Est_DropOff = obj.getString("EstDropOff");
                    msg.Est_PickUp = obj.getString("EstPickUp");
                    msg.Late_DO = "";//obj.getString("Late_DO");
                    msg.On_Board = obj.getString("OnBoard");
                    msg.Run_Name = obj.getString("RunName");
                    msg.SchTime = obj.getString("SchTime");
                    msg.SubTypeAbbr = obj.getString("SubTypeAbbr");
                    msg.MinsLate = obj.getString("MinsLate");
                    msg.IsLateTrip = obj.getBoolean("IsLateTrip");
                    msg.EstPickupTime = obj.getInt("EstPickUpTime");
                    msg.OnBoardTime = obj.getInt("OnBoardTime");
                    msg.EstDropOffTime = obj.getInt("EstDropOffTime");
                    if (! (msg.ClientID.equals("0") ||msg.ClientID.equals("-1")) ) {
                        msg._id = index;
                        index++;
                        msgs.add(msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("lolo", AJAX_MESSAGE);
                return null;
            }
            return msgs;
        }
    }

    public static void AddNotificationCounter() {
        NotificationCounter++;
    }

    public static int GetNotificationsCount() {
        return NotificationCounter;
    }

}
