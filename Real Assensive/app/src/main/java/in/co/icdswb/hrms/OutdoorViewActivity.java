package in.co.icdswb.hrms;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.Adapter.Adapteroutdoorview;
import in.co.icdswb.hrms.AdapterList.OutdoorviewList;
import in.co.icdswb.hrms.SetGetActivity.User;
import in.co.icdswb.hrms.SharedPrefManagerActivity.SharedPrefManager;

public class OutdoorViewActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public List<OutdoorviewList> outdoorviewLists;
    public Adapteroutdoorview adapteroutdoorview;
    String taid,dateOD;
    String emp_id;
    ImageView homeID;
    ArrayList<OutdoorviewList> arrlist = new ArrayList<OutdoorviewList>();
    SharedPreferences sp;
    String EMPODINN;
    String EMPODIN ="1";
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    Intent myIntent = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outdoorviewactivity);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        taid = intent.getStringExtra("taid");
        dateOD = intent.getStringExtra("date");
        Log.e("taid",taid+" "+dateOD);
        outdoorviewLists = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeID = (ImageView)findViewById(R.id.homeID);
        homeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
                startActivity(intent);
            }
        });
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        if(EMPODIN.equals(EMPODINN)){
             // Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
            startService();
        }
        else {

        }
            odView();
    }
    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(OutdoorViewActivity.this, MyService.class);
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(OutdoorViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(OutdoorViewActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

        }
    }
    private void odView(){
        arrlist = new ArrayList<>();
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        emp_id = String.valueOf(user.getEmp_id());
        String url ="http://ascensiveeducare.com/hrms/service/od_view.php?";
        Log.e("URL",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("data2", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("Approve"," "+jsonObj);
                            JSONArray load = jsonObj.getJSONArray("data");
                            for (int i =0; i<load.length(); i++){
                                JSONObject viewObj = load.getJSONObject(i);
                                String od_perpouse = viewObj.getString("od_perpouse");
                                String od_time = viewObj.getString("od_time");
                                String od_type = viewObj.getString("od_type");
                                String manul_loc = viewObj.getString("manul_loc");
                                Log.e("viewOD"," "+od_perpouse+" "+od_time+" "+od_type+ ""+manul_loc);

                                outdoorviewLists.add(new OutdoorviewList(
                                        viewObj.getString("od_perpouse"),
                                        viewObj.getString("od_time"),
                                        viewObj.getString("od_type"),
                                        viewObj.getString("manul_loc")
                                ));
                                arrlist.addAll(outdoorviewLists);
                                adapteroutdoorview = new Adapteroutdoorview(OutdoorViewActivity.this, outdoorviewLists);
                                recyclerView.setAdapter(adapteroutdoorview);
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
                params.put("emp_id", emp_id);
                params.put("od_date", dateOD);
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
