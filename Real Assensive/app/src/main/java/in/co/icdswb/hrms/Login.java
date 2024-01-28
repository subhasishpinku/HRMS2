package in.co.icdswb.hrms;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityService.TelephonyInfo;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;
import in.co.icdswb.hrms.activityGetnumber.OtpReceivedInterface;
import in.co.icdswb.hrms.activityGetnumber.SmsBroadcastReceiver;

import static in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper.TABLE_DATETIMESYSTEM;
import static in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper.TABLE_LOGDATEID;
import static in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper.TABLE_LOGINSHOW;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener, Spinner.OnItemSelectedListener {
    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    private int RESOLVE_HINT = 2;
    Button loginId,registerId;
    TextView editTexUser, PasswordID;
    String user1, pass1;
    String imei;
    String androidid,model,brand;
    public static boolean isMultiSimEnabled = false;
    public static List<SubscriptionInfo> subInfoList;
    public static ArrayList<String> numbers;
    private SubscriptionManager subscriptionManager;
    static final Integer PHONESTATS = 0x1;
    private final String TAG= Login.class.getSimpleName();
    public static final int REQUEST_CODE_PHONE_STATE_READ = 100;
    private int checkedPermission = PackageManager.PERMISSION_DENIED;
    private DatabaseHelper db;
    DatabaseHelper DB = null;
    String SystemTime,formattedDate;
    String office_close_time;
    String Time ="0";
    String Time1 = "0";
    String closeTime;
    ProgressBar progressBar;
    String dateInString1;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    SharedPreferences sp;
    Intent myIntent = null;
    int sVersionCode;
    String sVersionName,sPackName;
    TextView versionId;
    String vname;
    String vvname = "1.0";
    CoordinatorLayout coordinatorLayout;
    String emp_id;
    String Empstatus;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String EMPSTATUS = "EMPSTATUS";
    SharedPreferences sharedpreferences;
    String  STATUSEMP;
    String FG = "0";
    String FGF = "1";
    String DATETIME = "11:10:10";
    String FGG;
    String EMPODIN ="1";
    String dbfg ="1";
    TelephonyManager telephonyManager,mTelephony;
    String IMEEI;
    String imeii;
    private int inter = 0;
    String currentVersion;
    String mobileno ="0";
    Credential credential;
    String phoneNumb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        loginId = (Button) findViewById(R.id.loginId);
        editTexUser = (TextView) findViewById(R.id.editTexUser);
        PasswordID = (TextView) findViewById(R.id.PasswordID);
//        PasswordID.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        PasswordID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if(i== EditorInfo.IME_ACTION_DONE){
//
//                }
//                return false;
//            }
//        });
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        registerId = (Button) findViewById(R.id.registerId);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        versionId =(TextView)findViewById(R.id.versionId);
        registerId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                    intent.putExtra("androidid",androidid);
                    intent.putExtra("model",model);
                    intent.putExtra("brand",brand);
                    startActivity(intent);
            }
        });
        ///phone number method off device////
       // checkphno();
        getClientPhoneNumber();
        Log.e("DEVICEINFO",
                "SERIAL: " + Build.SERIAL + "\n" +
                        "MODEL: " + Build.MODEL + "\n" +
                        "ID: " + Build.ID + "\n" +
                        "Manufacture: " + Build.MANUFACTURER + "\n" +
                        "Brand: " + Build.BRAND + "\n" +
                        "Type: " + Build.TYPE + "\n" +
                        "User: " + Build.USER + "\n" +
                        "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                        "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                        "SDK:  " + Build.VERSION.SDK + "\n" +
                        "BOARD: " + Build.BOARD + "\n" +
                        "BRAND: " + Build.BRAND + "\n" +
                        "HOST: " + Build.HOST + "\n" +
                        "FINGERPRINT: "+Build.FINGERPRINT + "\n" +
                        "Version Code: " + Build.VERSION.RELEASE);
        model = Build.MODEL;
        brand = Build.BRAND;
        Log.e("DEVICEINFOO",model);
        androidid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("iccid"," "+" "+androidid+" "+model+" "+brand);
        //////////////
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        loginId.setOnClickListener(this);
        db = new DatabaseHelper(getApplicationContext());
        numbers = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager = SubscriptionManager.from(Login.this);
        }
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTexUser.setText(loginPreferences.getString("emp_id", ""));
            PasswordID.setText(loginPreferences.getString("pwd", ""));
            saveLoginCheckBox.setChecked(true);

        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        PackageInfo pinfo = null;
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            sVersionCode = pinfo.versionCode;
            sVersionName = pinfo.versionName;
            sPackName = getPackageName();
            int nSdkVersion = Integer.parseInt(Build.VERSION.SDK);
            int nSdkVers = Build.VERSION.SDK_INT;
            Log.e("VersionCode", String.valueOf(sVersionCode));
            versionId.setText("Ver:"+" "+sVersionName);
            Log.e("sVersionName", sVersionName);
            Log.e("sPackName", sPackName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        Date sdate=cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        SystemTime=dateFormat.format(sdate);
        Log.e("Sys"," "+SystemTime);
        askForPermission(Manifest.permission.READ_PHONE_STATE, PHONESTATS);
        showDeviceInfo();
        checkServiceStatus();
        sp = getSharedPreferences(Consts.FLAG, MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sp.edit();
        edit1.putString("FLAG", "0");
        edit1.commit();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
        Cursor res = db.getFlagData();
        if(res.getCount() == 0) {
            Log.e("ErrorDDB","Nothing found");
            db.FLAGTABLEINSERT(FG,DATETIME,formattedDate);
            return;
        }
        else {

        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
           String flagId =  res.getString(0);
            FGG =  res.getString(1);
            buffer.append("logindate :"+ res.getString(2)+"\n");
            Log.e("DBclose", FGG+" "+flagId); }
           if (FGG.equals(FGF)){
               sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
               SharedPreferences.Editor ed = sp.edit();
               ed.putString("EMPODIN", "1");
               ed.commit();
               Intent intent = new Intent(Login.this, Profile_Activity.class);
               startActivity(intent);
           }
           else {

           }
        //retrieve a reference to an instance of TelephonyManager
         telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
           IMEEI = getDeviceID(telephonyManager);
           Log.e("IMEINUMBERFIND"," "+IMEEI);
           mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Login.this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (mTelephony.getPhoneCount() == 2) {
                   String IME = mTelephony.getImei(0);
                    String IIME = mTelephony.getImei(1);
                    imeii = IME+","+IIME;
                    Log.e("IMEINUMBERFINDALLDEVIC1"," "+IME+" "+IIME);
                }else{
                    String  IMEE = mTelephony.getImei();
                    String IIIME = mTelephony.getImei(1);
                    imeii = IMEE+","+IIIME;
                    Log.e("IMEINUMBERFINDALLDEVIC2"," "+IMEE+" "+IIIME);
                }
            }else{
                if (mTelephony.getPhoneCount() == 2) {
                    String  IMEEE = mTelephony.getDeviceId(0);
                    String  IIIMEEE = mTelephony.getDeviceId(1);
                    imeii = IMEEE+","+IIIMEEE;
                    Log.e("IMEINUMBERFINDALLDEVIC3"," "+IMEEE+" "+IIIMEEE);
                } else {
                    String   IMEEEE = mTelephony.getDeviceId(0);
                    String  IIIIMEEE = mTelephony.getDeviceId(1);
                    imeii = IMEEEE+","+IIIIMEEE;
                    Log.e("IMEINUMBERFINDALLDEVIC4"," "+IMEEEE+" "+IIIIMEEE);
                }
            }
        } else {
            String   IMEEEEE = mTelephony.getDeviceId();
            imei =IMEEEEE;
            Log.e("IMEINUMBERFINDALLDEVIC5"," "+IMEEEEE);
        }

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new GetVersionCode().execute();


    }

   public void checkphno(){
     mSmsBroadcastReceiver = new SmsBroadcastReceiver();
     // set google api client for hint request
     mGoogleApiClient = new GoogleApiClient.Builder(this)
             .addConnectionCallbacks(this)
             .enableAutoManage(this, this)
             .addApi(Auth.CREDENTIALS_API)
             .build();

     mSmsBroadcastReceiver.setOnOtpListeners(this);
     IntentFilter intentFilter = new IntentFilter();
     intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
     getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);

     // get mobile number from phone
     getHintPhoneNumber();
 }
    @Override public void onConnected(@Nullable Bundle bundle) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override public void onConnectionSuspended(int i) {

    }

    @Override public void onOtpReceived(String otp) {
        Toast.makeText(this, "Otp Received " + otp, Toast.LENGTH_LONG).show();

    }

    @Override public void onOtpTimeout() {
        Toast.makeText(this, "Time out, please resend", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getHintPhoneNumber() {
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if we want hint number
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string
                    //mobileno = credential.getId();
                    phoneNumb = credential.getId();
                    Log.e("MOBILE",phoneNumb);
                    String ext = "", phoneN = "";
                    if (phoneNumb.startsWith("+") || phoneNumb.length() > 10) {
                        ext=phoneNumb.substring(0, 3);
                        phoneN=phoneNumb.substring(3);
                        mobileno= phoneN;
                        Log.e("MOBILE",mobileno+" "+" "+ext);
                    } else {
                        ext = "";
                        phoneN = phoneNumb;

                    }


                }

            }
        }
    }

    String getDeviceID(TelephonyManager phonyManager){
        @SuppressLint({"MissingPermission", "NewApi", "LocalSuppress"}) String id = phonyManager.getDeviceId(0)+" "+phonyManager.getDeviceId(1);
        if (id == null){
            id = "not available";
        }

        int phoneType = phonyManager.getPhoneType();
        switch(phoneType){
            case TelephonyManager.PHONE_TYPE_NONE:
             //   return "NONE: " + id;
                return  id;
            case TelephonyManager.PHONE_TYPE_GSM:
                //return "GSM: IMEI=" + id;
                return  id;

            case TelephonyManager.PHONE_TYPE_CDMA:
               // return "CDMA: MEID/ESN=" + id;
                 return  id;
            /*
             *  for API Level 11 or above
             *  case TelephonyManager.PHONE_TYPE_SIP:
             *   return "SIP";
             */

            default:
                //return "UNKNOWN: ID=" + id;
                return  id;
        }

    }
    private void versionCheck(){
    if (isNetworkAvailable()){
        versionUpdate();
        versionUpdatee();
    }
    else {
        versionId.setText("Ver:"+" "+sVersionName);
    }
    }
    private void versionUpdate(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("VerCheck", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("version"," "+obj);
                            vname = obj.getString("name");
                            String code = obj.getString("code");
                            Empstatus = obj.getString("emp_status");
                            String emp_id = obj.getString("emp_id");
                            Log.e("versionApk", vname+ " "+code+" "+Empstatus+" "+emp_id);
                            versionId.setText("Ver:"+" "+vname);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(EMPSTATUS, Empstatus);
                            editor.clear();
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        versionId.setText("Ver:"+" "+sVersionName);
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", sVersionName);
                params.put("code", String.valueOf(sVersionCode));
                params.put("emp_id", editTexUser.getText().toString().trim());
                return params;
            }
        };
        // VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }
    private void versionUpdatee(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.VERSIONN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response1", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("versionn"," "+obj);
                            vvname = obj.getString("name");
                            String vcodee = obj.getString("code");
                            Log.e("versionn", vvname+ " "+vcodee);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        // VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }

    private void SnackBarMessage(){
      final String link = "http://ascensiveeducare.com/APP/";
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "New version available.Click ok for download", Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(link);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
        snackbar.show();
    }



    class checkconne extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            int kk = 0;
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                kk = urlc.getResponseCode();
            } catch (IOException e) {

                Log.e("qweqwe", "Error checking internet connection", e);
            }
            inter = kk;
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (inter == 204) {
                try {
                    currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                new GetVersionCode().execute();

            } else {
                final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "No internet connection!", Snackbar.LENGTH_LONG);
                snackBar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call your action method here
                        snackBar.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
                snackBar.setActionTextColor(Color.RED);
                View sbView = snackBar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackBar.show();
            }
        }
    }

    class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Login.this.getPackageName()  + "&hl=en")
                        .timeout(1000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }
        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    if (onlineVersion.equals(currentVersion)) {

                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder( Login.this).create();
                        alertDialog.setTitle("Update");
                        alertDialog.setIcon(R.mipmap.logo1);
                        alertDialog.setMessage("New Update is available");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Login.this.getPackageName())));
                                    Log.e("U","U");
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Login.this.getPackageName())));
                                    Log.e("U","U1");
                                }
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        alertDialog.show();
                    }
                }
                else {


                }
            }
            Log.e("update", "Current version " + currentVersion + "playstore version "+" " + onlineVersion);

        }
    }
    public void viewAll() {
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            Log.e("Error","Nothing found");
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
            closeTime = res.getString(7);
            Log.e("close", closeTime);
        }
        Log.e("Data",buffer.toString());


    }
    public boolean isNetworkAvailable() {
        boolean connect=false;
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            connect=false;
        }else{
            connect= true;
        }
        return connect;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginId:
                versionCheck();
                if(isNetworkAvailable()){
                    myIntent = new Intent(Login.this, GPSTracker.class);
                    startService(myIntent);
                    startService();
                    validLoginUser();


                }
                else {

                    Cursor res = db.getAllLoginData();
                    if(res.getCount() == 0) {
                        // show message
                        Log.e("Error","Nothing found");
                        Toast.makeText(Login.this,"Please Login With in Working Hour",Toast.LENGTH_LONG).show();
                        return;
                    }

                    else {

                        vlaidLoginFromDatabase();
                    }
                }
                SQLiteDatabase database = db.getReadableDatabase();
                database.execSQL( "UPDATE "+TABLE_LOGINSHOW +" SET " + TABLE_DATETIMESYSTEM+ " = '"+formattedDate+"' WHERE "+TABLE_LOGDATEID+ " = "+dbfg);
                break;
            default:
        }
    }
    private void AllEmai(){
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();
        imei = imeiSIM1+","+imeiSIM2;
        Log.e("IMEI",imei);
        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
        boolean isDualSIM = telephonyInfo.isDualSIM();
        //    Toast.makeText(getApplicationContext(),"imei"+" "+imeiSIM1+ " "+"IME2"+imeiSIM2,Toast.LENGTH_SHORT).show();
        Log.e("device"," IME1:"+imeiSIM1 + "" + " IME2 : " + imeiSIM2 + "" + " IS DUAL SIM : " + imeiSIM2 + "" + " IS SIM1 READY : " + isSIM1Ready + "" + " IS SIM2 READY : " + isSIM2Ready);


    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Login.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permission)) {
                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(Login.this, new String[]{permission}, requestCode);
            }
        } else {
            imei = getImeiNumber();
            getClientPhoneNumber();
            androidid=getAndroidId();
            Log.e("iccid1"," "+" "+androidid);
            //   Toast.makeText(this,permission + " is already granted.", Toast.LENGTH_SHORT).show();
            AllEmai();
            checkServiceStatus();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imei = getImeiNumber();
                    getClientPhoneNumber();
                    androidid=getAndroidId();
                    Log.e("iccid2"," "+" "+androidid);
                    AllEmai();
                    checkServiceStatus();
//                    mSmsBroadcastReceiver = new SmsBroadcastReceiver();
//
//                    //set google api client for hint request
//                    mGoogleApiClient = new GoogleApiClient.Builder(this)
//                            .addConnectionCallbacks(this)
//                            .enableAutoManage(this, this)
//                            .addApi(Auth.CREDENTIALS_API)
//                            .build();
//
//                    mSmsBroadcastReceiver.setOnOtpListeners(this);
//                    IntentFilter intentFilter = new IntentFilter();
//                    intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
//                    getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
//
//                    // get mobile number from phone
//                    getHintPhoneNumber();

                } else {
                    //  Toast.makeText(Login.this, "You have Denied the Permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please enable services to get gps", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @SuppressLint("MissingPermission")
    private String getImeiNumber() {
        final TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        }
        else {
            return telephonyManager.getDeviceId();
        }
    }
    @SuppressLint({"NewApi", "MissingPermission"})
    private void getClientPhoneNumber() {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                subInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            }
            if (subInfoList.size() > 1)
            {
                isMultiSimEnabled = true;
            }
            for (SubscriptionInfo subscriptionInfo : subInfoList)
            {
                numbers.add(subscriptionInfo.getNumber());
            }
            phoneNumb = numbers.get(0);
            Log.e(TAG,"Sim 1:-"+numbers.get(0));
            Log.e(TAG,"Sim 2:-"+ numbers.get(1));
            Log.e("MOBILE2",phoneNumb+" "+" ");
            String ext = "", phoneN = "";
            if (phoneNumb.startsWith("+") || phoneNumb.length() > 10) {
                ext=phoneNumb.substring(0, 2);
                phoneN=phoneNumb.substring(2);
                mobileno= phoneN;
                Log.e("MOBILE1",mobileno+" "+" "+ext);
            } else {
                ext = "";
                phoneN = phoneNumb;

            }
        }catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }

    }
    private String getAndroidId() {
        androidid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("TAG",Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ALLOWED_GEOLOCATION_ORIGINS));
        Log.e("TAG",Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD));
        Log.e("iccid3"," "+" "+androidid);
        return androidid;
    }
    @SuppressLint("NewApi")
    private void requestPermission() {
        Toast.makeText(Login.this, "Requesting permission", Toast.LENGTH_SHORT).show();
        this.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                REQUEST_CODE_PHONE_STATE_READ);
    }
    @SuppressLint("MissingPermission")
    public void showDeviceInfo() {
        TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        StringBuilder stringBuilder = new StringBuilder();
        if (checkedPermission != PackageManager.PERMISSION_DENIED) {
            stringBuilder.append("Board : " + Build.BOARD + "\n");
            stringBuilder.append("Brand : " + Build.BRAND + "\n");
            stringBuilder.append("DEVICE : " + Build.DEVICE + "\n");
            stringBuilder.append("Display : " + Build.DISPLAY + "\n");
            stringBuilder.append("FINGERPRINT : " + Build.FINGERPRINT + "\n");
            stringBuilder.append("HARDWARE : " + Build.HARDWARE + "\n");
            stringBuilder.append("ID : " + Build.ID + "\n");
            stringBuilder.append("Manufacturer : " + Build.MANUFACTURER + "\n");
            stringBuilder.append("MODEL : " + Build.MODEL + "\n");
            stringBuilder.append("SERIAL : " + Build.SERIAL + "\n");
            stringBuilder.append("VERSION : " + Build.VERSION.SDK_INT + "\n");
            stringBuilder.append("Line 1 : " + manager.getLine1Number() + "\n");
            stringBuilder.append("Device ID/IMEI : " + manager.getDeviceId() + "\n");
            Log.e("Imeii", " "+ manager.getDeviceId());
            stringBuilder.append("IMSI : " + manager.getSubscriberId());
        } else {
            stringBuilder.append("Can't access device info !");
        }

    }
    public void vlaidLoginFromDatabase(){
        user1 = editTexUser.getText().toString().trim();
        pass1 = PasswordID.getText().toString().trim();
        if (TextUtils.isEmpty(user1)) {
            editTexUser.setError("Please enter username");
            editTexUser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass1)) {
            PasswordID.setError("Please enter your password");
            PasswordID.requestFocus();
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
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
        //Log.e("Time"," "+date.getTime());
       // Log.e("Timee"," "+Time);
        /////////////////Second time////////////////////////////////////////
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm");
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Office_close_time :"+ res.getString(7)+"\n\n");
            dateInString1 =  res.getString(7);
        }

        Date date1 = null;
        try {
            date1 = sdf1.parse(dateInString1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(dateInString1);
        Time1 = String.valueOf(date1.getTime());
        System.out.println("Date - Time in milliseconds : " + date1.getTime());
        Log.e("Time1"," "+date1.getTime());
        ///////////////////////////////////////////////////////////////////
        if (date.getTime()>date1.getTime()){
            viewAll();
            Toast.makeText(getApplicationContext(),"OD Time Is Over",Toast.LENGTH_SHORT).show();
        }
        else {
            boolean validLogin = validateLogin(user1, pass1, getBaseContext());
            if(validLogin)
            {
                sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                SharedPreferences.Editor edit1 = sp.edit();
                edit1.putString("EMPODIN", "0");
                edit1.commit();
                Intent in = new Intent(getBaseContext(), Profile_Activity.class);
                startActivity(in);
                //finish();
            }
        }
    }
    private void validLoginUser() {
        user1 = editTexUser.getText().toString().trim();
        pass1 = PasswordID.getText().toString().trim();
        if (TextUtils.isEmpty(user1)) {
            editTexUser.setError("Please enter username");
            editTexUser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass1)) {
            PasswordID.setError("Please enter your password");
            PasswordID.requestFocus();
            return;
        }
        valid(user1,pass1);
    }
    public void valid(final String user1,final String pass1){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.USER_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("username",""+obj);
                            //if no error in response
                            int logstatus = obj.getInt("logstatus");
                            int imeistatus = obj.getInt("imeistatus");
                            String imeistatus_msg = obj.getString("imeistatus_msg");
                            String logstatus_msg = obj.getString("logstatus_msg");
                            Log.e("status", logstatus+ " "+"  "+imeistatus);
                            Log.e("message",imeistatus_msg+ " "+" "+logstatus_msg);
                            if (logstatus==1) {
                                JSONObject userJson = obj.getJSONObject("sencod");
                                String emp_desig = userJson.getString("emp_desig");
                                String emp_img  = userJson.getString("emp_img");
                                office_close_time = userJson.getString("office_close_time");
                                String emp_branch = userJson.getString("emp_branch");
                                String emp_name = userJson.getString("emp_name");
                                emp_id = userJson.getString("emp_id");
                                String user_id = userJson.getString("user_id");
                                String pwd = userJson.getString("pwd");
                                Log.e("emp_desig", emp_desig+" "+emp_img+" "+office_close_time+" "+emp_branch+" "+emp_name+" "+emp_id+" "+user_id+" "+pwd);
                                // String ccloseTime = "14:19";
                                sp = getSharedPreferences(Consts.DATETIME, MODE_PRIVATE);
                                SharedPreferences.Editor editt = sp.edit();
                                editt.putString("DATETIME", DATETIME);
                                editt.commit();
                                boolean isUpdate =  db.addlogin(logstatus,imeistatus,imeistatus_msg,logstatus_msg,emp_desig,emp_img,office_close_time,emp_branch,emp_name,emp_id,imei,pwd);
                                db = new DatabaseHelper(getApplicationContext());
                                if (isUpdate==true){
                                    db.updateData("1",logstatus,imeistatus,imeistatus_msg,logstatus_msg,emp_desig,emp_img,office_close_time,emp_branch,emp_name,emp_id,imei,pwd);
                                }
                                else {
                                }
                                User user = new User(
                                        userJson.getString("emp_desig"),
                                        userJson.getString("emp_img"),
                                        userJson.getString("office_close_time"),
                                        userJson.getString("emp_branch"),
                                        userJson.getString("emp_name"),
                                        userJson.getString("emp_id")
                                );
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //finish();

                                if (saveLoginCheckBox.isChecked()) {
                                    loginPrefsEditor.putBoolean("saveLogin", true);
                                    loginPrefsEditor.putString("emp_id", emp_id);
                                    loginPrefsEditor.putString("pwd", pwd);
                                    loginPrefsEditor.commit();
                                } else {
                                    loginPrefsEditor.clear();
                                    loginPrefsEditor.commit();
                                }

                                if (imeistatus==1) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
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
                                //    Log.e("Time"," "+date.getTime());
                                //    Log.e("Timee"," "+Time);
                                    /////////////////Second time////////////////////////////////////////
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm");
                                    String dateInString1 = office_close_time;
                                    Date date1 = null;
                                    try {
                                        date1 = sdf1.parse(dateInString1);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println(dateInString1);
                                    Time1 = String.valueOf(date1.getTime());
                                    System.out.println("Date - Time in milliseconds : " + date1.getTime());
                                    Log.e("Time1"," "+date1.getTime());
                                    ///////////////////////////////////////////////////////////////////
                                    // / 28800000 32400000 14400000
                                    ///time off
//                                    if (date.getTime()>date1.getTime()){
//
//                                        Toast.makeText(getApplicationContext(),"OD Time Is Over",Toast.LENGTH_SHORT).show();
//                                    }
//                                    else {
//
//                                    }

                                    if(isNetworkAvailable()){
                                        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                                        STATUSEMP = sharedpreferences.getString("EMPSTATUS","");
                                        Log.e("STATUSEMP",STATUSEMP);
                                        if (STATUSEMP.equals("0")) {
                                            sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
                                            SharedPreferences.Editor edit1 = sp.edit();
                                            edit1.putString("EMPODIN", "0");
                                            edit1.commit();
                                            startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
                                            Toast.makeText(getApplicationContext(), obj.getString("logstatus_msg"), Toast.LENGTH_SHORT).show();
                                            Log.e("status_emp", String.valueOf(Empstatus));
                                            new checkconne().execute();
                                        }
                                        else {
                                            Toast.makeText(Login.this,"Employee no existence",Toast.LENGTH_SHORT).show();
                                            Log.e("status_emp", String.valueOf(Empstatus));
                                        }
//                                            if (sVersionName.equals(vvname)){
//                                                sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                                                 STATUSEMP = sharedpreferences.getString("EMPSTATUS","");
//                                                Log.e("STATUSEMP",STATUSEMP);
//
//
//                                            }
//                                            else {
//                                                new checkconne().execute();
//                                                SnackBarMessage();
//                                            }
                                    }

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), obj.getString("imeistatus_msg"), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), obj.getString("logstatus_msg"), Toast.LENGTH_SHORT).show();
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
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                        } else if (error instanceof AuthFailureError) {
                            //Error indicating that there was an Authentication Failure while performing the request
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(Login.this,"Please Login With in Working Hour",Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        Cursor res = db.getAllLoginData();
                        if(res.getCount() == 0) {
                            progressBar.setVisibility(View.INVISIBLE);
                            // show message
                            sp = getSharedPreferences(Consts.DATETIME, MODE_PRIVATE);
                            SharedPreferences.Editor editt = sp.edit();
                            editt.putString("DATETIME", DATETIME);
                            editt.commit();
                            Log.e("Error","Nothing found");
                            return;
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            vlaidLoginFromDatabase();
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
                            closeTime = res.getString(7);
                            Log.e("close", closeTime);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //351595114389020 351596114389028
                //359429095059034 359430095059032
                params.put("user_id", user1);
                params.put("pwd", pass1);
                params.put("contact_no", mobileno);
             //   params.put("imei", imei);
                params.put("imei", "0");
                params.put("ando_id",androidid);
//                if (IMEEI.equals(null) && imei.equals(null)){
//                    params.put("imei", imeii);
//                    //  params.put("imei", "867520042769876,867520042769868");
//                    Log.e("loginID","0"+" "+user1+" "+pass1+" "+imei+" "+imeii);
//                }
//                else if (imei.equals(null)){
//                    imei = getDeviceID(telephonyManager);
//                    params.put("imei", imei);
//                    //  params.put("imei", "867520042769876,867520042769868");
//                    Log.e("loginID","1"+" "+user1+" "+pass1+" "+imei+" "+imeii);
//                }
//                else {
//                    params.put("imei", imei);
//                    //params.put("imei", "867520042769876,867520042769868");
//                    Log.e("loginID","2"+" "+user1+" "+pass1+" "+imei+" "+imeii);
//                }
                Log.e("MOBILE","2"+" "+user1+" "+pass1+" "+"imei"+" "
                        +imeii+" "+" "+mobileno+" "+androidid);
                return params;
            }
        };
        // VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);

    }

    private boolean validateLogin(String user1, String pass1, Context baseContext) {
        DB = new DatabaseHelper(getBaseContext());
        SQLiteDatabase db = DB.getReadableDatabase();
        String[] columns = {"password"};
        String selection = "empid=? AND password=?";
        String[] selectionArgs = {user1,pass1};
        Cursor cursor = null;
        try{
            cursor = db.query(DatabaseHelper.TABLE_NAMELOGIN, columns, selection, selectionArgs, null, null, null);
            startManagingCursor(cursor);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        int numberOfRows = cursor.getCount();
        if(numberOfRows <= 0)
        {
            Toast.makeText(getApplicationContext(), "User Id and Password Not Matching", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(), Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }
    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();

        }
        else
        {
            gpsTracker.showSettingsAlert();
        }

    }

    private void checkServiceStatus() {

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Login.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("LOG","Inside onResume");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.e("LOG","Inside onStart");

    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.e("LOG","Inside onReStart");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("LOG","Inside onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e("LOG","Inside onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("LOG","Inside onDestroy");
    }


}
