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

public class CustomerSQLDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Customers_Database.db";
    private static String DATABASE_PATH = "";
    private static final String DATABASE_EX_PATH = "/Accountant/Databases/";
    private static final String TABLE_NAME = "Customers";
    private static final String KEY_COLUMN = "colum";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_CODE = "code";
    private static final String KEY_MODE = "mode";

    private static final String[] COLUMNS = {KEY_COLUMN, KEY_NAME, KEY_NUMBER, KEY_AMOUNT, KEY_BALANCE, KEY_CODE,KEY_MODE};

    String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + "colum INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "number TEXT, " + "amount TEXT, " + "balance TEXT, "+ "code TEXT, " + "mode TEXT )";
    SQLiteDatabase db;
    Context context;
    boolean isThereBackup;

    public CustomerSQLDatabaseHandler(Context context) {
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

    public void deleteCustomer(CustomerListChildItem customer) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.delete(TABLE_NAME, "number = ?", new String[]{customer.getNumber()});
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "customers.db");
        }catch(Exception e){

        }
    }

    public CustomerListChildItem getCustomer(String Number) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " number = ?", // c. selections
                new String[]{Number}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null)
            cursor.moveToFirst();
        CustomerListChildItem customer = new CustomerListChildItem();
        customer.setColumn(cursor.getInt(0));
        customer.setName(cursor.getString(1));
        customer.setNumber(cursor.getString(2));
        customer.setAmount(cursor.getString(3));
        customer.setBalance(cursor.getString(4));
        customer.setCode(cursor.getString(5));
        customer.setMode(cursor.getString(6));
        return customer;
    }

    public List<CustomerListChildItem> allCustomers() {
        List<CustomerListChildItem> customers = new ArrayList<CustomerListChildItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        CustomerListChildItem customer = null;
        if (cursor.moveToFirst()) {
            do {
                customer = new CustomerListChildItem();
                customer.setColumn(cursor.getInt(0));
                customer.setName(cursor.getString(1));
                customer.setNumber(cursor.getString(2));
                customer.setAmount(cursor.getString(3));
                customer.setBalance(cursor.getString(4));
                customer.setCode(cursor.getString(5));
                customer.setMode(cursor.getString(6));
                customers.add(customer);
            } while (cursor.moveToNext());
        }
        return customers;
    }

    public void addCustomer(CustomerListChildItem customer) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, customer.getColumn());
        values.put(KEY_NAME, customer.getName());
        values.put(KEY_NUMBER, customer.getNumber());
        values.put(KEY_AMOUNT, customer.getAmount());
        values.put(KEY_BALANCE, customer.getBalance());
        values.put(KEY_CODE, customer.getCode());
        values.put(KEY_MODE, customer.getMode());
        // insert
        db.insert(TABLE_NAME, null, values);
        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "customers.db");
        }catch(Exception e){

        }
    }

    public int updateCustomer(CustomerListChildItem customer) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN, customer.getColumn());
        values.put(KEY_NAME, customer.getName());
        values.put(KEY_NUMBER, customer.getNumber());
        values.put(KEY_AMOUNT, customer.getAmount());
        values.put(KEY_BALANCE, customer.getBalance());
        values.put(KEY_CODE, customer.getCode());
        values.put(KEY_MODE, customer.getMode());
        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "name = ?", // selections
                new String[]{customer.getName()});

        db.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
            if (!f.exists())
                f.mkdirs();
            backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + "customers.db");
        }catch(Exception e){

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
