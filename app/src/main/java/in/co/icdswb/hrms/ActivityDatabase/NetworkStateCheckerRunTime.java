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

import in.co.icdswb.hrms.ActivityService.MyService;
import in.co.icdswb.hrms.ActivityVolley.VolleySingleton;

public class NetworkStateCheckerRunTime extends BroadcastReceiver {

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

                //getting all the unsynced names
                Cursor cursor = db.getUnsynceRuntime();
                int i = 1;
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        OdRunTime(
                                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNTABEID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNEMPID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNODDATE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNODTIME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNACTUALLOC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNLAT)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNLNG)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNODTYPE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNTIMESYNCDATE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_RUNODSTATUS))
                        );
                      //  Toast.makeText(context, "Network Loop Runtine  "+i,Toast.LENGTH_SHORT).show();
                        Log.e("Network Loop Runtine","Runtime"+i);
                       i++;
//                        removeodInRuntime();
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    /*
     * method taking two arguments
     * name that is to be saved and id of the name from SQLite
     * if the name is successfully sent
     * we will update the status as synced in SQLite
     * */
    private void OdRunTime(final int rid, final String rempid, final String roddate,final String rodtime, final String ractual_loc,final String rlat,final String rlng,final String rodtype,final String  syncruntime,final String rstatus) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyService.SERVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                           // Toast.makeText(context, "Network Loop Remove ",Toast.LENGTH_SHORT).show();
                            removeodInRuntime();
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateRuntimeSyncStatus(rid, MyService.NAME_SYNCED_WITH_SERVERRUN);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(MyService.RUNDATA_SAVED_BROADCAST));
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
                params.put("emp_id", rempid);
                params.put("od_date", roddate);
                params.put("od_time", rodtime);
                params.put("actual_loc", ractual_loc);
                params.put("lat", rlat);
                params.put("long", rlng);
                params.put("'od_type", rodtype);
                params.put("srink_date", syncruntime);
                Log.e("RREmpId",rempid+" "+roddate+" "+rodtime+" "+ractual_loc+" "+rlat+" "+rlng+" "+rodtype+ " "+syncruntime+" ");
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void removeodInRuntime(){
//        Toast.makeText(context,"DeleteRunTIme",Toast.LENGTH_SHORT).show();
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        Cursor c = sqlDB.rawQuery("DELETE FROM syncruntime", null);
        String rid="";
        String rempid= "";
        String roddate ="";
        String rodtime = "";
        String ractual_loc ="";
        String rlat="";
        String rlng ="";
        String rodtype ="";
        String srinkdateruntime ="";
        String rstatus = "";
        int status = 0;
        if (c.moveToFirst()) {

            while ( !c.isAfterLast() ) {

                rid = c.getString( c.getColumnIndex("rid"));
                rempid = c.getString( c.getColumnIndex("rempid"));
                roddate = c.getString( c.getColumnIndex("roddate"));
                rodtime = c.getString( c.getColumnIndex("odtimesyc"));
                ractual_loc = c.getString( c.getColumnIndex("rodtime"));
                rlat = c.getString( c.getColumnIndex("rlat"));
                rlng = c.getString( c.getColumnIndex("rlng"));
                rodtype = c.getString( c.getColumnIndex("rodtype"));
                srinkdateruntime = c.getString( c.getColumnIndex("srinkdateruntime"));
                rstatus = c.getString( c.getColumnIndex("rstatus"));
                if(!rid.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+rid+"'");
                }
                if(!rempid.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+rempid+"'");
                }
                if(!roddate.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+roddate+"'");
                }
                if(!rodtime.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+rodtime+"'");
                }
                if(!ractual_loc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+ractual_loc+"'");
                }
                if(!rlat.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+rlat+"'");
                }
                if(!rlng.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+rlng+"'");
                }
                if(!rodtype.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+rodtype+"'");
                }
                if(!srinkdateruntime.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+srinkdateruntime+"'");
                }
                if(!rstatus.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+rstatus+"'");
                }
                c.moveToNext();
            }
        }

        c.close();
    }
}

