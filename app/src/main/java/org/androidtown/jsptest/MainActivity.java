package org.androidtown.jsptest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textview;
    loadJsp task;
    //String serverURL = "http://192.168.0.153:9999/Movie/movietable.jsp";
    //192.168.0.177
    String serverURL = "http://192.168.204.1:9999/Movie/movietable_jsptest.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = (TextView)findViewById(R.id.textview);

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                task=new loadJsp();
                task.execute();

            }
        });
    }
    class loadJsp extends AsyncTask<Void,String,Void> {
        String data=null;
        String receiveMsg="";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                /*conn.setRequestProperty("theaterid","theaterid");
                conn.setRequestProperty("theatername","theatername");
                conn.setRequestProperty("location_x","location_x");
                conn.setRequestProperty("location_y","location_y");*/
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                Log.i("yj","URL접속");

                OutputStreamWriter ows = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
                ows.write("key=" + "ok");
                ows.flush();
                Log.i("yj","key");

                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader in = new InputStreamReader(conn.getInputStream(),"UTF-8");
                    BufferedReader reader = new BufferedReader(in);
                    StringBuffer buffer = new StringBuffer();

                    Log.i("yj","while위");

                    while((data = reader.readLine()) != null) {
                        buffer.append(data);
                        Log.i("yj","data = " + data);
                    }
                    receiveMsg=buffer.toString();
                    Log.i("yj","서버에서 안드로이드로 전달 됨");
                    Log.i("yj","receiveMsg = " + receiveMsg); //

                    JSONObject json = new JSONObject(receiveMsg);
                    Log.i("yj","json = " + json);
                    JSONArray jArr = json.getJSONArray("datasend");
                    Log.i("yj","jArr = " + jArr);

                    StringBuffer sb = new StringBuffer();
                    for(int i=0; i<jArr.length(); i++){
                        json = jArr.getJSONObject(i);
                        String theaterid = json.getString("theaterid");
                        String theatername = json.getString("theatername");
                        String location_x = json.getString("location_x");
                        String location_y = json.getString("location_y");
                        sb.append("[ "+theaterid+" ]\n");
                        sb.append("[ "+theatername+" ]\n");
                        sb.append(location_x+"\n");
                        sb.append(location_y+"\n");
                        sb.append("\n");
                    }

                    textview.append(sb);

                }
                else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

