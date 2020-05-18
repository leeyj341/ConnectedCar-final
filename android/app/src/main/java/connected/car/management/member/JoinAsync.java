package connected.car.management.member;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class JoinAsync extends AsyncTask<MemberVO, Void, Integer> {
    Context context;

    public JoinAsync() {

    }

    public JoinAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(MemberVO... vo) {
        int result = 0;

        try {
            URL url = new URL("http://192.168.43.175:8088/connectedcar/member/join.do");
            Gson gson = new Gson();
            String sVo = gson.toJson(vo[0]);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), sVo))
                    .build();

            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            Log.d("test", response.body().string());
            result = json.getInt("resultNum");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer value) {
        super.onPostExecute(value);
        if(value == 1)
            Toast.makeText(context, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
        else if(value == 0)
            Toast.makeText(context, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
    }
}
