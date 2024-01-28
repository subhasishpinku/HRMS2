package in.co.icdswb.hrms.ActivityDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String  DB_NAME ="HRMS";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAMELOGIN ="login";
    public static final String TABLE_ID ="id";
    public static final String TABLE_LOGINSTATUS="logstatus";
    public static final String TABLE_IMEISTATUS="imeistatus";
    public static final String TABLE_IMEISTATUSIMG="imeistatusmsg";
    public static final String TABLE_LOGSTATUSMSG="logstatusmsg";
    public static final String TABLE_EMPDESIG="empdesig";
    public static final String TABLE_EMPIMG="empimg";
    public static final String TABLE_OFFICECLOSETIME="officeclosetime";
    public static final String TABLE_EMPBRANCH="empbranch";
    public static final String TABLE_EMPNAME="empname";
    public static final String TABLE_EMPID="empid";
    public static final String TABLE_EMPIMEI="imeiid";
    public static final String TABLE_EMPPASS="password";
    ///Sync odIn/////////////////////////////////////////////////////
    public static final String TABLE_EMPNAMESYC = "odin";
    public static final String TABLE_SYNCID = "idsync";
    public static final String TABLE_EMPIDSYC = "empidsyc";
    public static final String TABLE_ODDATESYC = "oddatesyc";
    public static final String TABLE_ODTIMESYC = "odtimesyc";
    public static final String TABLE_ADDRESSSYC = "addresssyc";
    public static final String TABLE_LOCATIONSYC = "locationsyc";
    public static final String TABLE_PURPOSESYC = "purposesyc";
    public static final String TABLE_LATSYC = "latsyc";
    public static final String TABLE_LNGSYC = "lngsyc";
    public static final String TABLE_ODTYPESYC = "odtypesyc";
    public static final String TABLE_ODINSYNCDATE = "srinkdate";
    public static final String COLUMN_STATUSODIN = "odinstatus";
    /////////////////////////////////////////////////////////////////
    ////////////////////RunTimeSync////////////////////////////////
    public static final String TABLE_RUNTABLENAME ="syncruntime";
    public static final String TABLE_RUNTABEID ="rid";
    public static final String TABLE_RUNEMPID = "rempid";
    public static final String TABLE_RUNODDATE = "roddate";
    public static final String TABLE_RUNODTIME ="rodtime";
    public static final String TABLE_RUNACTUALLOC ="ractual_loc";
    public static final String TABLE_RUNLAT = "rlat";
    public static final String TABLE_RUNLNG = "rlng";
    public static final String TABLE_RUNODTYPE ="rodtype";
    public static final String TABLE_RUNTIMESYNCDATE = "srinkdateruntime";
    public static final String TABLE_RUNODSTATUS ="rstatus";
    //////////////////////////////////////////////////////////////
    ///////////////////Sync OdOut/////////////////////////////////
    public  static final String TABLE_ODOUTTABLENAME = "syncout";
    public static final String TABLE_ODOUTTABEID ="oid";
    public static final String TABLE_ODOUTEMPID = "empidodout";
    public static final String TABLE_ODOUTDATE= "oddate";
    public static final String TABLE_ODOUTODTIME = "odtimeodout";
    public static final String TABLE_ODOUTACTUALLOC = "actuallocodout";
    public static final String TABLE_ODOUTLATODOUT = "latodout";
    public static final String TABLE_ODOUTLANGODOUT = "langodout";
    public static final String TABLE_ODOUTYPEOUT = "odtypeodout";
    public static final String TABLE_ODOUTSRINKDATE = "srinkdateodout";
    public static final String TABLE_ODOUTSTATUES = "statusout";
    /////////////////////////////////////////////////////////////
    public static final String TABLE_LOGINSHOW = "loginshow";
    public static final String TABLE_LOGDATEID = "logdateid";
    public static final String TABLE_LOGINFLAG = "loginflag";
    public static final String TABLE_LOGINDATE = "logindate";
    public static final String TABLE_DATETIMESYSTEM ="datetimesystem";
    //////////////////////////////////////////////////////////
    public static final String TABLE_FLAGTABLE ="flagtable";
    public static final String TABLE_FLAGID ="flagid";
    public static final String TABLE_FLAGUPDATE ="flagupdate";
    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql2 = "CREATE TABLE " + TABLE_NAMELOGIN
                + "(" + TABLE_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_LOGINSTATUS + " TINYINT, "
                + TABLE_IMEISTATUS + " TINYINT, "
                + TABLE_IMEISTATUSIMG + " VARCHAR, "
                + TABLE_LOGSTATUSMSG + " VARCHAR, "
                + TABLE_EMPDESIG + " VARCHAR, "
                + TABLE_EMPIMG + " VARCHAR, "
                + TABLE_OFFICECLOSETIME + " VARCHAR, "
                + TABLE_EMPBRANCH + " VARCHAR, "
                + TABLE_EMPNAME + " VARCHAR, "
                + TABLE_EMPID + " VARCHAR, "
                + TABLE_EMPIMEI + " VARCHAR, "
                + TABLE_EMPPASS + " VARCHAR);";
        db.execSQL(sql2);

        String OdIn = "CREATE TABLE " + TABLE_EMPNAMESYC
                + "(" + TABLE_SYNCID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_EMPIDSYC + " VARCHAR, "
                + TABLE_ODDATESYC + " VARCHAR, "
                + TABLE_ODTIMESYC + " VARCHAR, "
                + TABLE_ADDRESSSYC + " VARCHAR, "
                + TABLE_LOCATIONSYC + " VARCHAR, "
                + TABLE_PURPOSESYC + " VARCHAR, "
                + TABLE_LATSYC + " VARCHAR, "
                + TABLE_LNGSYC + " VARCHAR, "
                + TABLE_ODTYPESYC + " VARCHAR, "
                + TABLE_ODINSYNCDATE + " VARCHAR, "
                + COLUMN_STATUSODIN + " TINYINT);";
        db.execSQL(OdIn);

        String runtime = "CREATE TABLE " + TABLE_RUNTABLENAME
                + "(" + TABLE_RUNTABEID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_RUNEMPID + " VARCHAR, "
                + TABLE_RUNODDATE + " VARCHAR, "
                + TABLE_RUNODTIME + " VARCHAR, "
                + TABLE_RUNACTUALLOC + " VARCHAR, "
                + TABLE_RUNLAT + " VARCHAR, "
                + TABLE_RUNLNG + " VARCHAR, "
                + TABLE_RUNODTYPE + " VARCHAR, "
                + TABLE_RUNTIMESYNCDATE + " VARCHAR, "
                + TABLE_RUNODSTATUS + " TINYINT);";
        db.execSQL(runtime);

        String odout = "CREATE TABLE " + TABLE_ODOUTTABLENAME
                + "(" + TABLE_ODOUTTABEID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_ODOUTEMPID + " VARCHAR, "
                + TABLE_ODOUTDATE + " VARCHAR, "
                + TABLE_ODOUTODTIME + " VARCHAR, "
                + TABLE_ODOUTACTUALLOC + " VARCHAR, "
                + TABLE_ODOUTLATODOUT + " VARCHAR, "
                + TABLE_ODOUTLANGODOUT + " VARCHAR, "
                + TABLE_ODOUTYPEOUT + " VARCHAR, "
                + TABLE_ODOUTSRINKDATE + " VARCHAR, "
                + TABLE_ODOUTSTATUES + " TINYINT);";
        db.execSQL(odout);
        String flagdate = "CREATE TABLE " + TABLE_LOGINSHOW
                + "(" + TABLE_LOGDATEID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_LOGINFLAG + " VARCHAR, "
                + TABLE_LOGINDATE + " VARCHAR, "
                + TABLE_DATETIMESYSTEM + " VARCHAR);";
        db.execSQL(flagdate);
        String flagbtn = "CREATE TABLE " + TABLE_FLAGTABLE
                + "(" + TABLE_FLAGID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TABLE_FLAGUPDATE + " VARCHAR);";
        db.execSQL(flagbtn);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS TABLE_NAMELOGIN";
        db.execSQL(sql);
        String OdIn = "DROP TABLE IF EXISTS TABLE_EMPNAMESYC";
        db.execSQL(OdIn);
        String odruntimein = "DROP TABLE IF EXISTS TABLE_RUNTABLENAME";
        db.execSQL(odruntimein);
        String odout = "DROP TABLE IF EXISTS TABLE_ODOUTTABLENAME";
        db.execSQL(odout);
        String flag = "DROP TABLE IF EXISTS TABLE_LOGINSHOW";
        db.execSQL(flag);
        String flagbtncreate = "DROP TABLE IF EXISTS TABLE_FLAGTABLE";
        db.execSQL(flagbtncreate);
    }
   public boolean addlogin(int logstatus,int imeistatus,String imeistatus_msg,String logstatus_msg,String emp_desig,String emp_img,String office_close_time,String emp_branch,String emp_name,String emp_id,String imei,String pwd){
    SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(TABLE_LOGINSTATUS,logstatus);
       contentValues.put(TABLE_IMEISTATUS,imeistatus);
       contentValues.put(TABLE_IMEISTATUSIMG,imeistatus_msg);
       contentValues.put(TABLE_LOGSTATUSMSG,logstatus_msg);
       contentValues.put(TABLE_EMPDESIG,emp_desig);
       contentValues.put(TABLE_EMPIMG,emp_img);
       contentValues.put(TABLE_OFFICECLOSETIME,office_close_time);
       contentValues.put(TABLE_EMPBRANCH,emp_branch);
       contentValues.put(TABLE_EMPNAME,emp_name);
       contentValues.put(TABLE_EMPID,emp_id);
       contentValues.put(TABLE_EMPIMEI,imei);
       contentValues.put(TABLE_EMPPASS,pwd);
       db.insert(TABLE_NAMELOGIN, null, contentValues);
       db.close();
       return true;
   }

    public boolean updateData(String id,int logstatus,int imeistatus,String imeistatus_msg,String logstatus_msg,String emp_desig,String emp_img,String office_close_time,String emp_branch,String emp_name,String emp_id,String imei,String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_ID,id);
        contentValues.put(TABLE_LOGINSTATUS,logstatus);
        contentValues.put(TABLE_IMEISTATUS,imeistatus);
        contentValues.put(TABLE_IMEISTATUSIMG,imeistatus_msg);
        contentValues.put(TABLE_LOGSTATUSMSG,logstatus_msg);
        contentValues.put(TABLE_EMPDESIG,emp_desig);
        contentValues.put(TABLE_EMPIMG,emp_img);
        contentValues.put(TABLE_OFFICECLOSETIME,office_close_time);
        contentValues.put(TABLE_EMPBRANCH,emp_branch);
        contentValues.put(TABLE_EMPNAME,emp_name);
        contentValues.put(TABLE_EMPID,emp_id);
        contentValues.put(TABLE_EMPIMEI,imei);
        contentValues.put(TABLE_EMPIMEI,pwd);
        db.update(TABLE_NAMELOGIN, contentValues, "id = ?",new String[] { id });
        return true;
    }
    public Cursor getAllLoginData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAMELOGIN,null);
        return res;
    }
 ///////////////////////odin sync//////////////////////////////////////////////////
 public boolean ODIN(String empId,String date,String timee,String address,String location,String purpose,String lat,String lng,String odtype ,String srinkdate, int status) {
     SQLiteDatabase db = this.getWritableDatabase();
     ContentValues contentValues = new ContentValues();
     contentValues.put(TABLE_EMPIDSYC, empId);
     contentValues.put(TABLE_ODDATESYC, date);
     contentValues.put(TABLE_ODTIMESYC, timee);
     contentValues.put(TABLE_ADDRESSSYC, address);
     contentValues.put(TABLE_LOCATIONSYC, location);
     contentValues.put(TABLE_PURPOSESYC, purpose);
     contentValues.put(TABLE_LATSYC, lat);
     contentValues.put(TABLE_LNGSYC, lng);
     contentValues.put(TABLE_ODTYPESYC, odtype);
     contentValues.put(TABLE_ODINSYNCDATE, srinkdate);
     contentValues.put(COLUMN_STATUSODIN, status);
     db.insert(TABLE_EMPNAMESYC, null, contentValues);
     db.close();
     return true;
 }

    public boolean updateOdSyncStatus(int idsync, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUSODIN, status);
        db.update(TABLE_EMPNAMESYC, contentValues, TABLE_SYNCID + "=" + idsync, null);
        db.close();
        return true;
     }

    public Cursor getodinSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_EMPNAMESYC + " ORDER BY " + TABLE_SYNCID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_EMPNAMESYC + " WHERE " + COLUMN_STATUSODIN + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
/// ///////// odruntime in/////////////////////////////////////////////
public boolean OdInRuntime(String rempId,String rdate,String rtimee,String actualoc,String lat,String lng,String runtype ,String syncruntime,int status) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(TABLE_RUNEMPID, rempId);
    contentValues.put(TABLE_RUNODDATE, rdate);
    contentValues.put(TABLE_RUNODTIME, rtimee);
    contentValues.put(TABLE_RUNACTUALLOC, actualoc);
    contentValues.put(TABLE_RUNLAT, lat);
    contentValues.put(TABLE_RUNLNG, lng);
    contentValues.put(TABLE_RUNODTYPE, runtype);
    contentValues.put(TABLE_RUNTIMESYNCDATE,syncruntime);
    contentValues.put(TABLE_RUNODSTATUS, status);
    db.insert(TABLE_RUNTABLENAME, null, contentValues);
    db.close();
    return true;
    }
    public boolean updateRuntimeSyncStatus(int rid, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_RUNODSTATUS, status);
        db.update(TABLE_RUNTABLENAME, contentValues, TABLE_RUNTABEID + "=" + rid, null);
        db.close();
        return true;
    }

    public Cursor getruntimeSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_RUNTABLENAME + " ORDER BY " + TABLE_RUNTABEID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getUnsynceRuntime() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_RUNTABLENAME + " WHERE " + TABLE_RUNODSTATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
//////////////////////////////////////////////////////////////////////
////////////////ODOut////////////////////////////////////////////////
public boolean synodoutnsave(String outempid,String outdate,String outtime,String outactualoc,String outlat,String outlang,String outtype ,String syncruntime,int ostatus) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(TABLE_ODOUTEMPID, outempid);
    contentValues.put(TABLE_ODOUTDATE, outdate);
    contentValues.put(TABLE_ODOUTODTIME, outtime);
    contentValues.put(TABLE_ODOUTACTUALLOC, outactualoc);
    contentValues.put(TABLE_ODOUTLATODOUT, outlat);
    contentValues.put(TABLE_ODOUTLANGODOUT, outlang);
    contentValues.put(TABLE_ODOUTYPEOUT, outtype);
    contentValues.put(TABLE_ODOUTSRINKDATE,syncruntime);
    contentValues.put(TABLE_ODOUTSTATUES, ostatus);
    db.insert(TABLE_ODOUTTABLENAME, null, contentValues);
    db.close();
    return true;
}

    public boolean updateOdoutSyncStatus(int oid, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_ODOUTSTATUES, status);
        db.update(TABLE_ODOUTTABLENAME, contentValues, TABLE_ODOUTTABEID + "=" + oid, null);
        db.close();
        return true;
    }

    public Cursor getodutSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_ODOUTTABLENAME + " ORDER BY " + TABLE_ODOUTTABEID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public Cursor getUnsynceOdout() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_ODOUTTABLENAME + " WHERE " + TABLE_ODOUTSTATUES + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

//////////////////////////////////////////////////////////////////////
    public boolean FLAGTABLEINSERT(String loginflag, String logindate,String datetimesystem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_LOGINFLAG, loginflag);
        contentValues.put(TABLE_LOGINDATE, logindate);
        contentValues.put(TABLE_DATETIMESYSTEM,datetimesystem);
        db.insert(TABLE_LOGINSHOW, null, contentValues);
        db.close();
        return true;
    }
    public boolean FLAGTABLEUPDATE(String logdateid,String loginflag, String logindate,String datetimesystem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_LOGDATEID, logdateid);
        contentValues.put(TABLE_LOGINFLAG, loginflag);
        contentValues.put(TABLE_LOGINDATE, logindate);
        contentValues.put(TABLE_DATETIMESYSTEM,datetimesystem);
        db.update(TABLE_LOGINSHOW, contentValues, "logdateid = ?",new String[] { logdateid });
        db.close();
        return true;
    }
    public Cursor getFlagData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_LOGINSHOW,null);
        return res;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean insertflag(String flageupdate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_FLAGUPDATE, flageupdate);
        db.insert(TABLE_FLAGTABLE, null, contentValues);
        db.close();
        return true;
    }
    public boolean updateflag(String flagid,String flageupdate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_FLAGID, flagid);
        contentValues.put(TABLE_FLAGUPDATE, flageupdate);
        db.update(TABLE_FLAGTABLE, contentValues, "flagid = ?",new String[] { flagid });
        db.close();
        return true;
    }
    public Cursor getupdateflagdata() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_FLAGTABLE,null);
        return res;
    }
}
