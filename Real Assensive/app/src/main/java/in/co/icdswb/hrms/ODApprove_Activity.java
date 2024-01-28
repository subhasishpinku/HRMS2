package in.co.icdswb.hrms;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.Adapter.OdApproval;
import in.co.icdswb.hrms.AdapterList.OdApprovalList;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;

public class ODApprove_Activity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    List<OdApprovalList> odapprovalLists;
    private OdApproval adapter;
    Spinner s_month,s_year;
    String year,month;
    ImageView homeID;
    String formattedDate3,day;
    SharedPreferences sp;
    String EMPODIN ="1";
    String EMPODINN;
    Intent myIntent = null;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    ArrayList<OdApprovalList> arrlist = new ArrayList<OdApprovalList>();
    String emp_id;
    String repalcemonth;
    ArrayList<String> arrayList;
    String yearName;
    Button findID;
    String msg,error;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odapproval);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        homeID = (ImageView) findViewById(R.id.homeID);
        s_month = (Spinner)findViewById(R.id.s_month);
        s_year = (Spinner)findViewById(R.id.s_year);
        findID = (Button)findViewById(R.id.findID);
        findID.setOnClickListener(this);
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        if(EMPODIN==sp.getString("EMPODIN","")){
           // Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
            startService();
        }
        else {

        }
        spyear();
        spmonth();
        dateTime();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        odapprovalLists = new ArrayList<>();
        homeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Profile_Activity.class);
                startActivity(intent);
            }
        });
        arrlist = new ArrayList<>();
        OdApprove();
    }


    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(ODApprove_Activity.this, MyService.class);
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(ODApprove_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(ODApprove_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

        }
    }
    private void spmonth(){
        String[] list1 = new String[] {"Month","JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list1);
        s_month.setAdapter(adapter1);
        s_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                month = s_month.getSelectedItem().toString();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.RED);
                Log.e("Year value",month+"");
                if (month =="Month"){
                    repalcemonth = "00";
                }
                if (month=="JANUARY"){
                    repalcemonth = "01";
                }
                if (month=="FEBRUARY"){
                    repalcemonth = "02";
                }
                if (month=="MARCH"){
                    repalcemonth = "03";
                }
                if (month=="APRIL"){
                    repalcemonth = "04";
                }
                if (month=="MAY"){
                    repalcemonth = "05";
                }
                if (month=="JUNE"){
                    repalcemonth = "06";
                }
                if (month=="JULY"){
                    repalcemonth = "07";
                }
                if (month=="AUGUST"){
                    repalcemonth = "08";
                }
                if (month=="SEPTEMBER"){
                    repalcemonth = "09";
                }
                if (month=="OCTOBER"){
                    repalcemonth = "10";
                }
                if (month=="NOVEMBER"){
                    repalcemonth = "11";
                }
                if (month=="DECEMBER"){
                    repalcemonth = "12";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }
    private void spyear(){
        arrayList = new ArrayList<>();
        s_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                year = s_year.getSelectedItem().toString();
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.RED);
                Log.e("Year value",year+"");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.YEARVALUE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);
                        Log.e("data3", response);
                        String data = "";
                        try {
                            // Create the root JSONObject from the JSON string.
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray yearvalue = jsonObj.getJSONArray("date");
                            // looping through All Contacts
                            for (int i = 0; i < yearvalue.length(); i++) {
                                JSONObject object = yearvalue.getJSONObject(i);
                                yearName = object.getString("year");
                                arrayList.add(yearName);
                                Log.e("yearName",yearName);

                            }
                            s_year.setAdapter(new ArrayAdapter<String>(ODApprove_Activity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
                        }
                        catch (JSONException e) {
                            e.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);

    }

    private void dateTime(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate3 = df3.format(c.getTime());
        SimpleDateFormat parseFormat = new SimpleDateFormat("EEEE");
        Date date =new Date();
        day= parseFormat.format(date);
    }

     private void OdApprove(){
         User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
         emp_id = String.valueOf(user.getEmp_id());
         String url ="http://ascensiveeducare.com/hrms/service/load_ad_unapprov.php?"+"emp_id"+"="+emp_id;
         StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         Log.e("data1", response);
                         try {
                             JSONObject jsonObj = new JSONObject(response);
                             Log.e("Approve"," "+jsonObj);
                             JSONArray load = jsonObj.getJSONArray("record");
                             for (int i =0; i<load.length(); i++){
                                 JSONObject loadOB = load.getJSONObject(i);
                                 String od_id = loadOB.getString("od_id");
                                 String od_days = loadOB.getString("od_days");
                                 String od_date = loadOB.getString("od_date");
                                 String in_time = loadOB.getString("in_time");
                                 String out_time = loadOB.getString("out_time");
                                 String duration = loadOB.getString("duration");
                                 String rm_approv = loadOB.getString("rm_approv");
                                 String hr_approv = loadOB.getString("hr_approv");
                                 String ceo_aprov = loadOB.getString("ceo_aprov");
                                 String final_status = loadOB.getString("final_status");
                                 Log.e("odid"," "+od_id+" "+od_days+" "+od_date+" "+in_time+" "+out_time+" "+duration+ " "+rm_approv+" "+hr_approv+" "+ceo_aprov+" "+final_status+" ");
                                 odapprovalLists.add(new OdApprovalList(
                                         loadOB.getString("od_id"),
                                         loadOB.getString("od_days"),
                                         loadOB.getString("od_date"),
                                         loadOB.getString("in_time"),
                                         loadOB.getString("out_time"),
                                         loadOB.getString("duration"),
                                         loadOB.getString("rm_approv"),
                                         loadOB.getString("hr_approv"),
                                         loadOB.getString("ceo_aprov"),
                                         loadOB.getString("final_status")

                                 ));
                                 arrlist.addAll(odapprovalLists);
                                 adapter = new OdApproval(ODApprove_Activity.this, odapprovalLists);
                                 recyclerView.setAdapter(adapter);
                             }
                         }
                         catch (JSONException e) {
                             e.printStackTrace();}
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }) {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> params = new HashMap<>();
                 return params;
             }
         };
         //VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
         stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
         stringRequest.setShouldCache(false);
         volleySingleton.addToRequestQueue(stringRequest);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findID:
                if (repalcemonth.equals("00")){
                    Toast.makeText(getApplicationContext(),"Select Month",Toast.LENGTH_SHORT).show();
                }
                if (year.equals("Select Year")){
                    Toast.makeText(getApplicationContext(),"Select Year",Toast.LENGTH_SHORT).show();
                }
                odapprovalLists.clear();
                searchApprove();
                recyclerView.setAdapter(null);
                break;
            default:
        }
    }
     private void searchApprove(){
         User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
         emp_id = String.valueOf(user.getEmp_id());
         String url ="http://ascensiveeducare.com/hrms/service/od_serch.php?"+"emp_id"+"="+emp_id+"&month"+"="+repalcemonth+"&year"+"="+year;
         Log.e("URL",url);
         StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         Log.e("data2", response);
                         try {
                             JSONObject jsonObj = new JSONObject(response);
                             Log.e("Approve"," "+jsonObj);
                             JSONArray check = jsonObj.getJSONArray("response");
                             for(int j =0; j<check.length();j++){
                                 JSONObject ck = check.getJSONObject(j);
                                 msg = ck.getString("msg");
                                 error = ck.getString("error");
                             }
                             if (error.equals(true)){
                                 Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                             }
                             else {
                                 Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                             }
                             JSONArray load = jsonObj.getJSONArray("log_view");
                             for (int i =0; i<load.length(); i++){
                                 JSONObject loadOB = load.getJSONObject(i);
                                 String od_id = loadOB.getString("od_id");
                                 String od_days = loadOB.getString("od_days");
                                 String od_date = loadOB.getString("od_date");
                                 String in_time = loadOB.getString("in_time");
                                 String out_time = loadOB.getString("out_time");
                                 String duration = loadOB.getString("duration");
                                 String rm_approv = loadOB.getString("rm_approv");
                                 String hr_approv = loadOB.getString("hr_approv");
                                 String ceo_aprov = loadOB.getString("ceo_aprov");
                                 String final_status = loadOB.getString("final_status");
                                 Log.e("odid1"," "+od_id+" "+od_days+" "+od_date+" "+in_time+" "+out_time+" "+duration+ " "+rm_approv+" "+hr_approv+" "+ceo_aprov+" "+final_status+" ");
                                 odapprovalLists.add(new OdApprovalList(
                                         loadOB.getString("od_id"),
                                         loadOB.getString("od_days"),
                                         loadOB.getString("od_date"),
                                         loadOB.getString("in_time"),
                                         loadOB.getString("out_time"),
                                         loadOB.getString("duration"),
                                         loadOB.getString("rm_approv"),
                                         loadOB.getString("hr_approv"),
                                         loadOB.getString("ceo_aprov"),
                                         loadOB.getString("final_status")
                                 ));
                                 arrlist.addAll(odapprovalLists);
                                 adapter = new OdApproval(ODApprove_Activity.this, odapprovalLists);
                                 recyclerView.setAdapter(adapter);
                             }
                         }
                         catch (JSONException e) {
                             e.printStackTrace();}
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }) {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> params = new HashMap<>();
                 Log.e("APPROVE",year+" "+repalcemonth+emp_id);
                 return params;
             }
         };

         // VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
         stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
         stringRequest.setShouldCache(false);
         volleySingleton.addToRequestQueue(stringRequest);
     }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
        startActivity(intent);
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
