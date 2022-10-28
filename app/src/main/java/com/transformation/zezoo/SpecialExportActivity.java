package com.transformation.zezoo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.Content;
import com.codekidlabs.storagechooser.StorageChooser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SpecialExportActivity extends AppCompatActivity {
    TextView txt_user, txt_error;
    TextInputLayout til_pass;
    EditText edit_pass;
    Button btn_enter;
    Context context = this;
    Resources res;
    EditText edit_code, edit_code_;
    Button btn_request, btn_later, btn_enable, btn_exist_key;
    boolean isInternetAvailable;
    KeySQLDatabaseHandler dbk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        res = getResources();
        forceRTLIfSupported();
        lockIfThere();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (!getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isActive", false) && getSharedPreferences("PREFERENCE", MODE_PRIVATE).getLong("trialTransforms", Long.valueOf(telephonyManager.getDeviceId())) >= Long.valueOf(telephonyManager.getDeviceId()) + 35) {
            createEnableAppDialog();
            closeApplication();
        }
        OpenSQLDatabaseHandler dbo1 = new OpenSQLDatabaseHandler(context);
        dbo1.allOpens();
        if(!getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isActive", false) ) {
            try {
                dbo1.importDB(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "open.db");
                OpenSQLDatabaseHandler o = new OpenSQLDatabaseHandler(context);
                o.allOpens();
                if (!getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isActive", false) && Long.valueOf(o.getOpen().getOpen()) >= Long.valueOf(telephonyManager.getDeviceId()) + 35) {
                    createEnableAppDialog();
                    closeApplication();
                }
            } catch (Exception e) {
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "open.db");
                if (f.exists()) {
                    Toast.makeText(context, "حدث خطأ لم تتم الإستعادة!", 2000).show();
                    closeApplication();
                }
            }
        }
        isAccessibilityEnabled(this);
        Fragment fragment = new BasicActivity();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSpecial", true);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            closeApplication();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "اضغط مرة أخرى للخروج", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void closeApplication() {
        finish();
        finishAffinity();
        moveTaskToBack(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void lockIfThere() {
        if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("passwordName", "").matches(".*[a-zA-Z0-9أ-ي]+.*")) {
            final Dialog d = new Dialog(context);
            d.setContentView(R.layout.dialog_lock);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            txt_user = (TextView) d.findViewById(R.id.txt_user);
            txt_error = (TextView) d.findViewById(R.id.txt_error);
            edit_pass = (EditText) d.findViewById(R.id.password);
            til_pass = (TextInputLayout) d.findViewById(R.id.til_password);
            btn_enter = (Button) d.findViewById(R.id.email_sign_in_button);
            txt_user.setText(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", ""));
            edit_pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    txt_error.setText("يرجى إدخال كلمة السر للدخول");
                    txt_error.setTextColor(res.getColor(R.color.spec_black));
                    edit_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            btn_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edit_pass.getText().toString().matches(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("passwordName", ""))) {
                        d.cancel();
                    } else {
                        txt_error.setText("كلمة السر خاطئة!");
                        txt_error.setTextColor(res.getColor(R.color.red));
                        edit_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                    }
                }
            });
            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.show();
        }
    }

    private boolean isAccessibilityEnabled(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + USSDService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v("Transform", "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("Transform", "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v("Transform", "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v("Transform", "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v("Transform", "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v("Transform", "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    public void isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // if connected with internet
            Toast.makeText(this, "يوجد اتصال بالإنترنت", 1500).show();
            isInternetAvailable = true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, "لا يوجد اتصال بالإنترنت!", 1500).show();
            isInternetAvailable = false;
        }
    }

    private void createEnableAppDialog() {
isInternetOn();
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_buy_app);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        edit_code = (EditText) d.findViewById(R.id.edit_code);
        edit_code_ = (EditText) d.findViewById(R.id.edit_code_);
        btn_request = (Button) d.findViewById(R.id.btn_request);
        btn_later = (Button) d.findViewById(R.id.btn_later);
        btn_enable = (Button) d.findViewById(R.id.btn_enable);
        btn_exist_key = (Button) d.findViewById(R.id.btn_exist_key);

        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edit_code.getText().toString().matches(".*[0-9]+.*") && edit_code_.getText().toString().matches(".*[0-9]+.*")) {
                    btn_enable.setEnabled(true);
                    btn_enable.setTextColor(res.getColor(R.color.purple));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edit_code_.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edit_code.getText().toString().matches(".*[0-9]+.*") && edit_code_.getText().toString().matches(".*[0-9]+.*")) {
                    btn_enable.setEnabled(true);
                    btn_enable.setTextColor(res.getColor(R.color.purple));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (isInternetAvailable) {
                    String text = "Sim Serial : " + String.valueOf(Long.valueOf(telephonyManager.getSimSerialNumber()) - 999999999).replaceAll("1", "A").replaceAll("3", "B").replaceAll("5", "C").replaceAll("7", "D").replaceAll("9", "E") + "" +
                            "                                               IMEI : " + String.valueOf(Long.valueOf(telephonyManager.getDeviceId()) - 999999999).replaceAll("0", "A").replaceAll("2", "B").replaceAll("4", "C").replaceAll("6", "D").replaceAll("8", "E");
                    try {
                        new SendMailAsynTask(SpecialExportActivity.this, "Transform Activation:" + getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", ""), text, "zezoocvi.77@gmail.com").execute();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "لم يتم إرسال طلب التفعيل يرجى المحاولة لاحقا", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "لا يمكن التفعيل, يجب الاتصال بالإنترنت", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if ((edit_code.getText().toString().replaceAll("A", "1").replaceAll("B", "3").replaceAll("C", "5").replaceAll("D", "7").replaceAll("E", "9")).matches(String.valueOf(Long.valueOf(telephonyManager.getSimSerialNumber()) - 999999999))
                        || (edit_code_.getText().toString().replaceAll("A", "0").replaceAll("B", "2").replaceAll("C", "4").replaceAll("D", "6").replaceAll("E", "8")).matches(String.valueOf(Long.valueOf(telephonyManager.getDeviceId()) - 999999999))) {
                    getSharedPreferences("PREFERENCE", context.MODE_PRIVATE).edit().putBoolean("isActive", true).commit();
                    Toast.makeText(getApplicationContext(), "#تم تنشيط البرنامج#", 2000).show();
                    dbk = new KeySQLDatabaseHandler(SpecialExportActivity.this);
                    dbk.allKeys();
                    dbk.updateKey(new KeyItem(1, edit_code_.getText().toString(), edit_code.getText().toString()));
                    KeySQLDatabaseHandler k = new KeySQLDatabaseHandler(context);
                    k.allKeys();
                    try {
                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
                        if (!f.exists())
                            f.mkdirs();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH", Locale.US);
                        Date now = new Date();
                        k.backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + formatter.format(now) + "_key.db");
                        Toast.makeText(context, "تم الحفظ في ذاكرة الهاتف في مجلد Transform", 4000).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "حدث خطأ لم يتم الحفظ في الذاكرة!", 4000).show();
                    }
                    d.cancel();
                } else {
                    Toast.makeText(getApplicationContext(), "خطأ في الأكواد!", 800).show();
                }
            }
        });
        btn_exist_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "يرجى اختيار ملف قاعدة البيانات التي تكون نهاية اسمه key.db", 15000).show();
                Content c = new Content();
                c.setCreateLabel("اختيار الملف");
                c.setInternalStorageText("الذاكرة الداخلية");
                c.setCancelLabel("إلغاء");
                c.setSelectLabel("اختيار");
                c.setOverviewHeading("اختيار الذاكرة");
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(SpecialExportActivity.this)
                        .withFragmentManager(getFragmentManager()).withContent(c)
                        .withMemoryBar(true)
                        .allowCustomPath(true)
                        .setType(StorageChooser.FILE_PICKER).disableMultiSelect()
                        .build();
                // Show dialog whenever you want by
                chooser.show();
                // get path that the user has chosen
                chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
                    @Override
                    public void onSelect(String path) {
                        if (path.contains("key.db")) {
                            KeySQLDatabaseHandler k = new KeySQLDatabaseHandler(context);
                            k.allKeys();
                            try {
                                k.importDB(path);
                                KeySQLDatabaseHandler k1 = new KeySQLDatabaseHandler(context);
                                k1.allKeys();
                                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                if ((k1.getKey().getSimCode().replaceAll("A", "1").replaceAll("B", "3").replaceAll("C", "5").replaceAll("D", "7").replaceAll("E", "9")).matches(String.valueOf(Long.valueOf(telephonyManager.getSimSerialNumber()) - 999999999))
                                        || (k1.getKey().getImeiCode().replaceAll("A", "0").replaceAll("B", "2").replaceAll("C", "4").replaceAll("D", "6").replaceAll("E", "8")).matches(String.valueOf(Long.valueOf(telephonyManager.getDeviceId()) - 999999999))) {
                                    getSharedPreferences("PREFERENCE", context.MODE_PRIVATE).edit().putBoolean("isActive", true).commit();
                                    Toast.makeText(getApplicationContext(), "#تم تنشيط البرنامج#", 2000).show();
                                    d.cancel();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ لم تتم الإستعادة!", 2000).show();
                            }
                        } else {
                            Toast.makeText(context, "الملف غير صالح!", 2000).show();
                        }
                    }
                });
            }
        });
        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
            }
        });
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if(num.startsWith("+"))
                                    num = "0" + num.substring(num.indexOf("963") + 3);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isSpecial", true);
                                bundle.putString("phone", num);
                                Fragment fragment = new BasicActivity();
                                fragment.setArguments(bundle);
                                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.fragment, fragment);
                                ft.commit();
                            }
                        }
                    }
                    break;
                }
        }
    }
}
