package in.co.icdswb.hrms.ActivityDatabase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.co.icdswb.hrms.ActivityService.Profile_Activity;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;

public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DatabaseHelper db;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Cursor cursor = db.getUnsyncedNames();
                int i = 1;
                if (cursor.moveToFirst()){
                    do {
                        ODINSYNC(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_SYNCID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_EMPIDSYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODDATESYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODTIMESYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ADDRESSSYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_LOCATIONSYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_PURPOSESYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_LATSYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_LNGSYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODTYPESYC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODINSYNCDATE))

                        );
//                       Toast.makeText(context, "Network Loop ODIn "+i,Toast.LENGTH_SHORT).show();
//                        i++;
                    //    removeodIn();
                    } while (cursor.moveToNext());
                }
            }
        }
    }


    private void ODINSYNC(final int idsync, final String empId, final String date, final String timee, final String address, final String location, final String purpose, final String lat, final String lng,final String odtypesyc,String srinkdate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Profile_Activity.ODIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                         //   Toast.makeText(context, "Network Loop OUT Remove ",Toast.LENGTH_SHORT).show();
                            removeodIn();
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateOdSyncStatus(idsync, Profile_Activity.NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(Profile_Activity.DATA_SAVED_BROADCAST));

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
                params.put("emp_id", empId);
                params.put("od_date", date);
                params.put("od_time",  timee);
                params.put("manul_loc", address);
                params.put("actual_loc", location);
                params.put("od_perpouse", purpose);
                params.put("lat", lat);
                params.put("long", lng);
                params.put("od_type", odtypesyc);
                params.put("srink_date", date);
                Log.e("Sync_Service_Od",empId+" "+date+" "+timee+" "+address+" "+location+" "+purpose+" "+lat+" "+lng+" "+date);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void removeodIn(){
       // Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show();
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        Cursor c = sqlDB.rawQuery("DELETE FROM odin", null);
        String idsync="";
        String empidsyc= "";
        String oddatesyc ="";
        String odtimesyc = "";
        String addresssyc ="";
        String locationsyc="";
        String purposesyc ="";
        String latsyc ="";
        String lngsyc ="";
        String odtypesyc = "";
        String srinkdate= "";
        String odinstatus= "";
        int status = 0;
        if (c.moveToFirst()) {

            while ( !c.isAfterLast() ) {

                idsync = c.getString( c.getColumnIndex("idsync"));
                empidsyc = c.getString( c.getColumnIndex("empidsyc"));
                oddatesyc = c.getString( c.getColumnIndex("oddatesyc"));
                odtimesyc = c.getString( c.getColumnIndex("odtimesyc"));
                addresssyc = c.getString( c.getColumnIndex("addresssyc"));
                locationsyc = c.getString( c.getColumnIndex("locationsyc"));
                purposesyc = c.getString( c.getColumnIndex("purposesyc"));
                latsyc = c.getString( c.getColumnIndex("latsyc"));
                lngsyc = c.getString( c.getColumnIndex("lngsyc"));
                odtypesyc = c.getString( c.getColumnIndex("odtypesyc"));
                srinkdate = c.getString( c.getColumnIndex("srinkdate"));
                odinstatus = c.getString( c.getColumnIndex("odinstatus"));
                if(!idsync.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+idsync+"'");
                }
                if(!empidsyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+empidsyc+"'");
                }
                if(!oddatesyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+oddatesyc+"'");
                }
                if(!odtimesyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+odtimesyc+"'");
                }
                if(!addresssyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+addresssyc+"'");
                }
                if(!locationsyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+locationsyc+"'");
                }
                if(!purposesyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+purposesyc+"'");
                }
                if(!latsyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+latsyc+"'");
                }
                if(!lngsyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+lngsyc+"'");
                }
                if(!odtypesyc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+odtypesyc+"'");
                }
                if(!srinkdate.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+srinkdate+"'");
                }
                if(!odinstatus.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+odinstatus+"'");
                }
                c.moveToNext();
            }
        }

        c.close();
    }



}
