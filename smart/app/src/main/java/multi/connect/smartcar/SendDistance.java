package multi.connect.smartcar;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendDistance extends AsyncTask<String,Void,Void> {
    @Override
    protected Void doInBackground(String... strings) {
        try {
            //Log.d("server","distance보내기 Test");
            URL url = new URL("http://172.20.10.11:8088/connectedcar/member/join.do");
            OkHttpClient client = new OkHttpClient();
            JSONObject json = new JSONObject();
            json.put("car_id", strings[0]);
            json.put("randKm", getRandomKm());
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), json.toString()))
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject resultJson = new JSONObject(response.body().string());
            Log.d("test", resultJson.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getRandomKm(){
        int km = ((int)Math.random()*3000) + 3000;
        return km;
    }
}
