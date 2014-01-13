package info.srihawong.tbike;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
public class ApiJsonData {
    static String url;
    private String result;
    private JSONObject jsonObjectData;
    public ApiJsonData(String url) {
        this.url = url;
    }

    public String getJsonString(){
        this.getData();
        return result;
    }

    public JSONObject getJsonObject(){
        this.getData();
        try {
            jsonObjectData = new JSONObject(this.result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectData;
    }

    /*public List<String,String> ApiJsonData(String url){
        this.url = url;
        getData();
        return List<String,String>;
    }*/
    private void getData(){
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
                result =  stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
