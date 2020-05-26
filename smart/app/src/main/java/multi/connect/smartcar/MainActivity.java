package multi.connect.smartcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import de.nitri.gauge.Gauge;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AsyncTaskSerial asyncTaskSerial;
    InputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Socket socket;
    OutputStream os;
    PrintWriter pw;
    Button btn30, btn60, btn90;
    ImageButton up, down;
    Gauge speedometer;
    TMapView tmapview;
    LinearLayout linearLayoutTmap;
    ToggleButton power;
    Bitmap carImg;
    Bitmap startImg;
    Bitmap endImg;
    AlertDialog alert;
    TextView distance;
    int speed = 0;
    String[] permission_list = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    LinearLayout loading,mapViewTotal,naviSearchView;
    TMapPoint tMapPoint,startpoint,endpoint;
    TMapCircle tMapCircle;
    Location location;
    LocationManager lm;
    FloatingActionButton fabNavi,btnBack,fabMsg;
    EditText destiName;
    Button btnDesti;
    ListView destiList;
    ArrayAdapter<POI> POIAdapter;
    String des,loc_lat,loc_lon;
    TMapTapi tmaptapi;
    boolean isTmapApp;
    TMapGpsManager tMapGpsManager;
    int zoomLev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Serial 통신
        asyncTaskSerial = new AsyncTaskSerial();
        asyncTaskSerial.execute(10,20);
        distance = findViewById(R.id.distance);
        //power -setting
        power = findViewById(R.id.power);
        /*btn30.setEnabled(false);
        btn60.setEnabled(false);
        btn90.setEnabled(false);
        up.setEnabled(false);
        down.setEnabled(false);*/

        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    power.setBackgroundDrawable(getResources().getDrawable(R.drawable.switchoff));
                    Toast.makeText(MainActivity.this,"주행 보조 시스템이 켜졌습니다.",Toast.LENGTH_SHORT).show();
                    btn30.setEnabled(true);
                    btn60.setEnabled(true);
                    btn90.setEnabled(true);
                    up.setEnabled(true);
                    down.setEnabled(true);
                    new Thread(new Runnable() {
                        String message = "";
                        @Override
                        public void run() {
                            message = "auto_on";
                            pw.println(message);
                            pw.flush();
                        }
                    }).start();
                }else{
                    power.setBackgroundDrawable(getResources().getDrawable(R.drawable.switchon));
                    Toast.makeText(MainActivity.this,"주행 보조 시스템이 꺼졌습니다.",Toast.LENGTH_SHORT).show();
                    btn30.setEnabled(false);
                    btn60.setEnabled(false);
                    btn90.setEnabled(false);
                    up.setEnabled(false);
                    down.setEnabled(false);
                    new Thread(new Runnable() {
                        String message = "";
                        @Override
                        public void run() {
                            message = "auto_off";
                            pw.println(message);
                            pw.flush();
                        }
                    }).start();
                }
            }
        });

        //속도 제어
        speedometer = findViewById(R.id.myGauge);
        up = findViewById(R.id.btnUp);  up.setOnClickListener(this);
        down = findViewById(R.id.btnDown);  down.setOnClickListener(this);
        btn30 = findViewById(R.id.btn30);  btn30.setOnClickListener(this);
        btn60 = findViewById(R.id.btn60);  btn60.setOnClickListener(this);
        btn90 = findViewById(R.id.btn90);  btn90.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        speedometer.setLowerText("0");
        linearLayoutTmap = findViewById(R.id.layoutMapView);
        loading = findViewById(R.id.loading);
        mapViewTotal = findViewById(R.id.mapViewTotal);
        //power -setting
        btn30.setEnabled(false);
        btn60.setEnabled(false);
        btn90.setEnabled(false);
        up.setEnabled(false);
        down.setEnabled(false);
        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    power.setBackgroundDrawable(getResources().getDrawable(R.drawable.switchoff));
                    Toast.makeText(MainActivity.this,"주행 보조 시스템이 켜졌습니다.",Toast.LENGTH_SHORT).show();
                    btn30.setEnabled(true);
                    btn60.setEnabled(true);
                    btn90.setEnabled(true);
                    up.setEnabled(true);
                    down.setEnabled(true);
                }else{
                    power.setBackgroundDrawable(getResources().getDrawable(R.drawable.switchon));
                    Toast.makeText(MainActivity.this,"주행 보조 시스템이 꺼졌습니다.",Toast.LENGTH_SHORT).show();
                    btn30.setEnabled(false);
                    btn60.setEnabled(false);
                    btn90.setEnabled(false);
                    up.setEnabled(false);
                    down.setEnabled(false);
                }
            }
        });
        //네비 목적지 검색
        naviSearchView = findViewById(R.id.naviSearchView);
        fabMsg = findViewById(R.id.fabMsg);
        fabMsg.setOnClickListener(this);
        fabNavi = findViewById(R.id.fabNavi);
        fabNavi.setOnClickListener(this);
        destiName = findViewById(R.id.destiName);
        btnDesti = findViewById(R.id.btnDesti);
        btnDesti.setOnClickListener(this);
        destiList = findViewById(R.id.destiList);
        POIAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        destiList.setAdapter(POIAdapter);
        destiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] destiList = parent.getItemAtPosition(position).toString().split(",");
                des = destiList[0];
                String[] locList = destiList[1].split(" ");
                loc_lat = locList[1];
                loc_lon = locList[3];
                Toast.makeText(getApplicationContext(),des,Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder
                        = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder .setTitle("목적지를 "+des+"으로 설정합니다")
                        .setPositiveButton("T네비", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                destiName.setText("");
                                POIAdapter.clear();
                                tmaptapi= new TMapTapi(MainActivity.this);
                                tmaptapi.setSKTMapAuthentication("l7xx69415d661c8445a8b35bd80789e07ebf");
                                isTmapApp = tmaptapi.isTmapApplicationInstalled();
                                if (isTmapApp){
                                    startpoint = tMapGpsManager.getLocation();
                                    HashMap pathInfo = new HashMap();
                                    pathInfo.put("rGoName", des);
                                    pathInfo.put("rGoY", loc_lat);
                                    pathInfo.put("rGoX", loc_lon);
                                    tmaptapi.invokeRoute(pathInfo);
                                    naviSearchView.setVisibility(View.INVISIBLE);
                                    mapViewTotal.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                        .setNeutralButton("경로표시", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                destiName.setText("");
                                POIAdapter.clear();
                                startpoint = tMapGpsManager.getLocation();
                                endpoint = new TMapPoint(Double.parseDouble(loc_lat),Double.parseDouble(loc_lon));
                                searchRoute(startpoint,endpoint);
                                tmapview.setZoomLevel(16);
                                naviSearchView.setVisibility(View.INVISIBLE);
                                mapViewTotal.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("취소", null);
                alert = builder.create();
                alert.show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 1000);
        } else {
            init();
        }

    }
    //서버 연결
    class AsyncTaskSerial extends AsyncTask<Integer,String,String> {

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                socket = new Socket("70.12.226.101", 12345);
                if (socket != null) {
                    ioWork();
                }
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            String msg;
                            try {
                                msg = br.readLine();
                                if(msg.length() > 0) {
                                    Log.d("test", "서버로 부터 수신된 메시지>>"
                                            + msg);
                                    publishProgress(msg);
                                }
                            } catch (IOException e) {
                            }
                        }
                    }
                });
                t1.start();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            distance.setText(values[0]);

        }
    }
        void ioWork(){
            try {
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                os = socket.getOutputStream();
                pw = new PrintWriter(os,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    private void searchPOI() {
        TMapData data = new TMapData();
        String keyword = destiName.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tmapview.removeAllMarkerItem();
                            POIAdapter.clear();

                            for (TMapPOIItem poi : arrayList) {
                                POIAdapter.add(new POI(poi));
                            }
                        }
                    });
                }
            });
        }
    }

    private void moveMap(double lat,double lng){
        tmapview.setCenterPoint(lng,lat);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        init();
        setGps();
    }

    public void init() {
        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey("l7xx69415d661c8445a8b35bd80789e07ebf");
        tmapview.setZoomLevel(19);
        //아이콘 표시
        carImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.pickupcar);
        startImg = BitmapFactory.decodeResource(this.getResources(),R.drawable.pin);
        endImg =BitmapFactory.decodeResource(this.getResources(),R.drawable.endpin);
        Bitmap carIcon = Bitmap.createScaledBitmap(carImg, 40, 60, true);


        tmapview.setIcon(carIcon);
        tmapview.setIconVisibility(true);

        //현재위치를 중앙으로 이동
        tmapview.setTrackingMode(true);
        //보는 방향 전조등
        tmapview.setSightVisible(true);
        //현재 보는 방향
        tmapview.setCompassMode(true);
        //현재위치 불러오기
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location net_location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location gps_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (net_location!=null){
            location = net_location;
        }else {
            if (gps_location!=null){
                location =gps_location;
            }
        }
        tmapview.setLocationPoint(location.getLongitude(),location.getLatitude());
        linearLayoutTmap.addView(tmapview);

        /*//지도에 마커찍기
        final ArrayList alTMapPoint = new ArrayList();
        alTMapPoint.add(new TMapPoint(37.576016, 126.976867));//광화문
        alTMapPoint.add(new TMapPoint(37.570432, 126.992169));// 종로3가
        alTMapPoint.add(new TMapPoint(37.570194, 126.983045));//종로5가
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapmark);
        for(int i=0; i<alTMapPoint.size(); i++){
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            // 마커 아이콘 지정
            markerItem1.setIcon(bitmap);
            // 마커의 좌표 지정
            markerItem1.setTMapPoint((TMapPoint)alTMapPoint.get(i));
            //지도에 마커 추가
            tmapview.addMarkerItem("markerItem"+i, markerItem1);
        }*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.INVISIBLE);
                mapViewTotal.setVisibility(View.VISIBLE);
            }
        },1900);

    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                tmapview.setLocationPoint(longitude, latitude);
                tmapview.setCenterPoint(longitude, latitude);

                if (tMapGpsManager.getLocation().equals(endpoint)){
                    tmapview.removeTMapPath();
                }
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                    1000, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        }else if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        }
        tMapGpsManager = new TMapGpsManager(this);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(5);
        tMapGpsManager.setProvider(TMapGpsManager.GPS_PROVIDER);
        tMapGpsManager.OpenGps();
    }

    public void searchRoute(TMapPoint startpoint,TMapPoint endpoint){
        TMapData data = new TMapData();
        data.findPathData(startpoint,endpoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap startIcon = Bitmap.createScaledBitmap(startImg, 40, 50, true);
                        Bitmap endIcon = Bitmap.createScaledBitmap(endImg, 40, 50, true);
                        path.setLineWidth(5);
                        path.setLineColor(Color.RED);
                        tmapview.addTMapPath(path);
                        tmapview.setTMapPathIcon(startIcon,endIcon);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnUp){
            if(speed<=237) {
                speed = speed + 3;
            }else {
                speed = 240;
            }
            speedometer.moveToValue(speed);
            speedometer.setLowerText(Integer.toString(speed));
        }else if(v.getId()==R.id.btnDown){
            if(speed>=3) {
                speed = speed - 3;
            }else if(speed<3){
                speed = 0;
            }
            speedometer.moveToValue(speed);
            speedometer.setLowerText(Integer.toString(speed));
        }else if(v.getId()==R.id.btn30){
            speed = 30;
            speedometer.moveToValue(speed);
            speedometer.setLowerText(Integer.toString(speed));
        }else if(v.getId()==R.id.btn60){
            speed = 60;
            speedometer.moveToValue(speed);
            speedometer.setLowerText(Integer.toString(speed));
        }else if(v.getId()==R.id.btn90){
            speed = 90;
            speedometer.moveToValue(speed);
            speedometer.setLowerText(Integer.toString(speed));
        }else if(v.getId()==R.id.btnBack){
            destiName.setText("");
            naviSearchView.setVisibility(View.INVISIBLE);
            mapViewTotal.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.fabNavi){
            naviSearchView.setVisibility(View.VISIBLE);
            mapViewTotal.setVisibility(View.INVISIBLE);
        }else if(v.getId()==R.id.btnDesti){
            searchPOI();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm!=null) {
                imm.hideSoftInputFromWindow(naviSearchView.getWindowToken(), 0);
            }
        }else if(v.getId()==R.id.fabMsg){
            zoomLev = tmapview.getZoomLevel();
            Toast.makeText(MainActivity.this,Integer.toString(zoomLev),Toast.LENGTH_SHORT).show();
            tmapview.removeTMapCircle("circle1");
            tMapPoint = new TMapPoint(tMapGpsManager.getLocation().getLatitude(),tMapGpsManager.getLocation().getLongitude());
            tMapCircle = new TMapCircle();
            tMapCircle.setCenterPoint(tMapPoint);
            switch (zoomLev){
                case 19:
                    tMapCircle.setRadius(50);
                    break;
                case 18:
                    tMapCircle.setRadius(170);
                    break;
                case 17:
                    tMapCircle.setRadius(290);
                    break;
                case 16:
                    tMapCircle.setRadius(420);
            }
            tMapCircle.setCircleWidth(2);
            tMapCircle.setLineColor(Color.BLUE);
            tMapCircle.setAreaColor(Color.GRAY);
            tMapCircle.setAreaAlpha(100);
            tmapview.addTMapCircle("circle1", tMapCircle);
        }
    }


}