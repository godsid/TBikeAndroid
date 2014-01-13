package info.srihawong.tbike;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Banpot.S on 10/1/2557.
 */
public class JsonURL extends AsyncTask<String, Integer, Long>{
    final static int TIMEOUT = 15000;
    String result = null;
    private String url;

    private Activity activity;

    public JsonURL(Activity activity, String url) {
        this.activity = activity;
        this.url = url;

    }



    @Override
    protected Long doInBackground(String... params) {
        HttpGet httpGet = new HttpGet(this.url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response =  httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode<400){
                HttpEntity entity =  response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line ;
                StringBuilder stringBuilder = new StringBuilder();
                while((line = reader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();
                this.result =  stringBuilder.toString();
                //android.util.Log.d("tui",result);
                android.util.Log.d("App","Http response success");
            }else{
                //Toast.makeText()
                android.util.Log.d("App","Http response error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;

        return null;
    } // end of doInBackground

    @Override
    protected void onPostExecute(Long along) {

    } //end of onPostExecute

    /*
    class getApiDataAsync extends AsyncTask<String, Integer, Long>{

        @Override
        protected Long doInBackground(String... params) {
            final String url = params[0];
            String result = null;
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();
            try {
               HttpResponse response =  httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if(statusCode<400){
                    HttpEntity entity =  response.getEntity();
                    InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line ;
                StringBuilder stringBuilder = new StringBuilder();
                while((line = reader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                inputStream.close();
                result =  stringBuilder.toString();
                android.util.Log.d("tui",result);
                    android.util.Log.d("Error","Http response success");
            }else{
                //Toast.makeText()
                android.util.Log.d("Error","Http response error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;

            return null;
        }

        @Override
        protected void onPostExecute(Long result) {

            super.onPostExecute(result);
        }
    }*/

} //End of JsonURL class
