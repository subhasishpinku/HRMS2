package in.co.icdswb.hrms.ActivityService;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper;
import in.co.icdswb.hrms.ActivityDatabase.NetworkStateCheckerRunTime;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.Alam.AlarmReceiver;
import in.co.icdswb.hrms.AlamActivity;
import in.co.icdswb.hrms.GPSTracker;
import in.co.icdswb.hrms.Login;
import in.co.icdswb.hrms.NotificationActivity.App;
import in.co.icdswb.hrms.R;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;

import static in.co.icdswb.hrms.ActivityService.Profile_Activity.ODOUT_NOT_SYNCED_WITH_SERVER;
import static in.co.icdswb.hrms.ActivityService.Profile_Activity.ODOUT_SYNCED_WITH_SERVER;

public class MyService extends Service  implements LocationListener , OnMapReadyCallback {
    boolean isGPSEnable=false, isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler myHandler = new Handler();
    private Handler myHandlerr = new Handler();
    private Timer myTimer = null;//60000 300000 900000
    long timeInterval = 900000;
    private Timer mmyTimer = null;//60000 300000 900000
    long ttimeInterval = 900000;
    public static String str_receiver = "com.bracesmedia.tracking";
    Intent intent;
    public static final String SERVICE = "http://ascensiveeducare.com/hrms/service/od_runtime.php";
    //from MyService to Profile_Activity
    final static String KEY_INT_FROM_SERVICE = "KEY_INT_FROM_SERVICE";
    final static String KEY_STRING_FROM_SERVICE = "KEY_STRING_FROM_SERVICE";
    final static String ACTION_UPDATE_CNT = "UPDATE_CNT";
    final static String ACTION_UPDATE_MSG = "UPDATE_MSG";
    //from Profile_Activity to MyService
    final static String KEY_MSG_TO_SERVICE = "KEY_MSG_TO_SERVICE";
    final static String ACTION_MSG_TO_SERVICE = "MSG_TO_SERVICE";
    MyServiceReceiver myServiceReceiver;
    MyServiceThread myServiceThread;
    int cnt;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    String  address,stateName,countryName;
    private int PERMISSION_REQUEST_CODE = 100;
    String stringLatitude;
    String stringLongitude;
    String country;
    String city;
    String postalCode;
    String addressLine;
    String time1,Date,empId;
    String addressLinee;
    CountDownTimer timer;
    Timer Endtimer;
    SharedPreferences sp;
    ////////sync////////////////////////////////////
    public static final int NAME_SYNCED_WITH_SERVERRUN = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVERRUN = 0;
    private BroadcastReceiver broadcastReceiver;
    public static final String RUNDATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    private DatabaseHelper db;
    DatabaseHelper DB = null;
    ////////////////////////////////////////////////
    String EMPODIN ="1";
    String EMPODINN;
    String  time;
    int hour,mintes,mint,mintesss;
    String offlineAddress,offlineAddress1;
    boolean mAllowRebind;
    ExampleService exampleService;
    private NetworkStateCheckerRunTime networkStateCheckerRunTime;
    String runtime,SystemTime,Time,Time1;
    private static final int ALARM_REQUEST_CODE = 133;
    PendingIntent pendingIntent;
    private NotificationManagerCompat notificationManager;
    String FLAG = "0";
    Timer timerr;
    Timer loguot;
    int hourr = 0;
    int mintess =0;
    int second =0;
    String DATETIME,FGG;
    String FLAGG = "1";
    int MIN;
    String FGF = "1";
    String dbfg= "1";
    int dd,mm,yy,ddd,mmm,yyy;
    String sdd,smm,syy,dddd,dmmm,dyyy;
    String formattedDate,checkdate;
    boolean state;
    boolean mobileDataEnabled = false;
    boolean wifidata;
    @Override
    public void onCreate() {
        super.onCreate();
     //   Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_LONG).show();
        myServiceReceiver = new MyServiceReceiver();
        myTimer = new Timer();
        myTimer.schedule(new TimerTaskToGetLocation(), 1, timeInterval);
        mmyTimer = new Timer();
        mmyTimer.schedule(new Logg(), 1, ttimeInterval);
        intent = new Intent(str_receiver);
        exampleService =new ExampleService();
        db = new DatabaseHelper(this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(RUNDATA_SAVED_BROADCAST));
      //  registerReceiver(new NetworkStateCheckerRunTime(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        networkStateCheckerRunTime =new NetworkStateCheckerRunTime();
        registerReceiver(networkStateCheckerRunTime, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        Intent alarmIntent = new Intent(MyService.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MyService.this, ALARM_REQUEST_CODE, alarmIntent, 0);
        notificationManager = NotificationManagerCompat.from(this);
        viewAll();
        LogOutForCalenderTime();
        if (FGG.equals(FGF)){
            Logout();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
            }
        }
        else{
            //load your map here
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    //   Toast.makeText(getApplicationContext(),"onStartCommand", Toast.LENGTH_LONG).show();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MSG_TO_SERVICE);
        registerReceiver(myServiceReceiver, intentFilter);
        myServiceThread = new MyServiceThread();
        myServiceThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("EXIT", "onBind!");
        return null;

    }
    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onDestroy", "onUnbind!");
        return mAllowRebind;
    }
    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {
        Log.e("onDestroy", "onRebind!");
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("onDestroy", "onStart!");
    }
    public class ExampleService extends IntentService {

        // Must create a default constructor
        public ExampleService() {
            super("example-service");
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            // This describes what will happen when service is triggered
            Log.e("onDestroy", "onHandleIntent!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     //   Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_LONG).show();
        Log.e("onDestroy","onDestroy");
        myServiceThread.setRunning(false);
        Log.e("myServiceThread"," "+myServiceThread);
        unregisterReceiver(myServiceReceiver);
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINServicee",EMPODINN);
//        if(EMPODIN==sp.getString("EMPODIN","")){
//            odOut();
//            Log.e("onDestroy",sp.getString("EMPODIN",""));
//        }
//        else {
//
//        }
        try{
            if(broadcastReceiver!=null)
                unregisterReceiver(broadcastReceiver);
            if(networkStateCheckerRunTime!=null)
                unregisterReceiver(networkStateCheckerRunTime);


        }catch(Exception e){}

    }
    public boolean isNetworkAvailable() {
        boolean connect=false;
        ConnectivityManager conMgr =  (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            connect=false;
        }else{
            connect= true;
        }
        return connect;
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
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy");
        String od_date = df3.format(c.getTime());
        Calendar cc = Calendar.getInstance();
        System.out.println("Current time => " + cc.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        String  formattedtime = time.format(c.getTime());
        sp = getSharedPreferences(Consts.EMPID, MODE_PRIVATE);
        String  EmpID = sp.getString("EmpId", "");
        Log.e("add", EmpID);
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sp.edit();
        edit1.putString("EMPODIN", "0"); //chang vlue 1 for od not logout
        edit1.commit();
      //  SharedPrefManager.getInstance(getApplicationContext()).logout();
        SQLiteDatabase dbb = db.getReadableDatabase();
        dbb.execSQL("UPDATE loginshow SET loginflag='0' WHERE logdateid=" +dbfg);
        Intent intent = new Intent(MyService.this, Login.class);
        startActivity(intent);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ODOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            //converting the string to json array object
                            JSONArray obj = new JSONArray(response);
                            Log.e("ODOUT"," "+obj);
//                            Toast.makeText(getApplicationContext(), "ODOUT_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                          odout(empId,Date,time1,addressLinee,String.valueOf(latitude),String.valueOf(longitude),"O",Date,ODOUT_SYNCED_WITH_SERVER);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
               //         Toast.makeText(getApplicationContext(), "ODOUT_NOT_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                       odout(empId,Date,time1,addressLinee,String.valueOf(latitude),String.valueOf(longitude),"O",Date,ODOUT_NOT_SYNCED_WITH_SERVER);
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emp_id", empId);
                params.put("od_date", Date);
                params.put("od_time", time1);
                params.put("actual_loc", addressLinee);
                params.put("lat", String.valueOf(latitude));
                params.put("long", String.valueOf(longitude));
                params.put("od_type", "O");
                params.put("srink_date", Date);
                Log.e("all_data_show"," "+empId+" "+Date+" "+time1+" "+addressLinee+" "+String.valueOf(latitude)+" "+String.valueOf(longitude)+" "+Date);
                return params;
            }
        };
     //   VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }

    private void odout(String outempid,String outdate,String outtime,String outactualoc, String outlat,String outlang,String outtype,String syncruntime,int status ){
        sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
        outactualoc = sp.getString("addressLinee", "");
        sp = getSharedPreferences(Consts.OFFLINEADDRESS, MODE_PRIVATE);
        offlineAddress1 = sp.getString("OFFLINEADDRESS", "");
        Log.e("add", outactualoc+" "+offlineAddress1);
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
        }
       // db.synodoutnsave(empId,outdate,outtime,sp.getString("addressLinee", ""),outlat,outlang,outtype,syncruntime,status);
        db.synodoutnsave(empId,outdate,outtime,sp.getString("OFFLINEADDRESS", ""),outlat,outlang,outtype,syncruntime,status);
        Log.e("all_data_show"," "+empId+" "+Date+" "+time1+" "+addressLinee+" "+String.valueOf(latitude)+" "+String.valueOf(longitude)+" "+Date);
        SQLiteDatabase dbb = db.getReadableDatabase();
        dbb.execSQL("UPDATE loginshow SET loginflag='0' WHERE logdateid=" +dbfg);
        Intent intent = new Intent(MyService.this, Login.class);
        startActivity(intent);
    }

    public void viewAll() {
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            buffer.append("Office_close_time :"+ res.getString(7)+"\n\n");
            empId = String.valueOf(res.getString(10));
            time = String.valueOf(res.getString(7));
            String currentString = time;
            String[] separated = currentString.split(":");
            hour = Integer.parseInt(separated[0].trim());
            mintes = Integer.parseInt(separated[1].trim());
            Log.e("mintes", String.valueOf(mintes));
            Log.e("hour", String.valueOf(hour));
        }
        Cursor ress = db.getFlagData();
        if(ress.getCount() == 0) {
            Log.e("Error","Nothing found");
            return;
        }
        StringBuffer bufferr = new StringBuffer();
        while (ress.moveToNext()) {
            String flagId =  ress.getString(0);
            FGG =  ress.getString(1);
            DATETIME = ress.getString(2);
            checkdate = ress.getString(3);
            Log.e("Serviceclose", FGG+" "+flagId+" "+DATETIME); }
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        String currentdate = formattedDate;
        String[] separated = currentdate.split("-");
        dd = Integer.parseInt(separated[0].trim());
        mm = Integer.parseInt(separated[1].trim());
        yy = Integer.parseInt(separated[2].trim());
        sdd = String.valueOf(dd);
        smm = String.valueOf(mm);
        syy = String.valueOf(yy);
        Log.e("DSDATE", "SYS"+" "+sdd+" "+smm+" "+syy);
        String dbdate = checkdate;
        String[] separatedd = dbdate.split("-");
        ddd = Integer.parseInt(separatedd[0].trim());
        mmm = Integer.parseInt(separatedd[1].trim());
        yyy = Integer.parseInt(separatedd[2].trim());
        dddd = String.valueOf(ddd);
        dmmm = String.valueOf(mmm);
        dyyy = String.valueOf(yyy);
        Log.e("DSDATE","DB"+" "+dddd+" "+dmmm+" "+dyyy);
        if (FGG.equals(FGF)){
            if (sdd.equals(dddd) && smm.equals(dmmm)){
            }
            else {
                //odOut();
            } }
        else {

        }
    }
    //////////////////////////Logout for mili time second///////////////////////////////////
    public void autoLogOut(){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent i = new Intent(MyService.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                //  finish();
                off();
                return;
            }
        }, 60000);
    }
    //////////////////////////Logout for mili time second///////////////////////////////////
    private void LogOutForCalenderTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, mintes);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        Endtimer = new Timer();
        Endtimer.schedule(new RemindEndTask(), time);

    }
    class RemindEndTask extends TimerTask {
        public void run() {
            System.out.println("Beep!");
            //  finish();
            //off();
          //  myTimer.cancel();
            sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
            EMPODINN = sp.getString("EMPODIN","");
            Log.e("OODDINService",EMPODINN);
            if(EMPODIN.equals(sp.getString("EMPODIN",""))){
                Log.e("NO_ODIN","Yes");
                sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                SharedPreferences.Editor edit1 = sp.edit();
                edit1.putString("EMPODIN", "1"); //change value 1 for od not logout
                edit1.commit();
             //   odOut();
                Log.e("Stop","Stop");
//                Intent i = new Intent(MyService.this, Login.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);

//                User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
//               String img = String.valueOf(user.getEmp_img());
//                Bitmap bmp = null;
//                try {
//                    InputStream in = new URL(img).openStream();
//                    bmp = BitmapFactory.decodeStream(in);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                Calendar cal = Calendar.getInstance();
                Date sdate=cal.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                SystemTime=dateFormat.format(sdate);
                Log.e("Sys"," "+SystemTime);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String dateInString = SystemTime;
                Date date = null;
                try {
                    date = sdf.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(dateInString);
                Time = String.valueOf(date.getTime());
                System.out.println("Date - Time in milliseconds : " + date.getTime());
                Log.e("TTIME"," "+date.getTime());
                 Log.e("Timee","Timee"+Time);
                /////////////////Second time////////////////////////////////////////
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                String dateInString1 = time;
                Date date1 = null;
                try {
                    date1 = sdf1.parse(dateInString1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(dateInString1);
               // time = String.valueOf(date1.getTime());
                System.out.println("Date - Time in milliseconds : " + date1.getTime());
                Log.e("TTIME"," "+date1.getTime());
                Time1 = String.valueOf(date1.getTime());
                Log.e("Timee","Timee1"+Time1);
                ///////////////////////////////////////////////////////////////////
                // / 28800000 32400000 14400000
                if (Time.equals(Time1)){
                    Log.e("TTIME"," "+ "OK");
                    String title = SystemTime;
                    String mess = "Your Offical Time is Over.Please Confirm Your OD Status";
                 //   Bitmap larBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_user);
                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CH1)
                            .setSmallIcon(R.drawable.hlogo)
                            .setContentTitle(title)
                            .setContentText(mess)
                         //   .setLargeIcon(larBitmap)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setColor(Color.BLUE)
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(true)
                            .build();
                    notificationManager.notify(1,notification);
                    Calendar call = Calendar.getInstance();
                    Date sdatee=call.getTime();
                    SimpleDateFormat dateFormatt = new SimpleDateFormat("HH:mm:ss");
                    SystemTime=dateFormatt.format(sdatee);
                    Log.e("Sys"," "+SystemTime);
//                sp = getSharedPreferences(Consts.DATETIME, MODE_PRIVATE);
//                SharedPreferences.Editor editt = sp.edit();
//                editt.putString("DATETIME", SystemTime);
//                editt.commit();
                    sp = getSharedPreferences(Consts.FLAG, MODE_PRIVATE);
                    SharedPreferences.Editor edit11 = sp.edit();
                    edit11.putString("FLAG", "1");
                    edit11.commit();
                   // odOut();

                    Intent i = new Intent(getApplicationContext(), AlamActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else {
                    sp = getSharedPreferences(Consts.FLAG, MODE_PRIVATE);
                    FLAG = sp.getString("FLAG","");
                    Log.e("FLAG",FLAG);
                    if(FLAGG.equals(sp.getString("FLAG",""))){
                        Log.e("TTIME"," "+ "NOK");
                        sp = getSharedPreferences(Consts.DATETIME, MODE_PRIVATE);
                        DATETIME = sp.getString("DATETIME","");
                        Log.e("DATETIME",DATETIME);
                        String currentString = DATETIME;
                        // String currentString = UPADATETIME;
                        String[] separated = currentString.split(":");
                        hourr = Integer.parseInt(separated[0].trim());
                        mintess = Integer.parseInt(separated[1].trim());
                        second = Integer.parseInt(separated[2].trim());
                        int minm = 15;
                        mint = mintess+minm;
                        Log.e("MINN","Y"+mint);
                        if (mint > 60){
                            int min = 60;
                            mintesss = mint / min;
                            Log.e("MINN","YN"+mintesss);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hourr+1);
                            calendar.set(Calendar.MINUTE, mintesss);
                            calendar.set(Calendar.SECOND, second);
                            Date time = calendar.getTime();
                            timerr = new Timer();
                            timerr.schedule(new RemindTaskk(), time);
                        }
                        else {
                            Log.e("MINN","YYN"+mint);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hourr);
                            calendar.set(Calendar.MINUTE, mint);
                            calendar.set(Calendar.SECOND, second);
                            Date time = calendar.getTime();
                            timerr = new Timer();
                            timerr.schedule(new RemindTaskk(), time);

                        }
                    }
                    else {

                    }
                }
                if (date1.getTime()>date.getTime()){

                }
                else {
                    sp = getSharedPreferences(Consts.FLAG, MODE_PRIVATE);
                    FLAG = sp.getString("FLAG","");
                    Log.e("FLAG",FLAG);
                     Log.e("TTIME"," "+ "NNOOK");
                        sp = getSharedPreferences(Consts.DATETIME, MODE_PRIVATE);
                        DATETIME = sp.getString("DATETIME","");
                        Log.e("DATETIME",DATETIME);
                        String currentString = DATETIME;
                        // String currentString = UPADATETIME;
                        String[] separated = currentString.split(":");
                        hourr = Integer.parseInt(separated[0].trim());
                        mintess = Integer.parseInt(separated[1].trim());
                        second = Integer.parseInt(separated[2].trim());
                        int minm = 15;
                        mint = mintess+minm;
                        Log.e("MINN","Y"+mint);
                        if (mint > 60){
                            int min = 60;
                            mintesss = mint / min;
                            Log.e("MINN","YN"+mintesss);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hourr+1);
                            calendar.set(Calendar.MINUTE, mintesss);
                            calendar.set(Calendar.SECOND, second);
                            Date time = calendar.getTime();
                            timerr = new Timer();
                            timerr.schedule(new RemindTaskk(), time);
                        }
                        else {
                            Log.e("MINN","YYN"+mint);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hourr);
                            calendar.set(Calendar.MINUTE, mint);
                            calendar.set(Calendar.SECOND, second);
                            Date time = calendar.getTime();
                            timerr = new Timer();
                            timerr.schedule(new RemindTaskk(), time);

                        }
                }

            }
            else {
                Log.e("NO_ODIN","No");
//                Intent i = new Intent(MyService.this, Login.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
            }
        }
    }
    class RemindTaskk extends TimerTask {
        public void run() {

//            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
//            img = String.valueOf(user.getEmp_img());
//            Bitmap bmp = null;
//            try {
//                InputStream in = new URL(img).openStream();
//                bmp = BitmapFactory.decodeStream(in);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            if (FGG.equals(FGF)){
                String title = SystemTime;
                String mess = "Your Offical Time is Over.Please Confirm Your OD Status";
                //  Bitmap larBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_user);
                Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CH2)
                        .setSmallIcon(R.drawable.hlogo)
                        .setContentTitle(title)
                        .setContentText(mess)
                        // .setLargeIcon(larBitmap)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setColor(Color.BLUE)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .build();
                notificationManager.notify(2,notification);
                Intent i = new Intent(MyService.this, AlamActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            else {

            }

        }
    }
    private void Logout(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date log = calendar.getTime();
        loguot = new Timer();
        loguot.schedule(new LogoutEndTask(), log);
    }
    class LogoutEndTask extends TimerTask {
        @Override
        public void run() {
            odOut();
        }
    }
    public void off() {
        LocationManager loc = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (loc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText( this, "GPS off", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
    }

    public class MyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals(ACTION_MSG_TO_SERVICE)){
                String msg = intent.getStringExtra(KEY_MSG_TO_SERVICE);
                msg = new StringBuilder(msg).reverse().toString();
                //send back to Profile_Activity
                Intent i = new Intent();
                i.setAction(ACTION_UPDATE_MSG);
                i.putExtra(KEY_STRING_FROM_SERVICE, msg);
                sendBroadcast(i);
            }


        }

    }
    private class MyServiceThread extends Thread{
        private boolean running;

        public void setRunning(boolean running){
            this.running = running;
        }

        @Override
        public void run() {
            cnt = 0;
            running = true;
            while (running){
                try {
                    Thread.sleep(1000);
                    Intent intent = new Intent();
                    intent.setAction(ACTION_UPDATE_CNT);
                    intent.putExtra(KEY_INT_FROM_SERVICE, cnt);
                    sendBroadcast(intent);
                    cnt++;
                    if (isNetworkAvailable()){
                        
                    }
                    else {

                    }
                    //mobilecheack();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void mobilecheack() {
        // TODO Auto-generated method stub
        state = isMobileDataEnable();
        if (state) {
            Log.e("cnt","0");

        } else {
            Log.e("cnt","1");
        }
    }
    public boolean isMobileDataEnable() {
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // method is callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(cm);
            try {
                WifiManager wifi = (WifiManager)
                        getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                WifiConfiguration wc = new WifiConfiguration();
                wc.SSID = "\"SSIDName\"";
                wc.preSharedKey  = "\"password\"";
                wc.hiddenSSID = true;
                wc.status = WifiConfiguration.Status.ENABLED;

                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

                wc.allowedPairwiseCiphers
                        .set(WifiConfiguration.PairwiseCipher.TKIP);
                wc.allowedPairwiseCiphers
                        .set(WifiConfiguration.PairwiseCipher.CCMP);
                wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

                wifidata=wifi.isWifiEnabled();
                Log.e("cnt"," "+wifidata);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mobileDataEnabled == false && wifidata == false){
                Log.e("cnt","mising data");

              
            }
            else if (mobileDataEnabled == true || wifidata == false){
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);

            }
            else if (mobileDataEnabled == false || wifidata == true){
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);

            }
              else {

            }

            Log.e("DATAMOBILE", " "+mobileDataEnabled);
        } catch (Exception e) {
            // Some problem accessible private API and do whatever error
            // handling here as you want..
        }
        return mobileDataEnabled;
    }

    public boolean toggleMobileDataConnection(boolean ON) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) this
                    .getSystemService(Context.CONNECTIVITY_SERVICE);final Class conmanClass = Class
                    .forName(conman.getClass().getName());
                    final Field iConnectivityManagerField = conmanClass
                    .getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField
                    .get(conman);
            final Class iConnectivityManagerClass = Class
                    .forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                    .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
        } catch (Exception e) {
        }
        return true;
    }


    private void getLocationInfo(){
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
            }
        if (!isGPSEnable && !isNetworkEnable){

        } else {
            if (isNetworkEnable) {
//                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, (LocationListener) this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                      //  Toast.makeText(this,"Location"+location.getLatitude()+" "+location.getLongitude(),Toast.LENGTH_SHORT).show();
                        time1 = new SimpleDateFormat("HH:mm").format(location.getTime());
                        Date = new SimpleDateFormat("yyyy-MM-dd").format(location.getTime());
                        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                        empId = String.valueOf(user.getEmp_id());
                        GPSTracker gpsTracker = new GPSTracker(this);
                        stringLatitude = String.valueOf(gpsTracker.latitude);
                        stringLongitude = String.valueOf(gpsTracker.longitude);
                        country = gpsTracker.getCountryName(this);
                        city = gpsTracker.getLocality(this);
                        postalCode = gpsTracker.getPostalCode(this);
                        addressLine = gpsTracker.getAddressLine(this);
                        //  Toast.makeText(getApplicationContext()," " +stringLatitude+ ""+stringLongitude+ ""+country+""+city+""+postalCode+ ""+addressLine+ "service lati", Toast.LENGTH_SHORT).show();
                        Log.e("gps",stringLatitude+ "," +stringLongitude+ ","+country+ ","+city+""+postalCode+""+addressLine);
                     //   addressLinee =  country+ ","+city+", "+postalCode+", "+addressLine;
                        addressLinee =  addressLine;
                        if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
                            Log.e("Location1", "Time GPS: " + time1); // This is what we want!
                        else
                            Log.e("Location1", "Time Device (" + location.getProvider() + "): " + time1);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                        runtime = time.format(c.getTime());
                        Log.e("Rtime",runtime);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.e("Service",", "+latitude +longitude);
                        sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
                        String  addressLinee = sp.getString("addressLinee", "");
                        Log.e("add", addressLinee);
                      //  Toast.makeText(getApplicationContext(),"SpAddress" +addressLinee, Toast.LENGTH_SHORT).show();

                        serviceUpdateLocation(location);
                       getCompleteAddressString(latitude,longitude);

                    }
                }

                if (isGPSEnable) {
                 //   location = null;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, (LocationListener) this);
                    if (locationManager!=null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location!=null){
                            Log.e("latitude",location.getLatitude()+"");
                            Log.e("longitude",location.getLongitude()+"");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.e("Service1",", "+location.getLatitude()+" " +location.getLongitude()+" "+"Loc"+latitude+" "+","+longitude);
                            //  getAddressFromLocation(latitude,longitude, new GeocoderHandler());
                            final String time2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(location.getTime());

                            if( location.getProvider().equals(LocationManager.GPS_PROVIDER))
                                Log.e("Location", "Time GPS: " + time2); // This is what we want!
                            else
                                Log.e("Location", "Time Device (" + location.getProvider() + "): " + time2);
                            serviceUpdateLocation(location);
                           getCompleteAddressString(latitude,longitude);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVICE,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                Log.e("RUNTIME"," "+obj);

                                                if (!obj.getBoolean("error")) {
                                                    //if there is a success
                                                    //storing the name to sqlite with status synced
                            //                        Toast.makeText(getApplicationContext(), "NOT_RUNTIMEODIN_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    //if there is some error
                                                    //saving the name to sqlite with status unsynced
                         //                           Toast.makeText(getApplicationContext(), "RUNTIMEODIN_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                                                    OdInRunTime(empId,Date,time1,addressLinee,String.valueOf(latitude),String.valueOf(longitude),"R",Date,NAME_SYNCED_WITH_SERVERRUN);

                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                                            EMPODINN = sp.getString("EMPODIN","");
                                            Log.e("OODDINService",EMPODINN);
                                            if(EMPODIN.equals(sp.getString("EMPODIN",""))){
                                           //    Toast.makeText(getApplicationContext(), "RUN_INSERT SQL DB", Toast.LENGTH_SHORT).show();
                                                OdInRunTime(empId,Date,time1,addressLinee,String.valueOf(latitude),String.valueOf(longitude),"R",Date, NAME_NOT_SYNCED_WITH_SERVERRUN);
                                            }
                                            else {
                                                // myTimer.cancel();
                                 //               Toast.makeText(getApplicationContext(), "RUN_NO_INSERT SQL DB", Toast.LENGTH_SHORT).show();

                                            }
//                                            if (FGG.equals(FGF)){
//                                                OdInRunTime(empId,Date,time1,addressLinee,String.valueOf(latitude),String.valueOf(longitude),"R",Date, NAME_NOT_SYNCED_WITH_SERVERRUN);
//                                            }
//                                            else {
//
//                                            }

                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                                    EMPODINN = sp.getString("EMPODIN","");
                                    Log.e("OODDINService",EMPODINN);
                                    Log.e("DDBB",EMPODINN);
                                    if(EMPODIN.equals(sp.getString("EMPODIN",""))){
                                        params.put("emp_id", empId);
                                        params.put("od_date", Date);
                                        params.put("od_time", runtime);
                                        params.put("actual_loc", addressLinee);
                                        params.put("lat", String.valueOf(latitude));
                                        params.put("long",String.valueOf(longitude));
                                        params.put("'od_type", "R");
                                        params.put("srink_date", Date);
                                        Log.e("YESS","YES");
                                        Log.e("rundate",empId+" "+Date+" "+runtime+" "+addressLinee+" "+String.valueOf(latitude)+" "+String.valueOf(longitude)+" "+Date+" "+EMPODIN);
                                        Log.e("EmpId"," "+empId);

                                    }
                                    else {
                                       // myTimer.cancel();
                                        Log.e("No","");
                                    }
//                                    if (FGG.equals(FGF)){
//                                        params.put("emp_id", empId);
//                                        params.put("od_date", Date);
//                                        params.put("od_time", runtime);
//                                        params.put("actual_loc", addressLinee);
//                                        params.put("lat", String.valueOf(latitude));
//                                        params.put("long",String.valueOf(longitude));
//                                        params.put("'od_type", "R");
//                                        params.put("srink_date", Date);
//                                        Log.e("YESS2","YES");
//                                        Log.e("rundate2",empId+" "+Date+" "+runtime+" "+addressLinee+" "+String.valueOf(latitude)+" "+String.valueOf(longitude)+" "+Date+" "+EMPODIN);
//                                        Log.e("EmpId2"," "+empId);
//                                    }
//                                    else {
//
//                                    }

                                    return params;
                                }
                            };

                         //   VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
                            stringRequest.setShouldCache(false);
                            volleySingleton.addToRequestQueue(stringRequest);
                        }
                    }
                }
            }
        }
    }

    private void OdInRunTime(String empIdd, String Date, String time1, String addressLinee, String latitude, String longitude,String  runtype,String syncruntime, int status) {
        sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
        addressLinee = sp.getString("addressLinee", "");
        Log.e("add", addressLinee);
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
            Log.e("RDemp_id",empId);
        }
      //  db.OdInRuntime(empId,Date,time1,sp.getString("addressLinee", ""),latitude,longitude,runtype,syncruntime,status);
        if (offlineAddress != null && offlineAddress.trim().length() > 0) {
            Log.e("aadd","4");
            db.OdInRuntime(empId,Date,time1,offlineAddress,latitude,longitude,runtype,syncruntime,status);
        } else {
            String  noaddressfound = latitude+"-"+longitude;
            db.OdInRuntime(empId,Date,time1,noaddressfound,latitude,longitude,runtype,syncruntime,status);
            Log.e("aadd","5");
        }
        Log.e("Remp_id",empId);
        Log.e("aadd"," "+offlineAddress);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


    }
    private class Logg extends TimerTask{
        @Override
        public void run() {
            myHandlerr.post(new Runnable() {
                @Override
                public void run() {
                    if (FGG.equals(FGF)){
                        Logout();
                    }
                }
            });
        }
    }
    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    getLocationInfo();
                    //autoLogout();
                    //  autoLogOut();
                    viewAll();
                    LogOutForCalenderTime();
                    if (FGG.equals(FGF)){
                        Logout();
                    }
                }
            });

        }
    }
    public void autoLogout(){
        CountDownTimer timer = new CountDownTimer(15 *60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                //Some code

            }

            public void onFinish() {
                //Logout
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        };
    }

    private void serviceUpdateLocation(Location location){
        intent.putExtra("latutide",location.getLatitude()+"");
        intent.putExtra("longitude",location.getLongitude()+"");
        sendBroadcast(intent);
        Log.e("cityyy"," "+location.getLatitude()+" "+location.getLongitude());
        getCompleteAddressString(location.getLatitude(),location.getLongitude());
    }
     private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                offlineAddress = strReturnedAddress.toString();
                Log.e("aadd",offlineAddress);
                sp = getSharedPreferences(Consts.OFFLINEADDRESS, MODE_PRIVATE);
                SharedPreferences.Editor    edit1    =   sp.edit();
                edit1.putString("OFFLINEADDRESS",offlineAddress);
                edit1.clear();
                edit1.commit();
                Log.e("My Current loction"+"Time"+" "+time1, strReturnedAddress.toString());
            } else {
                Log.e("My Current loction"+"Time"+" "+time1, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current loction"+"Time"+" "+time1, "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onLocationChanged(Location loc){
        locationManager.removeUpdates(this);
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        getCompleteAddressString(loc.getLatitude(),loc.getLongitude());
        Log.e("LOCATIONCHANGE"," "+loc.getLatitude()+" "+loc.getLongitude());
        String cityName = null;
        String locality = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0)
//                System.out.println(addresses.get(0).getLocality());
            cityName = addresses.get(0).getLocality();
            locality = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = longitude + " " + latitude + "My Currrent City is:"
                + cityName;
        addressLinee = cityName+" "+locality;
        sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor    edit1    =   sp.edit();
        edit1.putString("addressLinee",addressLinee);
        edit1.commit();
    //  Toast.makeText(getApplicationContext(),"location" +addressLinee,Toast.LENGTH_SHORT).show();
        Log.e("city"," "+s+" "+addressLinee);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }
}