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

public class NetworkStateCheckerOdOut extends BroadcastReceiver {

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
                Cursor cursor = db.getUnsynceOdout();
                int i = 1;
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        OdOut(
                                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTTABEID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTEMPID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTDATE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTODTIME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTACTUALLOC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTLATODOUT)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTLANGODOUT)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTYPEOUT)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTSRINKDATE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TABLE_ODOUTSTATUES))

                        );
//                        Toast.makeText(context, "Network Loop ODout  "+i,Toast.LENGTH_SHORT).show();
//                        i++;
                     //   removeododoutnRuntime();
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
    private void OdOut(final int oid, final String outempid, final String outdate,final String outtime, final String outactualoc,final String outlat,final String outlang,final String outtype,final String  syncruntime,final String ostatus) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Profile_Activity.ODOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                   //     Toast.makeText(context, "Network Loop OUT Remove ",Toast.LENGTH_SHORT).show();
                        removeododoutnRuntime();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateOdoutSyncStatus(oid, Profile_Activity.ODOUT_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(Profile_Activity.DATA_SAVED_BROADCASTOUT));
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
                params.put("emp_id", outempid);
                params.put("od_date", outdate);
                params.put("od_time", outtime);
                params.put("actual_loc", outactualoc);
                params.put("lat",  outlat);
                params.put("long", outlang);
                params.put("od_type", outtype);
                params.put("srink_date", syncruntime);
                Log.e("Sync_Service_OUT",outempid+" "+outdate+" "+outtime+" "+outactualoc+" "+outlat+" "+outlang+" "+outtype+" "+syncruntime);
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void removeododoutnRuntime(){
        //Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show();
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        Cursor c = sqlDB.rawQuery("DELETE FROM syncout", null);
        String oid ="";
        String outempid="";
        String outdate= "";
        String outtime ="";
        String outactualoc = "";
        String outlat ="";
        String outlang="";
        String outtype ="";
        String syncruntime ="";
        String ostatus ="";
        int status = 0;
        if (c.moveToFirst()) {

            while ( !c.isAfterLast() ) {
                oid = c.getString( c.getColumnIndex("oid"));
                outempid = c.getString( c.getColumnIndex("empidodout"));
                outdate = c.getString( c.getColumnIndex("oddate"));
                outtime = c.getString( c.getColumnIndex("odtimeodout"));
                outactualoc = c.getString( c.getColumnIndex("actuallocodout"));
                outlat = c.getString( c.getColumnIndex("latodout"));
                outlang = c.getString( c.getColumnIndex("langodout"));
                outtype = c.getString( c.getColumnIndex("odtypeodout"));
                syncruntime = c.getString( c.getColumnIndex("srinkdateodout"));
                ostatus = c.getString( c.getColumnIndex("statusout"));
                if(!oid.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+oid+"'");
                }
                if(!outempid.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+outempid+"'");
                }
                if(!outdate.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+outdate+"'");
                }
                if(!outtime.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+outtime+"'");
                }
                if(!outactualoc.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+outactualoc+"'");
                }
                if(!outlat.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+outlat+"'");
                }
                if(!outlang.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+outlang+"'");
                }
                if(!outtype.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+outtype+"'");
                }
                if(!syncruntime.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+syncruntime+"'");
                }
                if(!ostatus.equals("android_metadata")){
                    sqlDB.execSQL("DROP TABLE '"+ostatus+"'");
                }
                c.moveToNext();
            }
        }

        c.close();
    }
}

