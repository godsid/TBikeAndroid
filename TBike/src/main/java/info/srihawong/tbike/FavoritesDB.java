package info.srihawong.tbike;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * Created by Demo on 2/27/14 AD.
 */
public class FavoritesDB {

    private static final String TABLE_NAME = "Favorites";
    private static String DATABASE_NAME = "";
    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_FORUM_ID = "forum_id";
    private static final String COLUMN_TOPIC_ID = "topic_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TOPIC_CREATE_TIME = "topic_create_time";

    public static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ( " +
            "ID INTEGER PRIMARY KEY, " +
            COLUMN_FORUM_ID + " INTEGER,  " +
            COLUMN_TOPIC_ID + " INTEGER,  " +
            COLUMN_TITLE + " INTEGER, " +
            COLUMN_TOPIC_CREATE_TIME + " NUMERIC )";
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

    public int add(int forumID, int topicID,String title,Long topicCreateTimestamp){
        ContentValues values = new ContentValues();
        values.put(COLUMN_FORUM_ID,forumID);
        values.put(COLUMN_TOPIC_ID,topicID);
        values.put(COLUMN_TITLE,title);
        values.put(COLUMN_TOPIC_CREATE_TIME,topicCreateTimestamp);
        Long insert_id = db.insert(TABLE_NAME,null,values);
        Util.Log(insert_id.toString());

        return 1;
    }
    public int update(){
        return 1;
    }

    public int delete(){
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
        //onCreate(db);
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