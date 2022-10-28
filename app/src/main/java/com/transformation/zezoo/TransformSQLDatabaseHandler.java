package com.transformation.zezoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TransformSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Transforms_Database.db";
    private static String DATABASE_PATH = "";
    private static final String DATABASE_EX_PATH = "/Accountant/Databases/";
    private static final String TABLE_NAME = "Transforms";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";
    private static final String KEY_CODE = "code";
    private static final String KEY_DEBT = "debt";

    private static final String[] COLUMNS = {KEY_COLUMN, KEY_NAME, KEY_NUMBER, KEY_AMOUNT, KEY_DATE, KEY_CODE, KEY_DEBT};

    SQLiteDatabase db;
    Context context;
    String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "number TEXT, " + "amount TEXT, " + "date TEXT, " + "code TEXT, " + "debt TEXT )";

    public TransformSQLDatabaseHandler(Context context) {
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

    public void deleteTransform(TransformListChildItem transform) {
        // Get reference to writable DB
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(transform.getColumn())});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "transforms.db");
        } catch (Exception e) {

        }
    }

    public TransformListChildItem getTransform(int Column) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " colum = ?", // c. selections
                new String[]{String.valueOf(Column)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();

        TransformListChildItem transform = new TransformListChildItem();
        transform.setColumn(cursor.getInt(0));
        transform.setName(cursor.getString(1));
        transform.setNumber(cursor.getString(2));
        transform.setAmount(cursor.getString(3));
        transform.setDate(cursor.getString(4));
        transform.setCode(cursor.getString(5));
        transform.setDebt(cursor.getString(6));
        return transform;
    }

    public List<TransformListChildItem> allTransforms() {
        List<TransformListChildItem> transforms = new ArrayList<TransformListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        TransformListChildItem transform = null;
        if (cursor.moveToFirst()) {
            do {
                transform = new TransformListChildItem();
                transform.setColumn(cursor.getInt(0));
                transform.setName(cursor.getString(1));
                transform.setNumber(cursor.getString(2));
                transform.setAmount(cursor.getString(3));
                transform.setDate(cursor.getString(4));
                transform.setCode(cursor.getString(5));
                transform.setDebt(cursor.getString(6));
                transforms.add(transform);
            } while (cursor.moveToNext());
        }
        return transforms;
    }

    public List<String> allFolders() {
        List<String> folders = new ArrayList<String>();
        HashMap<Integer, TransformListChildItem> transformsWithoutGroup = new HashMap<Integer, TransformListChildItem>();
        String query1 = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db1 = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor cursor = db1.rawQuery(query1, null);
        TransformListChildItem transform = null;
        String folder = null;
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).matches(""))
                    folder = cursor.getString(2);
                else
                    folder = cursor.getString(1);
                if (!folders.contains(folder)) {
                    folders.add(folder);
                }
            } while (cursor.moveToNext());
        }
        return folders;
    }

    public void addTransform(TransformListChildItem transform) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, transform.getColumn());
        values.put(KEY_NAME, transform.getName());
        values.put(KEY_NUMBER, transform.getNumber());
        values.put(KEY_AMOUNT, transform.getAmount());
        values.put(KEY_DATE, transform.getDate());
        values.put(KEY_CODE, transform.getCode());
        values.put(KEY_DEBT, transform.getDebt());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "transforms.db");
        } catch (Exception e) {

        }
    }

    public int updateTransform(TransformListChildItem transform) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, transform.getColumn());
        values.put(KEY_NAME, transform.getName());
        values.put(KEY_NUMBER, transform.getNumber());
        values.put(KEY_AMOUNT, transform.getAmount());
        values.put(KEY_DATE, transform.getDate());
        values.put(KEY_CODE, transform.getCode());
        values.put(KEY_DEBT, transform.getDebt());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(transform.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "transforms.db");
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
