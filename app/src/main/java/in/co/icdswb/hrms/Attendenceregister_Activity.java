package in.co.icdswb.hrms;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.Locale;
import java.util.Map;

import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.Adapter.AttendanceregisterAdapter;
import in.co.icdswb.hrms.AdapterList.AttendanceregisterList;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;

public class Attendenceregister_Activity extends AppCompatActivity implements View.OnClickListener{
    ImageView homeID;
    RecyclerView recyclerView;
    List<AttendanceregisterList> attendanceregisterLists;
    AttendanceregisterAdapter adapter;
    String formattedDate3,day;
    ImageView ff_date,tt_date;
    TextView f_date,t_date;
    String sf_date,st_date;
    Calendar fromCalendar;
    DatePickerDialog.OnDateSetListener fromdate;
    Calendar toCalendar;
    DatePickerDialog.OnDateSetListener todate;
    Button searchID,searchIDD;
    ProgressDialog progressDoalog;
    Handler handle;
    String intime = "10:30";
    String outtime = "7:30";
    String emp_id;
    ArrayList<AttendanceregisterList> arrlist = new ArrayList<AttendanceregisterList>();
    ProgressBar progressBar;
    SharedPreferences sp;
    String FROMDATE="1";
    String TODATE ="1";
    String EMPODIN ="1";
    String EMPODINN;
    Intent myIntent = null;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendenceregister);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        homeID = (ImageView)findViewById(R.id.homeID);
        ff_date = (ImageView)findViewById(R.id.ff_date);
        tt_date = (ImageView)findViewById(R.id.tt_date);
        f_date = (TextView)findViewById(R.id.f_date);
        t_date = (TextView)findViewById(R.id.t_date);
        searchID =(Button)findViewById(R.id.searchID);
        searchIDD =(Button)findViewById(R.id.searchIDD);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        homeID.setOnClickListener(this);
        ff_date.setOnClickListener(this);
        tt_date.setOnClickListener(this);
        searchID.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceregisterLists = new ArrayList<>();
        sp = getSharedPreferences(Consts.FROMDATE, MODE_PRIVATE);
        SharedPreferences.Editor    edit    =   sp.edit();
        edit.putString("FROMDATE","0");
        edit.commit();
        sp = getSharedPreferences(Consts.TODATE, MODE_PRIVATE);
        SharedPreferences.Editor    editt    =   sp.edit();
        editt.putString("TODATE","0");
        editt.commit();
        fromCalendar = Calendar.getInstance();
        fromdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                fromCalendar.set(Calendar.YEAR, year);
                fromCalendar.set(Calendar.MONTH, monthOfYear);
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
             //   SearchList();

            }
        };
        toCalendar = Calendar.getInstance();
        todate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                toCalendar.set(Calendar.YEAR, year);
                toCalendar.set(Calendar.MONTH, monthOfYear);
                toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelto();
              //  SearchList();

            }
        };
        dateTime();
        //cardlist();
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        if(EMPODIN==sp.getString("EMPODIN","")){
          //  Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
            startService();
        }
        else {

        }
        adapter = new AttendanceregisterAdapter(Attendenceregister_Activity.this, attendanceregisterLists,recyclerView);
        recyclerView.setAdapter(adapter);
    }
    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(Attendenceregister_Activity.this, MyService.class);
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Attendenceregister_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(Attendenceregister_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

        }
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
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        f_date.setText(sdf.format(fromCalendar.getTime()));
        sf_date = f_date.getText().toString();
        attendanceregisterLists.clear();
    }

    private void updateLabelto() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        t_date.setText(sdf.format(toCalendar.getTime()));
        st_date = t_date.getText().toString();
        attendanceregisterLists.clear();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homeID:
                Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
                startActivity(intent);
                break;
            case R.id.ff_date:

                showdate();
                searchID.setVisibility(View.VISIBLE);
                searchIDD.setVisibility(View.GONE);
                break;
            case R.id.tt_date:

                showdateto();
                searchID.setVisibility(View.VISIBLE);
                searchIDD.setVisibility(View.GONE);
                break;
            case R.id.searchID:
                if (FROMDATE==sp.getString("FROMDATE","")){
                  SearchList();

              }
               else if (TODATE==sp.getString("TODATE","")){
                    SearchList();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Select Date",Toast.LENGTH_SHORT).show();
                }
                if (TODATE==sp.getString("TODATE","")){

                }
                else {
                    Toast.makeText(getApplicationContext(),"Select Date",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

    private void SearchList() {
        if (isNetworkAvailable()) {
            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            emp_id = String.valueOf(user.getEmp_id());
            Log.e("emp_id",emp_id);
            updateLabel();
           attendanceregisterLists.clear();
            cardlist(emp_id);
           attendanceregisterLists.clear();

        }
        else {

        }

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
    private void cardlist(final String emp_id){
        progressBar.setVisibility(View.INVISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.DALLYATENDANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        searchID.setVisibility(View.GONE);
                        searchIDD.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("response", response);
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.e("to_array"," "+array);
                            for(int i=0;i<array.length();i++){
                                JSONObject obj = array.getJSONObject(i);
                                Log.e("objAtten"," "+obj);
                                String attend_dt = obj.getString("attend_dt");
                                String intime = obj.getString("intime");
                                String out_time = obj.getString("out_time");
                                String days = obj.getString("days");
                                String remarks = obj.getString("remarks");
                                String inbranch = obj.getString("inbranch");
                                String outbranch = obj.getString("outbranch");
                                Log.e("Atten_Show"," "+attend_dt+ " "+intime+ " "+out_time+ " "+days+" "+remarks+" "+inbranch+ " "+outbranch);
                                attendanceregisterLists.add(new AttendanceregisterList(
                                        obj.getString("attend_dt"),
                                        obj.getString("days"),
                                        obj.getString("intime"),
                                        obj.getString("out_time"),
                                        obj.getString("remarks"),
                                        obj.getString("inbranch"),
                                       obj.getString("outbranch")

                                ));
                                arrlist.addAll(attendanceregisterLists);
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            recyclerView.setAdapter(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.VISIBLE);
                  
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emp_id", emp_id);
                params.put("frm_dt", sf_date);
                params.put("to_dt", st_date);
                Log.e("Attandance",emp_id+ " "+sf_date+" "+st_date);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);

    }

    private void showdate(){
        new DatePickerDialog(this, fromdate, fromCalendar
                .get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                fromCalendar.get(Calendar.DAY_OF_MONTH)).show();
        sp = getSharedPreferences(Consts.FROMDATE, MODE_PRIVATE);
        SharedPreferences.Editor    edit    =   sp.edit();
        edit.putString("FROMDATE","1");
        edit.commit();
    }

    private void showdateto(){
        new DatePickerDialog(this, todate, toCalendar
                .get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
                toCalendar.get(Calendar.DAY_OF_MONTH)).show();
        sp = getSharedPreferences(Consts.TODATE, MODE_PRIVATE);
        SharedPreferences.Editor    editt    =   sp.edit();
        editt.putString("TODATE","1");
        editt.commit();
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
