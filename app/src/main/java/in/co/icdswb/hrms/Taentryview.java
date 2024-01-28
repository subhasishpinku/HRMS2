package in.co.icdswb.hrms;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.SetGetActivity.UploadsetGetImageView;

public class Taentryview extends AppCompatActivity {
    String taid;
    TextView dateId,dayID,tvf,toloc,purposeeID,placeeID,modeIDD,amountID,amountIDD;
    //ImageView imgviewID;
    PhotoView imgviewID;
    int idta;
    String ta_img;
    ImageView homeID;
    SharedPreferences sp;
    String EMPODINN;
    String EMPODIN ="1";
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    Intent myIntent = null;
    TextView rejectId;
    List<UploadsetGetImageView> uploadsetGetImagesViewss;
    ArrayList<UploadsetGetImageView> arrlist = new ArrayList<UploadsetGetImageView>();
    ImageItemViewAdapter homeItemViewAdapter;
    RecyclerView rcv;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taentryview);
        Intent intent = getIntent();
        taid = intent.getStringExtra("taid");
        dateId = (TextView)findViewById(R.id.dateId);
        dayID = (TextView)findViewById(R.id.dayID);
        tvf = (TextView)findViewById(R.id.tvf);
        toloc = (TextView)findViewById(R.id.toloc);
        purposeeID = (TextView)findViewById(R.id.purposeeID);
        placeeID = (TextView)findViewById(R.id.placeeID);
        modeIDD = (TextView)findViewById(R.id.modeIDD);
        amountID = (TextView)findViewById(R.id.amountID);
        amountIDD = (TextView)findViewById(R.id.amountIDD);
        //imgviewID = (ImageView)findViewById(R.id.imgviewID);
        homeID= (ImageView)findViewById(R.id.homeID);
        imgviewID = (PhotoView) findViewById(R.id.imgviewID);
        rejectId = (TextView)findViewById(R.id.rejectId);
        rcv = (RecyclerView)findViewById(R.id.rcv);
        layoutManager = new GridLayoutManager(getApplicationContext(),3);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(homeItemViewAdapter);
        uploadsetGetImagesViewss = new ArrayList<>();
        arrlist = new ArrayList<>();
        TAview(taid);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_user);
        RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        mDrawable.setCircular(true);
        imgviewID.setImageDrawable(mDrawable);
        imgviewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent1 = new Intent(Taentryview.this, ImageViewActivity.class);
                Bundle bundle_edit  =   new Bundle();
                bundle_edit.putString("img",ta_img);
                intent1.putExtras(bundle_edit);
            startActivity(intent1);
            }
        });
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
            startService();
        }
        else {

        }
        Taview(taid);
    }
    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(Taentryview.this, MyService.class);
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Taentryview.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(Taentryview.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

        }

    }
    private void Taview(final String taid){
        String url ="http://ascensiveeducare.com/hrms/service/ta_view.php?"+"ta_id"+"="+taid;
        Log.e("url",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("responsetaview", response);
                        try {
                            Log.e("response1",response);
                            JSONObject object = new JSONObject(response);
                            String date =  object.getString("date");
                            String days =  object.getString("days");
                            String from_place = object.getString("from_place");
                            String to_place = object.getString("to_place");
                            String purpose = object.getString("purpose");
                            String place_of_visite = object.getString("place_of_visite");
                            String ta_mode = object.getString("ta_mode");
                            String claimed_amt = object.getString("claimed_amt");
                            String approve_amt = object.getString("approve_amt");
                            ta_img = object.getString("ta_img");
                            String acct_remarks = object.getString("acct_remarks");
                            Log.e("taviewData"," "+date+ " "+days+" "+from_place+" "+to_place+" "+purpose+" "+place_of_visite+" "+ta_mode+" "+claimed_amt+" "+approve_amt+" "+ta_img+" "+acct_remarks+"");
                            dateId.setText(date);
                            dayID.setText(days);
                            tvf.setText(from_place);
                            toloc.setText(to_place);
                            purposeeID.setText(purpose);
                            placeeID.setText(place_of_visite);
                            modeIDD.setText(ta_mode);
                            amountID.setText(claimed_amt);
                            amountIDD.setText(approve_amt);
                            rejectId.setText(acct_remarks);
                          //  imgviewID.setImageDrawable(Drawable.createFromPath(ta_img));
//                            Glide.with(getApplicationContext())
//                                    .load(ta_img)
//                                    .into(imgviewID);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ta_id", taid);
                Log.e("taid",taid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }

    public void TAview(final String taid){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.TAVIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("taidIMAGE", response);
                        try {
                            Log.e("imageviewresponse",response);
                            JSONArray array = new JSONArray(response);
                            for (int i =0; i<array.length(); i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                String taimg = jsonObject.getString("ta_img");
                                Log.e("TAVIEW",taimg);
                                uploadsetGetImagesViewss.add(new UploadsetGetImageView(
                                        jsonObject.getString("ta_img")
                                ));
                                arrlist.addAll(uploadsetGetImagesViewss);
                            }
                            homeItemViewAdapter = new ImageItemViewAdapter(getApplicationContext(), uploadsetGetImagesViewss);
                            rcv.setAdapter(homeItemViewAdapter);
                            homeItemViewAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ta_id", taid);
                Log.e("taidIMAGE",taid);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }

    public class ImageItemViewAdapter extends RecyclerView.Adapter<ImageItemViewAdapter.ViewHolder> {
        private Context mCtx;
        private List<UploadsetGetImageView> uploadsetGetImageViews;
        int listview;
        public ImageItemViewAdapter(Context mCtx, List<UploadsetGetImageView> uploadsetGetImageViews) {
            this.mCtx = mCtx;
            this.uploadsetGetImageViews = uploadsetGetImageViews;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_row_view, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final UploadsetGetImageView  uploadsetGetImage = uploadsetGetImageViews.get(position);
            listview = position;
            Glide.with(mCtx)
                    .load(uploadsetGetImage.getTaimg())
                    .into(holder.imageItem);
            holder.imageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String img = uploadsetGetImage.getTaimg();
                    Log.e("CATID", img);
                    String title = "Image View Box";
                    String msg = "Do you want to start tracking?";
                    finalsubmit(title,msg,img);
                }
            });


        }
        @Override
        public int getItemCount() {
            return uploadsetGetImageViews.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView itemname,catid;
            private ImageView imageItem;
            LinearLayout lv;
            public ViewHolder(View view) {
                super(view);
                imageItem = (ImageView) view.findViewById(R.id.imageItem);
                lv = (LinearLayout) view.findViewById(R.id.lv);
            }
        }
    }
    public AlertDialog finalsubmit(String title, String msg, String img){
        Log.e("SHOWIMAGE",img);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.show_image, null);
        ((TextView)dialogView.findViewById(R.id.dialog_title)).setText(title);
        ImageView imageView = dialogView.findViewById(R.id.imageItem);
        Glide.with(getApplicationContext())
                .load(img)
                .into(imageView);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        ((Button)dialogView.findViewById(R.id.yId)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startService();

            }
        });
        ((Button)dialogView.findViewById(R.id.nId)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialog.dismiss();

            }
        });
        builder.setView(dialogView);
        dialog.show();
        return dialog;
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
