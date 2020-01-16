package dvr.com.bluetoothapp.local_data_base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bluetooth";
    private static final String TABLE_FEET_AND_INCHES = "feet_and_inches";
    private static final String TABLE_MM = "table_mm";
    private static final String TABLE_M = "table_m";
    private static final String KEY_ID = "id";
    private static final String KEY_FOP = "fop";
    private static final String KEY_FOS = "fos";
    private static final String KEY_MSP = "msp";
    private static final String KEY_MSS = "mss";
    private static final String KEY_AFP = "afp";
    private static final String KEY_AFS = "afs";
    private String type;

    public DatabaseHandler(Context context, String type) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.type=type;
        //3rd argument to be passed is CursorFactory instance
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        switch (type) {
            case "feet_and_inches":
                String FEET_INCHES_TABLE = "CREATE TABLE " + TABLE_FEET_AND_INCHES + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FOP + " TEXT," + KEY_FOS + " TEXT," + KEY_MSP + " TEXT," + KEY_MSS + " TEXT," + KEY_AFP + " TEXT," + KEY_AFS + " TEXT" + ")";
                db.execSQL(FEET_INCHES_TABLE);
                break;
            case "mm":
                String DATABASE_MM = "CREATE TABLE " + TABLE_MM + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FOP + " TEXT," + KEY_FOS + " TEXT," + KEY_MSP + " TEXT," + KEY_MSS + " TEXT," + KEY_AFP + " TEXT," + KEY_AFS + " TEXT" + ")";
                db.execSQL(DATABASE_MM);
                break;
            case "m":
                String DATABASE_M = "CREATE TABLE " + TABLE_M + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FOP + " TEXT," + KEY_FOS + " TEXT," + KEY_MSP + " TEXT," + KEY_MSS + " TEXT," + KEY_AFP + " TEXT," + KEY_AFS + " TEXT" + ")";
                db.execSQL(DATABASE_M);
                break;
        }
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        switch (type) {
            case "feet_and_inches":
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEET_AND_INCHES);
                break;
            case "mm":
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_MM);
                break;
            case "m":
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_M);
                break;
        }

        onCreate(db);
    }
    // code to add the new contact
    public  void addContact(GraphDataModelClass graphDataModelClass){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_FOP, graphDataModelClass.getFop());
//        values.put(KEY_FOS, graphDataModelClass.getFos());
//
//        values.put(KEY_FOP, graphDataModelClass.getMsp());
//        values.put(KEY_FOS, graphDataModelClass.getMss());
//
//        values.put(KEY_FOP, graphDataModelClass.getAfp());
//        values.put(KEY_FOS, graphDataModelClass.getAfs());

        switch (type) {
            case "feet_and_inches":
                db.insert(TABLE_FEET_AND_INCHES, null, values);
                break;
            case "mm":
                db.insert(TABLE_MM, null, values);
                break;
            case "m":
                db.insert(TABLE_M, null, values);
                break;
        }
        db.close();
    }

    public  GraphDataModelClass getContact(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=null;

        switch (type) {
            case "feet_and_inches":
                 cursor = db.query(TABLE_FEET_AND_INCHES, new String[]{KEY_ID, KEY_FOS,KEY_FOS,KEY_MSP,KEY_MSS,KEY_AFP,KEY_AFS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
                break;
            case "mm":
                cursor = db.query(TABLE_MM, new String[]{KEY_ID, KEY_FOS,KEY_FOS,KEY_MSP,KEY_MSS,KEY_AFP,KEY_AFS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
                break;
            case "m":
                cursor = db.query(TABLE_M, new String[]{KEY_ID, KEY_FOS,KEY_FOS,KEY_MSP,KEY_MSS,KEY_AFP,KEY_AFS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
                break;
        }

        if (cursor != null)
            cursor.moveToFirst();
//        GraphDataModelClass contact = new GraphDataModelClass(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),"","","","","","","","","","","");
        // return contact
        return null;
    }
    // code to get all contacts in a list view
    public List<GraphDataModelClass> getAllContacts() {
        List<GraphDataModelClass> contactList = new ArrayList<GraphDataModelClass>();
        // Select All Query
        String selectQuery="";

        switch (type) {
            case "feet_and_inches":
                selectQuery = "SELECT  * FROM " + TABLE_FEET_AND_INCHES;
                break;
            case "mm":
                selectQuery = "SELECT  * FROM " + TABLE_MM;
                break;
            case "m":
                selectQuery = "SELECT  * FROM " + TABLE_M;
                break;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//        if (cursor.moveToFirst()) {
//            do {
//                GraphDataModelClass contact = new GraphDataModelClass("","","","","","","","","","","","","","","","","","");
////                contact.setId(Integer.parseInt(cursor.getString(0)));
//                contact.setFop(cursor.getString(1));
//                contact.setFos(cursor.getString(2));
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }

        return contactList;
    }

    // code to update the single contact
    public int updateContact(GraphDataModelClass contact){
        SQLiteDatabase db = this.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(KEY_FOP, contact.getFop());
//        values.put(KEY_FOS, contact.getFos());
//        return db.update(TABLE_FEET_AND_INCHES, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(contact.getId())});


        return 0;
    }
    // Deleting single contact
    public void deleteContact(GraphDataModelClass contact) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_FEET_AND_INCHES, KEY_ID + " = ?",
//                new String[]{String.valueOf(contact.getId())});
//        db.close();
    }
private static DatabaseHandler databaseHandler=null;
    public static DatabaseHandler getInstance(Context context,String type)
    {
        if (databaseHandler==null)
        {
            databaseHandler=new DatabaseHandler(context,type);
        }

        return databaseHandler;
    }
}