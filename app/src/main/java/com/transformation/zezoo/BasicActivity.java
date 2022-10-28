package com.transformation.zezoo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;

public class BasicActivity extends Fragment implements AdapterView.OnItemClickListener {

    View view;
    Resources res;
    Context context;
    ImageButton mtn, syriatel;
    CustomAutoCompleteTextView phone, cus_code;
    EditText money, balance, repet;
    Button prepaid, postpaid, transformBtn, checkBtn;
    TextView dialog_txt;
    LinearLayout linear_quick_, linear_operation;
    Spinner spin_type, spin_operation, spin_operation_;
    RadioGroup rg_s, rg_m;
    RadioButton rb_m_1, rb_m_2, rb_s_1, rb_s_2;
    EditText code_syr, code_mtn, code_syr_, code_mtn_adsl, code_pre_syr, code_check_pre_syr, code_post_syr, code_check_post_syr, code_pre_post_who_syr, code_pre_mtn, code_check_pre_mtn, code_post_mtn, code_pre_who_mtn, code_post_who_mtn, code_post_adsl_mtn, code_check;
    Button next, later, previous, save;
    RelativeLayout relative_special;
    LinearLayout panel, linear_quick;
    TelephonyInfo telephonyInfo;
    SharedPreferences sharedPreferences;
    boolean mtnExist;
    boolean syriatelExist;
    int mtnSim;
    int syriatelSim;
    int mainNetwork;
    TextView tv1, tv2, tv3, tv4, tv = null, tv_pre_syr, tv_post_syr, tv_pre_mtn, tv_post_mtn;
    final static int REQUEST_READ_PHONE_STATE = 1;
    boolean isSetInitialBalanceEditText = false;
    boolean isSetInitialMoneyEditText = false;
    String type = "نقدي";
    private TransformSQLDatabaseHandler db;
    String currentName = "";
    String currentNumber;
    String currentAmount;
    String currentDate;
    boolean isCheckedManually = false;
    boolean isSpecial = false;
    public static final String BROADCAST_ACTION = "com.broadcast";
    String mode;
    int simType;
    CustomerSQLDatabaseHandler dbc;
    EditText edit_number, edit_name, edit_code;
    Button btn_save, btn_close;
    LinearLayout addNewBtn;
    private String currentCode, mode_ = "";
    int repeatNum = 1;
    BalanceSQLDatabaseHandler dbb;
    ArrayList<CustomerListChildItem> listAllCustomers;
    CustomCustomerEditArrayAdapter customCustomerEditArrayAdapter;
    ArrayList<CustomerListChildItem> listAllCustomersPanel;
    CustomCustomerEditArrayAdapter customCustomerEditArrayAdapterPanel;
    ViewPager viewPager;
    PanelFragment panelFragment;
    ListFragment listFragment;
    ViewPagerAdapter vpa;
    ListView customersList;
    private boolean isSyrWhoPre;
    int syr_index = 1;
    int mtn_index = 2;
    MovableFloatingActionButton balance_info_floating;
    boolean isTransform = false;
    TextView txt_hint;
    boolean isCheckingSyrActive = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_basic, container, false);
        context = view.getContext();
        res = context.getResources();
        isSpecial = getArguments().getBoolean("isSpecial");
        setHasOptionsMenu(true);
        setVaribales();
        setupAsFirst();
        setClicks();
        registerMyReceiver();
        createSearchLists();
        phone.setText(getArguments().getString("phone", ""));
        refreshSyrPreData();
        hideKeyboard();
        return view;
    }

    private void createSearchLists() {
        listAllCustomers = new ArrayList<>();
        customCustomerEditArrayAdapter = new CustomCustomerEditArrayAdapter(context, R.layout.list_customer_edit_item, listAllCustomers);
        phone.setAdapter(customCustomerEditArrayAdapter);
        cus_code.setAdapter(customCustomerEditArrayAdapter);
        phone.setOnItemClickListener(this);
        cus_code.setOnItemClickListener(this);
    }

    private void setupAsFirst() {
        tv1 = new TextView(context);
        tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv1.setTextSize(20);
        tv1.setTextColor(res.getColor(R.color.spec_black));
        tv1.setText(" - التأكد من الإعدادات");
        panel.addView(tv1);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv1.setText(tv1.getText() + ".");
            }
        }, 500);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv1.setText(tv1.getText() + ".");
            }
        }, 1000);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv1.setText(tv1.getText() + ".");
                if (sharedPreferences.getBoolean("isSettled", false)) {
                    setMtnSim();
                } else {
                    createSetupDialog1("", "", "", "", false);
                }
            }
        }, 1500);
    }

    private void createSetupDialog1(String s, String s1, String s2, String s3, boolean b) {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_setup);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rg_s = (RadioGroup) d.findViewById(R.id.radio_group_s);
        rg_m = (RadioGroup) d.findViewById(R.id.radio_group_m);
        rb_m_1 = (RadioButton) d.findViewById(R.id.radio_sim_1_m);
        rb_m_2 = (RadioButton) d.findViewById(R.id.radio_sim_2_m);
        rb_s_1 = (RadioButton) d.findViewById(R.id.radio_sim_1_s);
        rb_s_2 = (RadioButton) d.findViewById(R.id.radio_sim_2_s);
        code_syr = (EditText) d.findViewById(R.id.code_syriatel);
        code_syr_ = (EditText) d.findViewById(R.id.code_syriatel_);
        code_mtn = (EditText) d.findViewById(R.id.code_mtn);
        code_mtn_adsl = (EditText) d.findViewById(R.id.code_adsl_mtn);
        later = (Button) d.findViewById(R.id.btn_later);
        next = (Button) d.findViewById(R.id.btn_next);
        if (b) {
            code_syr.setText(s);
            code_syr_.setText(s1);
            code_mtn.setText(s2);
            code_mtn_adsl.setText(s3);
        }
        rb_m_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg_m.getCheckedRadioButtonId() == R.id.radio_sim_1_m)
                    rg_s.check(R.id.radio_sim_2_s);
                else
                    rg_s.check(R.id.radio_sim_1_s);
            }
        });
        rb_m_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg_m.getCheckedRadioButtonId() == R.id.radio_sim_2_m)
                    rg_s.check(R.id.radio_sim_1_s);
                else
                    rg_s.check(R.id.radio_sim_2_s);
            }
        });
        rb_s_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg_s.getCheckedRadioButtonId() == R.id.radio_sim_1_s)
                    rg_m.check(R.id.radio_sim_2_m);
                else
                    rg_m.check(R.id.radio_sim_1_m);

            }
        });
        rb_s_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rg_s.getCheckedRadioButtonId() == R.id.radio_sim_2_s)
                    rg_m.check(R.id.radio_sim_1_m);
                else
                    rg_m.check(R.id.radio_sim_2_m);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_syr_.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_mtn_adsl.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                    d.cancel();
                    if (rg_m.getCheckedRadioButtonId() == R.id.radio_sim_1_m)
                        mtn_index = 1;
                    else
                        mtn_index = 2;
                    if (rg_s.getCheckedRadioButtonId() == R.id.radio_sim_1_s)
                        syr_index = 1;
                    else
                        syr_index = 2;
                    createSetupDialog2(code_syr.getText().toString(), code_syr_.getText().toString(), code_mtn.getText().toString(), code_mtn_adsl.getText().toString(), mtn_index, syr_index);
                } else {
                    if (!code_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_syr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_mtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_syr_.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_syr_.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_mtn_adsl.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_mtn_adsl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                }
            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean("isSettled", false).commit();
                d.cancel();
            }
        });
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    private void createSetupDialog2(final String s, final String s1, final String s2, final String s3, final int mtn_sim_count, final int syriatel_sim_count) {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_setup_advanced);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        code_pre_syr = (EditText) d.findViewById(R.id.code_pre_syr);
        code_check_pre_syr = (EditText) d.findViewById(R.id.code_check_pre_syr);
        code_post_syr = (EditText) d.findViewById(R.id.code_post_syr);
        code_check_post_syr = (EditText) d.findViewById(R.id.code_check_post_syr);
        code_pre_post_who_syr = (EditText) d.findViewById(R.id.code_pre_post_who_syr);
        code_pre_mtn = (EditText) d.findViewById(R.id.code_pre_mtn);
        code_check_pre_mtn = (EditText) d.findViewById(R.id.code_check_pre_mtn);
        code_post_mtn = (EditText) d.findViewById(R.id.code_post_mtn);
        code_pre_who_mtn = (EditText) d.findViewById(R.id.code_pre_who_mtn);
        code_post_who_mtn = (EditText) d.findViewById(R.id.code_post_who_mtn);
        code_post_adsl_mtn = (EditText) d.findViewById(R.id.code_post_adsl_mtn);
        code_check = (EditText) d.findViewById(R.id.code_check_);
        previous = (Button) d.findViewById(R.id.btn_previous);
        save = (Button) d.findViewById(R.id.btn_save);
        String t_code_pre_syr = "<font color=#0b3b39>*150*1*</font><font color=#FE2E2E>" + s + "</font><font color=#0b3b39>*1*M*N*N#</font>";
        String t_code_check_pre_syr = "<font color=#0b3b39>*150*2*</font><font color=#FE2E2E>" + s1 + "</font><font color=#0b3b39>*</font><font color=#FE2E2E>" + s + "</font><font color=#0b3b39>*1#</font>";
        String t_code_post_syr = "<font color=#0b3b39>*150*1*</font><font color=#FE2E2E>" + s + "</font><font color=#0b3b39>*5*M*N*N#</font>";
        String t_code_check_post_syr = "<font color=#0b3b39>*150*2*</font><font color=#FE2E2E>" + s1 + "</font><font color=#0b3b39>*</font><font color=#FE2E2E>" + s + "</font><font color=#0b3b39>*5#</font>";
        String t_code_pre_post_who_syr = "<font color=#0b3b39>*150*3*</font><font color=#FE2E2E>" + s1 + "</font><font color=#0b3b39>*</font><font color=#FE2E2E>" + s + "</font><font color=#0b3b39>*C*C*1*M#</font>";
        String t_code_pre_mtn = "<font color=#0b3b39>*150*</font><font color=#FFBF00>" + s2 + "</font><font color=#0b3b39>*N*M#</font>";
        String t_code_check_pre_mtn = "<font color=#0b3b39>*155*1#</font>";
        String t_code_post_mtn = "<font color=#0b3b39>*154*</font><font color=#FFBF00>" + s2 + "</font><font color=#0b3b39>*N*M#</font>";
        String t_code_pre_who_mtn = "<font color=#0b3b39>*150*</font><font color=#FFBF00>" + s2 + "</font><font color=#0b3b39>*C*N*M#</font>";
        String t_code_post_who_mtn = "<font color=#0b3b39>*154*</font><font color=#FFBF00>" + s2 + "</font><font color=#0b3b39>*C*N*M#</font>";
        String t_code_post_adsl_mtn = "<font color=#0b3b39>*160*</font><font color=#FFBF00>" + s3 + "</font><font color=#0b3b39>*963N*M#</font>";
        String t_code_check = "<font color=#0b3b39>*134*1*2*N#</font>";
        code_pre_syr.setText(Html.fromHtml(t_code_pre_syr));
        code_check_pre_syr.setText(Html.fromHtml(t_code_check_pre_syr));
        code_post_syr.setText(Html.fromHtml(t_code_post_syr));
        code_check_post_syr.setText(Html.fromHtml(t_code_check_post_syr));
        code_pre_mtn.setText(Html.fromHtml(t_code_pre_mtn));
        code_check_pre_mtn.setText(Html.fromHtml(t_code_check_pre_mtn));
        code_post_mtn.setText(Html.fromHtml(t_code_post_mtn));
        code_pre_post_who_syr.setText(Html.fromHtml(t_code_pre_post_who_syr));
        code_pre_who_mtn.setText(Html.fromHtml(t_code_pre_who_mtn));
        code_post_who_mtn.setText(Html.fromHtml(t_code_post_who_mtn));
        code_post_adsl_mtn.setText(Html.fromHtml(t_code_post_adsl_mtn));
        code_check.setText(Html.fromHtml(t_code_check));

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                createSetupDialog1(s, s1, s2, s3, true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code_pre_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_check_pre_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_post_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_check_post_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_pre_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_check_pre_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_post_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_post_adsl_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                    sharedPreferences.edit().putBoolean("isSettled", true).commit();
                    sharedPreferences.edit().putInt("mtn_sim_count", mtn_sim_count).commit();
                    sharedPreferences.edit().putInt("syr_sim_count", syriatel_sim_count).commit();
                    sharedPreferences.edit().putString("s", s).commit();
                    sharedPreferences.edit().putString("s1", s1).commit();
                    sharedPreferences.edit().putString("s2", s2).commit();
                    sharedPreferences.edit().putString("s3", s3).commit();
                    sharedPreferences.edit().putString("code_pre_syr", code_pre_syr.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_check_pre_syr", code_check_pre_syr.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_post_syr", code_post_syr.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_check_post_syr", code_check_post_syr.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_pre_mtn", code_pre_mtn.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_check_pre_mtn", code_check_pre_mtn.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_post_mtn", code_post_mtn.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_pre_post_who_syr", code_pre_post_who_syr.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_pre_who_mtn", code_pre_who_mtn.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_post_who_mtn", code_post_who_mtn.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_post_adsl_mtn", code_post_adsl_mtn.getText().toString()).commit();
                    sharedPreferences.edit().putString("code_check", code_check.getText().toString()).commit();
                    syriatelSim = syriatel_sim_count;
                    mtnSim = mtn_sim_count;
                    setMtnSim();
                    d.cancel();
                } else {
                    if (!code_pre_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_pre_syr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_check_pre_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_check_pre_syr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_post_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_post_syr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_check_post_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_check_post_syr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_pre_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_pre_mtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_check_pre_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_check_pre_mtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_post_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_post_mtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if (!code_post_adsl_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*"))
                        code_post_adsl_mtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                }
            }
        });
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    private void setMtnSim() {
        mtnSim = sharedPreferences.getInt("mtn_sim_count", 2);
        syriatelSim = sharedPreferences.getInt("syr_sim_count", 1);
        tv2 = new TextView(context);
        tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTextSize(18);
        tv2.setTextColor(res.getColor(R.color.spec_black));
        tv2.setText(" - تحديد بطاقة MTN (" + String.valueOf(mtnSim) + ")");
        panel.addView(tv2);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv2.setText(tv2.getText() + ".");
            }
        }, 500);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv2.setText(tv2.getText() + ".");
            }
        }, 1000);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv2.setText(tv2.getText() + ".");
                boolean isAirplaneModeEnabled = true;
                try {
                    isAirplaneModeEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
                    Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
                    Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                    intent.putExtra("state", !isAirplaneModeEnabled);
                    context.sendBroadcast(intent);
                } catch (Exception e) {
                    if (isAirplaneModeEnabled) {
                        Toast.makeText(context, "الرجاء إيقاف وضع الطيران وتشغيل البرنامج من جديد", Toast.LENGTH_LONG).show();
                    }
                }
                if (mtnSim == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "الرجاء السماح للتطبيق بالحصول على معلومات الاتصال", Toast.LENGTH_SHORT).show();
                            mtnExist = false;
                        } else {
                            if (isSim1Available() && isSim1Mtn())
                                mtnExist = true;
                            else
                                mtnExist = false;
                        }
                    } else {
                        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (telephonyManager.getSimSerialNumber() != null) {
                            mtnExist = true;
                            syriatelExist = true;
                        } else {
                            mtnExist = false;
                            syriatelExist = false;
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "الرجاء السماح للتطبيق بالحصول على معلومات الاتصال", Toast.LENGTH_SHORT).show();
                            mtnExist = false;
                        } else {
                            if (isSim2Available() && isSim2Mtn())
                                mtnExist = true;
                            else
                                mtnExist = false;
                        }
                    } else {
                        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (telephonyManager.getSimSerialNumber() != null) {
                            mtnExist = true;
                            syriatelExist = true;
                        } else {
                            mtnExist = false;
                            syriatelExist = false;
                        }
                    }
                }
                if (mtnExist == true) {
                    mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                    mtn.setTag("n");
                    tv2.setText(tv2.getText() + "مفعلة");
                } else {
                    mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo_hiddin));
                    mtn.setTag("h");
                    tv2.setText(tv2.getText() + "غير مفعلة");
                    Toast.makeText(context, "بطاقة MTN غير مفعلة!", 700).show();
                }
                setSyriatelSim();
            }
        }, 1500);
    }

    private boolean isSim1Mtn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
            String carrier = infoSim1.getCarrierName().toString();
            if (carrier.matches("MTN")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSim2Mtn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
            String carrier = infoSim2.getCarrierName().toString();
            if (carrier.matches("MTN")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSim1Syr() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
            String carrier = infoSim1.getCarrierName().toString();
            if (carrier.matches("Syriatel") || carrier.matches("SYRIATEL")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSim2Syr() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
            String carrier = infoSim2.getCarrierName().toString();
            if (carrier.matches("Syriatel") || carrier.matches("SYRIATEL")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSim2Available() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
            if (infoSim2 != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isSim1Available() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
            if (infoSim1 != null) {
                return true;
            }
        }
        return false;
    }

    private void setSyriatelSim() {

        tv3 = new TextView(context);
        tv3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv3.setGravity(Gravity.RIGHT);
        tv3.setText(" - تحديد بطاقة Syriatel (" + String.valueOf(syriatelSim) + ")");
        tv3.setTextSize(18);
        tv3.setTextColor(res.getColor(R.color.spec_black));
        panel.addView(tv3);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv3.setText(tv3.getText() + ".");
            }
        }, 500);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv3.setText(tv3.getText() + ".");
            }
        }, 1000);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv3.setText(tv3.getText() + ".");
                boolean isAirplaneModeEnabled = true;
                try {
                    isAirplaneModeEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
                    Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
                    Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                    intent.putExtra("state", !isAirplaneModeEnabled);
                    context.sendBroadcast(intent);
                } catch (Exception e) {
                    if (isAirplaneModeEnabled) {
                        Toast.makeText(context, "الرجاء إيقاف وضع الطيران وتشغيل البرنامج من جديد", Toast.LENGTH_LONG).show();
                    }
                }
                if (syriatelSim == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "الرجاء السماح للتطبيق بالحصول على معلومات الاتصال", Toast.LENGTH_SHORT).show();
                            syriatelExist = false;
                        } else {
                            if (isSim1Available() && isSim1Syr())
                                syriatelExist = true;
                            else
                                syriatelExist = false;
                        }
                    } else {
                        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (telephonyManager.getSimSerialNumber() != null) {
                            mtnExist = true;
                            syriatelExist = true;
                        } else {
                            mtnExist = false;
                            syriatelExist = false;
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "الرجاء السماح للتطبيق بالحصول على معلومات الاتصال", Toast.LENGTH_SHORT).show();
                            syriatelExist = false;
                        } else {
                            if (isSim2Available() && isSim2Syr())
                                syriatelExist = true;
                            else
                                syriatelExist = false;
                        }
                    } else {
                        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (telephonyManager.getSimSerialNumber() != null) {
                            mtnExist = true;
                            syriatelExist = true;
                        } else {
                            mtnExist = false;
                            syriatelExist = false;
                        }
                    }
                }
                if (syriatelExist == true) {
                    syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                    syriatel.setTag("n");
                    tv3.setText(tv3.getText() + "مفعلة");
                } else {
                    syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo_hiddin));
                    syriatel.setTag("h");
                    tv3.setText(tv3.getText() + "غير مفعلة");
                    Toast.makeText(context, "بطاقة Syriatel غير مفعلة!", 700).show();
                }
                if (mtnExist || syriatelExist) {
                    tv4 = new TextView(context);
                    tv4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv4.setGravity(Gravity.CENTER);
                    tv4.setText("# التطبيق جاهز لتحويل الرصيد #");
                    tv4.setTextSize(20);
                    tv4.setTextColor(res.getColor(R.color.orange));
                    panel.addView(tv4);
                }
            }
        }, 1500);
    }

    private void setClicks() {
        if (!isSpecial) {
            prepaid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setUpTransform();
                    if (!isSpecial) {
                        if (cus_code.getVisibility() == View.GONE) {
                            if (phone.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*")) {
                                if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                    if (mtn.getTag().toString().matches("c")) {
                                        try {
                                            isTransform = true;
                                            String code = sharedPreferences.getString("code_pre_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                            transformBy(code, "code_pre_mtn", mtnSim);
                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                addTextToPanel(" - جاري تحويل (وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            else
                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            continueTransform("code_pre_mtn");
                                        } catch (Exception e) {
                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                        }
                                    } else if (syriatel.getTag().toString().matches("c")) {
                                        try {
                                            isTransform = true;
                                            String code = sharedPreferences.getString("code_pre_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                            transformBy(code, "code_pre_syr", syriatelSim);
                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                addTextToPanel(" - جاري تحويل (وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            else
                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            continueTransform("code_pre_syr");
                                        } catch (Exception e) {
                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {
                                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                }
                            } else if (!phone.getText().toString().matches(".*[0-9]+.*")) {
                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                            } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                            } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            }
                        } else {
                            if (mtn.getTag().toString().matches("c") || syriatel.getTag().toString().matches("c")) {
                                if (cus_code.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*") && ((mtn.getTag().toString().matches("c") && phone.getText().toString().matches(".*[0-9]+.*")) || syriatel.getTag().toString().matches("c"))) {
                                    if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                        if (mtn.getTag().toString().matches("c")) {
                                            try {
                                                isTransform = true;
                                                String code = sharedPreferences.getString("code_pre_who_mtn", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                transformBy(code, "code_pre_who_mtn", mtnSim);
                                                if (Integer.valueOf(repet.getText().toString()) == 1)
                                                    addTextToPanel(" - جاري تحويل (وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                else
                                                    addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                continueTransform("code_pre_who_mtn");
                                            } catch (Exception e) {
                                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                            }
                                        } else if (syriatel.getTag().toString().matches("c")) {
                                            try {
                                                isTransform = true;
                                                isSyrWhoPre = true;
                                                String code = sharedPreferences.getString("code_pre_post_who_syr", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                transformBy(code, "code_pre_post_who_syr", mtnSim);
                                                if (Integer.valueOf(repet.getText().toString()) == 1)
                                                    addTextToPanel(" - جاري تحويل (وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                else
                                                    addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                continueTransform("code_pre_post_who_syr");
                                            } catch (Exception e) {
                                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        cus_code.setVisibility(View.GONE);
                                        isCheckedManually = false;
                                        if (mtnExist) {
                                            mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                                            mtn.setTag("n");
                                        }
                                        if (syriatelExist) {
                                            syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                                            syriatel.setTag("n");
                                        }
                                    } else {
                                        phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                    }
                                } else if (!cus_code.getText().toString().matches(".*[0-9]+.*")) {
                                    cus_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                    balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                                } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                    money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                } else if (!phone.getText().toString().matches(".*[0-9]+.*") && mtn.getTag().toString().matches("c")) {
                                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                }
                            } else {
                                Toast.makeText(context, "يرجى اختيار الشركة عبر الضغط على الأيقونات في الأعلى", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        transformSpecial();
                    }
                }
            });
            postpaid.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    setUpTransform();
                    if (!isSpecial) {
                        if (cus_code.getVisibility() == View.GONE) {
                            if (phone.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*")) {
                                if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                    if (mtn.getTag().toString().matches("c")) {
                                        if (phone.getText().toString().startsWith("011") || phone.getText().toString().startsWith("033") || phone.getText().toString().startsWith("052") || phone.getText().toString().startsWith("014") || phone.getText().toString().startsWith("021") || phone.getText().toString().startsWith("051") || phone.getText().toString().startsWith("013") || phone.getText().toString().startsWith("016") || phone.getText().toString().startsWith("022") || phone.getText().toString().startsWith("031") || phone.getText().toString().startsWith("041") || phone.getText().toString().startsWith("043") || phone.getText().toString().startsWith("023") || phone.getText().toString().startsWith("015")) {
                                            try {
                                                isTransform = true;
                                                String code = sharedPreferences.getString("code_post_adsl_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                transformBy(code, "code_post_adsl_mtn", mtnSim);
                                                if (Integer.valueOf(repet.getText().toString()) == 1)
                                                    addTextToPanel(" - جاري تحويل (فاتورة ADSL)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                else
                                                    addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فاتورة ADSL)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                continueTransform("code_post_adsl_mtn");
                                            } catch (Exception e) {
                                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            try {
                                                isTransform = true;
                                                String code = sharedPreferences.getString("code_post_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                transformBy(code, "code_post_mtn", mtnSim);
                                                if (Integer.valueOf(repet.getText().toString()) == 1)
                                                    addTextToPanel(" - جاري تحويل (فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                else
                                                    addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                continueTransform("code_post_mtn");
                                            } catch (Exception e) {
                                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    } else if (syriatel.getTag().toString().matches("c")) {
                                        try {
                                            isTransform = true;
                                            String code = sharedPreferences.getString("code_post_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                            transformBy(code, "code_post_syr", syriatelSim);
                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                addTextToPanel(" - جاري تحويل (فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            else
                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            continueTransform("code_post_syr");
                                        } catch (Exception e) {
                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {
                                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                }
                            } else if (!phone.getText().toString().matches(".*[0-9]+.*")) {
                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                            } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                            } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            }
                        } else {
                            if (mtn.getTag().toString().matches("c") || syriatel.getTag().toString().matches("c")) {
                                if (cus_code.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*") && ((mtn.getTag().toString().matches("c") && phone.getText().toString().matches(".*[0-9]+.*")) || syriatel.getTag().toString().matches("c"))) {
                                    if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                        if (mtn.getTag().toString().matches("c")) {
                                            try {
                                                isTransform = true;
                                                String code = sharedPreferences.getString("code_post_who_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                transformBy(code, "code_post_who_mtn", mtnSim);
                                                if (Integer.valueOf(repet.getText().toString()) == 1)
                                                    addTextToPanel(" - جاري تحويل (فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                else
                                                    addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                continueTransform("code_post_who_mtn");
                                            } catch (Exception e) {
                                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                            }
                                        } else if (syriatel.getTag().toString().matches("c")) {
                                            try {
                                                isTransform = true;
                                                isSyrWhoPre = false;
                                                String code = sharedPreferences.getString("code_pre_post_who_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                transformBy(code, "code_pre_post_who_syr", syriatelSim);
                                                if (Integer.valueOf(repet.getText().toString()) == 1)
                                                    addTextToPanel(" - جاري تحويل (فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                else
                                                    addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                continueTransform("code_pre_post_who_syr");
                                            } catch (Exception e) {
                                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        cus_code.setVisibility(View.GONE);
                                        isCheckedManually = false;
                                        if (mtnExist) {
                                            mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                                            mtn.setTag("n");
                                        }
                                        if (syriatelExist) {
                                            syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                                            syriatel.setTag("n");
                                        }
                                    } else {
                                        phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                    }
                                } else if (!cus_code.getText().toString().matches(".*[0-9]+.*")) {
                                    cus_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                    balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                                } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                    money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                } else if (!phone.getText().toString().matches(".*[0-9]+.*") && mtn.getTag().toString().matches("c")) {
                                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                }
                            } else {
                                Toast.makeText(context, "يرجى اختيار الشركة عبر الضغط على الأيقونات في الأعلى", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        transformSpecial();
                    }
                }
            });
            prepaid.setOnLongClickListener(new View.OnLongClickListener()

            {
                @Override
                public boolean onLongClick(View view) {
                    if (!isSpecial) {
                        PopupMenu popup = new PopupMenu(context, prepaid);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                setUpTransform();
                                switch (menuItem.getItemId()) {
                                    case R.id.pop_pre:
                                        cus_code.setVisibility(View.GONE);
                                        isCheckedManually = false;
                                        if (phone.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*")) {
                                            if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                                if (mtn.getTag().toString().matches("c")) {
                                                    try {
                                                        isTransform = true;
                                                        String code = sharedPreferences.getString("code_pre_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                        transformBy(code, "code_pre_mtn", mtnSim);
                                                        if (Integer.valueOf(repet.getText().toString()) == 1)
                                                            addTextToPanel(" - جاري تحويل (وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                        else
                                                            addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                        continueTransform("code_pre_mtn");
                                                    } catch (Exception e) {
                                                        Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                    }
                                                } else if (syriatel.getTag().toString().matches("c")) {
                                                    try {
                                                        isTransform = true;
                                                        String code = sharedPreferences.getString("code_pre_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                        transformBy(code, "code_pre_syr", syriatelSim);
                                                        if (Integer.valueOf(repet.getText().toString()) == 1)
                                                            addTextToPanel(" - جاري تحويل (وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                        else
                                                            addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                        continueTransform("code_pre_syr");
                                                    } catch (Exception e) {
                                                        Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            } else {
                                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                            }
                                        } else if (!phone.getText().toString().matches(".*[0-9]+.*")) {
                                            phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                        } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                            balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                                        } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                            money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                        }
                                        return true;
                                    case R.id.pop_pre_who:
                                        cus_code.setVisibility(View.VISIBLE);
                                        isCheckedManually = true;
                                        if (mtn.getTag().toString().matches("c") || syriatel.getTag().toString().matches("c")) {
                                            if (cus_code.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*") && ((mtn.getTag().toString().matches("c") && phone.getText().toString().matches(".*[0-9]+.*")) || syriatel.getTag().toString().matches("c"))) {
                                                if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                                    if (mtn.getTag().toString().matches("c")) {
                                                        try {
                                                            isTransform = true;
                                                            String code = sharedPreferences.getString("code_pre_who_mtn", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                            transformBy(code, "code_pre_who_mtn", mtnSim);
                                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                                addTextToPanel(" - جاري تحويل (وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            else
                                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            continueTransform("code_pre_who_mtn");
                                                        } catch (Exception e) {
                                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else if (syriatel.getTag().toString().matches("c")) {
                                                        try {
                                                            isTransform = true;
                                                            isSyrWhoPre = true;
                                                            String code = sharedPreferences.getString("code_pre_post_who_syr", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                            transformBy(code, "code_pre_post_who_syr", mtnSim);
                                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                                addTextToPanel(" - جاري تحويل (وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            else
                                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            continueTransform("code_pre_post_who_syr");
                                                        } catch (Exception e) {
                                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    cus_code.setVisibility(View.GONE);
                                                    isCheckedManually = false;
                                                    if (mtnExist) {
                                                        mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                                                        mtn.setTag("n");
                                                    }
                                                    if (syriatelExist) {
                                                        syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                                                        syriatel.setTag("n");
                                                    }
                                                } else {
                                                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                                }
                                            } else if (!cus_code.getText().toString().matches(".*[0-9]+.*")) {
                                                cus_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                            } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                                balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                                            } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                                money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                            } else if (!phone.getText().toString().matches(".*[0-9]+.*") && mtn.getTag().toString().matches("c")) {
                                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                            }
                                        } else {
                                            Toast.makeText(context, "يرجى اختيار الشركة عبر الضغط على الأيقونات في الأعلى", Toast.LENGTH_LONG).show();
                                        }
                                        return true;
                                    default:
                                        return false;
                                }

                            }
                        });
                        popup.inflate(R.menu.popup_menu_prepaid);
                        popup.show();
                    }
                    return false;
                }
            });
            postpaid.setOnLongClickListener(new View.OnLongClickListener()

            {
                @Override
                public boolean onLongClick(View view) {
                    if (!isSpecial) {
                        PopupMenu popup = new PopupMenu(context, postpaid);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                setUpTransform();
                                switch (menuItem.getItemId()) {
                                    case R.id.pop_post:
                                        cus_code.setVisibility(View.GONE);
                                        isCheckedManually = false;
                                        if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                            if (mtn.getTag().toString().matches("c")) {

                                                    try {
                                                        isTransform = true;
                                                        String code = sharedPreferences.getString("code_post_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                        transformBy(code, "code_post_mtn", mtnSim);
                                                        if (Integer.valueOf(repet.getText().toString()) == 1)
                                                            addTextToPanel(" - جاري تحويل (فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                        else
                                                            addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                        continueTransform("code_post_mtn");
                                                    } catch (Exception e) {
                                                        Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                    }
                                            } else if (syriatel.getTag().toString().matches("c")) {
                                                try {
                                                    isTransform = true;
                                                    String code = sharedPreferences.getString("code_post_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                    transformBy(code, "code_post_syr", syriatelSim);
                                                    if (Integer.valueOf(repet.getText().toString()) == 1)
                                                        addTextToPanel(" - جاري تحويل (فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                    else
                                                        addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فاتورة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                    continueTransform("code_post_syr");
                                                } catch (Exception e) {
                                                    Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                        }
                                        return true;
                                    case R.id.pop_post_who:
                                        cus_code.setVisibility(View.VISIBLE);
                                        isCheckedManually = true;
                                        if (mtn.getTag().toString().matches("c") || syriatel.getTag().toString().matches("c")) {
                                            if (cus_code.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*") && ((mtn.getTag().toString().matches("c") && phone.getText().toString().matches(".*[0-9]+.*")) || syriatel.getTag().toString().matches("c"))) {
                                                if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                                    if (mtn.getTag().toString().matches("c")) {
                                                        try {
                                                            isTransform = true;
                                                            String code = sharedPreferences.getString("code_post_who_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                            transformBy(code, "code_post_who_mtn", mtnSim);
                                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                                addTextToPanel(" - جاري تحويل (فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            else
                                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            continueTransform("code_post_who_mtn");
                                                        } catch (Exception e) {
                                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else if (syriatel.getTag().toString().matches("c")) {
                                                        try {
                                                            isTransform = true;
                                                            isSyrWhoPre = false;
                                                            String code = sharedPreferences.getString("code_pre_post_who_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                            transformBy(code, "code_pre_post_who_syr", syriatelSim);
                                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                                addTextToPanel(" - جاري تحويل (فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            else
                                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فواتير جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            continueTransform("code_pre_post_who_syr");
                                                        } catch (Exception e) {
                                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    cus_code.setVisibility(View.GONE);
                                                    isCheckedManually = false;
                                                    if (mtnExist) {
                                                        mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                                                        mtn.setTag("n");
                                                    }
                                                    if (syriatelExist) {
                                                        syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                                                        syriatel.setTag("n");
                                                    }
                                                } else {
                                                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                                }
                                            } else if (!cus_code.getText().toString().matches(".*[0-9]+.*")) {
                                                cus_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                            } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                                balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                                            } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                                money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                            } else if (!phone.getText().toString().matches(".*[0-9]+.*") && mtn.getTag().toString().matches("c")) {
                                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                            }
                                        } else {
                                            Toast.makeText(context, "يرجى اختيار الشركة عبر الضغط على الأيقونات في الأعلى", Toast.LENGTH_LONG).show();
                                        }
                                        return true;
                                    case R.id.pop_post_adsl:
                                        cus_code.setVisibility(View.GONE);
                                        isCheckedManually = false;
                                        if (phone.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*")) {
                                            if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                                if (mtn.getTag().toString().matches("c")) {
                                                    if (phone.getText().toString().startsWith("011") || phone.getText().toString().startsWith("033") || phone.getText().toString().startsWith("052") || phone.getText().toString().startsWith("014") || phone.getText().toString().startsWith("021") || phone.getText().toString().startsWith("051") || phone.getText().toString().startsWith("013") || phone.getText().toString().startsWith("016") || phone.getText().toString().startsWith("022") || phone.getText().toString().startsWith("031") || phone.getText().toString().startsWith("041") || phone.getText().toString().startsWith("043") || phone.getText().toString().startsWith("023") || phone.getText().toString().startsWith("015")) {
                                                        try {
                                                            isTransform = true;
                                                            String code = sharedPreferences.getString("code_post_adsl_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                                            transformBy(code, "code_post_adsl_mtn", mtnSim);
                                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                                addTextToPanel(" - جاري تحويل (فاتورة ADSL)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            else
                                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(فاتورة ADSL)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                                            continueTransform("code_post_adsl_mtn");
                                                        } catch (Exception e) {
                                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(context, "يرجى إدخال رقم هاتف أرضي", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                            }
                                        } else if (!phone.getText().toString().matches(".*[0-9]+.*")) {
                                            phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                        } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                            balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                                        } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                            money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                        }
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popup.inflate(R.menu.popup_menu_postpaid);
                        popup.show();
                    }
                    return false;
                }
            });
            mtn.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    if (mtn.getTag().toString().matches("n")) {
                        if (isCheckedManually) {
                            mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo_checked));
                            mtn.setTag("c");
                            if (syriatelExist) {
                                syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                                syriatel.setTag("n");
                            }
                        } else {
                            try {
                                String code = sharedPreferences.getString("code_check_pre_mtn", "").replace("#", Uri.encode("#"));
                                transformBy(code, "code_check_pre_mtn", mtnSim);
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
            syriatel.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    if (syriatel.getTag().toString().matches("n")) {
                        if (isCheckedManually) {
                            syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo_checked));
                            syriatel.setTag("c");
                            if (mtnExist) {
                                mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                                mtn.setTag("n");
                            }
                        } else {
                            try {
                                String code = sharedPreferences.getString("code_check_pre_syr", "").replace("#", Uri.encode("#"));
                                transformBy(code, "code_check_pre_syr", syriatelSim);
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                            }
                            isCheckingSyrActive = true;
                        }
                    }
                }
            });
        } else {
            transformBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transformSpecial();
                }
            });
            checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, checkBtn);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.pop_bal_mtn:
                                    mode = "code_check_pre_mtn";
                                    simType = 1;
                                    transformSpecial();
                                    return true;
                                case R.id.pop_pre_bal_syr:
                                    mode = "code_check_pre_syr";
                                    simType = 2;
                                    transformSpecial();
                                    return true;
                                case R.id.pop_post_bal_syr:
                                    mode = "code_check_post_syr";
                                    simType = 2;
                                    transformSpecial();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.inflate(R.menu.popup_menu_check);
                    popup.show();
                }
            });
        }
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().matches(".*[a-zA-Zأ-ي]+.*")) {
                    customCustomerEditArrayAdapter.notifyDataSetChanged();
                    listAllCustomers = new ArrayList<>();
                    for (int k = 0; k < dbc.allCustomers().size(); k++) {
                        if (dbc.allCustomers().get(k).getName().contains(charSequence))
                            listAllCustomers.add(dbc.allCustomers().get(k));
                    }
                    customCustomerEditArrayAdapter = new CustomCustomerEditArrayAdapter(context, R.layout.list_customer_edit_item, listAllCustomers);
                    phone.setAdapter(customCustomerEditArrayAdapter);
                }
                if (charSequence.toString().contains("*") || charSequence.toString().contains("#")) {
                    phone.setInputType(InputType.TYPE_CLASS_TEXT);
                    phone.setTextColor(res.getColor(R.color.spec_black));
                    phone.setText("");
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(17);
                    phone.setFilters(FilterArray);
                } else if (charSequence.toString().startsWith("0") || charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2") || charSequence.toString().startsWith("3") || charSequence.toString().startsWith("4") || charSequence.toString().startsWith("5") || charSequence.toString().startsWith("6") || charSequence.toString().startsWith("7") || charSequence.toString().startsWith("8") || charSequence.toString().startsWith("9")) {
                    if (phone.getInputType() == InputType.TYPE_CLASS_TEXT) {
                        phone.setInputType(InputType.TYPE_CLASS_PHONE);
                        InputFilter[] FilterArray = new InputFilter[1];
                        FilterArray[0] = new InputFilter.LengthFilter(10);
                        phone.setFilters(FilterArray);
                    }
                    customCustomerEditArrayAdapter.notifyDataSetChanged();
                    listAllCustomers = new ArrayList<>();
                    for (int k = 0; k < dbc.allCustomers().size(); k++) {
                        if (dbc.allCustomers().get(k).getNumber().contains(charSequence))
                            listAllCustomers.add(dbc.allCustomers().get(k));
                    }
                    customCustomerEditArrayAdapter = new CustomCustomerEditArrayAdapter(context, R.layout.list_customer_edit_item, listAllCustomers);
                    phone.setAdapter(customCustomerEditArrayAdapter);
                    if (charSequence.toString().startsWith("093") || charSequence.toString().startsWith("93") || charSequence.toString().startsWith("098") || charSequence.toString().startsWith("98") || charSequence.toString().startsWith("099") || charSequence.toString().startsWith("99")) {
                        if (syriatelExist) {
                            if (!isCheckedManually) {
                                syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo_checked));
                                syriatel.setTag("c");
                            }
                        }
                    } else if (charSequence.toString().startsWith("094") || charSequence.toString().startsWith("94") || charSequence.toString().startsWith("095") || charSequence.toString().startsWith("95") || charSequence.toString().startsWith("096") || charSequence.toString().startsWith("96") || charSequence.toString().startsWith("011") || charSequence.toString().startsWith("033") || charSequence.toString().startsWith("052") || charSequence.toString().startsWith("014") || charSequence.toString().startsWith("021") || charSequence.toString().startsWith("051") || charSequence.toString().startsWith("013") || charSequence.toString().startsWith("016") || charSequence.toString().startsWith("022") || charSequence.toString().startsWith("031") || charSequence.toString().startsWith("041") || charSequence.toString().startsWith("043") || charSequence.toString().startsWith("023") || charSequence.toString().startsWith("015")) {
                        if (mtnExist) {
                            if (!isCheckedManually) {
                                mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo_checked));
                                mtn.setTag("c");
                            }
                        }
                    } else {
                        if (mtnExist && !(charSequence.toString().length() == 9 && !(charSequence.toString().startsWith("*") || charSequence.toString().startsWith("#") || charSequence.toString().startsWith("0") || charSequence.toString().startsWith("9")))) {
                            if (!isCheckedManually) {
                                mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                                mtn.setTag("n");
                            }
                        }
                        if (syriatelExist && !(charSequence.toString().length() == 8 && !(charSequence.toString().startsWith("0") || charSequence.toString().startsWith("9")))) {
                            if (!isCheckedManually) {
                                syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                                syriatel.setTag("n");
                            }
                        }
                    }
                    if (charSequence.toString().length() == 8 && !(charSequence.toString().startsWith("0") || charSequence.toString().startsWith("9"))) {
                        if (syriatelExist) {
                            if (!isCheckedManually) {
                                syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo_checked));
                                syriatel.setTag("c");
                            }
                        }
                        phone.setTextColor(res.getColor(R.color.green));
                        phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_true, 0, R.drawable.ic_edit_true, 0);
                    } else if (charSequence.toString().length() == 9 && !(charSequence.toString().startsWith("*") || charSequence.toString().startsWith("#") || charSequence.toString().startsWith("0") || charSequence.toString().startsWith("9"))) {
                        if (mtnExist) {
                            if (!isCheckedManually) {
                                mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo_checked));
                                mtn.setTag("c");
                            }
                        }
                        phone.setTextColor(res.getColor(R.color.green));
                        phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_true, 0, R.drawable.ic_edit_true, 0);
                    } else if ((charSequence.toString().length() == 9 && charSequence.toString().startsWith("9")) || (charSequence.toString().length() == 10 && charSequence.toString().startsWith("0"))) {
                        phone.setTextColor(res.getColor(R.color.green));
                        phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_true, 0, R.drawable.ic_edit_true, 0);
                    } else {
                        phone.setTextColor(res.getColor(R.color.red));
                        phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /**if (isSetInitialMoneyEditText)
                 isSetInitialMoneyEditText = false;
                 else {
                 if (money.getText().toString().matches(".*[0-9]+.*")) {
                 money.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                 if (money.getText().toString().matches("75")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("50");

                 } else if (money.getText().toString().matches("100")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("90");

                 } else if (money.getText().toString().matches("125")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("100");

                 } else if (money.getText().toString().matches("175")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("150");

                 } else if (money.getText().toString().matches("225")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("200");

                 } else if (money.getText().toString().matches("250")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("225");

                 } else if (money.getText().toString().matches("300")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("250");

                 } else if (money.getText().toString().matches("350")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("300");

                 } else if (money.getText().toString().matches("500")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("450");

                 } else if (money.getText().toString().matches("550")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("500");

                 } else if (money.getText().toString().matches("750")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("650");

                 } else if (money.getText().toString().matches("850")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("750");

                 } else if (money.getText().toString().matches("1000")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("900");

                 } else if (money.getText().toString().matches("1100")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("1000");

                 } else if (money.getText().toString().matches("1350")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("1000");

                 } else if (money.getText().toString().matches("1650")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("1500");

                 } else if (money.getText().toString().matches("2200")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("2000");

                 } else if (money.getText().toString().matches("4400")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("4000");

                 } else if (money.getText().toString().matches("5500")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("5000");

                 } else if (money.getText().toString().matches("11000")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("10000");

                 } else if (money.getText().toString().matches("16500")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("15000");

                 } else if (money.getText().toString().matches("22000")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("20000");
                 } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                 isSetInitialBalanceEditText = true;
                 balance.setText("");
                 }
                 } else {
                 isSetInitialBalanceEditText = true;
                 balance.setText("");
                 }
                 }**/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (sharedPreferences.getInt("autoPriceMode", 0) == 0 || (sharedPreferences.getInt("autoPriceMode", 0) == 1 && !isSpecial)) {
                    if (isSetInitialBalanceEditText)
                        isSetInitialBalanceEditText = false;
                    else {
                        if (balance.getText().toString().matches(".*[0-9]+.*")) {
                            balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            if (balance.getText().toString().matches("50")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("75");

                            } else if (balance.getText().toString().matches("75")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("100");

                            } else if (balance.getText().toString().matches("90")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("100");

                            } else if (balance.getText().toString().matches("100")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("125");

                            } else if (balance.getText().toString().matches("105")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("125");

                            } else if (balance.getText().toString().matches("150")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("175");

                            } else if (balance.getText().toString().matches("200")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("225");

                            } else if (balance.getText().toString().matches("225")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("250");

                            } else if (balance.getText().toString().matches("250")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("300");

                            } else if (balance.getText().toString().matches("300")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("350");

                            } else if (balance.getText().toString().matches("450")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("500");

                            } else if (balance.getText().toString().matches("500")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("550");

                            } else if (balance.getText().toString().matches("650")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("750");

                            } else if (balance.getText().toString().matches("750")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("850");

                            } else if (balance.getText().toString().matches("900")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("1000");

                            } else if (balance.getText().toString().matches("1000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("1100");

                            } else if (balance.getText().toString().matches("1000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("1350");

                            } else if (balance.getText().toString().matches("1500")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("1650");

                            } else if (balance.getText().toString().matches("2000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("2200");

                            } else if (balance.getText().toString().matches("4000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("4400");

                            } else if (balance.getText().toString().matches("5000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("5500");

                            } else if (balance.getText().toString().matches("10000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("11000");

                            } else if (balance.getText().toString().matches("15000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("16500");

                            } else if (balance.getText().toString().matches("20000")) {
                                isSetInitialMoneyEditText = true;
                                money.setText("22000");
                            } else {

                            }
                        } else {
                            money.setText("");
                        }
                    }
                } else if (sharedPreferences.getInt("autoPriceMode", 0) == 1) {
                    if (balance.getText().toString().matches(".*[0-9]+.*")) {
                        balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        if (spin_operation.getSelectedItem().toString().matches(res.getStringArray(R.array.transformSpecial)[5])) {
                            money.setText(String.valueOf(((Double.valueOf(sharedPreferences.getString("syr_pre_who_price", "600")) * Double.valueOf(balance.getText().toString())) / 10000) + Double.valueOf(balance.getText().toString())));
                        } else if (spin_operation.getSelectedItem().toString().matches(res.getStringArray(R.array.transformSpecial)[6])) {
                            money.setText(String.valueOf(((Double.valueOf(sharedPreferences.getString("mtn_pre_who_price", "600")) * Double.valueOf(balance.getText().toString())) / 10000) + Double.valueOf(balance.getText().toString())));
                        } else if (spin_operation.getSelectedItem().toString().matches(res.getStringArray(R.array.transformSpecial)[7])) {
                            money.setText(String.valueOf(((Double.valueOf(sharedPreferences.getString("mtn_post_who_price", "600")) * Double.valueOf(balance.getText().toString())) / 10000) + Double.valueOf(balance.getText().toString())));
                        } else {
                            if (isSetInitialBalanceEditText)
                                isSetInitialBalanceEditText = false;
                            else {
                                if (balance.getText().toString().matches(".*[0-9]+.*")) {
                                    balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    if (balance.getText().toString().matches("50")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("75");

                                    } else if (balance.getText().toString().matches("75")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("100");

                                    } else if (balance.getText().toString().matches("90")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("100");

                                    } else if (balance.getText().toString().matches("100")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("125");

                                    } else if (balance.getText().toString().matches("105")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("125");

                                    } else if (balance.getText().toString().matches("150")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("175");

                                    } else if (balance.getText().toString().matches("200")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("225");

                                    } else if (balance.getText().toString().matches("225")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("250");

                                    } else if (balance.getText().toString().matches("250")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("300");

                                    } else if (balance.getText().toString().matches("300")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("350");

                                    } else if (balance.getText().toString().matches("450")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("500");

                                    } else if (balance.getText().toString().matches("500")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("550");

                                    } else if (balance.getText().toString().matches("650")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("750");

                                    } else if (balance.getText().toString().matches("750")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("850");

                                    } else if (balance.getText().toString().matches("900")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("1000");

                                    } else if (balance.getText().toString().matches("1000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("1100");

                                    } else if (balance.getText().toString().matches("1000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("1350");

                                    } else if (balance.getText().toString().matches("1500")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("1650");

                                    } else if (balance.getText().toString().matches("2000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("2200");

                                    } else if (balance.getText().toString().matches("4000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("4400");

                                    } else if (balance.getText().toString().matches("5000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("5500");

                                    } else if (balance.getText().toString().matches("10000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("11000");

                                    } else if (balance.getText().toString().matches("15000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("16500");

                                    } else if (balance.getText().toString().matches("20000")) {
                                        isSetInitialMoneyEditText = true;
                                        money.setText("22000");
                                    } else {

                                    }
                                } else {
                                    money.setText("");
                                }
                            }
                        }
                    } else {
                        money.setText("");
                    }
                } else {
                    if (balance.getText().toString().matches(".*[0-9]+.*"))
                        balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    else
                        money.setText("");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cus_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customCustomerEditArrayAdapter.notifyDataSetChanged();
                listAllCustomers = new ArrayList<>();
                for (int k = 0; k < dbc.allCustomers().size(); k++) {
                    if (dbc.allCustomers().get(k).getCode().contains(charSequence)) {
                        listAllCustomers.add(dbc.allCustomers().get(k));
                    }
                }
                customCustomerEditArrayAdapter = new CustomCustomerEditArrayAdapter(context, R.layout.list_customer_edit_item, listAllCustomers);
                cus_code.setAdapter(customCustomerEditArrayAdapter);
                if (cus_code.getText().toString().matches(".*[0-9]+.*"))
                    cus_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("isFocusLast", true))
                    phone.setSelection(phone.getText().length());
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (sharedPreferences.getBoolean("isFocusLast", true))
                        phone.setSelection(phone.getText().length());
            }
        });
        phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (sharedPreferences.getBoolean("isFocusLast", true))
                    phone.setSelection(phone.getText().length());
                return false;
            }
        });
        cus_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("isFocusLast", true))
                    cus_code.setSelection(cus_code.getText().length());
            }
        });
        cus_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (sharedPreferences.getBoolean("isFocusLast", true))
                        cus_code.setSelection(cus_code.getText().length());
            }
        });
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("isFocusLast", true))
                    money.setSelection(money.getText().length());
            }
        });
        money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (sharedPreferences.getBoolean("isFocusLast", true))
                        money.setSelection(money.getText().length());
            }
        });
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean("isFocusLast", true))
                    balance.setSelection(balance.getText().length());
            }
        });
        balance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (sharedPreferences.getBoolean("isFocusLast", true))
                        balance.setSelection(balance.getText().length());
            }
        });
        repet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (sharedPreferences.getBoolean("isFocusLast", true))
                        repet.setSelection(repet.getText().length());
            }
        });
        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();
                if (index == 0)
                    type = "نقدي";
                else
                    type = "دين";
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        if (isSpecial) {
            spin_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int index = adapterView.getSelectedItemPosition();
                    switch (index) {
                        case 0:
                            mode = "code_pre_mtn";
                            cus_code.setVisibility(View.GONE);
                            isCheckedManually = false;
                            simType = 1;
                            break;
                        case 1:
                            mode = "code_pre_syr";
                            simType = 2;
                            cus_code.setVisibility(View.GONE);
                            isCheckedManually = false;
                            break;
                        case 2:
                            mode = "code_post_mtn";
                            simType = 1;
                            cus_code.setVisibility(View.GONE);
                            isCheckedManually = false;
                            break;
                        case 3:
                            mode = "code_post_syr";
                            simType = 2;
                            cus_code.setVisibility(View.GONE);
                            isCheckedManually = false;
                            break;
                        case 4:
                            mode = "code_post_adsl_mtn";
                            simType = 1;
                            cus_code.setVisibility(View.GONE);
                            isCheckedManually = false;
                            break;
                        case 5:
                            mode = "code_pre_post_who_syr";
                            cus_code.setVisibility(View.VISIBLE);
                            isCheckedManually = true;
                            simType = 2;
                            break;
                        case 6:
                            mode = "code_pre_who_mtn";
                            cus_code.setVisibility(View.VISIBLE);
                            isCheckedManually = true;
                            simType = 1;
                            break;
                        case 7:
                            mode = "code_post_who_mtn";
                            cus_code.setVisibility(View.VISIBLE);
                            isCheckedManually = true;
                            simType = 1;
                            break;
                        case 8:
                            mode = "code_check";
                            cus_code.setVisibility(View.GONE);
                            isCheckedManually = false;
                            simType = 1;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createAddNewDialog();
               readContact();
            }
        });
        money.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                setUpTransform();
                if (!isSpecial) {
                    if (cus_code.getVisibility() == View.GONE) {
                        if (phone.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*")) {
                            if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                if (mtn.getTag().toString().matches("c")) {
                                    try {
                                        isTransform = true;
                                        String code = sharedPreferences.getString("code_pre_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        transformBy(code, "code_pre_mtn", mtnSim);
                                        if (Integer.valueOf(repet.getText().toString()) == 1)
                                            addTextToPanel(" - جاري تحويل (وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                        else
                                            addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                        continueTransform("code_pre_mtn");
                                    } catch (Exception e) {
                                        Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                    }
                                } else if (syriatel.getTag().toString().matches("c")) {
                                    try {
                                        isTransform = true;
                                        String code = sharedPreferences.getString("code_pre_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        transformBy(code, "code_pre_syr", syriatelSim);
                                        if (Integer.valueOf(repet.getText().toString()) == 1)
                                            addTextToPanel(" - جاري تحويل (وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                        else
                                            addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                        continueTransform("code_pre_syr");
                                    } catch (Exception e) {
                                        Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                            }
                        } else if (!phone.getText().toString().matches(".*[0-9]+.*")) {
                            phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                        } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                            balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                        } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                            money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                        }
                    } else {
                        if (mtn.getTag().toString().matches("c") || syriatel.getTag().toString().matches("c")) {
                            if (cus_code.getText().toString().matches(".*[0-9]+.*") && balance.getText().toString().matches(".*[0-9]+.*") && money.getText().toString().matches(".*[0-9]+.*") && ((mtn.getTag().toString().matches("c") && phone.getText().toString().matches(".*[0-9]+.*")) || syriatel.getTag().toString().matches("c"))) {
                                if (phone.getCurrentTextColor() == res.getColor(R.color.green)) {
                                    if (mtn.getTag().toString().matches("c")) {
                                        try {
                                            isTransform = true;
                                            String code = sharedPreferences.getString("code_pre_who_mtn", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                            transformBy(code, "code_pre_who_mtn", mtnSim);
                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                addTextToPanel(" - جاري تحويل (وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            else
                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            continueTransform("code_pre_who_mtn");
                                        } catch (Exception e) {
                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                        }
                                    } else if (syriatel.getTag().toString().matches("c")) {
                                        try {
                                            isTransform = true;
                                            isSyrWhoPre = true;
                                            String code = sharedPreferences.getString("code_pre_post_who_syr", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                            transformBy(code, "code_pre_post_who_syr", mtnSim);
                                            if (Integer.valueOf(repet.getText().toString()) == 1)
                                                addTextToPanel(" - جاري تحويل (وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            else
                                                addTextToPanel("- جاري تحويل/" + repet.getText().toString() + "/" + "(وحدات جملة)" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                                            continueTransform("code_pre_post_who_syr");
                                        } catch (Exception e) {
                                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    cus_code.setVisibility(View.GONE);
                                    isCheckedManually = false;
                                    if (mtnExist) {
                                        mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
                                        mtn.setTag("n");
                                    }
                                    if (syriatelExist) {
                                        syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
                                        syriatel.setTag("n");
                                    }
                                } else {
                                    phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                                }
                            } else if (!cus_code.getText().toString().matches(".*[0-9]+.*")) {
                                cus_code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                            } else if (!balance.getText().toString().matches(".*[0-9]+.*")) {
                                balance.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_error, 0);
                            } else if (!money.getText().toString().matches(".*[0-9]+.*")) {
                                money.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            } else if (!phone.getText().toString().matches(".*[0-9]+.*") && mtn.getTag().toString().matches("c")) {
                                phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, R.drawable.ic_edit_error, 0);
                            }
                        } else {
                            Toast.makeText(context, "يرجى اختيار الشركة عبر الضغط على الأيقونات في الأعلى", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    transformSpecial();
                }
                return false;
            }
        });
        balance_info_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_info, (ViewGroup) view.findViewById(R.id.toast_info_root));
                TextView syr_pre = (TextView) layout.findViewById(R.id.txt_balance_s);
                TextView syr_post = (TextView) layout.findViewById(R.id.txt_balance_s_);
                TextView mtn_pre = (TextView) layout.findViewById(R.id.txt_balance_m);
                TextView mtn_post = (TextView) layout.findViewById(R.id.txt_balance_m_);
                TextView balance = (TextView) layout.findViewById(R.id.txt_balance);
                TextView profits = (TextView) layout.findViewById(R.id.txt_profits);
                TextView debt = (TextView) layout.findViewById(R.id.txt_debt);
                dbb = new BalanceSQLDatabaseHandler(context);
                syr_pre.setText(dbb.getBalance().getSyrBalance());
                syr_post.setText(dbb.getBalance().getSyrBalance_());
                mtn_pre.setText(dbb.getBalance().getMtnBalance());
                mtn_post.setText(dbb.getBalance().getMtnBalance_());
                balance.setText(dbb.getBalance().getBalance());
                profits.setText(dbb.getBalance().getProfits());
                debt.setText(dbb.getBalance().getDebt());

                Toast toast = new Toast(context);
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    public void readContact() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpTransform() {
        listAllCustomersPanel = new ArrayList<>();
        for (int l = db.allTransforms().size() - 1; l >= 0; l--) {
            for (int m = 0; m < dbc.allCustomers().size(); m++) {
                if (dbc.allCustomers().get(m).getName().matches(db.allTransforms().get(l).getName()))
                    listAllCustomersPanel.add(dbc.allCustomers().get(m));
            }
        }
        List<CustomerListChildItem> allEvents = listAllCustomersPanel;
        List<CustomerListChildItem> noRepeat = new ArrayList<CustomerListChildItem>();
        for (CustomerListChildItem event : allEvents) {
            boolean isFound = false;
            // check if the event name exists in noRepeat
            for (CustomerListChildItem e : noRepeat) {
                if (e.getName().equals(event.getName()) || (e.equals(event))) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) noRepeat.add(event);
        }
        listAllCustomersPanel.clear();
        listAllCustomersPanel.addAll(noRepeat);
        customCustomerEditArrayAdapterPanel = new CustomCustomerEditArrayAdapter(context, R.layout.list_customer_edit_item, listAllCustomersPanel);
        customersList.setAdapter(customCustomerEditArrayAdapterPanel);
        if (!repet.getText().toString().matches(".*[0-9]+.*"))
            repet.setText("1");
        repeatNum = Integer.valueOf(repet.getText().toString());
    }

    private void continueTransform(String type) {
        String view = "";
        String view_ = "";
        if (type.matches("code_pre_mtn") || type.matches("code_pre_syr")) {
            view = "وحدات";
            view_ = "و";
        } else if (type.matches("code_post_mtn") || type.matches("code_post_syr")) {
            view = "فاتورة";
            view_ = "ف";
        } else if (type.matches("code_post_adsl_mtn")) {
            view = "فاتورة ADSL";
            view_ = "ف";
        } else if (type.matches("code_pre_who_mtn")) {
            view = "جملة وحدات";
            view_ = "ج.و";
        } else if (type.matches("code_post_who_mtn")) {
            view = "جملة فواتير";
            view_ = "ج.ف";
        } else if (type.matches("code_pre_post_who_syr")) {
            view = "جملة";
            view_ = "ج";
        }
        currentNumber = phone.getText().toString();
        currentAmount = spin_type.getSelectedItem().toString() + "/" + money.getText().toString() + "/" + balance.getText().toString() + "/" + view;
        currentDate = getDate() + " | " + getTime();
        currentCode = cus_code.getText().toString();
        if (repeatNum == 1) {
            phone.setText("");
            money.setText("");
            balance.setText("");
        }
        if (mtnExist) {
            mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo));
            mtn.setTag("n");
        }
        if (syriatelExist) {
            syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo));
            syriatel.setTag("n");
        }
        phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void addTextToPanel(String s) {
        tv = new TextView(context);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.RIGHT);
        tv.setTextSize(17);
        tv.setTextColor(res.getColor(R.color.spec_black));
        tv.setText(s);
        panel.addView(tv);
    }

    private void transformBy(String code, String code_, int numSim) {
        hideKeyboard();
        Intent serviceIntent = new Intent(context, USSDService.class);
        serviceIntent.putExtra("type", code_);
        context.startService(serviceIntent);
        Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + code));
        hideKeyboard();
        callIntent.putExtra("simSlot", numSim - 1);
        callIntent.putExtra("com.android.phone.extra.slot", numSim - 1);
        context.startActivity(callIntent);
    }

    private void transformSpecial() {
        if (!repet.getText().toString().matches(".*[0-9]+.*"))
            repet.setText("1");
        repeatNum = Integer.valueOf(repet.getText().toString());
        try {
            isTransform = true;
            hideKeyboard();
            String code1;
            if (mode.matches("code_pre_who_mtn") || mode.matches("code_post_who_mtn") || mode.matches("code_pre_post_who_syr")) {
                code1 = sharedPreferences.getString(mode, "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
            } else {
                code1 = sharedPreferences.getString(mode, "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
            }
            Intent serviceIntent = new Intent(context, USSDService.class);
            serviceIntent.putExtra("type", mode);
            context.startService(serviceIntent);
            Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel:" + code1));
            hideKeyboard();
            if (simType == 1) {
                callIntent.putExtra("simSlot", mtnSim - 1);
                callIntent.putExtra("com.android.phone.extra.slot", mtnSim - 1);
            } else {
                callIntent.putExtra("simSlot", syriatelSim - 1);
                callIntent.putExtra("com.android.phone.extra.slot", syriatelSim - 1);
            }
            context.startActivity(callIntent);
            String view = "";
            String view_ = "";
            if (mode.matches("code_pre_mtn") || mode.matches("code_pre_syr")) {
                view = "وحدات";
                view_ = "و";
            } else if (mode.matches("code_post_mtn") || mode.matches("code_post_syr")) {
                view = "فاتورة";
                view_ = "ف";
            } else if (mode.matches("code_post_adsl_mtn")) {
                view = "فاتورة ADSL";
                view_ = "ف";
            } else if (mode.matches("code_pre_who_mtn")) {
                view = "جملة وحدات";
                view_ = "ج.و";
            } else if (mode.matches("code_post_who_mtn")) {
                view = "جملة فواتير";
                view_ = "ج.ف";
            } else if (mode.matches("code_pre_post_who_syr")) {
                view = "جملة";
                view_ = "ج";
            }
            if (!(mode.matches("code_check_pre_mtn") || mode.matches("code_check_pre_syr") || mode.matches("code_check_post_syr"))) {
                tv = new TextView(context);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.RIGHT);
                tv.setTextSize(17);
                tv.setTextColor(res.getColor(R.color.spec_black));
                if (Integer.valueOf(repet.getText().toString()) == 1)
                    tv.setText(" - جاري تحويل (" + view + ")" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                else
                    tv.setText("- جاري تحويل/" + repet.getText().toString() + "/" + "(" + view + ")" + balance.getText().toString() + " للرقم " + phone.getText().toString());
                panel.addView(tv);
                currentNumber = phone.getText().toString();
                currentAmount = spin_type.getSelectedItem().toString() + "/" + money.getText().toString() + "/" + balance.getText().toString() + "/" + view;
                currentDate = getDate() + " | " + getTime();
                currentCode = cus_code.getText().toString();
                if (repeatNum == 1) {
                    phone.setText("");
                    money.setText("");
                    balance.setText("");
                }
                phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                cus_code.setVisibility(View.GONE);
                isCheckedManually = false;
            } else {

            }
        } catch (Exception e) {
            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setVaribales() {
        res = getResources();
        telephonyInfo = TelephonyInfo.getInstance(context);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        vpa = new ViewPagerAdapter();
        viewPager.setAdapter(vpa);
        viewPager.setCurrentItem(0);
        panel = vpa.getPanel(viewPager);
        customersList = vpa.getList(viewPager);
        mtn = (ImageButton) view.findViewById(R.id.mtn_img);
        syriatel = (ImageButton) view.findViewById(R.id.syriatel_img);
        phone = (CustomAutoCompleteTextView) view.findViewById(R.id.edit_number);
        money = (EditText) view.findViewById(R.id.edit_money);
        balance = (EditText) view.findViewById(R.id.edit_balance);
        repet = (EditText) view.findViewById(R.id.edit_repet);
        cus_code = (CustomAutoCompleteTextView) view.findViewById(R.id.edit_code);
        //وحدات
        prepaid = (Button) view.findViewById(R.id.btn_prepaid);
        //فاتورة
        postpaid = (Button) view.findViewById(R.id.btn_postpay);
        tv_pre_syr = (TextView) view.findViewById(R.id.tv_pre_syr);
        tv_pre_mtn = (TextView) view.findViewById(R.id.tv_pre_mtn);
        tv_post_syr = (TextView) view.findViewById(R.id.tv_post_syr);
        tv_post_mtn = (TextView) view.findViewById(R.id.tv_post_mtn);
        transformBtn = (Button) view.findViewById(R.id.btn_transform);
        checkBtn = (Button) view.findViewById(R.id.btn_check);
        addNewBtn = (LinearLayout) view.findViewById(R.id.save_number);
        spin_type = (Spinner) view.findViewById(R.id.spin_type);
        spin_operation = (Spinner) view.findViewById(R.id.spin_operation);
        relative_special = (RelativeLayout) view.findViewById(R.id.relative_operation);
        linear_quick = (LinearLayout) view.findViewById(R.id.linear_quick);
        linear_quick_ = (LinearLayout) view.findViewById(R.id.linear_quick_);
        linear_operation = (LinearLayout) view.findViewById(R.id.linear_operation);
        balance_info_floating = (MovableFloatingActionButton) view.findViewById(R.id.fab_bal_info);
        sharedPreferences = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        mtn.setImageDrawable(res.getDrawable(R.drawable.mtn_logo_hiddin));
        syriatel.setImageDrawable(res.getDrawable(R.drawable.syriatel_logo_hiddin));
        syriatel.setTag("h");
        mtn.setTag("h");
        String[] type = res.getStringArray(R.array.debt_cash);
        ArrayAdapter<CharSequence> spinnerTypeArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item, type);
        spinnerTypeArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spin_type.setAdapter(spinnerTypeArrayAdapter);
        db = new TransformSQLDatabaseHandler(context);
        dbc = new CustomerSQLDatabaseHandler(context);
        dbb = new BalanceSQLDatabaseHandler(context);
        if (isSpecial) {
            String[] transformTypes = res.getStringArray(R.array.transformSpecial);
            ArrayAdapter<CharSequence> spinnerOperArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item_operation, transformTypes);
            spinnerOperArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_operation);
            spin_operation.setAdapter(spinnerOperArrayAdapter);
            spin_operation.setSelection(0);
            relative_special.setVisibility(View.VISIBLE);
            linear_quick.setVisibility(View.GONE);
            linear_quick_.setVisibility(View.GONE);
            linear_operation.setVisibility(View.VISIBLE);
        } else {
            relative_special.setVisibility(View.GONE);
            linear_quick.setVisibility(View.VISIBLE);
            linear_quick_.setVisibility(View.VISIBLE);
            linear_operation.setVisibility(View.GONE);
        }
        if (!sharedPreferences.getBoolean("isShowButton", true))
            balance_info_floating.setVisibility(View.GONE);
        dbb.allBalances();
        tv_pre_syr.setText(dbb.getBalance().getSyrBalance());
        tv_post_syr.setText(dbb.getBalance().getSyrBalance_());
        tv_post_mtn.setText(dbb.getBalance().getMtnBalance_());
        tv_pre_mtn.setText(dbb.getBalance().getMtnBalance());

    }

    MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            context.registerReceiver(myBroadCastReceiver, intentFilter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {
        LinearLayout linear = (LinearLayout) view;
        TextView txtName = (TextView) linear.findViewById(R.id.txtName);
        TextView txtNumber = (TextView) linear.findViewById(R.id.txtNumber);
        for (int i = 0; i < dbc.allCustomers().size(); i++) {
            if (dbc.allCustomers().get(i).getName().matches(txtName.getText().toString())) {
                phone.setText(dbc.allCustomers().get(i).getNumber());
                if (cus_code.getVisibility() == VISIBLE)
                    cus_code.setText(dbc.allCustomers().get(i).getCode());
            }
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                    if (mtnExist || syriatelExist) {
                        if (tv != null) {
                            if (intent.getStringExtra("text").contains("بنجاح") || intent.getStringExtra("text").contains("تم تحويل")) {
                                if (!tv.getText().toString().contains("(تم)") && !tv.getText().toString().contains("(لم يتم)"))
                                    tv.setText(Html.fromHtml("<font color=#0b3b39>" + tv.getText() + "</font><font color=#99cc00>(تم)</font>"));
                                if (!sharedPreferences.getBoolean("isMessageRead", true)) {
                                    saveNewTransform(intent.getStringExtra("type"));
                                } else {
                                    Toast.makeText(context, intent.getStringExtra("text").replace(", موافق", "").replace(", تم", "").replace(", OK", ""), Toast.LENGTH_LONG).show();
                                }
                                if (repeatNum == 1) {
                                    type = intent.getStringExtra("type");
                                    if (type.contains("mtn")) {
                                        try {
                                            String code = sharedPreferences.getString("code_check_pre_mtn", "").replace("#", Uri.encode("#"));
                                            transformBy(code, "code_check_pre_mtn", mtnSim);
                                        } catch (Exception e) {
                                        }
                                    } else if (type.contains("sy")) {
                                        try {
                                            String code = sharedPreferences.getString("code_check_pre_syr", "").replace("#", Uri.encode("#"));
                                            transformBy(code, "code_check_pre_syr", syriatelSim);
                                        } catch (Exception e) {
                                        }
                                        isCheckingSyrActive = true;
                                    }
                                }
                            } else {
                                if (!tv.getText().toString().contains("(لم يتم)") && !tv.getText().toString().contains("(تم)"))
                                    tv.setText(Html.fromHtml("<font color=#0b3b39>" + tv.getText() + "</font><font color=#FE2E2E>(لم يتم)</font>"));
                                Toast.makeText(context, intent.getStringExtra("text").replace(", موافق", "").replace(", تم", "").replace(", OK", ""), Toast.LENGTH_LONG).show();
                            }
                        }
                        MainActivity main;
                        if (intent.getStringExtra("type").matches("_code_check_pre_syr") ) {
                            main = (MainActivity) getActivity();
                            BalanceItem bi = dbb.getBalance();
                            bi.setSyrBalance(String.valueOf(Integer.valueOf(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("هو") + 2).replaceAll("موافق", "").replaceAll("OK", "").replace('[', ' ').replace(']', ' ').replace(',', ' ').replaceAll(" ", ""))));
                            dbb.updateBalance(bi);
                            main.syrAnm.stop();
                            main.syrAnm_.stop();
                            main.mtnAnm.stop();
                            main.mtnAnm_.stop();
                            main.txt_balance_s.setText(bi.getSyrBalance());
                        }else if(intent.getStringExtra("type").matches("code_check_pre_syr")){
                            tv_pre_syr.setText(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("هو") + 2).replaceAll("موافق", "").replaceAll("OK", "").replace('[', ' ').replace(']', ' ').replace(',', ' ').replaceAll(" ", ""));
                            BalanceItem bi = dbb.getBalance();
                            bi.setSyrBalance(String.valueOf(Integer.valueOf(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("هو") + 2).replaceAll("موافق", "").replaceAll("OK", "").replace('[', ' ').replace(']', ' ').replace(',', ' ').replaceAll(" ", ""))));
                            dbb.updateBalance(bi);

                        }else if (intent.getStringExtra("type").matches("_code_check_post_syr") ) {
                            main = (MainActivity) getActivity();
                            BalanceItem bi = dbb.getBalance();
                            bi.setSyrBalance_(String.valueOf(Integer.valueOf(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("هو") + 2).replaceAll("موافق", "").replaceAll("OK", "").replace('[', ' ').replace(']', ' ').replace(',', ' ').replaceAll(" ", ""))));
                            dbb.updateBalance(bi);
                            main.syrAnm.stop();
                            main.syrAnm_.stop();
                            main.mtnAnm.stop();
                            main.mtnAnm_.stop();
                            main.txt_balance_s_.setText(bi.getSyrBalance_());
                        }else if(intent.getStringExtra("type").matches("code_check_post_syr")){
                            tv_post_syr.setText(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("هو") + 2).replaceAll("موافق", "").replaceAll("OK", "").replace('[', ' ').replace(']', ' ').replace(',', ' ').replaceAll(" ", ""));
                            BalanceItem bi = dbb.getBalance();
                            bi.setSyrBalance_(String.valueOf(Integer.valueOf(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("هو") + 2).replaceAll("موافق", "").replaceAll("OK", "").replace('[', ' ').replace(']', ' ').replace(',', ' ').replaceAll(" ", ""))));
                            dbb.updateBalance(bi);
                        } else if (intent.getStringExtra("type").matches("_code_check_pre_mtn")) {
                            main = (MainActivity) getActivity();
                             BalanceItem bi = dbb.getBalance();
                            bi.setMtnBalance(String.valueOf(Integer.valueOf(tv_pre_mtn.getText().toString().replaceAll(" ", ""))));
                            bi.setMtnBalance_(String.valueOf(Integer.valueOf(tv_post_mtn.getText().toString().replaceAll(" ", ""))));
                            dbb.updateBalance(bi);
                            main.syrAnm.stop();
                            main.syrAnm_.stop();
                            main.mtnAnm.stop();
                            main.mtnAnm_.stop();
                            main.txt_balance_m.setText(bi.getMtnBalance());
                            main.txt_balance_m_.setText(bi.getMtnBalance_());
                        }else if( intent.getStringExtra("type").matches("code_check_pre_mtn")){
                            tv_pre_mtn.setText(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("مسبق الدفع:") + 12, intent.getStringExtra("text").indexOf("ا", intent.getStringExtra("text").indexOf("مسبق الدفع:") + 12)).replaceAll("موافق", "").replaceAll("OK", "").replace("[", "").replace("]", "").replace(",", "").replace(".00", "").replaceAll("\\s+", ""));
                            tv_post_mtn.setText(intent.getStringExtra("text").substring(intent.getStringExtra("text").indexOf("اللاحق الدفع:") + 13).replaceAll("موافق", "").replaceAll("OK", "").replace("[", "").replace("]", "").replace(",", "").replace(".00", "").replaceAll("\\s+", ""));
                            BalanceItem bi = dbb.getBalance();
                            bi.setMtnBalance(String.valueOf(Integer.valueOf(tv_pre_mtn.getText().toString().replaceAll(" ", ""))));
                            bi.setMtnBalance_(String.valueOf(Integer.valueOf(tv_post_mtn.getText().toString().replaceAll(" ", ""))));
                            dbb.updateBalance(bi);
                        }
                        if (!sharedPreferences.getBoolean("isMessageRead", true)) {
                            type = intent.getStringExtra("type");
                            if (repeatNum > 1 && !(type.matches("code_check_pre_mtn") || type.matches("code_check_pre_syr") || type.matches("code_check_post_syr"))) {
                                try {
                                    String code = "";
                                    String view_ = "";
                                    int numSim = 1;
                                    if (type.matches("code_pre_mtn")) {
                                        view_ = "و";
                                        code = sharedPreferences.getString("code_pre_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = mtnSim;
                                    } else if (type.matches("code_pre_syr")) {
                                        view_ = "و";
                                        code = sharedPreferences.getString("code_pre_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = syriatelSim;
                                    } else if (type.matches("code_post_mtn")) {
                                        view_ = "ف";
                                        code = sharedPreferences.getString("code_post_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = mtnSim;
                                    } else if (type.matches("code_post_syr")) {
                                        view_ = "ف";
                                        code = sharedPreferences.getString("code_post_syr", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = syriatelSim;
                                    } else if (type.matches("code_post_adsl_mtn")) {
                                        view_ = "ف";
                                        code = sharedPreferences.getString("code_post_adsl_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = mtnSim;
                                    } else if (type.matches("code_pre_who_mtn")) {
                                        view_ = "ج.و";
                                        code = sharedPreferences.getString("code_pre_who_mtn", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = mtnSim;
                                    } else if (type.matches("code_post_who_mtn")) {
                                        view_ = "ج.ف";
                                        code = sharedPreferences.getString("code_post_who_mtn", "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = mtnSim;
                                    } else if (type.matches("code_pre_post_who_syr")) {
                                        view_ = "ج";
                                        code = sharedPreferences.getString("code_pre_post_who_syr", "").replaceAll("C", cus_code.getText().toString()).replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
                                        numSim = syriatelSim;
                                    }
                                    transformBy(code, type, numSim);
                                    currentNumber = phone.getText().toString();
                                    currentAmount = spin_type.getSelectedItem().toString() + "/" + money.getText().toString() + "/" + balance.getText().toString() + "/" + view;
                                    currentDate = getDate() + " | " + getTime();
                                    currentCode = cus_code.getText().toString();
                                    if (repeatNum == 1) {
                                        phone.setText("");
                                        money.setText("");
                                        balance.setText("");
                                        repet.setText("");
                                        phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        if(type.contains("mtn")){
                                            try {
                                                String code__ = sharedPreferences.getString("code_check_pre_mtn", "").replace("#", Uri.encode("#"));
                                                transformBy(code__, "code_check_pre_mtn", mtnSim);
                                            } catch (Exception e) {
                                            }
                                        }else if (type.contains("syr")){
                                            try {
                                                String code__ = sharedPreferences.getString("code_check_pre_syr", "").replace("#", Uri.encode("#"));
                                                transformBy(code__, "code_check_pre_syr", syriatelSim);
                                            } catch (Exception e) {
                                            }
                                            isCheckingSyrActive = true;
                                        }
                                    }
                                    repeatNum--;
                                } catch (Exception e) {

                                }
                            }
                        }
                        hideKeyboard();
                        if (isCheckingSyrActive) {
                            try {
                                String code = sharedPreferences.getString("code_check_post_syr", "").replace("#", Uri.encode("#"));
                                transformBy(code, "code_check_post_syr", syriatelSim);
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                            }
                            isCheckingSyrActive = false;
                        }
                    }
            } catch (Exception e) {
                Toast.makeText(context, "حدث خطأ!", Toast.LENGTH_LONG).show();
                Toast.makeText(context, intent.getStringExtra("text").replace(", موافق", "").replace(", تم", "").replace(", OK", ""), Toast.LENGTH_LONG).show();
            }
        }
    }

    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    private BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("smsBroadcastReceiver", "onReceive");
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                            try {
                                if (sharedPreferences.getBoolean("isMessageRead", true)) {
                                    if (msg_from.matches("MTN")) {
                                        if (msgBody.contains("تم تحويل")) {
                                            if (msgBody.contains("مسبق الدفع")) {
                                                currentNumber = "0" + msgBody.substring(msgBody.indexOf("963") + 3).replace(" ", "");
                                                currentAmount = "ن" + "/" + getMoneyByBalance(msgBody.substring(msgBody.indexOf("ل"), msgBody.indexOf("ليرة")).replace(",", "").replace(" ", "").replace(".00", "").replace("ليرة", "").replace("ل", "")) + "/" + msgBody.substring(msgBody.indexOf("ل"), msgBody.indexOf("ليرة")).replace(",", "").replace(".00", "").replace(" ", "").replace("ليرة", "").replace("ل", "") + "/" + "و";
                                                currentDate = getDate() + " | " + getTime();
                                                currentCode = "";
                                                type = "نقدي";
                                                saveNewTransform("code_pre_mtn");
                                                if (!sharedPreferences.getBoolean("isMessageRead", true)) {
                                                    if (repeatNum > 1) {
                                                        transformWith("code_pre_mtn");
                                                    }
                                                }
                                            } else if (msgBody.contains("لاحق الدفع")) {
                                                currentNumber = "0" + msgBody.substring(msgBody.indexOf("963") + 3).replace(" ", "");
                                                currentAmount = "ن" + "/" + getMoneyByBalanceF(msgBody.substring(msgBody.indexOf("ل"), msgBody.indexOf("ليرة")).replace(",", "").replace(" ", "").replace(".00", "").replace("ليرة", "").replace("ل", "")) + "/" + msgBody.substring(msgBody.indexOf("ل"), msgBody.indexOf("ليرة")).replace(",", "").replace(".00", "").replace(" ", "").replace("ليرة", "").replace("ل", "") + "/" + "ف";
                                                currentDate = getDate() + " | " + getTime();
                                                currentCode = "";
                                                type = "نقدي";
                                                if (msgBody.substring(msgBody.indexOf("963") + 3, msgBody.indexOf("963") + 5).contains("9"))
                                                    saveNewTransform("code_post_mtn");
                                                else
                                                    saveNewTransform("code_post_adsl_mtn");
                                                if (!sharedPreferences.getBoolean("isMessageRead", true)) {
                                                    if (repeatNum > 1) {
                                                        if (msgBody.substring(msgBody.indexOf("963") + 3, msgBody.indexOf("963") + 5).contains("9"))
                                                            transformWith("code_post_mtn");
                                                        else
                                                            transformWith("code_post_adsl_mtn");
                                                    }
                                                }
                                            }
                                            if (isTransform) {
                                                if (!tv.getText().toString().contains("(تم)"))
                                                    tv.setText(Html.fromHtml("<font color=#0b3b39>" + tv.getText() + "</font><font color=#99cc00>(تم)</font>"));
                                                else if (!tv.getText().toString().contains("(لم يتم)"))
                                                    tv.setText(Html.fromHtml("<font color=#0b3b39>" + tv.getText() + "</font><font color=#FE2E2E>(لم يتم)</font>"));
                                                Toast.makeText(context, intent.getStringExtra("text").replace(", موافق", "").replace(", تم", "").replace(", OK", ""), Toast.LENGTH_LONG).show();
                                                isTransform = false;
                                            }
                                        } else if (msgBody.contains("تم استلام")) {
                                            refreshMtnData(true);
                                        }
                                    } else if (msg_from.matches("Abili")) {
                                        if (msgBody.contains("تم تعبئة")) {
                                            if (msgBody.contains("المشترك")) {
                                                currentNumber = msgBody.substring(msgBody.indexOf("0"), msgBody.indexOf("ب", msgBody.indexOf("المشترك"))).replace(" ", "").replace("ب", "");
                                                currentAmount = "ن" + "/" + getMoneyByBalance(msgBody.substring(msgBody.indexOf("ب", msgBody.indexOf("المشترك")), msgBody.indexOf("ليرة")).replace(",", "").replace(" ", "").replace("ليرة", "").replace("ب", "").replace("ل", "")) + "/" + msgBody.substring(msgBody.indexOf("ب", msgBody.indexOf("المشترك")), msgBody.indexOf("ليرة")).replace(" ", "").replace(",", "").replace("ليرة", "").replace("ب", "").replace("ل", "") + "/" + "و";
                                                currentDate = getDate() + " | " + getTime();
                                                currentCode = "";
                                                type = "نقدي";
                                                saveNewTransform("code_pre_syr");
                                            } else if (msgBody.contains("لاحق الدفع")) {
                                                Toast.makeText(context, msg_from, Toast.LENGTH_SHORT).show();
                                                Toast.makeText(context, msgBody, Toast.LENGTH_LONG).show();
                                                if (!currentNumber.matches("") || !currentAmount.matches("")) {
                                                    saveNewTransform("code_post_syr");
                                                }
                                            }
                                            if (isTransform) {
                                                if (!tv.getText().toString().contains("(تم)"))
                                                    tv.setText(Html.fromHtml("<font color=#0b3b39>" + tv.getText() + "</font><font color=#99cc00>(تم)</font>"));
                                                else if (!tv.getText().toString().contains("(لم يتم)"))
                                                    tv.setText(Html.fromHtml("<font color=#0b3b39>" + tv.getText() + "</font><font color=#FE2E2E>(لم يتم)</font>"));
                                                Toast.makeText(context, intent.getStringExtra("text").replace(", موافق", "").replace(", تم", "").replace(", OK", ""), Toast.LENGTH_LONG).show();
                                                isTransform = false;
                                            }
                                        } else if (msgBody.contains("تم خصم")) {
                                            Toast.makeText(context, msg_from, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(context, msgBody, Toast.LENGTH_LONG).show();
                                            try {
                                                String code = sharedPreferences.getString("code_check_pre_syr", "").replace("#", Uri.encode("#"));
                                                Intent serviceIntent = new Intent(context, USSDService.class);
                                                serviceIntent.putExtra("type", "_code_check_pre_syr");
                                                serviceIntent.putExtra("isAutomaticRefresh", true);
                                                context.startService(serviceIntent);
                                                Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                callIntent.setData(Uri.parse("tel:" + code));
                                                callIntent.putExtra("simSlot", syriatelSim - 1);
                                                callIntent.putExtra("com.android.phone.extra.slot", syriatelSim - 1);
                                                context.startActivity(callIntent);
                                            } catch (Exception e) {
                                                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, msg_from, Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, msgBody, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    };

    private String getMoneyByBalanceF(String replace) {
        String money = replace;
        if (Integer.valueOf(replace) < 1000)
            money = String.valueOf(Integer.valueOf(replace) + 50);
        else if (Integer.valueOf(replace) % 1000 == 0)
            money = String.valueOf(Integer.valueOf(replace) + (50 * (Integer.valueOf(replace) / 1000)));
        else if (Integer.valueOf(replace) % 1000 != 0)
            money = String.valueOf(Integer.valueOf(replace) + 50 + (50 * (Integer.valueOf(replace) / 1000)));
        return money;
    }

    private String getMoneyByBalance(String replace) {
        String money = replace;
        if (replace.matches("50")) {
            money = "75";
        } else if (replace.matches("75")) {
            money = "100";
        } else if (replace.matches("90")) {
            money = "100";
        } else if (replace.matches("100")) {
            money = "125";
        } else if (replace.matches("105")) {
            money = "125";
        } else if (replace.matches("150")) {
            money = "175";
        } else if (replace.matches("200")) {
            money = "225";
        } else if (replace.matches("225")) {
            money = "250";
        } else if (replace.matches("250")) {
            money = "300";
        } else if (replace.matches("300")) {
            money = "350";
        } else if (replace.matches("450")) {
            money = "500";
        } else if (replace.matches("500")) {
            money = "550";
        } else if (replace.matches("650")) {
            money = "750";
        } else if (replace.matches("750")) {
            money = "850";
        } else if (replace.matches("900")) {
            money = "1000";
        } else if (replace.matches("1000")) {
            money = "1100";
        } else if (replace.matches("1000")) {
            money = "1350";
        } else if (replace.matches("1500")) {
            money = "1650";
        } else if (replace.matches("2000")) {
            money = "2200";
        } else if (replace.matches("4000")) {
            money = "4400";
        } else if (replace.matches("5000")) {
            money = "5500";
        } else if (replace.matches("10000")) {
            money = "11000";
        } else if (replace.matches("15000")) {
            money = "16500";
        } else if (replace.matches("20000")) {
            money = "22000";
        }
        return money;
    }

    void refreshSyrPreData() {
        if (sharedPreferences.getInt("timeRefreshData", 600000) != 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        String code = sharedPreferences.getString("code_check_pre_syr", "").replace("#", Uri.encode("#"));
                        Intent serviceIntent = new Intent(context, USSDService.class);
                        serviceIntent.putExtra("type", "_code_check_pre_syr");
                        serviceIntent.putExtra("isAutomaticRefresh", true);
                        context.startService(serviceIntent);
                        Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        callIntent.setData(Uri.parse("tel:" + code));
                        callIntent.putExtra("simSlot", syriatelSim - 1);
                        callIntent.putExtra("com.android.phone.extra.slot", syriatelSim - 1);
                        context.startActivity(callIntent);
                    } catch (Exception e) {
                        Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                    }
                    refreshSyrPreData();
                }
            }, sharedPreferences.getInt("timeRefreshData", 600000));
        }
    }

    void refreshSyrPostData(boolean isQuickly) {
        if (isQuickly) {
            try {
                String code = sharedPreferences.getString("code_check_post_syr", "").replace("#", Uri.encode("#"));
                Intent serviceIntent = new Intent(context, USSDService.class);
                serviceIntent.putExtra("type", "_code_check_post_syr");
                serviceIntent.putExtra("isAutomaticRefresh", true);
                context.startService(serviceIntent);
                Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + code));
                callIntent.putExtra("simSlot", syriatelSim - 1);
                callIntent.putExtra("com.android.phone.extra.slot", syriatelSim - 1);
                context.startActivity(callIntent);
            } catch (Exception e) {
                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
            }
            refreshSyrPostData(false);
        } else {
            if (sharedPreferences.getInt("timeRefreshData", 600000) != 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String code = sharedPreferences.getString("code_check_post_syr", "").replace("#", Uri.encode("#"));
                            Intent serviceIntent = new Intent(context, USSDService.class);
                            serviceIntent.putExtra("type", "_code_check_post_syr");
                            serviceIntent.putExtra("isAutomaticRefresh", true);
                            context.startService(serviceIntent);
                            Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            callIntent.setData(Uri.parse("tel:" + code));
                            callIntent.putExtra("simSlot", syriatelSim - 1);
                            callIntent.putExtra("com.android.phone.extra.slot", syriatelSim - 1);
                            context.startActivity(callIntent);
                        } catch (Exception e) {
                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                        }
                        refreshSyrPostData(false);
                    }
                }, sharedPreferences.getInt("timeRefreshData", 600000));
            }
        }
    }

    void refreshMtnData(boolean isQuickly) {
        if (isQuickly) {
            try {
                String code = sharedPreferences.getString("code_check_pre_mtn", "").replace("#", Uri.encode("#"));
                Intent serviceIntent = new Intent(context, USSDService.class);
                serviceIntent.putExtra("type", "_code_check_pre_mtn");
                context.startService(serviceIntent);
                Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + code));
                callIntent.putExtra("simSlot", mtnSim - 1);
                callIntent.putExtra("com.android.phone.extra.slot", mtnSim - 1);
                context.startActivity(callIntent);
            } catch (Exception e) {
                Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
            }
            refreshMtnData(false);
        } else {
            if (sharedPreferences.getInt("timeRefreshData", 600000) != 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String code = sharedPreferences.getString("code_check_pre_mtn", "").replace("#", Uri.encode("#"));
                            Intent serviceIntent = new Intent(context, USSDService.class);
                            serviceIntent.putExtra("type", "_code_check_pre_mtn");
                            context.startService(serviceIntent);
                            Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            callIntent.setData(Uri.parse("tel:" + code));
                            callIntent.putExtra("simSlot", mtnSim - 1);
                            callIntent.putExtra("com.android.phone.extra.slot", mtnSim - 1);
                            context.startActivity(callIntent);
                        } catch (Exception e) {
                            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                        }
                        refreshMtnData(false);
                    }
                }, sharedPreferences.getInt("timeRefreshData", 600000));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(smsBroadcastReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(smsBroadcastReceiver);
    }

    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    private void transformWith(String type) {
        try {
            isTransform = true;
            hideKeyboard();
            String code = sharedPreferences.getString(type, "").replaceAll("M", balance.getText().toString()).replaceAll("N", phone.getText().toString()).replace("#", Uri.encode("#"));
            Intent serviceIntent = new Intent(context, USSDService.class);
            serviceIntent.putExtra("type", type);
            context.startService(serviceIntent);
            Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel:" + code));
            hideKeyboard();
            if (simType == 1) {
                callIntent.putExtra("simSlot", mtnSim - 1);
                callIntent.putExtra("com.android.phone.extra.slot", mtnSim - 1);
            } else {
                callIntent.putExtra("simSlot", syriatelSim - 1);
                callIntent.putExtra("com.android.phone.extra.slot", syriatelSim - 1);
            }
            context.startActivity(callIntent);
            String view = "";
            String view_ = "";
            if (type.matches("code_pre_mtn") || mode.matches("code_pre_syr")) {
                view = "وحدات";
                view_ = "و";
            } else if (type.matches("code_post_mtn") || mode.matches("code_post_syr")) {
                view = "فاتورة";
                view_ = "ف";
            } else if (type.matches("code_post_adsl_mtn")) {
                view = "فاتورة ADSL";
                view_ = "ف";
            } else if (type.matches("code_pre_who_mtn")) {
                view = "جملة وحدات";
                view_ = "ج.و";
            } else if (type.matches("code_post_who_mtn")) {
                view = "جملة فواتير";
                view_ = "ج.ف";
            } else if (type.matches("code_pre_post_who_syr")) {
                view = "جملة";
                view_ = "ج";
            }
            if (!(type.matches("code_check_pre_mtn") || type.matches("code_check_pre_syr") || type.matches("code_check_post_syr"))) {
                currentNumber = phone.getText().toString();
                currentAmount = spin_type.getSelectedItem().toString() + "/" + money.getText().toString() + "/" + balance.getText().toString() + "/" + view;
                currentDate = getDate() + " | " + getTime();
                currentCode = cus_code.getText().toString();
                phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            repeatNum--;
            if (repeatNum == 1) {
                phone.setText("");
                money.setText("");
                balance.setText("");
                repet.setText("");
            }
        } catch (Exception e) {
            Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
        }
    }

    private String getDate() {
        String date = "";
        Date now = new Date();
        Date alsoNow = Calendar.getInstance().getTime();
        date = new SimpleDateFormat("yyyy/MM/dd").format(now);
        return arabicToDecimal(date);
    }

    private String getTime() {
        String time = "";
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm");
        time = simpledateformat.format(calander.getTime());
        return arabicToDecimal(time);
    }

    private void saveNewTransform(String type1) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        OpenSQLDatabaseHandler o = new OpenSQLDatabaseHandler(context);
        o.allOpens();
        if (!sharedPreferences.getBoolean("isActive", false)) {
            sharedPreferences.edit().putLong("trialTransforms", sharedPreferences.getLong("trialTransforms", Long.valueOf(telephonyManager.getDeviceId())) + 1).commit();
            o.updateOpen(new OpenItem(1, String.valueOf(Long.valueOf(o.getOpen().getOpen()) + 1)));
            try {
                o.backup(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "open.db");
            } catch (Exception e) {
                Toast.makeText(context, "adssdsd", Toast.LENGTH_LONG).show();
            }
        }
        dbb = new BalanceSQLDatabaseHandler(context);
        db = new TransformSQLDatabaseHandler(context);
        db.allTransforms();
        TransformListChildItem transform = new TransformListChildItem();
        transform.setColumn(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("currentTransformColumn", 0) + 1);
        try {
            currentName = dbc.getCustomer(currentNumber).getName();
        } catch (Exception e) {
            currentName = "";
        }
        transform.setName(currentName);
        transform.setNumber(currentNumber);
        transform.setAmount(currentAmount);
        transform.setDate(currentDate);
        transform.setCode(currentCode);
        transform.setDebt(type);
        db.addTransform(transform);
        int prof = 5300;
        int j = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("currentTransformColumn", 0);
        context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putInt("currentTransformColumn", j + 1).commit();
        BalanceItem b = new BalanceItem();
        b.setColumn(1);
        if (type.matches("نقدي")) {
            b.setBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getBalance()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1)))));
            b.setDebt(dbb.getBalance().getDebt());
        } else {
            b.setDebt(String.valueOf(Integer.valueOf(dbb.getBalance().getDebt()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1)))));
            b.setBalance(dbb.getBalance().getBalance());
        }
        if (type1.matches("code_pre_syr")) {
            prof = Integer.valueOf(sharedPreferences.getString("syr_pre_pur_price", "5300"));
            b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
            if (!dbb.getBalance().getSyrBalance().matches("0")) {
                if (type.matches("نقدي"))
                    b.setSyrBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                else
                    b.setSyrBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance())));
            } else
                b.setSyrBalance("0");
            b.setSyrBalance_(dbb.getBalance().getSyrBalance_());
            b.setMtnBalance(dbb.getBalance().getMtnBalance());
            b.setMtnBalance_(dbb.getBalance().getMtnBalance_());
        } else if (type1.matches("code_pre_mtn")) {
            prof = Integer.valueOf(sharedPreferences.getString("mtn_pre_pur_price", "5300"));
            b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
            if (!dbb.getBalance().getMtnBalance().matches("0")) {
                if (type.matches("نقدي"))
                    b.setMtnBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                else
                    b.setMtnBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance())));
            } else
                b.setMtnBalance("0");
            b.setSyrBalance_(dbb.getBalance().getSyrBalance_());
            b.setSyrBalance(dbb.getBalance().getSyrBalance());
            b.setMtnBalance_(dbb.getBalance().getMtnBalance_());
        } else if (type1.matches("code_post_syr")) {
            prof = Integer.valueOf(sharedPreferences.getString("syr_post_pur_price", "5100"));
            b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
            if (!dbb.getBalance().getSyrBalance_().matches("0")) {
                if (type.matches("نقدي"))
                    b.setSyrBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance_()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                else
                    b.setSyrBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance_())));
            } else
                b.setSyrBalance_("0");
            b.setSyrBalance(dbb.getBalance().getSyrBalance());
            b.setMtnBalance(dbb.getBalance().getMtnBalance());
            b.setMtnBalance_(dbb.getBalance().getMtnBalance_());
        } else if (type1.matches("code_post_mtn")) {
            prof = Integer.valueOf(sharedPreferences.getString("mtn_post_pur_price", "5100"));
            b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
            if (!dbb.getBalance().getMtnBalance_().matches("0")) {
                if (type.matches("نقدي"))
                    b.setMtnBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance_()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                else
                    b.setMtnBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance_())));
            } else
                b.setMtnBalance_("0");
            b.setSyrBalance(dbb.getBalance().getSyrBalance());
            b.setMtnBalance(dbb.getBalance().getMtnBalance());
            b.setSyrBalance_(dbb.getBalance().getSyrBalance_());
        } else if (type1.matches("code_post_adsl_mtn")) {
            prof = Integer.valueOf(sharedPreferences.getString("mtn_post_a_pur_price", "5100"));
            b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
            if (!dbb.getBalance().getMtnBalance_().matches("0")) {
                if (type.matches("نقدي"))
                    b.setMtnBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance_()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                else
                    b.setMtnBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance_())));
            } else
                b.setMtnBalance_("0");
            b.setSyrBalance(dbb.getBalance().getSyrBalance());
            b.setMtnBalance(dbb.getBalance().getMtnBalance());
            b.setSyrBalance_(dbb.getBalance().getSyrBalance_());
        } else if (type1.matches("code_pre_who_mtn")) {
            if (!sharedPreferences.getString("mtn_pre_pur_price", "5300").matches(""))
                prof = Integer.valueOf(sharedPreferences.getString("mtn_pre_who_pur_price", "5300"));
            else
                prof = 5300;
            b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
            if (!dbb.getBalance().getMtnBalance().matches("0")) {
                if (type.matches("نقدي"))
                    b.setMtnBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                else
                    b.setMtnBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance())));
            } else
                b.setMtnBalance("0");
            b.setSyrBalance_(dbb.getBalance().getSyrBalance_());
            b.setSyrBalance(dbb.getBalance().getSyrBalance());
            b.setMtnBalance_(dbb.getBalance().getMtnBalance_());
        } else if (type1.matches("code_post_who_mtn")) {
            if (!sharedPreferences.getString("mtn_post_who_pur_price", "5100").matches(""))
                prof = Integer.valueOf(sharedPreferences.getString("mtn_post_who_pur_price", "5100"));
            else
                prof = 5100;
            b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
            if (!dbb.getBalance().getMtnBalance_().matches("0")) {
                if (type.matches("نقدي"))
                    b.setMtnBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance_()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                else
                    b.setMtnBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getMtnBalance_())));
            } else
                b.setMtnBalance_("0");
            b.setSyrBalance(dbb.getBalance().getSyrBalance());
            b.setMtnBalance(dbb.getBalance().getMtnBalance());
            b.setSyrBalance_(dbb.getBalance().getSyrBalance_());
        } else if (type1.matches("code_pre_post_who_syr")) {
            if (isSyrWhoPre) {
                if (!sharedPreferences.getString("syr_pre_who_pur_price", "5300").matches(""))
                    prof = Integer.valueOf(sharedPreferences.getString("syr_pre_who_pur_price", "5300"));
                else
                    prof = 5300;
                b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
                if (!dbb.getBalance().getSyrBalance().matches("0")) {
                    if (type.matches("نقدي"))
                        b.setSyrBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                    else
                        b.setSyrBalance(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance())));
                } else
                    b.setSyrBalance("0");
                b.setSyrBalance_(dbb.getBalance().getSyrBalance_());
                b.setMtnBalance(dbb.getBalance().getMtnBalance());
                b.setMtnBalance_(dbb.getBalance().getMtnBalance_());
            } else {
                if (!sharedPreferences.getString("syr_post_who_pur_price", "5100").matches(""))
                    prof = Integer.valueOf(sharedPreferences.getString("syr_post_who_pur_price", "5100"));
                else
                    prof = 5100;
                b.setProfits(String.valueOf(Integer.valueOf(dbb.getBalance().getProfits()) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))) - ((Integer.valueOf(currentAmount.substring(currentAmount.indexOf("/", currentAmount.indexOf("/") + 1) + 1, currentAmount.lastIndexOf("/"))) * prof) / 5000)));
                if (!dbb.getBalance().getSyrBalance_().matches("0")) {
                    if (type.matches("نقدي"))
                        b.setSyrBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance_()) - Integer.valueOf(Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1))))));
                    else
                        b.setSyrBalance_(String.valueOf(Integer.valueOf(dbb.getBalance().getSyrBalance_())));
                } else
                    b.setSyrBalance_("0");
                b.setSyrBalance(dbb.getBalance().getSyrBalance());
                b.setMtnBalance(dbb.getBalance().getMtnBalance());
                b.setMtnBalance_(dbb.getBalance().getMtnBalance_());
            }
        }
        dbb.updateBalance(b);
        boolean isNumberSaved = false;
        for (int i = 0; i < dbc.allCustomers().size(); i++) {
            if (currentNumber.matches(dbc.allCustomers().get(i).getNumber())) {
                isNumberSaved = true;
                break;
            }
        }
        if (isNumberSaved) {
            CustomerListChildItem customer = dbc.getCustomer(currentNumber);
            String cash = String.valueOf(Integer.valueOf(customer.getBalance().substring(6, customer.getBalance().indexOf("دين: ")).replaceAll(" ", "")));
            String debt = String.valueOf(Integer.valueOf(customer.getBalance().substring(customer.getBalance().indexOf("دين: ") + 5).replaceAll(" ", "")));
            if (type.matches("نقدي"))
                cash = String.valueOf(Integer.valueOf(customer.getBalance().substring(6, customer.getBalance().indexOf("دين: ")).replaceAll(" ", "")) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1)).replaceAll(" ", "")));
            else
                debt = String.valueOf(Integer.valueOf(customer.getBalance().substring(customer.getBalance().indexOf("دين: ") + 5).replaceAll(" ", "")) + Integer.valueOf(currentAmount.substring(2, currentAmount.indexOf("/", currentAmount.indexOf("/") + 1)).replaceAll(" ", "")));
            dbc.updateCustomer(new CustomerListChildItem(customer.getColumn(), customer.getName(), customer.getNumber(), String.valueOf(Integer.valueOf(customer.getAmount()) + 1), "نقدي: " + cash + " " + "دين: " + debt, customer.getCode(), customer.getMode()));

        }
    }

    private void createAddNewDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_add_new);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        edit_number = (EditText) d.findViewById(R.id.edit_number);
        edit_name = (EditText) d.findViewById(R.id.edit_name);
        edit_code = (EditText) d.findViewById(R.id.edit_code);
        btn_save = (Button) d.findViewById(R.id.btn_save);
        btn_close = (Button) d.findViewById(R.id.btn_close);
        edit_number.setText(phone.getText().toString());
        edit_code.setText(cus_code.getText().toString());
        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edit_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_number.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spin_operation_ = (Spinner) d.findViewById(R.id.spin_operation);
        String[] transformTypes = res.getStringArray(R.array.transformSpecial);
        ArrayAdapter<CharSequence> spinnerOperArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item_operation, transformTypes);
        spinnerOperArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_operation);
        spin_operation_.setAdapter(spinnerOperArrayAdapter);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_number.getText().toString().matches(".*[0-9]+.*") && edit_name.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                    if (edit_number.getText().toString().length() > 8) {
                        boolean isNewName = true;
                        boolean isNewNumber = true;
                        for (int i = 0; i < dbc.allCustomers().size(); i++) {
                            if (edit_number.getText().toString().matches(dbc.allCustomers().get(i).getNumber())) {
                                isNewNumber = false;
                                break;
                            }
                            if (edit_name.getText().toString().matches(dbc.allCustomers().get(i).getName())) {
                                isNewName = false;
                                break;
                            }
                        }
                        if (isNewName && isNewNumber) {
                            dbc = new CustomerSQLDatabaseHandler(context);
                            CustomerListChildItem customer = new CustomerListChildItem();
                            customer.setColumn(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("currentCustomerColumn", 0) + 1);
                            customer.setName(edit_name.getText().toString());
                            customer.setNumber(edit_number.getText().toString());
                            customer.setBalance("نقدي: 0 دين: 0");
                            customer.setAmount("0");
                            customer.setCode(edit_code.getText().toString());
                            customer.setMode(mode_);
                            dbc.addCustomer(customer);
                            context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putInt("currentCustomerColumn", context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("currentCustomerColumn", 0) + 1).commit();
                            listAllCustomersPanel = new ArrayList<>();
                            for (int l = db.allTransforms().size() - 1; l >= 0; l--) {
                                for (int m = 0; m < dbc.allCustomers().size(); m++) {
                                    if (dbc.allCustomers().get(m).getName().matches(db.allTransforms().get(l).getName()))
                                        listAllCustomersPanel.add(dbc.allCustomers().get(m));
                                }
                            }
                            List<CustomerListChildItem> allEvents = listAllCustomersPanel;
                            List<CustomerListChildItem> noRepeat = new ArrayList<CustomerListChildItem>();
                            for (CustomerListChildItem event : allEvents) {
                                boolean isFound = false;
                                // check if the event name exists in noRepeat
                                for (CustomerListChildItem e : noRepeat) {
                                    if (e.getName().equals(event.getName()) || (e.equals(event))) {
                                        isFound = true;
                                        break;
                                    }
                                }
                                if (!isFound) noRepeat.add(event);
                            }
                            listAllCustomersPanel.clear();
                            listAllCustomersPanel.addAll(noRepeat);
                            customCustomerEditArrayAdapterPanel = new CustomCustomerEditArrayAdapter(context, R.layout.list_customer_edit_item, listAllCustomersPanel);
                            customersList.setAdapter(customCustomerEditArrayAdapterPanel);
                            d.cancel();
                        } else if (!isNewName) {
                            edit_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            Toast.makeText(context, "الرقم موجود مسبقاً", Toast.LENGTH_LONG).show();
                        } else if (!isNewNumber) {
                            edit_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            Toast.makeText(context, "الرقم موجود مسبقاً", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        edit_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                        Toast.makeText(context, "الرقم صغير", Toast.LENGTH_LONG).show();
                    }
                } else if (!edit_number.getText().toString().matches(".*[0-9]+.*")) {
                    edit_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                } else {
                    edit_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
            }
        });
        spin_operation_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                switch (index) {
                    case 0:
                        mode_ = "code_pre_mtn";
                        break;
                    case 1:
                        mode_ = "code_pre_syr";
                        break;
                    case 2:
                        mode_ = "code_post_mtn";
                        break;
                    case 3:
                        mode_ = "code_post_syr";
                        break;
                    case 4:
                        mode_ = "code_post_adsl_mtn";
                        break;
                    case 5:
                        mode_ = "code_pre_post_who_syr";
                        break;
                    case 6:
                        mode_ = "code_pre_who_mtn";
                        break;
                    case 7:
                        mode_ = "code_post_who_mtn";
                        break;
                    case 8:
                        mode_ = "code_check";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spin_operation_.setSelection(0);
        d.show();
    }

    Menu men;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        men = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_b_contact:
                MainActivity m2 = (MainActivity) getActivity();
                m2.readContact();
                return true;
            case R.id.menu_settings:
                MainActivity m = (MainActivity) getActivity();
                m.result.setSelection(8);
                return true;
            case R.id.menu_b_about:
                MainActivity m1 = (MainActivity) getActivity();
                m1.createAboutDialog();
                return true;
            case R.id.menu_b_exit:
                getActivity().finish();
                getActivity().finishAffinity();
                getActivity().moveTaskToBack(true);
                return true;
            case android.R.id.home:
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method called when this Activity finished
     * Override this method to unregister MyBroadCastReceiver
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // make sure to unregister your receiver after finishing of this activity
        try {
            context.unregisterReceiver(myBroadCastReceiver);
        } catch (Exception e) {

        }
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);

        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.fragment_panel;
                    View view = inflater.inflate(resId, null);
                    ScrollView layout = (ScrollView) view.findViewById(R.id.scroll_panel);
                    panel = (LinearLayout) layout.findViewById(R.id.panel);
                    tv1 = new TextView(context);
                    tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv1.setTextSize(20);
                    tv1.setTextColor(res.getColor(R.color.spec_black));
                    tv1.setText(" - التأكد من الإعدادات");
                    panel.addView(tv1);
                    ((ViewPager) collection).addView(view, 0);
                    return view;
                case 1:
                    resId = R.layout.fragment_list;
                    View view1 = inflater.inflate(resId, null);
                    RelativeLayout linear = (RelativeLayout) view1.findViewById(R.id.linearList);
                    customersList = (ListView) linear.findViewById(R.id.list_customers_f);
                    txt_hint = (TextView) linear.findViewById(R.id.txt_hint);
                    txt_hint.setVisibility(View.GONE);
                    listAllCustomersPanel = new ArrayList<>();
                    if(sharedPreferences.getInt("NumberListType", 0) == 0) {
                        for (int l = db.allTransforms().size() - 1; l >= 0; l--) {
                            CustomerListChildItem cl = new CustomerListChildItem();
                            TransformListChildItem tl = db.allTransforms().get(l);
                            cl.setNumber(tl.getNumber());
                            boolean isCusExist = false;
                            String cus_name = "*بدون اسم*";
                            for (int m = 0; m < dbc.allCustomers().size(); m++) {
                                if (dbc.allCustomers().get(m).getName().matches(tl.getName()))
                                    cus_name = dbc.allCustomers().get(m).getName();
                            }
                            cl.setName(cus_name);
                            cl.setMode(tl.getAmount().substring(tl.getAmount().lastIndexOf("/")+1));
                            cl.setCode(tl.getCode());
                            int tot = 0;
                            for (int s = db.allTransforms().size() - 1; s >= 0; s--) {
                                TransformListChildItem tl1 = db.allTransforms().get(s);
                                    if (tl1.getNumber().matches(cl.getNumber()))
                                        tot  = tot + Integer.valueOf(tl1.getAmount().substring(tl1.getAmount().indexOf("/",tl1.getAmount().indexOf("/")+1)+1,tl1.getAmount().lastIndexOf("/")));
                            }
                            cl.setBalance(tl.getAmount().substring(tl.getAmount().indexOf("/",tl.getAmount().indexOf("/")+1)+1,tl.getAmount().lastIndexOf("/"))+ "  " + tl.getDate().substring(0,tl.getDate().indexOf("|")).replaceAll(" ","") + "t" + String.valueOf(tot));
                            if(!listAllCustomersPanel.contains(cl))
                            listAllCustomersPanel.add(cl);
                        }
                        txt_hint.setText("آخر الأرقام المحولة");
                    }else if (sharedPreferences.getInt("NumberListType", 0) == 1) {
                        listAllCustomersPanel.addAll(dbc.allCustomers());
                        for(int i =0 ; i < listAllCustomersPanel.size();i++){
                            CustomerListChildItem cl = listAllCustomersPanel.get(i);
                            int tot = 0;
                            String last = "";
                            for (int s = db.allTransforms().size() - 1; s >= 0; s--) {
                                TransformListChildItem tl = db.allTransforms().get(s);
                                if (tl.getNumber().matches(cl.getNumber()))
                                    tot  = tot + Integer.valueOf(tl.getAmount().substring(tl.getAmount().indexOf("/",tl.getAmount().indexOf("/")+1)+1,tl.getAmount().lastIndexOf("/")));
                            }
                            for (int l = db.allTransforms().size() - 1; l >= 0; l--) {
                                TransformListChildItem tl = db.allTransforms().get(l);
                                if (tl.getNumber().matches(cl.getNumber())) {
                                    last = tl.getAmount().substring(tl.getAmount().indexOf("/",tl.getAmount().indexOf("/")+1)+1,tl.getAmount().lastIndexOf("/"))+ "  " + tl.getDate().substring(0,tl.getDate().indexOf("|")).replaceAll(" ","");
                                }
                            }
                            cl.setBalance(last + "t" + String.valueOf(tot));
                            listAllCustomersPanel.set(i,cl);
                        }
                        txt_hint.setText("الأسماء المحفوظة");
                    } else {
                        for (int l = db.allTransforms().size() - 1; l >= 0; l--) {
                            for (int m = 0; m < dbc.allCustomers().size(); m++) {
                                if (dbc.allCustomers().get(m).getName().matches(db.allTransforms().get(l).getName()))
                                    listAllCustomersPanel.add(dbc.allCustomers().get(m));
                            }
                        }
                        List<CustomerListChildItem> allEvents = listAllCustomersPanel;
                        List<CustomerListChildItem> noRepeat = new ArrayList<CustomerListChildItem>();
                        for (CustomerListChildItem event : allEvents) {
                            boolean isFound = false;
                            // check if the event name exists in noRepeat
                            for (CustomerListChildItem e : noRepeat) {
                                if (e.getName().equals(event.getName()) || (e.equals(event))) {
                                    isFound = true;
                                    break;
                                }
                            }
                            if (!isFound) noRepeat.add(event);
                        }
                        listAllCustomersPanel.clear();
                        listAllCustomersPanel.addAll(noRepeat);
                        for(int i =0 ; i < listAllCustomersPanel.size();i++){
                            CustomerListChildItem cl = listAllCustomersPanel.get(i);
                            int tot = 0;
                            String last = "";
                            for (int s = db.allTransforms().size() - 1; s >= 0; s--) {
                                TransformListChildItem tl = db.allTransforms().get(s);
                                if (tl.getNumber().matches(cl.getNumber()))
                                    tot  = tot + Integer.valueOf(tl.getAmount().substring(tl.getAmount().indexOf("/",tl.getAmount().indexOf("/")+1)+1,tl.getAmount().lastIndexOf("/")));
                            }
                            for (int l = db.allTransforms().size() - 1; l >= 0; l--) {
                                TransformListChildItem tl = db.allTransforms().get(l);
                                if (tl.getNumber().matches(cl.getNumber())) {
                                    last = tl.getAmount().substring(tl.getAmount().indexOf("/",tl.getAmount().indexOf("/")+1)+1,tl.getAmount().lastIndexOf("/"))+ "  " + tl.getDate().substring(0,tl.getDate().indexOf("|")).replaceAll(" ","");
                                }
                            }
                            cl.setBalance(last + "t" + String.valueOf(tot));
                            listAllCustomersPanel.set(i,cl);
                        }
                        txt_hint.setText("آخر الأسماء المحفوظة المحولة");
                    }
                    customCustomerEditArrayAdapterPanel = new CustomCustomerEditArrayAdapter(context, R.layout.list_customer_edit_item, listAllCustomersPanel);
                    customersList.setAdapter(customCustomerEditArrayAdapterPanel);
                    if (listAllCustomersPanel.size() == 0)
                        txt_hint.setVisibility(VISIBLE);
                    customersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int j, long g) {
                            LinearLayout linear = (LinearLayout) view;
                            TextView txtName = (TextView) linear.findViewById(R.id.txtName);
                            TextView txtNumber = (TextView) linear.findViewById(R.id.txtNumber);
                            for (int i = 0; i < dbc.allCustomers().size(); i++) {
                                if (dbc.allCustomers().get(i).getName().matches(txtName.getText().toString())) {
                                    phone.setText(dbc.allCustomers().get(i).getNumber());
                                    if (cus_code.getVisibility() == VISIBLE)
                                        cus_code.setText(dbc.allCustomers().get(i).getCode());
                                }
                            }
                        }
                    });

                    ((ViewPager) collection).addView(view1, 0);
                    return view1;
            }
            return view;
        }

        LinearLayout getPanel(View viewPager) {
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = R.layout.fragment_panel;
            View view = inflater.inflate(resId, null);
            ScrollView layout = (ScrollView) view.findViewById(R.id.scroll_panel);
            LinearLayout panel_ = (LinearLayout) layout.findViewById(R.id.panel);
            return panel_;
        }

        ListView getList(View viewPager) {
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = R.layout.fragment_list;
            View view = inflater.inflate(resId, null);
            RelativeLayout linear = (RelativeLayout) view.findViewById(R.id.linearList);
            ListView customersList1 = (ListView) linear.findViewById(R.id.list_customers_f);
            return customersList1;
        }
    }
}
