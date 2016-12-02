package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WedDetailsManager";

    // Notes and Widgets table name
    private static final String TABLE_WED_DETAILS = "wed_details";
    // Notes Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAMES = "names";
    private static final String KEY_UNIQUE_ID = "unique_id";
    private static final String KEY_DATE_MAR = "date_mar";
    private static final String KEY_TYPE_VIEW_CREATED = "view_created_viewed";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_WED_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMES + " TEXT," + KEY_DATE_MAR + " TEXT,"
                + KEY_UNIQUE_ID + " TEXT," + KEY_TYPE_VIEW_CREATED + " TEXT" + ")";

        db.execSQL(CREATE_NOTES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new note
    public void addWedDetails(WedPojo wedDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAMES, wedDetails.getName());
            values.put(KEY_DATE_MAR, wedDetails.getDate());
            values.put(KEY_UNIQUE_ID, wedDetails.getId());
            values.put(KEY_TYPE_VIEW_CREATED, wedDetails.getType());

            // Inserting Row
            db.insert(TABLE_WED_DETAILS, null, values);
        } catch (SQLiteException e) {
            Log.d("check", e.getMessage());
        }
    }

  /*  // Getting single note
    public NoteDetails getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WED_DETAILS, new String[]{KEY_ID,
                        KEY_NAMES, KEY_DATE_MAR, KEY_UNIQUE_ID, KEY_TYPE_VIEW_CREATED, KEY_PENDING_INTENT_ID, KEY_FONT_SELECTED, KEY_SHOPPING_NOTE_STRING, KEY_TYPE_OF_NOTE, KEY_SHOPPING_TITLE,
                        KEY_TITLE_LOCK, KEY_LOCKED_OR_NOT, KEY_LOCKED_PASSWORD, KEY_WIDGET_ID_NOTES, KEY_PARAM_1, KEY_PARAM_2}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        NoteDetails note = null;
        if (cursor != null) {
            note = new NoteDetails(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)
                    , cursor.getString(9), cursor.getString(10), cursor.getInt(11), cursor.getString(12),
                    cursor.getInt(13), cursor.getString(14), cursor.getString(15));
            cursor.close();
        }
        // return note.

        return note;
    }*/

    // Getting All notes
    public List<WedPojo> getAllWedDetails() {
        List<WedPojo> wedDetailsPojo = new ArrayList<>();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_WED_DETAILS;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    WedPojo wedDetails = new WedPojo();
                    wedDetails.set_id(Integer.parseInt(cursor.getString(0)));
                    wedDetails.setName(cursor.getString(1));
                    wedDetails.setDate(cursor.getString(2));
                    wedDetails.setId(cursor.getString(3));
                    wedDetails.setType(cursor.getString(4));

                    // Adding notes to list
                    wedDetailsPojo.add(wedDetails);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.d("check", e.getMessage());
        }
        return wedDetailsPojo;
    }


    // Deleting single note
    public void deleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WED_DETAILS, KEY_UNIQUE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}