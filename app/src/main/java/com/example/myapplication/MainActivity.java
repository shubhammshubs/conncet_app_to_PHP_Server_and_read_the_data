package com.example.myapplication;



import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String apiurl = "https://apip.trifrnd.in/portal/attend.php?apicall=readall";
    ListView lv;
    ArrayList<String>holder=new ArrayList<>();



    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        fetchdata();

    }
    public void fetchdata()
    {
        lv=(ListView) findViewById(R.id.lv);
        class dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data)
            {
                try {

                    JSONArray ja= new JSONArray(data);
                    JSONObject jo =null;
                    holder.clear();
                    for(int i=0;i< ja.length();i++)
                    {
                        jo=ja.getJSONObject(i);
                        String id=jo.getString("id");
                        String fname=jo.getString("fname");
                        String lname=jo.getString("lname");
                        String email=jo.getString("email");
                        String mobile=jo.getString("mobile");

                        String fullName = id + " " + fname+ " " + lname+ " " + email+ " " + mobile;


                        holder.add(fullName);

                    }

                    ArrayAdapter<String> at = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,holder);
                    lv.setAdapter(at);


                }catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),data , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;

                    while( (line=br.readLine())!=null)
                    {
                        data.append(line+"\n");
                    }
                    br.close();
                    return data.toString();
                }
                catch (Exception ex)
                {
                    return ex.getMessage();
                }
            }
        }

        dbManager obj = new dbManager();
        obj.execute(apiurl);
    }
}
