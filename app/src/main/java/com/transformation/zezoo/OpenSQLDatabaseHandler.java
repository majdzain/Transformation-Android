package com.transformation.zezoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OpenSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Opens_Database.db";
    private static String DATABASE_PATH = "";
    private static final String DATABASE_EX_PATH = "/Accountant/Databases/";
    private static final String TABLE_NAME = "Opens";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_OPEN="open";


    private static final String[] COLUMNS = {KEY_COLUMN, KEY_OPEN};

    String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "open TEXT )";
    SQLiteDatabase db;
    Context context;
    boolean isThereBackup;

    public OpenSQLDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(CREATION_TABLE);
        this.onCreate(db);
    }

    public void deleteOpen() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(1)});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
        } catch (Exception e) {

        }
    }

    public OpenItem getOpen() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                "colum = ?", // c. selections
                new String[]{String.valueOf(1)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
        OpenItem item = new OpenItem();
        item.setColumn(1);
        item.setOpen(cursor.getString(1));
        return item;
    }

    public List<OpenItem> allOpens() {
        List<OpenItem> items = new ArrayList<OpenItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        OpenItem item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new OpenItem();
                item.setColumn(cursor.getInt(0));
                item.setOpen(cursor.getString(1));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public void addOpen(OpenItem item) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, item.getColumn());
        values.put(KEY_OPEN, item.getOpen());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();

        } catch (Exception e) {

        }
    }

    public int updateOpen(OpenItem item) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, item.getColumn());
        values.put(KEY_OPEN, item.getOpen());
        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(item.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();

        } catch (Exception e) {

        }
        return i;
    }

    public void backup(String outFileName) throws IOException {
        //database path
        final String inFileName = context.getDatabasePath(DATABASE_NAME).toString();
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);
        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    public void importDB(String inFileName) throws IOException {
        final String outFileName = context.getDatabasePath(DATABASE_NAME).toString();
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);
        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
}
