package com.example.dbfm0.iotlabtest.feature;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "UsingThingspeakAPI";
    private static final String THINGSPEAK_CHANNEL_ID = "638545";
    private static final String THINGSPEAK_API_KEY = "ZQNB0YRT8RVE5DHU"; //GARBAGE KEY
    private static final String THINGSPEAK_API_KEY_STRING = "ZQNB0YRT8RVE5DHU";
    /* Be sure to use the correct fields for your own app*/
    private static final String THINGSPEAK_FIELD1 = "field1";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    TextView t1,t2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=(TextView)findViewById(R.id.textView2);
        t2=(TextView)findViewById(R.id.textView3);
        b1=(Button) findViewById(R.id.button);
        t2.setText("");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new FetchThingspeakTask().execute();
                }
                catch(Exception e){
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        });

    }
    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            t2.setText("Fetching Data from Server.Please Wait...");
            Log.i("one", "error1");
        }
        protected String doInBackground(Void... urls) {
            try {
                Log.i("one", "error2");
                URL url = //new URL("https://api.thingspeak.com/channels/638545/fields/1.json?results=1");
                        new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("one", "error3");
                try {
                    Log.i("one", "error4");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    Log.i("one", "error5");
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    Log.i("one", "error6");
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            if(response == null) {
                Toast.makeText(MainActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                String v1 = channel.getString(THINGSPEAK_FIELD1);

                    t1.setText(v1+" lux");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
