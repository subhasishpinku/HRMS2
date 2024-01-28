package in.co.icdswb.hrms.ActivityService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
import in.co.icdswb.hrms.ActivityDatabase.NetworkStateChecker;
import in.co.icdswb.hrms.ActivityDatabase.NetworkStateCheckerOdOut;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.Alam.AlarmNotificationService;
import in.co.icdswb.hrms.Alam.AlarmReceiver;
import in.co.icdswb.hrms.Alam.AlarmSoundService;
import in.co.icdswb.hrms.AlamActivity;
import in.co.icdswb.hrms.Attendenceregister_Activity;
import in.co.icdswb.hrms.BuildConfig;
import in.co.icdswb.hrms.GPSTracker;
import in.co.icdswb.hrms.Login;
import in.co.icdswb.hrms.NotificationActivity.App;
import in.co.icdswb.hrms.ODApprove_Activity;
import in.co.icdswb.hrms.ODLog_Activity;
import in.co.icdswb.hrms.ODMapsActivity;
import in.co.icdswb.hrms.R;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;
import in.co.icdswb.hrms.TaEntry_Activity;

import static com.android.volley.VolleyLog.TAG;

public class Profile_Activity extends Activity implements View.OnClickListener, LocationListener {
    ImageView obattId,odlogId,odapproveId,dallyattviewId,taentryId,logoutId;
    AlertDialog.Builder builder;
    private AlertDialog alertDialog = null;
    TextView dateID,timeID,usernameID,empID,empbranchID,empdesinID;
    EditText addressID,purposeID;
    ImageView profileId;
    private DatabaseHelper db;
    DatabaseHelper DB = null;
    /////////////////////////////////////////////
    LocationManager locationManager;
    boolean GpsStatus ;
    MyMainReceiver myMainReceiver;
    Intent myIntent = null;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    private PendingIntent pendingIntent;
    Timer timer;
    private static final int ALARM_REQUEST_CODE = 133;
    String empId,timee,date,lat,lng;
    String location,address,purpose;
    Button submitID;
    String  emp_id,od_date,formattedtime;
    SharedPreferences sp;
    ////////////////////////////////sync//////////////////////////////
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private BroadcastReceiver broadcastReceiver;
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    public static final int ODOUT_SYNCED_WITH_SERVER = 1;
    public static final int ODOUT_NOT_SYNCED_WITH_SERVER = 0;
    private BroadcastReceiver broadcastReceiverout;
    public static final String DATA_SAVED_BROADCASTOUT = "net.simplifiedcoding.datasavedout";
    public static final String ODIN = "http://ascensiveeducare.com/hrms/service/od_in_type.php";
    public static final String ODOUT = "http://ascensiveeducare.com/hrms/service/od_out_type.php";
    ///////////////////////////////////////////////
    public static final String SERVICE = "http://ascensiveeducare.com/hrms/service/od_runtime.php";
    public static final int NAME_SYNCED_WITH_SERVERRUN = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVERRUN = 0;
    ///////////////////////////////////////
    Button start_time, end_time, exit;
    int flag = 1;
    int odin;
    String EMPODIN ="1";
    String EMPODINN;
    int hour,mintes,mint,mintesss;
    int hourr = 0;
    int mintess =0;
    int second =0;
    String time;
    String  offlineAddress,offlineAddress1;
    double latitude, longitude;
    ////////////////////////////////RunTime Location///////////////////////////////////////////
    private String mLastUpdateTime;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    //  private BroadcastReceiver sendBroadcastReceiver;
    private NetworkStateChecker networkStateChecker;
    private NetworkStateCheckerOdOut networkStateCheckerOdOut;
    CardView cardviewprofile,cardviewplaning,cardviewapprove,cardviewattendence,cardviewtaentry,hrmswebID;
    String SystemTime,Time,office_close_time,Time1,UPADATETIME;
    String DATETIME,checkdate;
    int MIN;
    private NotificationManagerCompat notificationManager;
    String FLAG;
    String FLAGG = "1";
    String  img;
    String dbfg = "1";
    String FG = "0";
    String FGG;
    String FGF = "1";
    int dd,mm,yy,ddd,mmm,yyy;
    String sdd,smm,syy,dddd,dmmm,dyyy;
    String formattedDate;
    Button startjobId;
    LinearLayout llv,llvv;
    String flagid,flageupdate;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private int PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        cardviewprofile = (CardView)findViewById(R.id.cardviewprofile);
        cardviewplaning = (CardView)findViewById(R.id.cardviewplaning);
        cardviewapprove = (CardView)findViewById(R.id.cardviewapprove);
        cardviewattendence = (CardView)findViewById(R.id.cardviewattendence);
        cardviewtaentry = (CardView)findViewById(R.id.cardviewtaentry);
        hrmswebID = (CardView)findViewById(R.id.hrmswebID);
        cardviewprofile.setOnClickListener(this);
        cardviewplaning.setOnClickListener(this);
        cardviewapprove.setOnClickListener(this);
        cardviewattendence.setOnClickListener(this);
        cardviewtaentry.setOnClickListener(this);
        hrmswebID.setOnClickListener(this);
        obattId = (ImageView)findViewById(R.id.obattId);
        odlogId = (ImageView)findViewById(R.id.odlogId);
        odapproveId = (ImageView)findViewById(R.id.odapproveId);
        dallyattviewId = (ImageView)findViewById(R.id.dallyattviewId);
        taentryId = (ImageView)findViewById(R.id.taentryId);
        logoutId = (ImageView)findViewById(R.id.logoutId);
        usernameID = (TextView)findViewById(R.id.usernameID);
        empID = (TextView)findViewById(R.id.empID);
        empbranchID = (TextView)findViewById(R.id.empbranchID);
        empdesinID = (TextView)findViewById(R.id.empdesinID);
        profileId = (ImageView)findViewById(R.id.profileId);
        startjobId =(Button) findViewById(R.id.startjobId);
        llv = (LinearLayout)findViewById(R.id.llv);
        llvv = (LinearLayout)findViewById(R.id.llvv);
        startjobId.setOnClickListener(this);
        obattId.setOnClickListener(this);
        odlogId.setOnClickListener(this);
        odapproveId.setOnClickListener(this);
        dallyattviewId.setOnClickListener(this);
        taentryId.setOnClickListener(this);
        logoutId.setOnClickListener(this);
        db = new DatabaseHelper(this);
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String  susernameID = String.valueOf(user.getEmp_name());
        usernameID.setText(susernameID);
        String  sempidID = String.valueOf(user.getEmp_id());
        empID.setText(sempidID);
        String  sempbranchID = String.valueOf(user.getEmp_branch());
        empbranchID.setText(sempbranchID);
        String  sempdesinID = String.valueOf(user.getEmp_desig());
        empdesinID.setText(sempdesinID);
        img = String.valueOf(user.getEmp_img());
        emp_id = String.valueOf(user.getEmp_id());
        sp = getSharedPreferences(Consts.EMPID, MODE_PRIVATE);
        SharedPreferences.Editor    edit    =   sp.edit();
        edit.putString("EmpId",emp_id);
        edit.commit();
        Glide.with(getApplicationContext())
                .load(img)
                .into(profileId);
        //  time =  String.valueOf(user.getOffice_close_time());
        Log.e("PROFIMG",img);
        viewAll();
        checkServiceStatus();
        start_time = (Button) findViewById(R.id.start_time);
        end_time = (Button) findViewById(R.id.end_time);
        exit = (Button) findViewById(R.id.exit);
        start_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        exit.setOnClickListener(this);
        myIntent = new Intent(Profile_Activity.this, GPSTracker.class);
        startService(myIntent);
        if (isNetworkAvailable()){
            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled()){
                lat = String.valueOf(gpsTracker.latitude);
                lng = String.valueOf(gpsTracker.longitude);
                String country = gpsTracker.getCountryName(this);
                String  city = gpsTracker.getLocality(this);
                String postalCode = gpsTracker.getPostalCode(this);
                location = gpsTracker.getAddressLine(this);
                Log.e("gps",lat+ "," +lng+ ","+country+ ","+city+""+postalCode+""+location);
            }
            else
            {
                gpsTracker.showSettingsAlert();
            }
        }
        else {
            //WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            //wifi.setWifiEnabled(true);
           // startActivityForResult(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS), 0);

        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        broadcastReceiverout = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiverout, new IntentFilter(DATA_SAVED_BROADCASTOUT));
        networkStateChecker = new NetworkStateChecker();
        registerReceiver(networkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        networkStateCheckerOdOut =new NetworkStateCheckerOdOut();
        registerReceiver(networkStateCheckerOdOut, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        loadODIn();
        loadodOut();
        sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
        String  addressLinee = sp.getString("addressLinee", "");
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        // odin = Integer.parseInt(String.valueOf(EMPODIN));
        Log.e("value"," "+EMPODINN);
        Log.e("OODDIN",EMPODINN);
        if (EMPODIN==sp.getString("EMPODIN","")){
            start_time.setVisibility(View.GONE);
            Log.e("FOD1",sp.getString("EMPODIN",""));
        }
        else if (EMPODIN==sp.getString("EMPODIN","1")){
            //  Toast.makeText(getApplicationContext(),"Hi1",Toast.LENGTH_SHORT).show();
            start_time.setVisibility(View.VISIBLE);
            end_time.setVisibility(View.GONE);
            Log.e("FOD2",sp.getString("EMPODIN",""));
        }
        else {
            start_time.setVisibility(View.VISIBLE);
            end_time.setVisibility(View.GONE);
            Log.e("FOD3",sp.getString("EMPODIN",""));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, mintes);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        timer = new Timer();
        timer.schedule(new RemindTask(), time);
        sp = getSharedPreferences(Consts.OFFLINEADDRESS, MODE_PRIVATE);
        offlineAddress1 = sp.getString("OFFLINEADDRESS", "");
        // Toast.makeText(getApplicationContext(),offlineAddress,Toast.LENGTH_SHORT).show();
        init();
        restoreValuesFromBundle(savedInstanceState);
        startLocationButtonClick();
        Intent alarmIntent = new Intent(Profile_Activity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(Profile_Activity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
        notificationManager = NotificationManagerCompat.from(this);

        Cursor res = db.getFlagData();
        if(res.getCount() == 0) {
            Log.e("Error","Nothing found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            String flagId =  res.getString(0);
            FGG =  res.getString(1);
            DATETIME = res.getString(2);
            checkdate = res.getString(3);
            buffer.append("logindate :"+ res.getString(2)+"\n");
            Log.e("DBPROclose", FGG+" "+flagId+" "+DATETIME+" "+checkdate);

        }

//        Calendar cal = Calendar.getInstance();//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        int hour = cal.get(Calendar.HOUR_OF_DAY);
//        int minute = cal.get(Calendar.MINUTE);
//        int second = cal.get(Calendar.SECOND);
//        System.out.printf("Now is %4d/%02d/%02d %02d:%02d:%02d\n",
//                year, month+1, day, hour, minute, second);
//        Log.e("DDDD",year+" "+month+" "+day+" "+hour+" "+minute+" "+" "+second);
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
            sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.putString("EMPODIN", "1");
            ed.commit();
            end_time.setVisibility(View.VISIBLE);
            start_time.setVisibility(View.GONE);
            startService();
            if (sdd.equals(dddd) && smm.equals(dmmm)){
            }
            else {
                SQLiteDatabase dbb = db.getReadableDatabase();
                dbb.execSQL("UPDATE loginshow SET loginflag='0' WHERE logdateid=" +dbfg);
                //SharedPrefManager.getInstance(getApplicationContext()).logout();
                Intent intent5 = new Intent(this, Login.class);
                startActivity(intent5);
            }
        }
        else {

        }
        Cursor reflag = db.getupdateflagdata();
        if(reflag.getCount() == 0) {
            Log.e("Error","Nothing found");
            db.insertflag("0");
            return;
        }
        StringBuffer buffe = new StringBuffer();
        while (reflag.moveToNext()) {
            flagid =  reflag.getString(0);
            flageupdate =  reflag.getString(1);
            Log.e("FLAGLOG", " "+flagid+" "+flageupdate);
            if (flageupdate.equals("1")){
                llv.setVisibility(View.VISIBLE);
                llvv.setVisibility(View.GONE);
                startService();
            }
            else if (flageupdate.equals("0")){
                llv.setVisibility(View.GONE);
                llvv.setVisibility(View.VISIBLE);
                startService();
            }
            else {
                llvv.setVisibility(View.GONE);
                startService();

            }
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(Profile_Activity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Profile_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Profile_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
            }
        }
        else{
            //load your map here
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
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
    class RemindTask extends TimerTask {
        public void run() {
            sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
            EMPODINN = sp.getString("EMPODIN","");
            Log.e("OODDINService",EMPODINN);
            if(EMPODIN==sp.getString("EMPODIN","")){
                Log.e("ONO_ODINN","PYes");
                // odOut();
//                Intent i = new Intent(Profile_Activity.this, Login.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
                Calendar cal = Calendar.getInstance();
                Date sdate=cal.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                SystemTime=dateFormat.format(sdate);
                Log.e("Sys"," "+SystemTime);
                sp = getSharedPreferences(Consts.FLAG, MODE_PRIVATE);
                SharedPreferences.Editor edit1 = sp.edit();
                edit1.putString("FLAG", "1");
                edit1.commit();
                showDiolog();
            }
            else {
                Log.e("ONO_ODINN","PNo");
//                Intent i = new Intent(Profile_Activity.this, Login.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);


            }
        }
    }
    public void showDiolog(){
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
        //  Log.e("Timee"," "+Time);
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
        Time1 = String.valueOf(date1.getTime());
        System.out.println("Date - Time in milliseconds : " + date1.getTime());
        Log.e("TTIME"," "+date1.getTime());
        ///////////////////////////////////////////////////////////////////
        // / 28800000 32400000 14400000
        if (date.getTime()==date1.getTime()){
            Log.e("TTIME"," "+ "OK");


        }
        else {

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
            String title = SystemTime;
            String mess = "OD TIME IS OVER";
            Bitmap larBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_user);
            Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CH2)
                    .setSmallIcon(R.drawable.hlogo)
                    .setContentTitle(title)
                    .setContentText(mess)
                    .setLargeIcon(larBitmap)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.BLUE)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .build();
            notificationManager.notify(2,notification);
            Intent i = new Intent(Profile_Activity.this, AlamActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
    public void setAlarm() {
        int alarmTriggerTime = 1*60*60;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, alarmTriggerTime);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmTriggerTime, pendingIntent);
        Toast.makeText(this, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();
    }
    //Stop/Cancel alarm manager
    public void stopAlarmManager() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent
        stopService(new Intent(Profile_Activity.this, AlarmSoundService.class));
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);
        Toast.makeText(this, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
    }
    public void viewAll() {
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            // show message
            Log.e("Error","Nothing found");
            //  Toast.makeText(Profile_Activity.this,"Please Login With in Working Hour",Toast.LENGTH_LONG).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Logstatus :"+ res.getString(1)+"\n");
            buffer.append("Imeistatus :"+ res.getString(2)+"\n");
            buffer.append("Imeistatus_msg :"+ res.getString(3)+"\n\n");
            buffer.append("Logstatus_msg :"+ res.getString(4)+"\n\n");
            buffer.append("Emp_desig :"+ res.getString(5)+"\n\n");
            buffer.append("Emp_img :"+ res.getString(6)+"\n\n");
            buffer.append("Office_close_time :"+ res.getString(7)+"\n\n");
            buffer.append("Emp_branch :"+ res.getString(8)+"\n\n");
            buffer.append("Emp_name :"+ res.getString(9)+"\n\n");
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            buffer.append("imei_id :"+ res.getString(11)+"\n\n");
            buffer.append("imei_id :"+ res.getString(12)+"\n\n");
            usernameID.setText(res.getString(9));
            empID.setText(res.getString(10));
            empbranchID.setText(res.getString(8));
            empdesinID.setText(res.getString(5));
            String  img1 = String.valueOf(res.getString(6));
            Glide.with(getApplicationContext())
                    .load(img1)
                    .into(profileId);
            empId = String.valueOf(res.getString(10));
            time = String.valueOf(res.getString(7));
            String currentString = time;
            String[] separated = currentString.split(":");
            hour = Integer.parseInt(separated[0].trim());
            mintes = Integer.parseInt(separated[1].trim());
            Log.e("mintes", String.valueOf(mintes));
            Log.e("hour", String.valueOf(hour));
            int mm = 30;
            //  MIN = mintes+mm;
            // Log.e("MIN", String.valueOf(MIN));
            int div = mintes / mm;
            Log.e("TTIME"," "+ div);
        }
        Log.e("Data",buffer.toString());

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        String dateInString1 = time;
        Date date1 = null;
        try {
            date1 = sdf1.parse(dateInString1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(dateInString1);
        Time1 = String.valueOf(date1.getTime());
        Log.e("DBTIME"," "+Time1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.obattId:
                Intent intent = new Intent(this, ODMapsActivity.class);
                startActivity(intent);
                break;
            case R.id.odlogId:
                Intent intent1 = new Intent(this, ODLog_Activity.class);
                startActivity(intent1);
                break;
            case R.id.odapproveId:
                Intent intent2 = new Intent(this, ODApprove_Activity.class);
                startActivity(intent2);
                break;
            case R.id.dallyattviewId:
                Intent intent3 = new Intent(this, Attendenceregister_Activity.class);
                startActivity(intent3);
                break;
            case R.id.taentryId:
                Intent intent4 = new Intent(this, TaEntry_Activity.class);
                startActivity(intent4);
                break;
            case R.id.cardviewprofile:
                Intent intentt = new Intent(this, ODMapsActivity.class);
                startActivity(intentt);
                break;
            case R.id.cardviewplaning:
                Intent intentt1 = new Intent(this, ODLog_Activity.class);
                startActivity(intentt1);
                break;
            case R.id.cardviewapprove:
                Intent intentt2 = new Intent(this, ODApprove_Activity.class);
                startActivity(intentt2);
                break;
            case R.id.cardviewattendence:
                Intent intentt3 = new Intent(this, Attendenceregister_Activity.class);
                startActivity(intentt3);
                break;
            case R.id.cardviewtaentry:
                Intent intentt4 = new Intent(this, TaEntry_Activity.class);
                startActivity(intentt4);
                break;

            case R.id.logoutId:

//                start_time.setVisibility(View.VISIBLE);
//                end_time.setVisibility(View.GONE);
                if (EMPODIN==sp.getString("EMPODIN","")){
                    sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = sp.edit();
                    edit1.putString("EMPODIN", "0");
                    edit1.commit();
                    double Latitude = Double.parseDouble(lat);
                    double Langitude = Double.parseDouble(lng);
                    getCompleteAddressString(Latitude,Langitude);
                    odOut();
                    //stopService();
                    //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                }
                else {
                    SQLiteDatabase updateflag = db.getReadableDatabase();
                    updateflag.execSQL("UPDATE flagtable SET flagupdate='0' WHERE flagid=" +dbfg);
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    Intent intent5 = new Intent(this, Login.class);
                    startActivity(intent5);
                    finish();
                    //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.hrmswebID:
                String weblink = "http://hrms.ascensiveeducare.com";
                Uri uri = Uri.parse(weblink);
                Intent web = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(web);
                break;
            case R.id.start_time:
                //start_time.setEnabled(false);
                double Latitude = Double.parseDouble(lat);
                double Langitude = Double.parseDouble(lng);
                getCompleteAddressString(Latitude,Langitude);
                alertDiologBox();
                break;
            case R.id.end_time:
                sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("EMPODIN", "0");
                edit.commit();
                //start_time.setEnabled(true);
                odOut();
                break;
            case R.id.exit:
                startService();
                SyncAllData();
                break;
            case R.id.startjobId:
                SQLiteDatabase updateflag = db.getReadableDatabase();
                updateflag.execSQL("UPDATE flagtable SET flagupdate='1' WHERE flagid=" +dbfg);
                finish();
                startActivity(getIntent());
                startService();
                break;
            default:
        }
    }

    public void SyncAllData() {
        Cursor res = db.getruntimeSync();
        if(res.getCount() == 0) {
            // show message
            Log.e("Error","Nothing found");
            Toast.makeText(Profile_Activity.this,"No Record Found",Toast.LENGTH_LONG).show();
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("rid :"+ res.getString(0)+"\n");
            buffer.append("rempid :"+ res.getString(1)+"\n");
            buffer.append("roddate :"+ res.getString(2)+"\n");
            buffer.append("rodtime :"+ res.getString(3)+"\n\n");
            buffer.append("ractual_loc :"+ res.getString(4)+"\n\n");
            buffer.append("rlat :"+ res.getString(5)+"\n\n");
            buffer.append("rlng :"+ res.getString(6)+"\n\n");
            buffer.append("rodtype :"+ res.getString(7)+"\n\n");
            buffer.append("srinkdateruntime :"+ res.getString(8)+"\n\n");
            buffer.append("rstatus :"+ res.getString(9)+"\n\n");

        }
        // Show all data
        Log.e("Data",buffer.toString());
    }

    private void alertDiologBox(){
        builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Activity.this);
        // Get custom login form view.
        final View loginFormView = getLayoutInflater().inflate(R.layout.alertdiologlayout, null);
        // Set above view in alert dialog.
        builder.setView(loginFormView);
        Button closeID = (Button) loginFormView.findViewById(R.id.closeID);
        submitID = (Button)loginFormView.findViewById(R.id.submitID);
        dateID = (TextView) loginFormView.findViewById(R.id.dateID);
        timeID = (TextView) loginFormView.findViewById(R.id.timeID);
        addressID = (EditText)loginFormView.findViewById(R.id.addressID);
        purposeID = (EditText)loginFormView.findViewById(R.id.purposeID);
        dateTime();
        submitID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                StartOD();
//                Intent intent = new Intent(getApplicationContext(),Profile_Activity.class);
//                startActivity(intent);
                try {
                    // Close Alert Dialog.
                    alertDialog.cancel();
                    //     alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = sp.edit();
                    edit1.putString("EMPODIN", "1");
                    edit1.commit();
                    start_time.setVisibility(View.GONE);
                    end_time.setVisibility(View.VISIBLE);
                    StartOD();
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
        closeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                    startActivity(getIntent());
                    alertDialog.cancel();
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        builder.setCancelable(false);
        //  alertDialog.setCanceledOnTouchOutside(false);
        alertDialog = builder.create();
        alertDialog.show();

    }
    private void dateTime(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate3 = df3.format(c.getTime());
        dateID.setText(formattedDate3);
        Calendar cc = Calendar.getInstance();
        System.out.println("Current time => " + cc.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        String formattedtime = time.format(c.getTime());
        timeID.setText(formattedtime);

    }
    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(Profile_Activity.this, MyService.class);
            startService(myIntent);
            // ContextCompat.startForegroundService(Profile_Activity.this, new Intent(getApplicationContext(), MyService.class));
        }
        else
        {
            gpsTracker.showSettingsAlert();
        }

    }

    private void stopService(){
        if(myIntent != null){
            stopService(myIntent);

        }
        myIntent = null;
    }

    public void off(){
        LocationManager loc = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );
        if( loc.isProviderEnabled(LocationManager.GPS_PROVIDER ) )
        {
            // Toast.makeText( this, "GPS off", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
            startActivity(myIntent);
        }
    }
    private void StartOD(){
        address = addressID.getText().toString().trim();
        purpose = purposeID.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            addressID.setError("Please enter Your address");
            addressID.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(purpose)) {
            timeID.setError("Please enter your purpose");
            timeID.requestFocus();
            return;
        }
        if (isNetworkAvailable()){
            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled()){
                lat = String.valueOf(gpsTracker.latitude);
                lng = String.valueOf(gpsTracker.longitude);
                String country = gpsTracker.getCountryName(this);
                String  city = gpsTracker.getLocality(this);
                String postalCode = gpsTracker.getPostalCode(this);
                String  addressLine = gpsTracker.getAddressLine(this);
                //  Toast.makeText(getApplicationContext()," " +stringLatitude+ ""+stringLongitude+ ""+country+""+city+""+postalCode+ ""+addressLine+ "service lati", Toast.LENGTH_SHORT).show();
                Log.e("gps_ODin",lat+ "," +lng+ ","+country+ ","+city+""+postalCode+""+addressLine);
                //location = country+ ","+city+""+postalCode+","+addressLine;
                location = addressLine;
            }
            else
            {
                gpsTracker.showSettingsAlert();
            }
        }
        else {

        }

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        date = df3.format(c.getTime());
        Calendar cc = Calendar.getInstance();
        System.out.println("Current time => " + cc.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        timee = time.format(c.getTime());
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        empId = String.valueOf(user.getEmp_id());
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
        }

        SQLiteDatabase dbb = db.getReadableDatabase();
        dbb.execSQL("UPDATE loginshow SET loginflag='1' WHERE logdateid=" +dbfg);
        startService();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,ODIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response_OdIn",response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("OdLog",""+obj);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                // Toast.makeText(getApplicationContext(), "NOT_ODIN_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                synodinsave(empId,date,timee,address,location,purpose,lat,lng,"I",date,NAME_SYNCED_WITH_SERVER);
                                //  Toast.makeText(getApplicationContext(), "ODIN_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        synodinsave(empId,date,timee,address,location,purpose,lat,lng,"I",date, NAME_NOT_SYNCED_WITH_SERVER);
                        //  Toast.makeText(getApplicationContext(), "ODIN_NOT_SYNCED_WITH_SERVER", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emp_id", empId);
                params.put("od_date", date);
                params.put("od_time",  timee);
                params.put("manul_loc", address);
                params.put("actual_loc", location);
                params.put("od_perpouse", purpose);
                params.put("lat", lat);
                params.put("long", lng);
                params.put("od_type", "I");
                params.put("srink_date", date);
                Log.e("aadd"," "+location);
                Log.e("Service_Od",empId+" "+date+" "+timee+" "+address+" "+location+" "+purpose+" "+lat+" "+lng);
                return params;
            }
        };
        //  VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }
    private void synodinsave(String empId, String date, String timee, String address, String locationn, String purpose, String lat, String lng, String odtype,String srinkdate, int status) {
        addressID.setText("");
        purposeID.setText("");
       // sp = getSharedPreferences(Consts.SP_NAME, MODE_PRIVATE);
       // location = sp.getString("addressLinee", "");
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
        }
        //db.ODIN(empId,date,timee,address,sp.getString("addressLinee", ""),purpose,lat,lng,odtype,srinkdate,status);
      // db.ODIN(empId,date,timee,address,location,purpose,lat,lng,odtype,srinkdate,status);
        Log.e("aadd","OK1"+location);
        db.ODIN(empId,date,timee,address,location,purpose,lat,lng,odtype,srinkdate,status);
        if (location != null && location.trim().length() > 0) {
            Log.e("aadd","0");
            db.ODIN(empId,date,timee,address,location,purpose,lat,lng,odtype,srinkdate,status);
        } else {
            String  noaddressfound = lat+"-"+lng;
            db.ODIN(empId,date,timee,address,noaddressfound,purpose,lat,lng,odtype,srinkdate,status);
            Log.e("aadd","1");
        }
        Log.e("ODINLOCATIONOFFLINE",empId+" "+date+" "+timee+" "+address+" "+location+" "+purpose+" "+lat+" "+" "+lng+" "+odtype+" "+srinkdate+" "+status+" ");
    }
    private void loadODIn() {
        Cursor res = db.getodinSync();
        if(res.getCount() == 0) {
            Log.e("Error","Nothing found");
            return;
        }else {
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("emp_id :"+ res.getString(1)+"\n");
            buffer.append("od_date :"+ res.getString(2)+"\n");
            buffer.append("od_time :"+ res.getString(3)+"\n\n");
            buffer.append("manul_loc :"+ res.getString(4)+"\n\n");
            buffer.append("actual_loc :"+ res.getString(5)+"\n\n");
            buffer.append("od_perpouse :"+ res.getString(6)+"\n\n");
            buffer.append("lat :"+ res.getString(7)+"\n\n");
            buffer.append("long :"+ res.getString(8)+"\n\n");
            buffer.append("od_type :"+ res.getString(9)+"\n\n");
            buffer.append("srink_date :"+ res.getString(10)+"\n\n");
            buffer.append("status :"+ res.getString(11)+"\n\n");
            Log.e("all data", buffer.toString());
        }
    }
    private void loadodOut() {
        Cursor res = db.getodutSync();
        if (res.getCount() == 0) {
            Log.e("Error", "Nothing found");

            return;
        } else {
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :" + res.getString(0) + "\n");
            buffer.append("emp_id :" + res.getString(1) + "\n");
            buffer.append("od_date :" + res.getString(2) + "\n");
            buffer.append("od_time :" + res.getString(3) + "\n\n");
            buffer.append("actual_loc :" + res.getString(4) + "\n\n");
            buffer.append("lat :" + res.getString(5) + "\n\n");
            buffer.append("long :" + res.getString(6) + "\n\n");
            buffer.append("od_type :" + res.getString(7) + "\n\n");
            buffer.append("srink_date :" + res.getString(8) + "\n\n");
            buffer.append("status :" + res.getString(9) + "\n\n");
            Log.e("all odout", buffer.toString());

        }

    }

    @Override
    protected void onStart() {
        myMainReceiver = new MyMainReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_UPDATE_CNT);
        intentFilter.addAction(MyService.ACTION_UPDATE_MSG);
        registerReceiver(myMainReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myMainReceiver);
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stopService();
        try{
            if(broadcastReceiver!=null)
                unregisterReceiver(broadcastReceiver);
            if (broadcastReceiverout!=null)
                unregisterReceiver(broadcastReceiverout);
            if (networkStateChecker!=null)
                unregisterReceiver(networkStateChecker);
            if (networkStateCheckerOdOut!=null)
                unregisterReceiver(networkStateCheckerOdOut);

        }catch(Exception e){}
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        if(EMPODIN==sp.getString("EMPODIN","")){
            //   Toast.makeText(getApplicationContext(),"onDestroy Profile",Toast.LENGTH_SHORT).show();
            Log.e("onDestroy Profile","onDestroy Profile");
            odOut();
        }
        else {

        }


    }

    private class MyMainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MyService.ACTION_UPDATE_CNT)){
                int int_from_service = intent.getIntExtra(MyService.KEY_INT_FROM_SERVICE, 0);
                // textViewCntReceived.setText(String.valueOf(int_from_service));
            }else if(action.equals(MyService.ACTION_UPDATE_MSG)){
                String string_from_service = intent.getStringExtra(MyService.KEY_STRING_FROM_SERVICE);
                //  textViewMsgReceived.setText(String.valueOf(string_from_service));
            }


        }

    }

    private void checkServiceStatus() {

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Profile_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(Profile_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please enable services to get gps", Toast.LENGTH_LONG).show();
                }
            }


        }
    }

    private void odOut(){
        SQLiteDatabase updateflag = db.getReadableDatabase();
        updateflag.execSQL("UPDATE flagtable SET flagupdate='0' WHERE flagid=" +dbfg);
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
        start_time.setVisibility(View.VISIBLE);
        end_time.setVisibility(View.GONE);
        // stopService();
        SQLiteDatabase dbb = db.getReadableDatabase();
        dbb.execSQL("UPDATE loginshow SET loginflag='0' WHERE logdateid=" +dbfg);
        //SharedPrefManager.getInstance(getApplicationContext()).logout();
        Intent intent5 = new Intent(this, Login.class);
        startActivity(intent5);
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
        Log.e("aadd","OK"+location);
        db.synodoutnsave(empId,outdate,outtime,location,outlat,outlang,outtype,syncruntime,status);
        if (location != null && location.trim().length() > 0) {
            Log.e("aadd","2");
            db.synodoutnsave(empId,outdate,outtime,location,outlat,outlang,outtype,syncruntime,status);
        } else {
            String  noaddressfound = outlat+"-"+outlang;
            db.synodoutnsave(empId,outdate,outtime,noaddressfound,outlat,outlang,outtype,syncruntime,status);
            Log.e("aadd","3");
        }
        Log.e("aadd"," "+location);
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            Log.e("ODINLOCATIONOFFLINEE"," "+addresses);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                location = strReturnedAddress.toString();
                Log.e("ODINLOCATIONOFFLINE",location+" "+"address1"+strAdd);
                sp = getSharedPreferences(Consts.OFFLINEADDRESS, MODE_PRIVATE);
                SharedPreferences.Editor    edit1    =   sp.edit();
                edit1.putString("OFFLINEADDRESS",location);
                edit1.clear();
                edit1.commit();
                Log.e("LOCADD"," "+location);
                Log.e("PMyCurrent loction"+"Time"+" "+timee, strReturnedAddress.toString());
            } else {
                Log.e("PMyCurrent loction"+"Time"+" "+timee, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MyPCurrent loction"+"Time"+" "+timee, "Canont get Address!");
        }
        return strAdd;
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }
            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }
        updateLocationUI();
    }
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            //Toast.makeText(getApplicationContext(),mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
            Log.e("Last Update" ,mLastUpdateTime);
            lat = String.valueOf(mCurrentLocation.getLatitude());
            lng = String.valueOf(mCurrentLocation.getLongitude());
            Log.e("ODINLOCATIONOFFLINE",mCurrentLocation.getLatitude()+" "+"latupdate"+mCurrentLocation.getLongitude());
            getCompleteAddressString(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            SystemTime = mLastUpdateTime;
            UPADATETIME = mLastUpdateTime;
            sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
            EMPODINN = sp.getString("EMPODIN","");
            Log.e("OODDINService",EMPODINN);
            if(EMPODIN==sp.getString("EMPODIN","")){
                Log.e("ONO_ODINN","PYes");
                sp = getSharedPreferences(Consts.FLAG, MODE_PRIVATE);
                FLAG = sp.getString("FLAG","");
                Log.e("FLAG",FLAG);
                if(FLAGG==sp.getString("FLAG","")){
                    Log.e("FLAGG",sp.getString("FLAG",""));
                    showDiolog();
                }
                else {

                }
            }
            else {
                Log.e("ONO_ODINN","PNo");
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //  Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Profile_Activity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(Profile_Activity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //   Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("Inside onResume");
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
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
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        lat = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());
        Log.e("ODINLOCATIONOFFLINE",location.getLatitude()+" "+location.getLongitude());
        getCompleteAddressString(location.getLatitude(),location.getLongitude());
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
