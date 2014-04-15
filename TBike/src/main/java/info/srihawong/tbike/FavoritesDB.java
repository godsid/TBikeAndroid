package info.srihawong.tbike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Demo on 2/27/14 AD.
 */

class FaviriteData{
    String id,forum_id,topic_id,user_id,title,username,sticky,topic_create_time;

    FaviriteData() {
    }
    FaviriteData(String id,String forum_id, String topic_id, String user_id, String title, String username, String sticky, String topic_create_time) {
        this.forum_id = forum_id;
        this.topic_id = topic_id;
        this.user_id = user_id;
        this.title = title;
        this.username = username;
        this.sticky = sticky;
        this.topic_create_time = topic_create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForum_id() {
        return forum_id;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSticky() {
        return sticky;
    }

    public void setSticky(String sticky) {
        this.sticky = sticky;
    }

    public String getTopic_create_time() {
        return topic_create_time;
    }

    public void setTopic_create_time(String topic_create_time) {
        this.topic_create_time = topic_create_time;
    }
}

public class FavoritesDB {

    private static final String TABLE_NAME = "Favorites";
    private static String DATABASE_NAME = "";
    private static final int DATABASE_VERSION = 3;
    private static final String COLUMN_FORUM_ID = "forum_id";
    private static final String COLUMN_TOPIC_ID = "topic_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_STICKY = "sticky";
    private static final String COLUMN_TOPIC_CREATE_TIME = "topic_create_time";

    public static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ( " +
            "ID INTEGER PRIMARY KEY, " +
            COLUMN_FORUM_ID + " INTEGER,  " +
            COLUMN_TOPIC_ID + " INTEGER,  " +
            COLUMN_USER_ID + " INTEGER,  " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_STICKY + " INTEGER, " +
            COLUMN_TOPIC_CREATE_TIME + " NUMERIC )";

    public static final String DROP_SQL = "DROP TABLE " + TABLE_NAME;

    private SQLiteDatabase db;
    private FavoritesDBHelper dbHelper;

    //private static final String COLUMN_FORUM_ID = "forum_id";
    //private static final String COLUMN_FORUM_ID = "forum_id";

    public FavoritesDB(Context context){
        //Sql db = new Sql(context);

        DATABASE_NAME = context.getString(R.string.database_name);
        Util.Log("Init Database " + DATABASE_NAME);
        dbHelper = new FavoritesDBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getReadableDatabase();
    }
    public Boolean isFavorite(String topicID){
        db = dbHelper.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_TOPIC_ID+" = "+ topicID,null);
        if(rs.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
    public Long add(int forumID,int topicID,int userID,String username,String title,int sticky ,String createdate){
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FORUM_ID,forumID);
        values.put(COLUMN_TOPIC_ID,topicID);
        values.put(COLUMN_USER_ID,userID);
        values.put(COLUMN_TITLE,title);
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_STICKY,sticky);

        //createdate.replace("วันนี้", DateFormat.format("yyyy-MM-d",new Date()));
        //createdate.replace("เมื่อวานนี้",DateFormat.format("yyyy-MM-d",(new Date())));

        values.put(COLUMN_TOPIC_CREATE_TIME,(long)1);

        Long insert_id = db.insert(TABLE_NAME,null,values);
        Util.Log(insert_id.toString());
        return insert_id;
    }

    public int update(){
        return 1;
    }

    public ArrayList<FaviriteData> getLimit(String search ,int limit,int offset){
        ArrayList<FaviriteData> listItems = new ArrayList<FaviriteData>();
        String where = "";
        db = dbHelper.getReadableDatabase();
        if(!search.isEmpty()){
            where = " WHERE "+COLUMN_TITLE +" LIKE '%"+search.trim()+"%' ";
        }
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+ " "+where+" ORDER BY ID DESC LIMIT "+String.valueOf(offset)+","+String.valueOf(limit),null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    listItems.add(new FaviriteData(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7)));
                }while (cursor.moveToNext());
            }
        }
        cursor.close();

        //ContentValues values = new ContentValues();
        return listItems;
        //Log.d("tui", rs.getColumnNames().toString());
    }
    public ArrayList<FaviriteData> getLimit(int limit,int offset){
           return getLimit("",limit,offset);
    }


    public int delete(int topicID){
        db = dbHelper.getWritableDatabase();
        String  selection = COLUMN_TOPIC_ID + " = ?";
        String[] selectionArgs = { String.valueOf(topicID) };
        Util.Log(String.valueOf(db.delete(TABLE_NAME, selection, selectionArgs)));

        return 0;
    }

}
class FavoritesDBHelper extends SQLiteOpenHelper {

    public FavoritesDBHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoritesDB.CREATE_SQL);
        Util.Log("Create DB Table of Favorites");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FavoritesDB.DROP_SQL);
        onCreate(db);
        Util.Log("Upgrade DB Table of Favorites From " +oldVersion+" To "+newVersion );
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Util.Log("Downgrade DB Table of Favorites From " +oldVersion+" To "+newVersion );
        //super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Util.Log("Open DB Table Favorites");
    }
}