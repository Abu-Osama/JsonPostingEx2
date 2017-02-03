package com.example.abuosama.jsonpostingex2;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyService extends Service {
    private  String name,country,twitter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //step read data coming  from frag
        Bundle bundle=intent.getExtras();
        name=bundle.getString("name");
        country=bundle.getString("country");
        twitter=bundle.getString("twitter");
        //step 13 strat asynctak pass url
        MyTask myTask=new MyTask();
        myTask.execute("http://hmkcode.appspot.com/jsonservlet");
        return super.onStartCommand(intent, flags, startId);


    }

    public MyService() {
    }

    public class  MyTask extends AsyncTask<String,Void,String>{

        //declare required varible
        URL myUrl;
        HttpURLConnection urlConnection;
        OutputStream outputStream;
        OutputStreamWriter outputStreamWriter;


        @Override
        protected String doInBackground(String... strings) {
            try {
                myUrl=new URL(strings[0]);
                urlConnection= (HttpURLConnection) myUrl.openConnection();
                //pre[areing for conncetion
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(60000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("content-type", "application/json");
                //prepare json data for posting
                JSONObject jsonObject=new JSONObject();  //{}
                jsonObject.accumulate("name",name);//name 34
                jsonObject.accumulate("country",country);
                jsonObject.accumulate("twitter",twitter);
                //prepare output stream
                outputStream=urlConnection.getOutputStream();
                outputStreamWriter=new OutputStreamWriter(outputStream);
                //write into json into outputateanm writer
                outputStreamWriter.write(jsonObject.toString());
                //forceully show everything to server
                outputStreamWriter.flush();
                //here at  this point of time server will start reading
                //let us ask server for response
                int response=urlConnection.getResponseCode();
                Log.d("b34","sucess");
                //close all connection properties

                if(response==HttpURLConnection.HTTP_OK){
                    return "sucess";

                }else {
                    return "failure";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("B34","error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                //close all connection
                if(urlConnection!=null) {
                    urlConnection.disconnect();//close conncetion
                    if(outputStream!=null){
                        try {
                            outputStream.close();

                            if(outputStreamWriter!=null){
                                outputStreamWriter.close();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MyService.this, "status:"+s, Toast.LENGTH_SHORT).show();
            //send broadcast to dynamic receiver

            Intent intent=new Intent();
            intent.setAction("Task_Done");
            intent.putExtra("result",s);
            sendBroadcast(intent);

            super.onPostExecute(s);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
