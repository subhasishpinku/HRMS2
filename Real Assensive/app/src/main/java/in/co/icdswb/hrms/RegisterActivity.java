package in.co.icdswb.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import in.co.icdswb.hrms.ActivityUrl.URLs;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;
import in.co.icdswb.hrms.SetGetActivity.UploadsetGetImageView;

public class RegisterActivity extends AppCompatActivity {
    String androidid,model,brand;
    TextView editTexUser, phnoId,hrmsId;
    String user,phone,hrmsid;
    Button registerId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Intent intent = getIntent();
        androidid = intent.getStringExtra("androidid");
        model = intent.getStringExtra("model");
        brand = intent.getStringExtra("brand");
        Log.e("viewdivice",androidid+" "+model+" "+brand);
        editTexUser = (TextView)findViewById(R.id.editTexUser);
        phnoId = (TextView)findViewById(R.id.phnoId);
        hrmsId = (TextView)findViewById(R.id.hrmsId);
        registerId = (Button)findViewById(R.id.registerId);
        registerId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rigister(androidid,model,brand);
            }
        });
    }
    public void rigister(final String androidid,final String model,final String brand){
        user = editTexUser.getText().toString().trim();
        phone = phnoId.getText().toString().trim();
        hrmsid = hrmsId.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            editTexUser.setError("Please enter name");
            editTexUser.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            phnoId.setError("Please enter Phone Number");
            phnoId.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(hrmsid)) {
            hrmsId.setError("Please enter Hrms Id");
            hrmsId.requestFocus();
            return;
        }
        int ph = phnoId.getText().length();
        if (ph<10){
            Toast.makeText(getApplicationContext(),"Enter 10 Digit Mobile Numbe",Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.HRMSREGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RegisterView", response);
                        try {
                            Log.e("RegisterView",response);
                            JSONObject object = new JSONObject(response);
                            String error = object.getString("error");
                            String msg = object.getString("msg");
                            if (error.equals("0")){
                                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                            }
                            else if (error.equals("1")){
                                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emp_name",user);
                params.put("hrms_id", hrmsid);
                params.put("contact_no", phone);
                params.put("model_no", model);
                params.put("brand", brand);
                params.put("ando_id", androidid);
                Log.e("RegisterView",hrmsid+" "
                        +phone+" "+model+" "+brand+" "+androidid+" "+user);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        stringRequest.setShouldCache(false);
        volleySingleton.addToRequestQueue(stringRequest);
    }

}
