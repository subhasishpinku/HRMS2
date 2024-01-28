package in.co.icdswb.hrms;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
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
import in.co.icdswb.hrms.Adapter.TaentryAdapter;
import in.co.icdswb.hrms.AdapterList.TaentryList;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;

public class TaEntry_Activity extends AppCompatActivity implements View.OnClickListener{
    String year,month,repalcemonth;
    Spinner s_month,s_year;
    RecyclerView recyclerView;
    List<TaentryList> taentryLists;
    ArrayList<TaentryList> arrlist = new ArrayList<TaentryList>();
    private TaentryAdapter adapter;
    String formattedDate3,day;
    FloatingActionButton fab;
    ImageView homeID;
    SharedPreferences sp;
    String EMPODIN ="1";
    String EMPODINN;
    Intent myIntent = null;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    String emp_id;
    Button searchID;
    TextView totalamountID,clamedamountID;
    LinearLayout lvamountID;
    String yearName;
    ArrayList<String> arrayList;
    String TAVIEW="0";
    String vyear = "1";
    String vvyear;
    String vmonth = "1";
    String vvmonth;
    String msg,error;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taentry);
        totalamountID = (TextView)findViewById(R.id.totalamountID);
        clamedamountID = (TextView)findViewById(R.id.clamedamountID);
        lvamountID = (LinearLayout)findViewById(R.id.lvamountID);
        searchID = (Button)findViewById(R.id.searchID);
        s_month = (Spinner)findViewById(R.id.s_month);
        s_year = (Spinner)findViewById(R.id.s_year);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchID.setOnClickListener(this);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        if(EMPODIN==sp.getString("EMPODIN","")){
           // Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
            startService();
        }
        else {

        }
        homeID = (ImageView)findViewById(R.id.homeID);
        homeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Profile_Activity.class);
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taentryLists = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        spmonth();
        spyear();
        dateTime();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        lvamountID.setVisibility(View.INVISIBLE);
        sp = getSharedPreferences(Consts.TAVIEW, MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sp.edit();
        edit1.putString("TAVIEW", "0");
        edit1.commit();
        tetEntry();

    }
    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(TaEntry_Activity.this, MyService.class);
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(TaEntry_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(TaEntry_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
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
               // ((TextView) parentView.getChildAt(0)).setTextColor(Color.RED);
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
               // ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                Log.e("Year_value",year+"");

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
                            s_year.setAdapter(new ArrayAdapter<String>(TaEntry_Activity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
                        }
                        catch (JSONException e) {
                            e.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homeID:
                Intent intent = new Intent(getApplication(), Profile_Activity.class);
                startActivity(intent);
                break;
            case R.id.fab:
                if (isNetworkAvailable()){
                    Intent intent1 = new Intent(TaEntry_Activity.this, Travelling_Activity.class);
                    startActivity(intent1);
                }
                else {
                    Toast.makeText(TaEntry_Activity.this,"NO INTERNET",Toast.LENGTH_SHORT).show();
                }

                break;
                case R.id.searchID:
                    sp = getSharedPreferences(Consts.TAVIEW, MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = sp.edit();
                    edit1.putString("TAVIEW", "1");
                    edit1.commit();

                    if (TAVIEW ==sp.getString("TAVIEW","")){

                    }
                    else {
                        taentryLists.clear();
                        searchView();
                        recyclerView.setAdapter(null);
                        totalamountID.setText("");
                        clamedamountID.setText("");
                    }

                    if (repalcemonth.equals("00")){
                        Toast.makeText(getApplicationContext(),"Select Month",Toast.LENGTH_SHORT).show();
                    }
                    if (year.equals("Select Year")){
                        Toast.makeText(getApplicationContext(),"Select Year",Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
        }
    }

     private void tetEntry(){
         User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
         emp_id = String.valueOf(user.getEmp_id());
         Log.e("emp_id_tey",emp_id);
         StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.TEYENTRY,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         Log.e("response", response);
                         try {
                             //converting the string to json array object
                             JSONArray array = new JSONArray(response);
                             Log.e("to_array_teyentry"," "+array);
                              for (int i = 0 ;i<array.length();i++){
                                  JSONObject obj = array.getJSONObject(i);
                                  String ta_id = obj.getString("ta_id");
                                  String ta_date = obj.getString("ta_date");
                                  String ta_days = obj.getString("ta_days");
                                  String ta_purpose = obj.getString("ta_purpose");
                                  String ta_approve_amt = obj.getString("ta_approve_amt");
                                  String ta_claimed_amt = obj.getString("ta_claimed_amt");
                                  String ta_ram_status = obj.getString("ta_ram_status");
                                  String ta_acct_status = obj.getString("ta_acct_status");
                                  String reject_status = obj.getString("reject_status");
                                  Log.e("reject",reject_status);
                                  Log.e("tey_entry"," "+ta_id+ " "+ta_date+ " "+ta_days+ " "+ta_purpose+" "+ta_approve_amt+ " "+ta_claimed_amt+" "+ta_ram_status+" "+ta_acct_status+" "+reject_status);
                                  taentryLists.add(new TaentryList(
                                          obj.getString("ta_id"),
                                          obj.getString("ta_date"),
                                          obj.getString("ta_days"),
                                          obj.getString("ta_purpose"),
                                          obj.getString("ta_approve_amt"),
                                          obj.getString("ta_claimed_amt"),
                                          obj.getString("ta_ram_status"),
                                          obj.getString("ta_acct_status"),
                                          obj.getString("reject_status")

                                  ));
                                  arrlist.addAll(taentryLists);
                                  adapter = new TaentryAdapter(TaEntry_Activity.this, taentryLists);
                                  recyclerView.setAdapter(adapter);

                              }



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
                 params.put("emp_id", emp_id);
                 return params;
             }
         };
         // VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
         stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
         stringRequest.setShouldCache(false);
         volleySingleton.addToRequestQueue(stringRequest);
    }
    private void searchView(){
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        emp_id = String.valueOf(user.getEmp_id());
        String url = "http://ascensiveeducare.com/hrms/service/ta_search.php";
//        String url ="http://122.176.27.154:81/HRMS/service/ta_search.php?"+"month"+"="+repalcemonth+"&year"+"="+year;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);
                        Log.e("data1", response);
                        try {
                            // Create the root JSONObject from the JSON string.
                            JSONObject jsonObj = new JSONObject(response);
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

                            JSONArray contacts = jsonObj.getJSONArray("log_view");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject object = contacts.getJSONObject(i);
                                String id  = object.getString("ta_id");
                                String ta_date = object.getString("ta_date");
                                String ta_days = object.getString("ta_days");
                                String ta_purpose = object.getString("ta_purpose");
                                String ta_approve_amt = object.getString("ta_approve_amt");
                                String ta_claimed_amt = object.getString("ta_claimed_amt");
                                String ta_ram_status = object.getString("ta_ram_status");
                                String ta_acct_status = object.getString("ta_acct_status");
                                String reject_status = object.getString("reject_status");
                                Log.e("reject",reject_status);
                                Log.e("hh"," "+id+" "+ta_date+" "+ta_days+" "+ta_purpose+ta_approve_amt+" "+ta_claimed_amt+" "+ta_ram_status+" "+ta_acct_status+" "+reject_status);
                             //   Toast.makeText(TaEntry_Activity.this, id, Toast.LENGTH_SHORT).show();
                                taentryLists.add(new TaentryList(
                                        object.getString("ta_id"),
                                        object.getString("ta_date"),
                                        object.getString("ta_days"),
                                        object.getString("ta_purpose"),
                                        object.getString("ta_approve_amt"),
                                        object.getString("ta_claimed_amt"),
                                        object.getString("ta_ram_status"),
                                        object.getString("ta_acct_status"),
                                        object.getString("reject_status")
                                ));
                                arrlist.addAll(taentryLists);
                                adapter = new TaentryAdapter(TaEntry_Activity.this, taentryLists);
                                recyclerView.setAdapter(adapter);
                            }
                            TotalAmount();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(getApplicationContext(),"clear",Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emp_id", emp_id);
                params.put("month", repalcemonth);
                params.put("year", year);
                Log.e("TAAENTRY",emp_id+" "+" "+year+" "+"  "+" "+repalcemonth);
                return params;
            }
        };

       // VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }

    private void TotalAmount(){
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        emp_id = String.valueOf(user.getEmp_id());
       // String url ="http://122.176.27.154:81/HRMS/service/ta_search_two.php?"+"month"+"="+repalcemonth+"&year"+"="+year;
        String url ="http://ascensiveeducare.com/hrms/service/ta_search_two.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                      progressBar.setVisibility(View.GONE);
                        lvamountID.setVisibility(View.VISIBLE);
                        Log.e("data2", response);
                        String data = "";
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String total_approve_amt = jsonObj.getString("total_approve_amt");
                            String total_claimed_amt = jsonObj.getString("total_claimed_amt");
                            Log.e("TotalAmount",total_approve_amt+ " "+total_claimed_amt);
                            totalamountID.setText(total_approve_amt);
                            clamedamountID.setText(total_claimed_amt);
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
                params.put("emp_id", emp_id);
                params.put("month", repalcemonth);
                params.put("year", year);
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
