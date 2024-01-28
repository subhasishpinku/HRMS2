package in.co.icdswb.hrms;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.Adapter.ODLogadapter;
import in.co.icdswb.hrms.AdapterList.ODLogList;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;

public class ODLog_Activity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    List<ODLogList> odLogLists;
    private ODLogadapter adapter;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button cal;
    TextView txt_tgl;
    ImageView homeID;
    String emp_id;
    String from_od_date;
    ArrayList<ODLogList> arrlist = new ArrayList<ODLogList>();
    ProgressBar progressBar;
    SharedPreferences sp;
    String EMPODIN ="1";
    String EMPODINN;
    Intent myIntent = null;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odlog);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        cal = (Button)findViewById(R.id.cal);
        cal.setOnClickListener(this);
        homeID = (ImageView)findViewById(R.id.homeID);
        homeID.setOnClickListener(this);
        txt_tgl =(TextView)findViewById(R.id.txt_tgl);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        odLogLists = new ArrayList<>();
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                SearchList();

            }
        };

       // latlongLog();
//       odLogLists.clear();
        SearchList();
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        if(EMPODIN==sp.getString("EMPODIN","")){
            //   Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
            startService();
        }
        else {

        }

    }
    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(ODLog_Activity.this, MyService.class);
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(ODLog_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(ODLog_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txt_tgl.setText(sdf.format(myCalendar.getTime()));
        from_od_date = txt_tgl.getText().toString();
        Log.e("datee",from_od_date);
        odLogLists.clear();
//        adapter.clearApplications();
      //  latlongLog();
    }
    private void SearchList() {
        if (isNetworkAvailable()) {
            updateLabel();
            odLogLists.clear();
            latlongLog();
            odLogLists.clear();
        }
        else {
            Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
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
    private void latlongLog(){
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        emp_id = String.valueOf(user.getEmp_id());
        Log.e("emp_id",emp_id);
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txt_tgl.setText(sdf.format(myCalendar.getTime()));
        from_od_date = txt_tgl.getText().toString();
        Log.e("datee",from_od_date);
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ODDT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("response", response);
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            Log.e("to_array"," "+array);
                            for (int i= 0 ; i<array.length();i++){
                                JSONObject obj = array.getJSONObject(i);
                                Log.e("obj"," "+obj);
                                String od_time = obj.getString("od_time");
                                String lat = obj.getString("lat");
                                String lang = obj.getString("long");
                                String actual_loc = obj.getString("actual_loc");
                                String od_type = obj.getString("od_type");
                                String color_code = obj.getString("color_code");
                                Log.e("location_tey"," "+od_time+ " "+lat+ " "+lang+ " "+actual_loc+" "+od_type+ " "+color_code);
                                odLogLists.add(new ODLogList(
                                        obj.getString("actual_loc"),
                                        obj.getString("od_time"),
                                        obj.getString("od_type"),
                                        obj.getString("color_code"),
                                        obj.getString("lat"),
                                        obj.getString("long")
                                ));
                                arrlist.addAll(odLogLists);
                            }
                            adapter = new ODLogadapter(ODLog_Activity.this, odLogLists);
                            recyclerView.setAdapter(adapter);

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
                params.put("from_od_date", from_od_date);
                params.put("to_od_date", from_od_date);
                Log.e("ODLOG",emp_id+ " "+from_od_date+" "+from_od_date);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cal:
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.homeID:
                Intent intent = new Intent(this, Profile_Activity.class);
                startActivity(intent);
                break;
                default:
        }
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
