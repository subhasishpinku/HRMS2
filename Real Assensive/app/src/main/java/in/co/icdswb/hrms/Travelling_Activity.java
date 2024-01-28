package in.co.icdswb.hrms;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import in.co.icdswb.hrms.ActivityDatabase.DatabaseHelper;
import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityUrl.Consts;
import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.SetGetActivity.UploadsetGetImage;


public class Travelling_Activity extends AppCompatActivity implements View.OnClickListener{
    ImageView homeID;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    File photoFile;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    public static final String UPLOAD_URL = "http://ascensiveeducare.com/hrms/service/ta_entry.php";
    public static final String UPLOAD_URL1 = "http://ascensiveeducare.com/hrms/service/ta_update.php";
    public static final String UPLOAD_URL2 = "http://ascensiveeducare.com/hrms/service/image_upload.php";
    EditText purposeID,visitplaceId,modeID,amountId,fromId,toID;
    TextView dateID;
    ImageView btnsubmit;
    String empId;
    private DatabaseHelper db;
    DatabaseHelper DB = null;
    String insert_id;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar1;
    DatePickerDialog.OnDateSetListener date1;
    Calendar myCalendar2;
    DatePickerDialog.OnDateSetListener date2;
    Button dateIDD,fromIdd,toIDD;
    int log_staus;
    SharedPreferences sp;
    String TRAVELL="1";
    SharedPreferences spp;
    String EMPODIN ="1";
    String EMPODINN;
    Intent myIntent = null;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    //private NetworkImageView imageView;
    ImageView imageView,imageView1;
    private File actualImage;
    public static final String UPLOAD_KEY = "tmp_name";
    String name ="Pinku_Chunku";
    BottomNavigationView bottomNavigationView;
    Menu nav_Menu;
    MenuItem target;
    MenuItem target1;
    List<UploadsetGetImage> uploadsetGetImages;
    ArrayList<UploadsetGetImage> arrlist = new ArrayList<UploadsetGetImage>();
    HomeItemViewAdapter homeItemViewAdapter;
    RecyclerView rcv;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout lvvv9;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelling);
        homeID = (ImageView) findViewById(R.id.homeID);
        homeID.setOnClickListener(this);
        dateID = (TextView)findViewById(R.id.dateID);
        fromId = (EditText) findViewById(R.id.fromId);
        toID = (EditText) findViewById(R.id.toID);
        purposeID = (EditText)findViewById(R.id.purposeID);
        visitplaceId = (EditText)findViewById(R.id.visitplaceId);
        modeID = (EditText)findViewById(R.id.modeID);
        amountId = (EditText)findViewById(R.id.amountId);
        dateIDD= (Button)findViewById(R.id.dateIDD);
        fromIdd=(Button)findViewById(R.id.fromIdd);
        toIDD= (Button)findViewById(R.id.toIDD);
        lvvv9 =(LinearLayout)findViewById(R.id.lvvv9);
        rcv = (RecyclerView)findViewById(R.id.rcv);
        layoutManager = new GridLayoutManager(getApplicationContext(),3);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(homeItemViewAdapter);
        uploadsetGetImages =new ArrayList<>();
        fromId.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        fromId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_NEXT){

                }
                return false;
            }
        });
        toID.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        toID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_NEXT){

                }
                return false;
            }
        });
        purposeID.setRawInputType(InputType.TYPE_CLASS_TEXT);
        purposeID.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        purposeID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_NEXT){

                }
                return false;
            }
        });
        visitplaceId.setRawInputType(InputType.TYPE_CLASS_TEXT);
        visitplaceId.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        visitplaceId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_NEXT){
                }
                return false;
            }
        });
        modeID.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        modeID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_NEXT){

                }
                return false;
            }
        });
        amountId.setImeOptions(EditorInfo.IME_ACTION_DONE);
        amountId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_NEXT){

                }
                return false;
            }
        });
        imageView = (ImageView) findViewById(R.id.imageView);
        if (imageView.getDrawable() == null){
         Log.e("IMAGVIEW","NoADD");
         }
        else if(imageView.getDrawable().equals("1")){
          //  uploadMultipart1(actualImage);
            Log.e("IMAGVIEW","ADD");
        }
        dateIDD.setOnClickListener(this);
        fromIdd.setOnClickListener(this);
        toIDD.setOnClickListener(this);
        db = new DatabaseHelper(this);
//        fab = (FloatingActionButton)findViewById(R.id.fab);
//        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
//        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
//        btnsubmit =(ImageView)findViewById(R.id.btnsubmit);
//        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
//        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
//        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
//        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
//        fab.setOnClickListener(this);
//        fab1.setOnClickListener(this);
//        fab2.setOnClickListener(this);
       // btnsubmit.setOnClickListener(this);
        sp = getSharedPreferences(Consts.EMPODIN, MODE_PRIVATE);
        EMPODINN = sp.getString("EMPODIN","");
        Log.e("OODDINService",EMPODINN);
        if(EMPODIN==sp.getString("EMPODIN","")){
           // Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
            startService();
        }
        else {

        }
        sp = getSharedPreferences(Consts.TRAVEL, MODE_PRIVATE);
        SharedPreferences.Editor edit1 = sp.edit();
        edit1.putString("TRAVEL", "0");
        edit1.commit();
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

            }
        };
        myCalendar1 = Calendar.getInstance();
        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabell();

            }
        };
        myCalendar2 = Calendar.getInstance();
        date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelll();

            }
        };
        requestStoragePermission();
        IDEmp();
                bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        nav_Menu = bottomNavigationView.getMenu();
        target = nav_Menu.findItem(R.id.close);
        target.setVisible(false);
        target1 = nav_Menu.findItem(R.id.btnsubmit);
        target1.setVisible(true);
         bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                        //    case R.id.start_time:
//                                sp = getSharedPreferences(Consts.TRAVEL, MODE_PRIVATE);
//                                SharedPreferences.Editor edit = sp.edit();
//                                edit.putString("TRAVEL", "1");
//                                edit.commit();
//                                if (isIntentAvailable(getApplicationContext())) {
//                                    Toast.makeText(getApplicationContext(),"hi1",Toast.LENGTH_LONG).show();
//                                }
//                                else {
//                                  //  takePhotoFromCamera();
//                                }
                          //      break;
                            case R.id.end_time:
//                                sp = getSharedPreferences(Consts.TRAVEL, MODE_PRIVATE);
//                                SharedPreferences.Editor edit1 = sp.edit();
//                                edit1.putString("TRAVEL", "1");
//                                edit1.commit();

                                showFileChooser();
                                break;
                            case R.id.btnsubmit:
//                                if (TRAVELL==sp.getString("TRAVEL","")) {
//                                }
//                                else {
//                                    Toast.makeText(Travelling_Activity.this, "Please Upload Image11", Toast.LENGTH_SHORT).show();
//                                }

                                if (isNetworkAvailable()) {
                                    registerUser();
//                                    Intent intent = new Intent(Travelling_Activity.this, Profile_Activity.class);
//                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(Travelling_Activity.this, "No Network Device", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.close:
                                Intent intent = new Intent(Travelling_Activity.this, Profile_Activity.class);
                                startActivity(intent);
                                break;
                            default:
                        }
                        return true;
                    }
               });
    }

    private void startService(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            checkServiceStatus();
            myIntent = new Intent(Travelling_Activity.this, MyService.class);
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(Travelling_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(Travelling_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;

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
    public void IDEmp() {
        Cursor res = db.getAllLoginData();
        if(res.getCount() == 0) {
            // show message
            Log.e("Error","Nothing found");
            //  Toast.makeText(Profile_Activity.this,"Please Login With in Working Hour",Toast.LENGTH_LONG).show();
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("emp_id :"+ res.getString(10)+"\n\n");
            empId = String.valueOf(res.getString(10));
        }
        // Show all data
        Log.e("Data",buffer.toString());
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateID.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabell() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromId.setText(sdf.format(myCalendar1.getTime()));

    }
    private void updateLabelll() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toID.setText(sdf.format(myCalendar2.getTime()));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homeID:
                Intent intent = new Intent(getApplication(), Profile_Activity.class);
                startActivity(intent);
                break;
//            case R.id.fab:
//
//                animateFAB();
//                break;
//            case R.id.fab1:
//                showFileChooser();
//                Log.d("Pinku", "Fab 1");
//                break;
//            case R.id.fab2:
//                Log.d("Pinku", "Fab 2");
//                break;
            case R.id.dateIDD:
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.fromIdd:
                new DatePickerDialog(this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.toIDD:
                new DatePickerDialog(this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
                break;
                default:
        }
    }
//    public void animateFAB(){
//
//        if(isFabOpen){
//
//            fab.startAnimation(rotate_backward);
//            fab1.startAnimation(fab_close);
//            fab2.startAnimation(fab_close);
//            fab1.setClickable(false);
//            fab2.setClickable(false);
//            isFabOpen = false;
//            Log.d("Pinku", "close");
//
//        } else {
//
//            fab.startAnimation(rotate_forward);
//            fab1.startAnimation(fab_open);
//            fab2.startAnimation(fab_open);
//            fab1.setClickable(true);
//            fab2.setClickable(true);
//            isFabOpen = true;
//            Log.d("Pinku","open");
//
//        }
//    }
    private void registerUser() {
        final String date = dateID.getText().toString().trim();
        final String from = fromId.getText().toString().trim();
        final String todate = toID.getText().toString().trim();
        final String purpose = purposeID.getText().toString().trim();
        final String visit = visitplaceId.getText().toString().trim();
        final String mode = modeID.getText().toString().trim();
        final String amount = amountId.getText().toString().trim();
        //first we will do the validations

        if (TextUtils.isEmpty(date)) {
            dateID.setError("Please enter Date");
            dateID.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(from)) {
            fromId.setError("Please enter From");
            fromId.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(todate)) {
            toID.setError("Enter a ToDate");
            toID.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(purpose)) {
            purposeID.setError("Enter a purpose");
            purposeID.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(visit)) {
            visitplaceId.setError("Enter a visit");
            visitplaceId.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mode)) {
            modeID.setError("Enter a Mode");
            modeID.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(amount)) {
            amountId.setError("Enter a Amount");
            amountId.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                   //     progressBar.setVisibility(View.GONE);
                        Log.e("UPLODIMAGE",response);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                             log_staus = obj.getInt("log_staus");

                            if (log_staus==1) {
                                 insert_id = obj.getString("insert_id");
                                 Log.e("insert_id", String.valueOf(insert_id));
                                 Toast.makeText(getApplicationContext(),"Submit Successfully",Toast.LENGTH_SHORT).show();
                                 target.setVisible(true);
                                 target1.setVisible(false);
//                                try {
//                                    createImageFile();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//
//                                }
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("log_staus"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Your Ta Is Not Insert Due To Server Offline", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("system_date", date);
                params.put("from_date",from);
                params.put("to_date", todate);
                params.put("purpose", purpose);
                params.put("place_of_visite", visit);
                params.put("mode", mode);
                params.put("ta_amount", amount);
                params.put("emp_id", empId);
//                params.put("tmp_name", "/storage/extSdCard/interview/interview/assets/image/Tulips.jpg");
//                params.put("name", "chunku");
                Log.e("tya",date+" "+from+" "+todate+" "+purpose+" "+visit+" "+mode+" "+amount+" "+empId);
                return params;
            }
        };

     //   VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);

    }
//    public void uploadMultipart() {
//        //getting name for the image
//        //String name = editText.getText().toString().trim();
//
//            String name ="Pinku_Chunku";
//            //getting the actual path of the image
//           // String path = getPath(filePath);
//
//            //Uploading code
//            try {
//                String uploadId = UUID.randomUUID().toString();
//                //Creating a multi part request
//                new MultipartUploadRequest(this, uploadId,UPLOAD_URL1)
//                        .addFileToUpload(String.valueOf(actualImage), "tmp_name") //Adding file
//                        .addParameter("my_file", name) //Adding text parameter to the request
//                        .addParameter("id", insert_id)
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//                Log.e("Simg",actualImage+" "+name+" "+insert_id);
//            } catch (Exception exc) {
//                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }
    public void uploadMultipart1(final File actualImage){
        AndroidNetworking.upload(UPLOAD_URL2)
                .addMultipartFile(UPLOAD_KEY,actualImage)
                .addMultipartParameter("ta_id",insert_id)
                .addMultipartParameter("emp_id",empId)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        String res = String.valueOf(response);
                        Log.e("rex",res);
                        try {
                            JSONObject obj =new JSONObject(res);
                            String status = obj.getString("log_staus");
                            empId = obj.getString("emp_id");
                            insert_id  = obj.getString("ta_id");
                            if (status.equals("Success")){
                                // Intent intent = new Intent(Travelling_Activity.this, Profile_Activity.class);
                                // startActivity(intent);
//                                        fromId.setText("");
//                                        toID.setText("");
//                                        purposeID.setText("");
//                                        visitplaceId.setText("");
//                                        modeID.setText("");
//                                        amountId.setText("");
                                         imageView.setImageBitmap(null);
                                         Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                                         uploadsetGetImages.clear();
                                         getImageView(insert_id,empId);
                                         uploadsetGetImages.clear();
                            }
                            else if (status.equals("Not Success")){
                                Toast.makeText(getApplicationContext(),status,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        lvvv9.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                  //  filePath = data.getData();

            try {
                          actualImage = FileUtil.from(this,data.getData());
//                         filePath  = Uri.fromFile(actualImage);
                         Log.e("Path", String.valueOf(actualImage));
                         bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(actualImage));
                        imageView.setImageBitmap(bitmap);
                        uploadMultipart1(actualImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
//                    bitmap = (Bitmap) data.getExtras().get("data");
//                    imageView.setImageBitmap(bitmap);
//                    Log.e("Camera"," "+bitmap);
//                }
             else {

//            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
////                setPic();
////                galleryAddPic();
////            }

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
              // setPic();
              //  galleryAddPic();
                    Log.e("Camera"," "+bitmap);
                } }
    }
//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        imageView.setImageBitmap(bitmap);
//    }
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            return path;
        }
       return null;

    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    public static boolean isIntentAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        Log.e("mCurrentPhotoPath",mCurrentPhotoPath);
//        String name ="Pinku_Chunku";
//        try {
//            String uploadId = UUID.randomUUID().toString();
//
//            //Creating a multi part request
//            new MultipartUploadRequest(this, uploadId,UPLOAD_URL1)
//                    .addFileToUpload(mCurrentPhotoPath, "tmp_name") //Adding file
//                    .addParameter("name", name) //Adding text parameter to the request
//                    .addParameter("id", insert_id)
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .startUpload(); //Starting the upload
//            Log.e("mCurrentPhotoPath",mCurrentPhotoPath+" "+name+" "+insert_id);
//        } catch (Exception exc) {
//            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        return image;
//    }
public void getImageView(final String insert_id,final String empId){
    //Log.e("TAENTRYVALUE",insert_id+" "+" "+empId);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.TRAYENTRY,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("TAENTRYVALUERES", response);
                    if (response.equals("null")){
                        homeItemViewAdapter.notifyDataSetChanged();
                    }
                    try {
                        //Log.e("TAENTRYVALUE",response);
                        JSONArray array = new JSONArray(response);
                        lvvv9.setVisibility(View.GONE);
                        for (int i =0; i<array.length(); i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            String taimg = jsonObject.getString("ta_img");
                            String tblid = jsonObject.getString("tbl_id");
                            Log.e("TAENTRYVALUE",taimg+" "+tblid);
                            uploadsetGetImages.add(new UploadsetGetImage(
                                    jsonObject.getString("ta_img"),
                                    jsonObject.getString("tbl_id")
                            ));
                            arrlist.addAll(uploadsetGetImages);
                            homeItemViewAdapter = new HomeItemViewAdapter(getApplicationContext(), uploadsetGetImages);
                            rcv.setAdapter(homeItemViewAdapter);
                            homeItemViewAdapter.notifyDataSetChanged();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Your Ta Is Not Insert Due To Server Offline", Toast.LENGTH_SHORT).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("ta_id", insert_id);
            params.put("emp_id", empId);
            Log.e("TAENTRYVALUE",insert_id+" "+" "+empId);
            return params;
        }
    };
    //   VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
    stringRequest.setShouldCache(false);
    volleySingleton.addToRequestQueue(stringRequest);
    }
    public class HomeItemViewAdapter extends RecyclerView.Adapter<HomeItemViewAdapter.ViewHolder> {
        private Context mCtx;
        private List<UploadsetGetImage> uploadsetGetImagess;
        int listview;
        public HomeItemViewAdapter(Context mCtx, List<UploadsetGetImage> uploadsetGetImagess) {
            this.mCtx = mCtx;
            this.uploadsetGetImagess = uploadsetGetImagess;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_row_view, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final UploadsetGetImage  uploadsetGetImage = uploadsetGetImagess.get(position);
            listview = position;
            holder.catid.setText(String.valueOf(uploadsetGetImage.getTblid()));
            //holder.imageItem.setImageDrawable(mCtx.getResources().getDrawable(homeitemViewCatagorySetGet.getImg()));
            Log.e("TAENTRYVALUE",uploadsetGetImage.getTaimg());
            Glide.with(mCtx)
                    .load(uploadsetGetImage.getTaimg())
                    .into(holder.imageItem);
                    holder.imageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String catID = holder.catid.getText().toString();
                    String img = uploadsetGetImage.getTaimg();
                    Log.e("CATID", img);
//                    Intent intent = new Intent(mCtx, ProductMaster.class);
//                    Bundle bundle_edit  =   new Bundle();
//                    bundle_edit.putString("catID",catID);
//                    bundle_edit.putString("img",uploadsetGetImage.getImg());
//                    intent.putExtras(bundle_edit);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mCtx.startActivity(intent);

                    String title = "Image View Box";

                    String msg = "Do you want to start tracking?";
                    finalsubmit(title,msg,img);
                }
            });
            holder.beleteId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String catID = holder.catid.getText().toString();
                    uploadsetGetImages.clear();
                    delete(catID);
                }
            });

        }
        @Override
        public int getItemCount() {
            return uploadsetGetImagess.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView itemname,catid;
            private ImageView imageItem;
            LinearLayout lv;
            Button beleteId;
            public ViewHolder(View view) {
                super(view);
                beleteId = (Button) view.findViewById(R.id.beleteId);
                imageItem = (ImageView) view.findViewById(R.id.imageItem);
                catid = (TextView)view.findViewById(R.id.catid);
                lv = (LinearLayout) view.findViewById(R.id.lv);
            }
        }
    }

    public void delete(final String catID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.TRAYENTRYDELTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Log.e("Delte", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            String logstaus = obj.getString("log_staus");
                            empId = obj.getString("emp_id");
                            insert_id  = obj.getString("ta_id");
                            Log.e("Delte"," "+obj);
                            if (logstaus.equals("Success")){
                                Toast.makeText(getApplicationContext(),"Delete Successfuly",Toast.LENGTH_SHORT).show();
                                getImageView(insert_id,empId);
                            }

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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_id", catID);
                params.put("emp_id",empId);
                params.put("ta_id",insert_id);
                Log.e("Delte",catID);
                return params;
            }
        };
        //   VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);

    }

    public AlertDialog finalsubmit(String title, String msg,String img){
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
