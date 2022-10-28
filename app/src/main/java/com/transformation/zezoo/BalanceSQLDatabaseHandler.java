package com.transformation.zezoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.Settings;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class BalanceSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Balances_Database.db";
    private static String DATABASE_PATH = "";
    private static final String DATABASE_EX_PATH = "/Accountant/Databases/";
    private static final String TABLE_NAME = "Balances";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_PROFITS = "profits";
    private static final String KEY_DEBT = "debt";
    private static final String KEY_SYRBALANCE = "syr_balance";
    private static final String KEY_SYRBALANCE_ = "syr_balance_";
    private static final String KEY_MTNBALANCE = "mtn_balance";
    private static final String KEY_MTNBALANCE_ = "mtn_balance_";


    private static final String[] COLUMNS = {KEY_COLUMN, KEY_BALANCE, KEY_PROFITS, KEY_DEBT, KEY_SYRBALANCE, KEY_SYRBALANCE_, KEY_MTNBALANCE, KEY_MTNBALANCE_};

    String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "balance TEXT, " + "profits TEXT, " + "debt TEXT, " + "syr_balance TEXT, " + "syr_balance_ TEXT, " + "mtn_balance TEXT, "+ "mtn_balance_ TEXT )";
    SQLiteDatabase db;
    Context context;
    boolean isThereBackup;

    public BalanceSQLDatabaseHandler(Context context) {
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

    public void deleteBalance() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "colum = ?", new String[]{String.valueOf(1)});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "balance.db");
        } catch (Exception e) {

        }
    }

    public BalanceItem getBalance() {
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
        BalanceItem item = new BalanceItem();
        item.setColumn(1);
        item.setBalance(cursor.getString(1));
        item.setProfits(cursor.getString(2));
        item.setDebt(cursor.getString(3));
        item.setSyrBalance(cursor.getString(4));
        item.setSyrBalance_(cursor.getString(5));
        item.setMtnBalance(cursor.getString(6));
        item.setMtnBalance_(cursor.getString(7));
        return item;
    }

    public List<BalanceItem> allBalances() {
        List<BalanceItem> items = new ArrayList<BalanceItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        BalanceItem item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new BalanceItem();
                item.setColumn(cursor.getInt(0));
                item.setBalance(cursor.getString(1));
                item.setProfits(cursor.getString(2));
                item.setDebt(cursor.getString(3));
                item.setSyrBalance(cursor.getString(4));
                item.setSyrBalance_(cursor.getString(5));
                item.setMtnBalance(cursor.getString(6));
                item.setMtnBalance_(cursor.getString(7));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    public void addBalance(BalanceItem item) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, item.getColumn());
        values.put(KEY_BALANCE, item.getBalance());
        values.put(KEY_PROFITS, item.getProfits());
        values.put(KEY_DEBT, item.getDebt());
        values.put(KEY_SYRBALANCE, item.getSyrBalance());
        values.put(KEY_SYRBALANCE_, item.getSyrBalance_());
        values.put(KEY_MTNBALANCE, item.getMtnBalance());
        values.put(KEY_MTNBALANCE_, item.getMtnBalance_());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "balance.db");
        } catch (Exception e) {

        }
    }

    public int updateBalance(BalanceItem item) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, item.getColumn());
        values.put(KEY_BALANCE, item.getBalance());
        values.put(KEY_PROFITS, item.getProfits());
        values.put(KEY_DEBT, item.getDebt());
        values.put(KEY_SYRBALANCE, item.getSyrBalance());
        values.put(KEY_SYRBALANCE_, item.getSyrBalance_());
        values.put(KEY_MTNBALANCE, item.getMtnBalance());
        values.put(KEY_MTNBALANCE_, item.getMtnBalance_());
        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "colum = ?", // selections
                new String[]{String.valueOf(item.getColumn())});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "balance.db");
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
