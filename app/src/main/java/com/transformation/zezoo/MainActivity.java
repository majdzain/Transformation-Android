package com.transformation.zezoo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.codekidlabs.storagechooser.Content;
import com.codekidlabs.storagechooser.StorageChooser;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    Resources res;
    AccountHeader headerResult = null;
    Drawer result = null;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBar;
    Context context = this;
    final int quick = 1, special = 2, recoder = 3, names = 4, save = 5, inventory = 6, backup = 7, settings = 8, enable = 9, contact = 10;
    EditText edit_code, edit_code_, edit_number, edit_name, edit_code__;
    Button btn_edit_bal,btn_request, btn_later, btn_enable, btn_close, btn_save, btn_t_backup, btn_t_restore, btn_c_backup, btn_c_restore, btn_debt_recorder, btn_reset, btn_b_backup, btn_b_restore, btn_exist_key;
    Spinner spin_oper;
    PrimaryDrawerItem bdiEnable;
    CustomerSQLDatabaseHandler db;
    String mode;
    TextView txt_balance, txt_profits, txt_debt, txt_balance_s, txt_balance_s_, txt_balance_m, txt_balance_m_,txt_balance_, txt_profits_, txt_debt_, txt_balance_ss, txt_balance_ss_, txt_balance_mm, txt_balance_mm_;
    ProgressBar progressBar;
    BalanceSQLDatabaseHandler dbb;
    AnimationDrawable syrAnm, syrAnm_, mtnAnm, mtnAnm_;
    ImageButton btn_refresh_syr, btn_refresh_syr_, btn_refresh_mtn, btn_refresh_mtn_;
    SharedPreferences sharedPreferences;
    int mtnSim;
    int syriatelSim;
    int selectionMode = 0;
    KeySQLDatabaseHandler dbk;
    TextView txt_user, txt_error;
    TextInputLayout til_pass;
    EditText edit_pass;
    Button btn_enter;
    IProfile profile;
    TransformSQLDatabaseHandler dbt;
    Button btn_enable_a, btn_contact;
    OpenSQLDatabaseHandler dbo;
    EditText edit_bal;
    private boolean isBalanceEditingRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        signupAsFirst();
        if (!getIntent().getBooleanExtra("isLogin", false))
            lockIfThere();
        detectInformationAsSecond();
        setContentView(R.layout.activity_main);
        forceRTLIfSupported();
        res = getResources();
        db = new CustomerSQLDatabaseHandler(context);
        setDrawer(savedInstanceState);
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

    private void detectInformationAsSecond() {
    }

    private void setDrawer(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setTitle("Accountant");
        toolbar.setTitleTextColor(res.getColor(R.color.white));
        toolbar.setBackgroundColor(res.getColor(R.color.purple));
        setSupportActionBar(toolbar);
        profile = null;
        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        if (!getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("managerName", "").matches(".*[a-zA-Z0-9أ-ي]+.*")) {
            profile = new ProfileDrawerItem().withEmail(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", "")).withIcon(R.mipmap.profile).withIdentifier(100).withSelectedTextColorRes(R.color.spec_black).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    Fragment f = new LoginFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentMain, f);
                    ft.commit();
                    return true;
                }
            });
        } else {
            profile = new ProfileDrawerItem().withName(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("managerName", "")).withEmail(getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", "مستخدم")).withIcon(R.mipmap.profile).withIdentifier(100).withSelectedTextColorRes(R.color.spec_black).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    Fragment f = new LoginFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentMain, f);
                    ft.commit();
                    return true;
                }
            });
        }
        // Create the AccountHeader
        // Tie DrawerLayout events to the ActionBarToggle
        headerResult = new AccountHeaderBuilder()
                .withActivity(this).withHeaderBackground(R.drawable.drawer_header4)
                .withTranslucentStatusBar(true).withTextColorRes(R.color.sugar).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf"))
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        result.closeDrawer();
                        Fragment f = new LoginFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragmentMain, f);
                        ft.commit();
                        return true;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

//create the drawer and remember the `Drawer` result object

        bdiEnable = new PrimaryDrawerItem().withName("شراء التطبيق").withIcon(GoogleMaterial.Icon.gmd_local_grocery_store).withIdentifier(enable).withTextColorRes(R.color.red).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withSelectable(false);
        if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isActive", false))
            bdiEnable.withTextColorRes(R.color.black_overlay).withSelectedIconColorRes(R.color.black_overlay).withIconColorRes(R.color.black_overlay).withEnabled(false).withSelectable(false);
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SectionDrawerItem().withName("أساسي").withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("الواجهة السريعة").withIcon(GoogleMaterial.Icon.gmd_dashboard).withIdentifier(quick).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("تحويل مخصص").withIcon(GoogleMaterial.Icon.gmd_stars).withIdentifier(special).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("سجل التحويل").withIcon(GoogleMaterial.Icon.gmd_format_list_bulleted).withIdentifier(recoder).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("الأسماء المحفوظة").withIcon(GoogleMaterial.Icon.gmd_contacts).withIdentifier(names).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new SectionDrawerItem().withName("أدوات").withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("حفظ رقم").withIcon(GoogleMaterial.Icon.gmd_person_add).withIdentifier(save).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withSelectable(false),
                        new PrimaryDrawerItem().withName("جرد الرصيد والأرباح").withIcon(GoogleMaterial.Icon.gmd_attach_money).withIdentifier(inventory).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withSelectable(false),
                        new SectionDrawerItem().withName("التفضيلات").withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("النسخ الاحتياطي والإستعادة").withIcon(GoogleMaterial.Icon.gmd_backup).withIdentifier(backup).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withSelectable(false),
                        new PrimaryDrawerItem().withName("الإعدادات").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(settings).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("اتصل بنا").withIcon(GoogleMaterial.Icon.gmd_copyright).withIdentifier(contact).withSelectedTextColorRes(R.color.spec_black).withSelectedIconColorRes(R.color.purple).withIconColorRes(R.color.purple).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withSelectable(false),
                        bdiEnable)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //creating fragment object
                        Fragment fragment = null;
                        //initializing the fragment object which is selected
                        switch ((int) drawerItem.getIdentifier()) {
                            case quick:
                                if (selectionMode != 0) {
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("isSpecial", false);
                                    fragment = new BasicActivity();
                                    fragment.setArguments(bundle);
                                    toolbar.setBackgroundColor(res.getColor(R.color.purple));
                                    toolbar.setTitle("الواجهة السريعة");
                                    selectionMode = 0;
                                } else {
                                    fragment = null;
                                }
                                break;
                            case special:
                                if (selectionMode != 1) {
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putBoolean("isSpecial", true);
                                    fragment = new BasicActivity();
                                    fragment.setArguments(bundle1);
                                    toolbar.setBackgroundColor(res.getColor(R.color.purple));
                                    toolbar.setTitle("التحويل المخصص");
                                    selectionMode = 1;
                                } else {
                                    fragment = null;
                                }
                                break;
                            case recoder:
                                fragment = new ListTransformsActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.light_green));
                                toolbar.setTitle("سجل التحويل");
                                selectionMode = 2;
                                break;
                            case names:
                                fragment = new ListCustomerActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.purple));
                                toolbar.setTitle("الأسماء المحفوظة");
                                selectionMode = 3;
                                break;
                            case save:
                                result.closeDrawer();
                                hideKeyboard(MainActivity.this);
                                createAddNewDialog();
                                break;
                            case inventory:
                                result.closeDrawer();
                                hideKeyboard(MainActivity.this);
                                createInventoryDialog();
                                break;
                            case backup:
                                result.closeDrawer();
                                hideKeyboard(MainActivity.this);
                                createImExDialog();
                                break;
                            case settings:
                                selectionMode = 4;
                                fragment = new SettingsActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.purple));
                                toolbar.setTitle("الإعدادات");
                                break;
                            case enable:
                                result.closeDrawer();
                                hideKeyboard(MainActivity.this);
                                createEnableAppDialog();
                                break;
                            case contact:
                                result.closeDrawer();
                                hideKeyboard(MainActivity.this);
                                createAboutDialog();
                                break;

                        }
                        //replacing the fragment
                        if (fragment != null) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragmentMain, fragment);
                            ft.commit();
                        }
                        result.closeDrawer();
                        return true;
                    }
                }).withDrawerGravity(Gravity.RIGHT).build();
        result.setSelection(1);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSpecial", false);
        Fragment fragment = new BasicActivity();
        fragment.setArguments(bundle);
        toolbar.setBackgroundColor(res.getColor(R.color.purple));
        toolbar.setTitle("الواجهة السريعة");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentMain, fragment);
        ft.commit();
        actionBar = result.getActionBarDrawerToggle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        actionBar.setDrawerIndicatorEnabled(true);
        actionBar.setDrawerSlideAnimationEnabled(true);
    }

    void createAboutDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_about);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_enable_a = (Button) d.findViewById(R.id.btn_enable);
        btn_contact = (Button) d.findViewById(R.id.btn_contact);
        if (sharedPreferences.getBoolean("isActive", false)) {
            btn_enable_a.setTextColor(res.getColor(R.color.black_overlay));
            btn_enable_a.setEnabled(false);
        }
        btn_enable_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                createEnableAppDialog();
            }
        });
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + "0968969114"));
                context.startActivity(callIntent);
            }
        });
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void createInventoryDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_inventory);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dbb = new BalanceSQLDatabaseHandler(context);
        txt_balance = (TextView) d.findViewById(R.id.txt_balance);
        txt_balance_s = (TextView) d.findViewById(R.id.txt_balance_s);
        txt_balance_s_ = (TextView) d.findViewById(R.id.txt_balance_s_);
        txt_balance_m = (TextView) d.findViewById(R.id.txt_balance_m);
        txt_balance_m_ = (TextView) d.findViewById(R.id.txt_balance_m_);
        txt_profits = (TextView) d.findViewById(R.id.txt_profits);
        txt_debt = (TextView) d.findViewById(R.id.txt_debt);
        txt_balance_ = (TextView) d.findViewById(R.id.txt_balance_);
        txt_balance_ss = (TextView) d.findViewById(R.id.txt_balance_ss);
        txt_balance_ss_ = (TextView) d.findViewById(R.id.txt_balance_ss_);
        txt_balance_mm = (TextView) d.findViewById(R.id.txt_balance_mm);
        txt_balance_mm_ = (TextView) d.findViewById(R.id.txt_balance_mm_);
        txt_profits_ = (TextView) d.findViewById(R.id.txt_profits_);
        txt_debt_ = (TextView) d.findViewById(R.id.txt_debt_);
        btn_debt_recorder = (Button) d.findViewById(R.id.btn_debt_recorder);
        btn_reset = (Button) d.findViewById(R.id.btn_reset);
        progressBar = (ProgressBar) d.findViewById(R.id.progress_bar);
        btn_refresh_syr = (ImageButton) d.findViewById(R.id.img_btn_s);
        btn_refresh_syr_ = (ImageButton) d.findViewById(R.id.img_btn_s_);
        btn_refresh_mtn = (ImageButton) d.findViewById(R.id.img_btn_m);
        btn_refresh_mtn_ = (ImageButton) d.findViewById(R.id.img_btn_m_);
        btn_edit_bal = (Button) d.findViewById(R.id.btn_edit_balance);
        syrAnm = (AnimationDrawable) btn_refresh_syr.getDrawable();
         syrAnm_ = (AnimationDrawable) btn_refresh_syr_.getDrawable();
        mtnAnm = (AnimationDrawable) btn_refresh_mtn.getDrawable();
        mtnAnm_ = (AnimationDrawable) btn_refresh_mtn_.getDrawable();
        edit_bal = (EditText) d.findViewById(R.id.edit_balance);
        syriatelSim = sharedPreferences.getInt("syr_sim_count", 1);
        mtnSim = sharedPreferences.getInt("mtn_sim_count", 2);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                txt_balance.setVisibility(View.VISIBLE);
                txt_profits.setVisibility(View.VISIBLE);
                txt_debt.setVisibility(View.VISIBLE);
                txt_balance_s.setVisibility(View.VISIBLE);
                txt_balance_s_.setVisibility(View.VISIBLE);
                txt_balance_m.setVisibility(View.VISIBLE);
                txt_balance_m_.setVisibility(View.VISIBLE);
                txt_balance_.setVisibility(View.VISIBLE);
                txt_profits_.setVisibility(View.VISIBLE);
                txt_debt_.setVisibility(View.VISIBLE);
                txt_balance_ss.setVisibility(View.VISIBLE);
                txt_balance_ss_.setVisibility(View.VISIBLE);
                txt_balance_mm.setVisibility(View.VISIBLE);
                txt_balance_mm_.setVisibility(View.VISIBLE);
                btn_refresh_syr.setVisibility(View.VISIBLE);
                btn_refresh_syr_.setVisibility(View.VISIBLE);
                btn_refresh_mtn.setVisibility(View.VISIBLE);
                btn_refresh_mtn_.setVisibility(View.VISIBLE);
                txt_balance.setText(dbb.getBalance().getBalance());
                txt_profits.setText(dbb.getBalance().getProfits());
                txt_debt.setText( dbb.getBalance().getDebt());
                txt_balance_s.setText(dbb.getBalance().getSyrBalance());
                txt_balance_s_.setText( dbb.getBalance().getSyrBalance_());
                txt_balance_m.setText(dbb.getBalance().getMtnBalance());
                txt_balance_m_.setText( dbb.getBalance().getMtnBalance_());
                syrAnm.stop();
                syrAnm_.stop();
                mtnAnm.stop();
                mtnAnm_.stop();
            }
        }, 2000);
        btn_refresh_syr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syrAnm.start();
                try {
                    String code = sharedPreferences.getString("code_check_pre_syr", "").replace("#", Uri.encode("#"));
                    Intent serviceIntent = new Intent(context, USSDService.class);
                    serviceIntent.putExtra("type", "_code_check_pre_syr");
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
        });
        btn_refresh_syr_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syrAnm_.start();
                try {
                    String code = sharedPreferences.getString("code_check_post_syr", "").replace("#", Uri.encode("#"));
                    Intent serviceIntent = new Intent(context, USSDService.class);
                    serviceIntent.putExtra("type", "_code_check_post_syr");
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
        });
        btn_refresh_mtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtnAnm.start();
                mtnAnm_.start();
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
            }
        });
        btn_refresh_mtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtnAnm.start();
                mtnAnm_.start();
                try {
                    String code = sharedPreferences.getString("code_check_pre_mtn", "").replace("#", Uri.encode("#"));
                    Intent serviceIntent = new Intent(context, USSDService.class);
                    serviceIntent.putExtra("type", "_code_check_pre_mtn");
                    context.startService(serviceIntent);
                    Intent callIntent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:" + code));
                    context.startActivity(callIntent);
                    callIntent.putExtra("com.android.phone.extra.slot", mtnSim - 1);
                } catch (Exception e) {
                    Toast.makeText(context, "حدث خطأ, يرجى مراجعة إعدادات التحويل", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_debt_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                createDebtRecorderDialog();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbb.deleteBalance();
                dbb.addBalance(new BalanceItem(1, "0", "0", "0", "0", "0", "0", "0"));
                txt_balance.setText( dbb.getBalance().getBalance());
                txt_profits.setText(dbb.getBalance().getProfits());
                txt_debt.setText( dbb.getBalance().getDebt());
                txt_balance_s.setText(  dbb.getBalance().getSyrBalance());
                txt_balance_s_.setText(dbb.getBalance().getSyrBalance_());
                txt_balance_m.setText(dbb.getBalance().getMtnBalance());
                txt_balance_m_.setText(dbb.getBalance().getMtnBalance_());
            }
        });
        btn_edit_bal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if(isBalanceEditingRun){
                      if(edit_bal.getText().toString().matches(".*[1-9]+.*")){
                          BalanceItem bi = dbb.getBalance();
                          bi.setBalance(edit_bal.getText().toString());
                          dbb.updateBalance(bi);
                          txt_balance.setText(edit_bal.getText().toString());
                      }
                      edit_bal.setVisibility(View.GONE);
                      isBalanceEditingRun = false;
                  }else{
                      edit_bal.setVisibility(View.VISIBLE);
                      edit_bal.setText(txt_balance.getText().toString());
                      isBalanceEditingRun = true;
                  }
            }
        });
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    private void createDebtRecorderDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_debt_recorder);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ListView list = (ListView) d.findViewById(R.id.debt_recorder_list);
        db = new CustomerSQLDatabaseHandler(context);
        dbt = new TransformSQLDatabaseHandler(context);
        ArrayList<CustomerListChildItem> listAllCustomers = new ArrayList<>();
        listAllCustomers.addAll(db.allCustomers());
        final ArrayList<CustomerListChildItem> listDebtCustomers = new ArrayList<>();
        for (int j = 0; j < listAllCustomers.size(); j++) {
            if (Integer.valueOf(listAllCustomers.get(j).getBalance().substring(listAllCustomers.get(j).getBalance().indexOf("دين: ") + 5)) != 0)
                listDebtCustomers.add(listAllCustomers.get(j));
        }
        CustomCustomerListAdapter customAdapter = new CustomCustomerListAdapter(context, listDebtCustomers);
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int p = i;
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("تنبيه!");
                alertDialog.setMessage("هل تريد تسوية الدين لهذا الزبون؟");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "نعم",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                CustomerListChildItem c = listDebtCustomers.get(p);
                                String debt = String.valueOf(Integer.valueOf(c.getBalance().substring(c.getBalance().indexOf("دين: ") + 5).replaceAll(" ", "")));
                                String cash = String.valueOf(Integer.valueOf(c.getBalance().substring(6, c.getBalance().indexOf("دين: ")).replaceAll(" ", "")) + Integer.valueOf(debt));
                                db.updateCustomer(new CustomerListChildItem(c.getColumn(),c.getName(),c.getNumber(),c.getAmount(),"نقدي: " + cash + " " + "دين: " + "0",c.getCode(),c.getMode()));
                                ArrayList<CustomerListChildItem> listAllCustomers = new ArrayList<>();
                                listAllCustomers.addAll(db.allCustomers());
                                final ArrayList<CustomerListChildItem> listDebtCustomers = new ArrayList<>();
                                for (int j = 0; j < listAllCustomers.size(); j++) {
                                    if (Integer.valueOf(listAllCustomers.get(j).getBalance().substring(listAllCustomers.get(j).getBalance().indexOf("دين: ") + 5)) != 0)
                                        listDebtCustomers.add(listAllCustomers.get(j));
                                }
                                CustomCustomerListAdapter customAdapter = new CustomCustomerListAdapter(context, listDebtCustomers);
                                list.setAdapter(customAdapter);
                                dbb.updateBalance(new BalanceItem(dbb.getBalance().getColumn(),dbb.getBalance().getBalance(),dbb.getBalance().getProfits(),String.valueOf(Integer.valueOf(dbb.getBalance().getDebt())-Integer.valueOf(debt)),dbb.getBalance().getSyrBalance(),dbb.getBalance().getSyrBalance_(),dbb.getBalance().getMtnBalance(),dbb.getBalance().getMtnBalance_()));
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    private void createAddNewDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_add_new);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        edit_number = (EditText) d.findViewById(R.id.edit_number);
        edit_name = (EditText) d.findViewById(R.id.edit_name);
        edit_code__ = (EditText) d.findViewById(R.id.edit_code);
        btn_save = (Button) d.findViewById(R.id.btn_save);
        btn_close = (Button) d.findViewById(R.id.btn_close);
        spin_oper = (Spinner) d.findViewById(R.id.spin_operation);
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
        String[] transformTypes = res.getStringArray(R.array.transformSpecial);
        ArrayAdapter<CharSequence> spinnerOperArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item_operation, transformTypes);
        spinnerOperArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_operation);
        spin_oper.setAdapter(spinnerOperArrayAdapter);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_number.getText().toString().matches(".*[0-9]+.*") && edit_name.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                    if (edit_number.getText().toString().length() > 8) {
                        boolean isNewName = true;
                        boolean isNewNumber = true;
                        for (int i = 0; i < db.allCustomers().size(); i++) {
                            if (edit_number.getText().toString().matches(db.allCustomers().get(i).getNumber())) {
                                isNewNumber = false;
                                break;
                            }
                            if (edit_name.getText().toString().matches(db.allCustomers().get(i).getName())) {
                                isNewName = false;
                                break;
                            }
                        }
                        if (isNewName && isNewNumber) {
                            db = new CustomerSQLDatabaseHandler(context);
                            CustomerListChildItem customer = new CustomerListChildItem();
                            customer.setColumn(context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("currentCustomerColumn", 0) + 1);
                            customer.setName(edit_name.getText().toString());
                            customer.setNumber(edit_number.getText().toString());
                            customer.setBalance("نقدي: 0 دين: 0");
                            customer.setAmount("0");
                            customer.setCode(edit_code__.getText().toString());
                            customer.setMode(mode);
                            db.addCustomer(customer);
                            context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putInt("currentCustomerColumn", context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("currentCustomerColumn", 0) + 1).commit();
                            d.cancel();
                            if (selectionMode == 3)
                                result.setSelection(4);
                        } else if (!isNewName) {
                            edit_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            Toast.makeText(getApplicationContext(), "الرقم موجود مسبقاً", Toast.LENGTH_LONG).show();
                        } else if (!isNewNumber) {
                            edit_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            Toast.makeText(getApplicationContext(), "الرقم موجود مسبقاً", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        edit_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                        Toast.makeText(getApplicationContext(), "الرقم صغير", Toast.LENGTH_LONG).show();
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
        spin_oper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int index = adapterView.getSelectedItemPosition();
                switch (index) {
                    case 0:
                        mode = "code_pre_mtn";
                        break;
                    case 1:
                        mode = "code_pre_syr";
                        break;
                    case 2:
                        mode = "code_post_mtn";
                        break;
                    case 3:
                        mode = "code_post_syr";
                        break;
                    case 4:
                        mode = "code_post_adsl_mtn";
                        break;
                    case 5:
                        mode = "code_pre_post_who_syr";
                        break;
                    case 6:
                        mode = "code_pre_who_mtn";
                        break;
                    case 7:
                        mode = "code_post_who_mtn";
                        break;
                    case 8:
                        mode = "code_check";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spin_oper.setSelection(0);
        d.show();
    }

    private void createImExDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_backup_restore);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_t_backup = (Button) d.findViewById(R.id.btn_transform_backup);
        btn_t_restore = (Button) d.findViewById(R.id.btn_transform_restore);
        btn_c_backup = (Button) d.findViewById(R.id.btn_customer_backup);
        btn_c_restore = (Button) d.findViewById(R.id.btn_customer_restore);
        btn_b_backup = (Button) d.findViewById(R.id.btn_balance_backup);
        btn_b_restore = (Button) d.findViewById(R.id.btn_balance_restore);
        btn_t_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransformSQLDatabaseHandler c = new TransformSQLDatabaseHandler(context);
                c.allTransforms();
                try {
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
                    if (!f.exists())
                        f.mkdirs();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH", Locale.US);
                    Date now = new Date();
                    c.backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + formatter.format(now) + "_transforms.db");
                    Toast.makeText(context, "تم الحفظ في ذاكرة الهاتف في مجلد Transform", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, "حدث خطأ لم يتم الحفظ في الذاكرة!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_t_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "يرجى اختيار ملف قاعدة البيانات التي تكون نهاية اسمه transforms.db", Toast.LENGTH_LONG).show();
                Content c = new Content();
                c.setCreateLabel("اختيار الملف");
                c.setInternalStorageText("الذاكرة الداخلية");
                c.setCancelLabel("إلغاء");
                c.setSelectLabel("اختيار");
                c.setOverviewHeading("اختيار الذاكرة");
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(MainActivity.this)
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
                        if (path.contains("transforms.db")) {
                            TransformSQLDatabaseHandler c = new TransformSQLDatabaseHandler(context);
                            c.allTransforms();
                            try {
                                c.importDB(path);
                                Toast.makeText(context, "تم استعادة قاعدة البيانات بنجاح", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ لم تتم الإستعادة!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context, "الملف غير صالح!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        btn_c_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerSQLDatabaseHandler c = new CustomerSQLDatabaseHandler(context);
                c.allCustomers();
                try {
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
                    if (!f.exists())
                        f.mkdirs();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH", Locale.US);
                    Date now = new Date();
                    c.backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + formatter.format(now) + "_customers.db");
                    Toast.makeText(context, "تم الحفظ في ذاكرة الهاتف في مجلد Transform", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, "حدث خطأ لم يتم الحفظ في الذاكرة!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_c_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "يرجى اختيار ملف قاعدة البيانات التي تكون نهاية اسمه customers.db", Toast.LENGTH_LONG).show();
                Content c = new Content();
                c.setCreateLabel("اختيار الملف");
                c.setInternalStorageText("الذاكرة الداخلية");
                c.setCancelLabel("إلغاء");
                c.setSelectLabel("اختيار");
                c.setOverviewHeading("اختيار الذاكرة");
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(MainActivity.this)
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
                        if (path.contains("customers.db")) {
                            CustomerSQLDatabaseHandler c = new CustomerSQLDatabaseHandler(context);
                            c.allCustomers();
                            try {
                                c.importDB(path);
                                Toast.makeText(context, "تم استعادة قاعدة البيانات بنجاح", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ لم تتم الإستعادة!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context, "الملف غير صالح!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        btn_b_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BalanceSQLDatabaseHandler b = new BalanceSQLDatabaseHandler(context);
                b.allBalances();
                try {
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
                    if (!f.exists())
                        f.mkdirs();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH", Locale.US);
                    Date now = new Date();
                    b.backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + formatter.format(now) + "_balance.db");
                    Toast.makeText(context, "تم الحفظ في ذاكرة الهاتف في مجلد Transform", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, "حدث خطأ لم يتم الحفظ في الذاكرة!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_b_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "يرجى اختيار ملف قاعدة البيانات التي تكون نهاية اسمه balance.db", Toast.LENGTH_LONG).show();
                Content c = new Content();
                c.setCreateLabel("اختيار الملف");
                c.setInternalStorageText("الذاكرة الداخلية");
                c.setCancelLabel("إلغاء");
                c.setSelectLabel("اختيار");
                c.setOverviewHeading("اختيار الذاكرة");
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(MainActivity.this)
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
                        if (path.contains("balance.db")) {
                            BalanceSQLDatabaseHandler b = new BalanceSQLDatabaseHandler(context);
                            b.allBalances();
                            try {
                                b.importDB(path);
                                Toast.makeText(context, "تم استعادة قاعدة البيانات بنجاح", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ لم تتم الإستعادة!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context, "الملف غير صالح!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        d.show();
    }

    boolean isInternetAvailable;

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
                        new SendMailAsynTask(MainActivity.this, "Transform Activation:" + getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("basicName", ""), text, "zezoocvi.77@gmail.com").execute();
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
                    Toast.makeText(getApplicationContext(), "#تم تنشيط البرنامج#", Toast.LENGTH_LONG).show();
                    dbk = new KeySQLDatabaseHandler(MainActivity.this);
                    dbk.allKeys();
                    dbk.updateKey(new KeyItem(1, edit_code_.getText().toString(), edit_code.getText().toString()));
                    bdiEnable.withTextColorRes(R.color.black_overlay).withSelectedIconColorRes(R.color.black_overlay).withIconColorRes(R.color.black_overlay).withEnabled(false).withSelectable(false);
                    KeySQLDatabaseHandler k = new KeySQLDatabaseHandler(context);
                    k.allKeys();
                    try {
                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Transform");
                        if (!f.exists())
                            f.mkdirs();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH", Locale.US);
                        Date now = new Date();
                        k.backup(Environment.getExternalStorageDirectory() + File.separator + "Transform" + File.separator + formatter.format(now) + "_key.db");
                        Toast.makeText(context, "تم الحفظ في ذاكرة الهاتف في مجلد Transform", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "حدث خطأ لم يتم الحفظ في الذاكرة!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "يرجى اختيار ملف قاعدة البيانات التي تكون نهاية اسمه key.db", Toast.LENGTH_LONG).show();
                Content c = new Content();
                c.setCreateLabel("اختيار الملف");
                c.setInternalStorageText("الذاكرة الداخلية");
                c.setCancelLabel("إلغاء");
                c.setSelectLabel("اختيار");
                c.setOverviewHeading("اختيار الذاكرة");
                StorageChooser chooser = new StorageChooser.Builder()
                        .withActivity(MainActivity.this)
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
                                    Toast.makeText(getApplicationContext(), "#تم تنشيط البرنامج#", Toast.LENGTH_LONG).show();
                                    bdiEnable.withTextColorRes(R.color.black_overlay).withSelectedIconColorRes(R.color.black_overlay).withIconColorRes(R.color.black_overlay).withEnabled(false).withSelectable(false);
                                    d.cancel();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "حدث خطأ لم تتم الإستعادة!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "الملف غير صالح!", Toast.LENGTH_LONG).show();
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

    private void signupAsFirst() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (!sharedPreferences.getBoolean("isActive", false) && sharedPreferences.getLong("trialTransforms", Long.valueOf(telephonyManager.getDeviceId())) >= Long.valueOf(telephonyManager.getDeviceId()) + 35) {
            createEnableAppDialog();
            closeApplication();
        }
        dbo = new OpenSQLDatabaseHandler(context);
        dbo.allOpens();
        dbo.addOpen(new OpenItem(1, String.valueOf(Long.valueOf(telephonyManager.getDeviceId()))));
        OpenSQLDatabaseHandler dbo1 = new OpenSQLDatabaseHandler(context);
        dbo1.allOpens();
        if (!sharedPreferences.getBoolean("isActive", false)) {
            try {
                dbo1.importDB(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "open.db");
                OpenSQLDatabaseHandler o = new OpenSQLDatabaseHandler(context);
                o.allOpens();
                if (!sharedPreferences.getBoolean("isActive", false) && Long.valueOf(o.getOpen().getOpen()) >= Long.valueOf(telephonyManager.getDeviceId()) + 35) {
                    createEnableAppDialog();
                    closeApplication();
                }
            } catch (Exception e) {
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "open.db");
                if (f.exists()) {
                    Toast.makeText(context, "حدث خطأ لم تتم الإستعادة!", Toast.LENGTH_LONG).show();
                    closeApplication();
                }
            }
        }
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            dbb = new BalanceSQLDatabaseHandler(context);
            dbb.allBalances();
            dbb.addBalance(new BalanceItem(1, "0", "0", "0", "0", "0", "0", "0"));
            dbk = new KeySQLDatabaseHandler(context);
            dbk.allKeys();
            dbk.addKey(new KeyItem(1, "", ""));
            //show sign up activity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }
            if (!isAccessibilityEnabled(this)) {
                Toast.makeText(getApplicationContext(), "يرجى تفعيل Transform حتى يمكن قراءة نتائج تحويل الرصيد", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        }
    }

    void createDesktopShortcuts() {
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

    public void readContact() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                                bundle.putBoolean("isSpecial", false);
                                bundle.putString("phone", num);
                                Fragment fragment = new BasicActivity();
                                fragment.setArguments(bundle);
                                toolbar.setBackgroundColor(res.getColor(R.color.purple));
                                toolbar.setTitle("الواجهة السريعة");
                                selectionMode = 0;
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.fragmentMain, fragment);
                                ft.commit();
                                result.setSelection(1);
                            }
                        }
                    }
                    break;
                }
        }
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
            Toast.makeText(this, "يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
            isInternetAvailable = true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, "لا يوجد اتصال بالإنترنت!", Toast.LENGTH_SHORT).show();
            isInternetAvailable = false;
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

