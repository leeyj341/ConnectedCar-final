package multi.connect.smartcar.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import multi.connect.smartcar.MainActivity;
import multi.connect.smartcar.R;

public class MsgActivity extends AppCompatActivity {
    String message;
    TextView receiveMsg;
    String text;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_msg);
        receiveMsg = findViewById(R.id.receiveMsg);
        Button OK = findViewById(R.id.msgCheck);
        Bundle extras = getIntent().getExtras();
        message = extras.getString("message");
        mediaPlayer = MediaPlayer.create(MsgActivity.this,R.raw.test);
        mediaPlayer.start();
        if(message.equals("EM")){
            text =getString(R.string.EM);
            mediaPlayer = MediaPlayer.create(MsgActivity.this,R.raw.basic1);
        }else if(message.equals("TRUNK")){
            text =getString(R.string.TRUNK);
            mediaPlayer = MediaPlayer.create(MsgActivity.this,R.raw.basic2);
        }else if(message.equals("CAUTION")) {
            text = getString(R.string.CAUTION);
            mediaPlayer = MediaPlayer.create(MsgActivity.this,R.raw.basic3);
        }
        mediaPlayer.start();
        receiveMsg.setText(text);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.pause();
            }
        });
    }
}
