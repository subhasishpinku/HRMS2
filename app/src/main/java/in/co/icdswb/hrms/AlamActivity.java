package in.co.icdswb.hrms;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper;
import in.co.icdswb.hrms.ActivityDatabase.NetworkStateCheckerOdOut;
import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.Alam.AlarmNotificationService;
import in.co.icdswb.hrms.Alam.AlarmReceiver;
import in.co.icdswb.hrms.Alam.AlarmSoundService;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;

import static in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper.TABLE_LOGDATEID;
import static in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper.TABLE_LOGINDATE;
import static in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper.TABLE_LOGINSHOW;

public class AlamActivity extends AppCompatActivity {
    Button start,stop;
    private PendingIntent pendingIntent;
    private static final int ALARM_REQUEST_CODE = 133;
    SharedPreferences sp;
    String EMPODINN;
    String EMPODIN ="1";
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    Intent myIntent = null;
    int alarmTriggerTime;
    String SystemTime,lat,lng,location,empId,emp_id,od_date,formattedtime,time;
    private DatabaseHelper db;
    public static final String ODOUT = "http://ascensiveeducare.com/hrms/service/od_out_type.php";
    public static final int ODOUT_SYNCED_WITH_SERVER = 1;
    public static final int ODOUT_NOT_SYNCED_WITH_SERVER = 0;
    private BroadcastReceiver broadcastReceiverout;
    public static final String DATA_SAVED_BROADCASTOUT = "net.simplifiedcoding.datasavedout";
    private NetworkStateCheckerOdOut networkStateCheckerOdOut;
    ImageView homeID;
    TextView closetimeID,currenttimeID;
    String dbfg= "1";
    String FGG;
    String FGF = "1";
    String DATETIME;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alam_activity);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        db = new DatabaseHelper(this);
        homeID =(ImageView)findViewById(R.id.homeID);
        closetimeID = (TextView)findViewById(R.id.closetimeID);
        currenttimeID = (TextView)findViewById(R.id.currenttimeID);
        broadcastReceiverout = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiverout, new IntentFilter(DATA_SAVED_BROADCASTOUT));
        networkStateCheckerOdOut =new NetworkStateCheckerOdOut();
        registerReceiver(networkStateCheckerOdOut, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        Calendar cal = Calendar.getInstance();
        Date sdate=cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        SystemTime=dateFormat.format(sdate);
        Log.e("Sys"," "+SystemTime);
        currenttimeID.setText(SystemTime);
        sp = getSharedPreferences(Consts.DATETIME, MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sp.edit();
        edit1.putString("DATETIME", SystemTime);
        edit1.commit();
       // SQLiteDatabase dbb = db.getReadableDatabase();
      //  dbb.execSQL("UPDATE loginshow SET logindate=SystemTime WHERE logdateid=" +dbfg);
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Office_close_time :"+ res.getString(7)+"\n\n");
            empId = String.valueOf(res.getString(10));
            time = String.valueOf(res.getString(7));
            closetimeID.setText(time);
        }
        Log.e("Data",buffer.toString());
        if(EMPODIN.equals(EMPODINN)){
            startService();
        }
        else {

        }
        Intent alarmIntent = new Intent(AlamActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AlamActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        image.startAnimation(animation1);
        Cursor re = db.getFlagData();
        if(re.getCount() == 0) {
            Log.e("Error","Nothing found");
            return;
        }
        StringBuffer buffe = new StringBuffer();
        while (re.moveToNext()) {
            String flagId =  re.getString(0);
            FGG =  re.getString(1);
            DATETIME = re.getString(2);
            buffe.append("logindate :"+ re.getString(2)+"\n");
            Log.e("DBPROclose", FGG+" "+flagId+" "+DATETIME);
        }
        if (FGG.equals(FGF)){
            setAlarm();
        }
        else {

        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarmManager();
                int stay = 2*60*60;
                Calendar cal = Calendar.getInstance();
                Date sdate=cal.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                SystemTime=dateFormat.format(sdate);
                SQLiteDatabase database = db.getReadableDatabase();
                database.execSQL( "UPDATE "+TABLE_LOGINSHOW +" SET " + TABLE_LOGINDATE+ " = '"+SystemTime+"' WHERE "+TABLE_LOGDATEID+ " = "+dbfg);
                Intent intent = new Intent(AlamActivity.this, Profile_Activity.class);
                startActivity(intent);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                if (gpsTracker.getIsGPSTrackingEnabled()){
                    lat = String.valueOf(gpsTracker.latitude);
                    lng = String.valueOf(gpsTracker.longitude);
                    String country = gpsTracker.getCountryName(getApplicationContext());
                    String  city = gpsTracker.getLocality(getApplicationContext());
                    String postalCode = gpsTracker.getPostalCode(getApplicationContext());
                    location = gpsTracker.getAddressLine(getApplicationContext());
                    Log.e("gps",lat+ "," +lng+ ","+country+ ","+city+""+postalCode+""+location);
                }
                else
                {
                    gpsTracker.showSettingsAlert();
                }

                   stopAlarmManager();
                    odOut();
            }
        });
        homeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarmManager();
                Intent intent = new Intent(AlamActivity.this, Profile_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(AlamActivity.this, MyService.class);
            startService(myIntent);
            // ContextCompat.startForegroundService(Profile_Activity.this, new Intent(getApplicationContext(), MyService.class));
        }
        else
        {
            gpsTracker.showSettingsAlert();
        }

    }
    private void checkServiceStatus() {

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AlamActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(AlamActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

        }
    }
    public void setAlarm() {
        int alarmTriggerTime = 1000;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, alarmTriggerTime);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmTriggerTime, pendingIntent);
        //Toast.makeText(this, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();
    }
    public void stopAlarmManager() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent
        stopService(new Intent(AlamActivity.this, AlarmSoundService.class));
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);
       // Toast.makeText(this, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
    }

    private void odOut(){
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
        }
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        emp_id = String.valueOf(user.getEmp_id());
        Log.e("emp_id",emp_id);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy");
        od_date = df3.format(c.getTime());
        Calendar cc = Calendar.getInstance();
        System.out.println("Current time => " + cc.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        formattedtime = time.format(c.getTime());
        sp = getSharedPreferences(Consts.EMPID, MODE_PRIVATE);
        String  EmpID = sp.getString("EmpId", "");
        Log.e("add", EmpID);
        Log.e("onDestroy","outod");
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sp.edit();
        edit1.putString("EMPODIN", "0");
        edit1.commit();
        // stopService();
        SQLiteDatabase dbb = db.getReadableDatabase();
        dbb.execSQL("UPDATE loginshow SET loginflag='0' WHERE logdateid=" +dbfg);
        SharedPrefManager.getInstance(getApplicationContext()).logout();
        Intent intent5 = new Intent(this, Login.class);
        startActivity(intent5);
        finish();
        finish();
        SharedPrefManager.getInstance(getApplicationContext()).logout();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ODOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            //converting the string to json array object
                            JSONArray obj = new JSONArray(response);
                            Log.e("ODOUT"," "+obj);
                            //          Toast.makeText(getApplicationContext(), "ODOUT_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                            odout(empId,od_date,formattedtime,location,lat,lng,"O",od_date,ODOUT_SYNCED_WITH_SERVER);
                            Intent intent = new Intent(AlamActivity.this, Login.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //         Toast.makeText(getApplicationContext(), "ODOUT_NOT_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                        odout(empId,od_date,formattedtime,location,lat,lng,"O",od_date,ODOUT_NOT_SYNCED_WITH_SERVER);
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
                String  latitude = sp.getString("latitude", "");
                String  longitude = sp.getString("longitude", "");
                String  time = sp.getString("time", "");
                String  date = sp.getString("date", "");
                Log.e("all_data"," "+latitude+" "+longitude+" "+time+" "+date+" ");
                params.put("emp_id", empId);
                params.put("od_date", od_date);
                params.put("od_time", formattedtime);
                params.put("actual_loc", location);
                params.put("lat",  lat);
                params.put("long", lng);
                params.put("od_type", "O");
                params.put("srink_date", od_date);
                Log.e("all_data_show"," "+emp_id+" "+od_date+" "+formattedtime+" "+location+" "+lat+" "+lng+" "+od_date);
                Log.e("aadd"," "+location);
                return params;
            }
        };
        //VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }
    private void odout(String outempid,String outdate,String outtime,String outactualoc, String outlat,String outlang,String outtype,String syncruntime,int status ){
        sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
        outactualoc = sp.getString("addressLinee", "");
        Log.e("add", outactualoc);
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
        }
        //   db.synodoutnsave(empId,outdate,outtime,sp.getString("addressLinee", ""),outlat,outlang,outtype,syncruntime,status);
        db.synodoutnsave(empId,outdate,outtime,location,outlat,outlang,outtype,syncruntime,status);
        Log.e("aadd"," "+location);
        Intent intent = new Intent(AlamActivity.this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("Inside onResume");
    }

    @Override
    public void onStart(){
        super.onStart();
        System.out.println("Inside onStart");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        System.out.println("Inside onReStart");
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("Inside onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        System.out.println("Inside onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("Inside onDestroy");
    }
}
