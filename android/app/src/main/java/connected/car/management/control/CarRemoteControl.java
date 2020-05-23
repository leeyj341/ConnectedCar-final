package connected.car.management.control;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import connected.car.management.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarRemoteControl extends Fragment {
    ToggleButton powerOff;

    AsyncTaskPower asyncTaskPower;
    Socket socket;

    InputStream is;
    InputStreamReader isr;
    BufferedReader br;

    OutputStream os;
    PrintWriter pw;


    public CarRemoteControl() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_remote_control, container, false);
        asyncTaskPower = new AsyncTaskPower();
        asyncTaskPower.execute();

        powerOff = view.findViewById(R.id.powerOff);
        powerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(powerOff.isChecked()){
                    powerOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.poweron));
                    send_msg(v);
                }else{
                    powerOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.poweroff));
                    send_msg(v);
                }
            }
        });
        return view;
    }
    public void send_msg(final View view){
        new Thread(new Runnable() {
            String message = "";
            @Override
            public void run() {
                //if(view.getBackground().getCurrent() == powerOff.getResources().getDrawable(R.drawable.poweron)){
                    message = "user/car/engineStart";
               // }else{
                //    message = "user/car/engineStop";
                //}
                pw.println(message);
                pw.flush();
            }
        }).start();
    }
    class AsyncTaskPower extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = new Socket("70.12.227.61", 12345);
                if(socket != null){
                    ioWork();
                }
                pw.println("user");

                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg;
                        try {
                            msg = br.readLine();
                            Log.d("remote", "서버로부터 수신된 메시지>>"+msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t1.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        void ioWork(){
            try {
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                os = socket.getOutputStream();
                pw = new PrintWriter(os, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
