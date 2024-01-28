package in.co.icdswb.hrms.SharedPrefManagerActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import in.co.icdswb.hrms.Login;
import in.co.icdswb.hrms.SetGetActivity.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "hrms";
    private static final String KEY_EMP_DESIG= "emp_desig";
    private static final String KEY_EMP_IMG = "emp_img";
    private static final String KEY_OFFICE_CLOSE_TIME = "office_close_time";
    private static final String KEY_EMP_BRANCH = "emp_branch";
    private static final String KEY_EMP_NAME = "emp_name";
    private static final String KEY_EMP_ID = "emp_id";
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMP_DESIG, user.getEmp_desig());
        editor.putString(KEY_EMP_IMG, user.getEmp_img());
        editor.putString(KEY_OFFICE_CLOSE_TIME,user.getOffice_close_time());
        editor.putString(KEY_EMP_BRANCH,user.getEmp_branch());
        editor.putString(KEY_EMP_NAME,user.getEmp_name());
        editor.putString(KEY_EMP_ID, user.getEmp_id());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMP_NAME, null) != null;
    }
    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(

                sharedPreferences.getString(KEY_EMP_DESIG, null),
                sharedPreferences.getString(KEY_EMP_IMG, null),
                sharedPreferences.getString(KEY_OFFICE_CLOSE_TIME, null),
                sharedPreferences.getString(KEY_EMP_BRANCH, null),
                sharedPreferences.getString(KEY_EMP_NAME, null),
                sharedPreferences.getString(KEY_EMP_ID, null)


        );
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();
        //  mCtx.startActivity(new Intent(mCtx, MainActivity.class));
//        Intent  myIntent = new Intent(mCtx,MainActivity.class);
//        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(mCtx, Login.class);
        mCtx.startActivity(intent);
    }
}
