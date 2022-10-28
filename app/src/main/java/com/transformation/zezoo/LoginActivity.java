package com.transformation.zezoo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.Content;
import com.codekidlabs.storagechooser.StorageChooser;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mNameView;
    private EditText mPasswordView, mManagerView;
    private View mProgressView;
    private View mLoginFormView;
    TextInputLayout til_email, til_pass;
    Resources res;
    boolean isInternetAvailable;
    EditText edit_code, edit_code_;
    Button btn_request, btn_later, btn_enable, btn_exist_key;
    KeySQLDatabaseHandler dbk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        res = getResources();
        isInternetOn();
        // Set up the login form.
        mNameView = (AutoCompleteTextView) findViewById(R.id.point);
        til_email = (TextInputLayout) findViewById(R.id.til_point);
        til_pass = (TextInputLayout) findViewById(R.id.til_password);
        mPasswordView = (EditText) findViewById(R.id.password);
        mManagerView = (EditText) findViewById(R.id.manager);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNameView.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("basicName", mNameView.getText().toString()).commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("managerName", mManagerView.getText().toString()).commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("passwordName", mPasswordView.getText().toString()).commit();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("isLogin", true);
                    startActivity(i);
                    createDesktopShortcuts();
                    dbk = new KeySQLDatabaseHandler(LoginActivity.this);
                    dbk.addKey(new KeyItem(1, "", ""));
                } else {
                    mNameView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        /**if (!getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isActive", false)) {
            createEnableAppDialog();
        } else {
            Toast.makeText(getApplicationContext(), "البرنامج مفعل", Toast.LENGTH_SHORT).show();
        }**/

    }

    private void createDesktopShortcuts() {
        Intent shortcutIntent = new Intent(getApplicationContext(), BasicExportActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "تحويل سريع");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.dashboard_icon));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so   don't duplicate
        getApplicationContext().sendBroadcast(addIntent);

        Intent shortcutIntent1 = new Intent(getApplicationContext(), SpecialExportActivity.class);
        shortcutIntent1.setAction(Intent.ACTION_MAIN);
        Intent addIntent1 = new Intent();
        addIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent1);
        addIntent1.putExtra(Intent.EXTRA_SHORTCUT_NAME, "تحويل مخصص");
        addIntent1.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.stars_icon));
        addIntent1.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent1.putExtra("duplicate", false);  //may it's already there so   don't duplicate
        getApplicationContext().sendBroadcast(addIntent1);
    }

    private void createEnableAppDialog() {
        isInternetOn();
        final Dialog d = new Dialog(this);
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
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (isInternetAvailable) {
                    String text = "Sim Serial : " + String.valueOf(Long.valueOf(telephonyManager.getSimSerialNumber()) - 999999999).replaceAll("1", "A").replaceAll("3", "B").replaceAll("5", "C").replaceAll("7", "D").replaceAll("9", "E") + "" +
                            "                                               IMEI : " + String.valueOf(Long.valueOf(telephonyManager.getDeviceId()) - 999999999).replaceAll("0", "A").replaceAll("2", "B").replaceAll("4", "C").replaceAll("6", "D").replaceAll("8", "E");
                    try {
                        new SendMailAsynTask(LoginActivity.this, "Transform Activation:" + getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", "user"), text, "zezoocvi.77@gmail.com").execute();
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
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if ((edit_code.getText().toString().replaceAll("A", "1").replaceAll("B", "3").replaceAll("C", "5").replaceAll("D", "7").replaceAll("E", "9")).matches(String.valueOf(Long.valueOf(telephonyManager.getSimSerialNumber()) - 999999999))
                        && (edit_code_.getText().toString().replaceAll("A", "0").replaceAll("B", "2").replaceAll("C", "4").replaceAll("D", "6").replaceAll("E", "8")).matches(String.valueOf(Long.valueOf(telephonyManager.getDeviceId()) - 999999999))) {
                    getSharedPreferences("PREFERENCE", LoginActivity.this.MODE_PRIVATE).edit().putBoolean("isActive", true).commit();
                    Toast.makeText(getApplicationContext(), "#تم تنشيط البرنامج#", Toast.LENGTH_LONG).show();
                    dbk = new KeySQLDatabaseHandler(LoginActivity.this);
                    dbk.allKeys();
                    dbk.updateKey(new KeyItem(1, edit_code_.getText().toString(), edit_code.getText().toString()));
                    try {
                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
                        if (!f.exists())
                            f.mkdirs();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH", Locale.US);
                        Date now = new Date();
                        dbk.backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + formatter.format(now) + "_key.db");
                        Toast.makeText(LoginActivity.this, "تم الحفظ في ذاكرة الهاتف في مجلد Transform",  Toast.LENGTH_LONG).show();
                        d.cancel();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "حدث خطأ لم يتم الحفظ في الذاكرة!",  Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "خطأ في الأكواد!",  Toast.LENGTH_LONG).show();
                }

            }
        });
        btn_exist_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "يرجى اختيار ملف قاعدة البيانات التي تكون نهاية اسمه key.db",  Toast.LENGTH_LONG).show();
                Content c = new Content();
                c.setCreateLabel("اختيار الملف");
                c.setInternalStorageText("الذاكرة الداخلية");
                c.setCancelLabel("إلغاء");
                c.setSelectLabel("اختيار");
                c.setOverviewHeading("اختيار الذاكرة");
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(LoginActivity.this)
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
                            KeySQLDatabaseHandler k = new KeySQLDatabaseHandler(LoginActivity.this);
                            k.allKeys();
                            try {
                                k.importDB(path);
                                KeySQLDatabaseHandler k1 = new KeySQLDatabaseHandler(LoginActivity.this);
                                k1.allKeys();
                                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                if ((k1.getKey().getSimCode().replaceAll("A", "1").replaceAll("B", "3").replaceAll("C", "5").replaceAll("D", "7").replaceAll("E", "9")).matches(String.valueOf(Long.valueOf(telephonyManager.getSimSerialNumber()) - 999999999))
                                        || (k1.getKey().getImeiCode().replaceAll("A", "0").replaceAll("B", "2").replaceAll("C", "4").replaceAll("D", "6").replaceAll("E", "8")).matches(String.valueOf(Long.valueOf(telephonyManager.getDeviceId()) - 999999999))) {
                                    getSharedPreferences("PREFERENCE", LoginActivity.this.MODE_PRIVATE).edit().putBoolean("isActive", true).commit();
                                    Toast.makeText(getApplicationContext(), "#تم تنشيط البرنامج#",  Toast.LENGTH_LONG).show();
                                    d.cancel();
                                }
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "حدث خطأ لم تتم الإستعادة!",  Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "الملف غير صالح!",  Toast.LENGTH_LONG).show();
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

    public void isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // if connected with internet
            Toast.makeText(this, "يوجد اتصال بالإنترنت",  Toast.LENGTH_LONG).show();
            isInternetAvailable = true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, "لا يوجد اتصال بالإنترنت!",  Toast.LENGTH_LONG).show();
            isInternetAvailable = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeApplication();
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
}

