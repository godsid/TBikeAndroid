package info.srihawong.tbike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Demo on 2/27/14 AD.
 */
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