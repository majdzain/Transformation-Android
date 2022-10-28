package com.transformation.zezoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;

public class SettingsActivity extends Fragment {
    View view;
    Resources res;
    Context context;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    SharedPreferences pref;
    EditText code_pre_syr, code_check_pre_syr, code_post_syr, code_check_post_syr, code_pre_post_who_syr, code_pre_mtn, code_check_pre_mtn, code_post_mtn, code_pre_who_mtn, code_post_who_mtn, code_post_adsl_mtn, code_check;
    RadioGroup rg_s, rg_m;
    RadioButton rb_m_1, rb_m_2, rb_s_1, rb_s_2;
    Button save;
    EditText syr_pre_pur_price, syr_pre_sale_price, syr_post_pur_price, syr_post_sale_price, mtn_pre_pur_price, mtn_pre_sale_price, mtn_post_pur_price, mtn_post_sale_price, mtn_post_a_pur_price, mtn_post_a_sale_price, syr_pre_who_pur_price, syr_pre_who_sale_price, mtn_pre_who_pur_price, mtn_pre_who_sale_price, syr_post_who_pur_price, syr_post_who_sale_price, mtn_post_who_pur_price, mtn_post_who_sale_price;
    Button save1;
    Spinner spin_type,spin_type_,spin_btn;
    EditText edit_refresh;
    CheckBox check_show,check_show1;
    Button save2, createShortcuts;
    private EditText mtn_pre_who_price,syr_pre_who_price,mtn_post_who_price,syr_post_who_price;
    private CheckBox check_request;
    private Spinner spin_auto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_settings, container, false);
        context = view.getContext();
        res = context.getResources();
        setHasOptionsMenu(true);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_settings);
        tabLayout.addTab(tabLayout.newTab().setText("التحويل"));
        tabLayout.addTab(tabLayout.newTab().setText("التسعيرات"));
        tabLayout.addTab(tabLayout.newTab().setText("عامة"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager_settings);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }


    class ViewPagerAdapter extends android.support.v4.view.PagerAdapter {
        String[] type = {"آخر الأرقام", "الأسماء المحفوظة","آخر المحفوظة"};
        String[] type_ = {"المنبثقة", "الرسائل"};
        String[] btn = {"تحويل لجهة اتصال", "حفظ رقم"};
        String[] auto = {"المفرّق", "الجملة", "إيقاف"};

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
            return 3;
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.fragment_transform;
                    View view = inflater.inflate(resId, null);
                    LinearLayout lt = (LinearLayout) view.findViewById(R.id.linear_transform);
                    pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    rg_s = (RadioGroup) lt.findViewById(R.id.radio_group_s);
                    rg_m = (RadioGroup) lt.findViewById(R.id.radio_group_m);
                    rb_m_1 = (RadioButton) lt.findViewById(R.id.radio_sim_1_m);
                    rb_m_2 = (RadioButton) lt.findViewById(R.id.radio_sim_2_m);
                    rb_s_1 = (RadioButton) lt.findViewById(R.id.radio_sim_1_s);
                    rb_s_2 = (RadioButton) lt.findViewById(R.id.radio_sim_2_s);
                    code_pre_syr = (EditText) lt.findViewById(R.id.code_pre_syr);
                    code_check_pre_syr = (EditText) lt.findViewById(R.id.code_check_pre_syr);
                    code_post_syr = (EditText) lt.findViewById(R.id.code_post_syr);
                    code_check_post_syr = (EditText) lt.findViewById(R.id.code_check_post_syr);
                    code_pre_post_who_syr = (EditText) lt.findViewById(R.id.code_pre_post_who_syr);
                    code_pre_mtn = (EditText) lt.findViewById(R.id.code_pre_mtn);
                    code_check_pre_mtn = (EditText) lt.findViewById(R.id.code_check_pre_mtn);
                    code_post_mtn = (EditText) lt.findViewById(R.id.code_post_mtn);
                    code_pre_who_mtn = (EditText) lt.findViewById(R.id.code_pre_who_mtn);
                    code_post_who_mtn = (EditText) lt.findViewById(R.id.code_post_who_mtn);
                    code_post_adsl_mtn = (EditText) lt.findViewById(R.id.code_post_adsl_mtn);
                    code_check = (EditText) lt.findViewById(R.id.code_check_);
                    save = (Button) lt.findViewById(R.id.btn_save);
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
                    String s = pref.getString("s", "12345");
                    String s1 = pref.getString("s1", "12345");
                    String s2 = pref.getString("s2", "12345");
                    String s3 = pref.getString("s3", "12345");
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
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (code_pre_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_check_pre_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_post_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_check_post_syr.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_pre_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_check_pre_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_post_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*") && code_post_adsl_mtn.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                                int mtn_index = 2;
                                int syr_index = 1;
                                if (rg_m.getCheckedRadioButtonId() == R.id.radio_sim_1_m)
                                    mtn_index = 1;
                                else
                                    mtn_index = 2;
                                if (rg_s.getCheckedRadioButtonId() == R.id.radio_sim_1_s)
                                    syr_index = 1;
                                else
                                    syr_index = 2;
                                pref.edit().putBoolean("isSettled", true).commit();
                                pref.edit().putInt("mtn_sim_count", mtn_index).commit();
                                pref.edit().putInt("syr_sim_count", syr_index).commit();
                                pref.edit().putString("code_pre_syr", code_pre_syr.getText().toString()).commit();
                                pref.edit().putString("code_check_pre_syr", code_check_pre_syr.getText().toString()).commit();
                                pref.edit().putString("code_post_syr", code_post_syr.getText().toString()).commit();
                                pref.edit().putString("code_check_post_syr", code_check_post_syr.getText().toString()).commit();
                                pref.edit().putString("code_pre_mtn", code_pre_mtn.getText().toString()).commit();
                                pref.edit().putString("code_check_pre_mtn", code_check_pre_mtn.getText().toString()).commit();
                                pref.edit().putString("code_post_mtn", code_post_mtn.getText().toString()).commit();
                                pref.edit().putString("code_pre_post_who_syr", code_pre_post_who_syr.getText().toString()).commit();
                                pref.edit().putString("code_pre_who_mtn", code_pre_who_mtn.getText().toString()).commit();
                                pref.edit().putString("code_post_who_mtn", code_post_who_mtn.getText().toString()).commit();
                                pref.edit().putString("code_post_adsl_mtn", code_post_adsl_mtn.getText().toString()).commit();
                                pref.edit().putString("code_check", code_check.getText().toString()).commit();
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
                            Toast.makeText(context,"تم الحفظ",Toast.LENGTH_SHORT).show();
                        }
                    });
                    ((ViewPager) collection).addView(view, 0);
                    return view;
                case 1:
                    resId = R.layout.fragment_price;
                    View view1 = inflater.inflate(resId, null);
                    LinearLayout lp = (LinearLayout) view1.findViewById(R.id.linear_price);
                    pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    syr_pre_pur_price = (EditText) lp.findViewById(R.id.syr_pre_pur_price);
                    syr_pre_sale_price = (EditText) lp.findViewById(R.id.syr_pre_sale_price);
                    syr_post_pur_price = (EditText) lp.findViewById(R.id.syr_post_pur_price);
                    syr_post_sale_price = (EditText) lp.findViewById(R.id.syr_post_sale_price);
                    mtn_pre_pur_price = (EditText) lp.findViewById(R.id.mtn_pre_pur_price);
                    mtn_pre_sale_price = (EditText) lp.findViewById(R.id.mtn_pre_sale_price);
                    mtn_post_pur_price = (EditText) lp.findViewById(R.id.mtn_post_pur_price);
                    mtn_post_sale_price = (EditText) lp.findViewById(R.id.mtn_post_sale_price);
                    mtn_post_a_pur_price = (EditText) lp.findViewById(R.id.mtn_post_a_pur_price);
                    mtn_post_a_sale_price = (EditText) lp.findViewById(R.id.mtn_post_a_sale_price);
                    mtn_pre_who_price = (EditText) lp.findViewById(R.id.mtn_pre_who_price);
                    mtn_pre_who_pur_price = (EditText) lp.findViewById(R.id.mtn_pre_who_pur_price);
                    mtn_pre_who_sale_price = (EditText) lp.findViewById(R.id.mtn_pre_who_sale_price);
                    syr_pre_who_price = (EditText) lp.findViewById(R.id.syr_pre_who_price);
                    syr_pre_who_pur_price = (EditText) lp.findViewById(R.id.syr_pre_who_pur_price);
                    syr_pre_who_sale_price = (EditText) lp.findViewById(R.id.syr_pre_who_sale_price);
                    syr_post_who_price = (EditText) lp.findViewById(R.id.syr_post_who_price);
                    syr_post_who_pur_price = (EditText) lp.findViewById(R.id.syr_post_who_pur_price);
                    syr_post_who_sale_price = (EditText) lp.findViewById(R.id.syr_post_who_sale_price);
                    mtn_post_who_price = (EditText) lp.findViewById(R.id.mtn_post_who_price);
                    mtn_post_who_pur_price = (EditText) lp.findViewById(R.id.mtn_post_who_pur_price);
                    mtn_post_who_sale_price = (EditText) lp.findViewById(R.id.mtn_post_who_sale_price);
                    save1 = (Button) lp.findViewById(R.id.btn_save);
                    syr_pre_pur_price.setText(pref.getString("syr_pre_pur_price", "5300"));
                    syr_pre_sale_price.setText(pref.getString("syr_pre_sale_price", "5000"));
                    syr_post_pur_price.setText(pref.getString("syr_post_pur_price", "5100"));
                    syr_post_sale_price.setText(pref.getString("syr_post_sale_price", "5000"));
                    mtn_pre_pur_price.setText(pref.getString("mtn_pre_pur_price", "5300"));
                    mtn_pre_sale_price.setText(pref.getString("mtn_pre_sale_price", "5000"));
                    mtn_post_pur_price.setText(pref.getString("mtn_post_pur_price", "5100"));
                    mtn_post_sale_price.setText(pref.getString("mtn_post_sale_price", "5000"));
                    mtn_post_a_pur_price.setText(pref.getString("mtn_post_a_pur_price", "5100"));
                    mtn_post_a_sale_price.setText(pref.getString("mtn_post_a_sale_price", "5000"));
                    syr_pre_who_price.setText(pref.getString("syr_pre_who_price", "600"));
                    syr_pre_who_pur_price.setText(pref.getString("syr_pre_who_pur_price", "5300"));
                    syr_pre_who_sale_price.setText(pref.getString("syr_pre_who_sale_price", "5000"));
                    syr_post_who_price.setText(pref.getString("syr_post_who_price", "600"));
                    syr_post_who_pur_price.setText(pref.getString("syr_post_who_pur_price", "5300"));
                    syr_post_who_sale_price.setText(pref.getString("syr_post_who_sale_price", "5000"));
                    mtn_pre_who_price.setText(pref.getString("mtn_pre_who_price", "600"));
                    mtn_pre_who_pur_price.setText(pref.getString("mtn_pre_who_pur_price", "5300"));
                    mtn_pre_who_sale_price.setText(pref.getString("mtn_pre_who_sale_price", "5000"));
                    mtn_post_who_price.setText(pref.getString("mtn_post_who_price", "600"));
                    mtn_post_who_pur_price.setText(pref.getString("mtn_post_who_pur_price", "5300"));
                    mtn_post_who_sale_price.setText(pref.getString("mtn_post_who_sale_price", "5000"));
                    save1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (syr_pre_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_pre_sale_price.getText().toString().matches(".*[0-9]+.*") && syr_post_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_post_sale_price.getText().toString().matches(".*[0-9]+.*") && mtn_pre_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_pre_sale_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_sale_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_a_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_a_sale_price.getText().toString().matches(".*[0-9]+.*")
                                    && mtn_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_who_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_who_sale_price.getText().toString().matches(".*[0-9]+.*") && syr_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*") && syr_post_who_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_post_who_sale_price.getText().toString().matches(".*[0-9]+.*") && syr_post_who_price.getText().toString().matches(".*[0-9]+.*") && syr_pre_who_price.getText().toString().matches(".*[0-9]+.*")&& mtn_post_who_price.getText().toString().matches(".*[0-9]+.*") && mtn_pre_who_price.getText().toString().matches(".*[0-9]+.*")) {
                                pref.edit().putString("syr_pre_who_price", syr_pre_who_price.getText().toString()).commit();
                                pref.edit().putString("syr_pre_sale_price", "5000").commit();
                                pref.edit().putString("syr_post_pur_price", "5000").commit();
                                pref.edit().putString("mtn_pre_who_price", mtn_pre_who_price.getText().toString()).commit();
                                pref.edit().putString("mtn_pre_sale_price", "5000").commit();
                                pref.edit().putString("mtn_post_sale_price", "5000").commit();
                                pref.edit().putString("mtn_post_a_sale_price", "5000").commit();
                                pref.edit().putString("mtn_post_who_price", mtn_post_who_price.getText().toString()).commit();
                                pref.edit().putString("mtn_post_who_sale_price", "5000").commit();
                                pref.edit().putString("mtn_pre_who_sale_price", "5000").commit();
                                pref.edit().putString("syr_post_who_price", syr_post_who_price.getText().toString()).commit();
                                pref.edit().putString("syr_post_who_sale_price", "5000").commit();
                                pref.edit().putString("syr_pre_who_sale_price", "5000").commit();
                                if (Integer.valueOf(syr_pre_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("syr_pre_pur_price", syr_pre_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("syr_pre_pur_price", String.valueOf((Integer.valueOf(syr_pre_pur_price.getText().toString()) * 5000) / Integer.valueOf(syr_pre_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(syr_post_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("syr_post_pur_price", syr_post_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("syr_post_pur_price", String.valueOf((Integer.valueOf(syr_post_pur_price.getText().toString()) * 5000) / Integer.valueOf(syr_post_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(syr_pre_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("mtn_pre_pur_price", mtn_pre_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("mtn_pre_pur_price", String.valueOf((Integer.valueOf(mtn_pre_pur_price.getText().toString()) * 5000) / Integer.valueOf(mtn_pre_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(mtn_post_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("mtn_post_pur_price", mtn_post_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("mtn_post_pur_price", String.valueOf((Integer.valueOf(mtn_post_pur_price.getText().toString()) * 5000) / Integer.valueOf(mtn_post_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(mtn_post_a_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("mtn_post_a_pur_price", mtn_post_a_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("mtn_post_a_pur_price", String.valueOf((Integer.valueOf(mtn_post_a_pur_price.getText().toString()) * 5000) / Integer.valueOf(mtn_post_a_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(mtn_post_who_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("mtn_post_who_pur_price", mtn_post_who_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("mtn_post_who_pur_price", String.valueOf((Integer.valueOf(mtn_post_who_pur_price.getText().toString()) * 5000) / Integer.valueOf(mtn_post_who_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(mtn_pre_who_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("mtn_pre_who_pur_price", mtn_pre_who_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("mtn_pre_who_pur_price", String.valueOf((Integer.valueOf(mtn_pre_who_pur_price.getText().toString()) * 5000) / Integer.valueOf(mtn_pre_who_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(syr_pre_who_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("syr_pre_who_pur_price", syr_pre_who_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("syr_pre_who_pur_price", String.valueOf((Integer.valueOf(syr_pre_who_pur_price.getText().toString()) * 5000) / Integer.valueOf(syr_pre_who_sale_price.getText().toString()))).commit();
                                }
                                if (Integer.valueOf(syr_post_who_sale_price.getText().toString()) == 5000)
                                    pref.edit().putString("syr_post_who_pur_price", syr_post_who_pur_price.getText().toString()).commit();
                                else {
                                    pref.edit().putString("syr_post_who_pur_price", String.valueOf((Integer.valueOf(syr_post_who_pur_price.getText().toString()) * 5000) / Integer.valueOf(syr_post_who_sale_price.getText().toString()))).commit();
                                }
                            } else {
                                if (!syr_pre_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_pre_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_pre_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_pre_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_post_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_post_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_post_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_post_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_pre_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_pre_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_pre_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_pre_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_post_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_post_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_post_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_post_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_post_a_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_post_a_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_post_a_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_post_a_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_post_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_post_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_post_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_post_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_pre_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_pre_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_pre_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_pre_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_post_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_post_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_post_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_post_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_post_who_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_post_who_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!syr_pre_who_price.getText().toString().matches(".*[0-9]+.*"))
                                    syr_pre_who_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_post_who_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_post_who_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                                if (!mtn_pre_who_price.getText().toString().matches(".*[0-9]+.*"))
                                    mtn_pre_who_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                            }
                            Toast.makeText(context,"تم الحفظ",Toast.LENGTH_SHORT).show();
                        }
                    });
                    ((ViewPager) collection).addView(view1, 0);
                    return view1;
                case 2:
                    resId = R.layout.fragment_common;
                    View view2 = inflater.inflate(resId, null);
                    ScrollView lc = (ScrollView) view2.findViewById(R.id.linear_common);
                    pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    spin_type = (Spinner) lc.findViewById(R.id.spin_type);
                    spin_auto = (Spinner) lc.findViewById(R.id.spin_auto);
                    spin_type_ = (Spinner) lc.findViewById(R.id.spin_type_);
                    spin_btn = (Spinner) lc.findViewById(R.id.spin_btn);
                    edit_refresh = (EditText) lc.findViewById(R.id.edit_refresh);
                    check_show = (CheckBox) lc.findViewById(R.id.checkShow);
                    check_show1 = (CheckBox) lc.findViewById(R.id.checkShow1);
                    check_request = (CheckBox) lc.findViewById(R.id.checkRequest);
                    createShortcuts = (Button) lc.findViewById(R.id.btn_create);
                    save2 = (Button) lc.findViewById(R.id.btn_save);
                    ArrayAdapter<CharSequence> spinnerTypeArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item, type);
                    spinnerTypeArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    spin_type.setAdapter(spinnerTypeArrayAdapter);
                    ArrayAdapter<CharSequence> spinnerTypeArrayAdapter_ = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item, type_);
                    spinnerTypeArrayAdapter_.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    spin_type_.setAdapter(spinnerTypeArrayAdapter_);
                    ArrayAdapter<CharSequence> spinnerAutoArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item, auto);
                    spinnerAutoArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    spin_auto.setAdapter(spinnerAutoArrayAdapter);
                    ArrayAdapter<CharSequence> spinnerBtnArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item, btn);
                    spinnerBtnArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    spin_btn.setAdapter(spinnerBtnArrayAdapter);
                    spin_type.setSelection(pref.getInt("NumberListType",0));
                    if(!pref.getBoolean("isMessageRead",false)){
                        spin_type_.setSelection(0);
                    }else{
                        spin_type_.setSelection(1);
                    }
                    spin_auto.setSelection(pref.getInt("autoPriceMode",0));
                    spin_btn.setSelection(pref.getInt("btnType",0));
                    edit_refresh.setText(String.valueOf(pref.getInt("timeRefreshData",600000)/60000));
                    check_show.setChecked(pref.getBoolean("isShowButton",true));
                    check_show1.setChecked(pref.getBoolean("isMessageRead_",true));
                    check_request.setChecked(pref.getBoolean("isFocusLast",true));
                    createShortcuts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity m3 = (MainActivity) getActivity();
                            m3.createDesktopShortcuts();
                        }
                    });
                    save2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (spin_type.getSelectedItem().toString().matches(type[0]))
                                pref.edit().putInt("NumberListType", 0).commit();
                            else if(spin_type.getSelectedItem().toString().matches(type[1]))
                                pref.edit().putInt("NumberListType", 1).commit();
                            else
                                pref.edit().putInt("NumberListType", 2).commit();
                            if (spin_auto.getSelectedItem().toString().matches(auto[0]))
                                pref.edit().putInt("autoPriceMode", 0).commit();
                            else if (spin_auto.getSelectedItem().toString().matches(auto[1]))
                                pref.edit().putInt("autoPriceMode", 1).commit();
                            else
                                pref.edit().putInt("autoPriceMode", 2).commit();
                            if (spin_btn.getSelectedItem().toString().matches(btn[0]))
                                pref.edit().putInt("btnType", 0).commit();
                            else
                                pref.edit().putInt("btnType", 1).commit();
                            if (spin_type_.getSelectedItem().toString().matches(type_[0]))
                                pref.edit().putBoolean("isMessageRead", false).commit();
                            else
                                pref.edit().putBoolean("isMessageRead", true).commit();
                            if(edit_refresh.getText().toString().matches(""))
                                pref.edit().putInt("timeRefreshData",0).commit();
                            else
                                pref.edit().putInt("timeRefreshData",Integer.valueOf(edit_refresh.getText().toString())*60000).commit();
                            pref.edit().putBoolean("isShowButton",check_show.isChecked()).commit();
                            pref.edit().putBoolean("isMessageRead_",check_show1.isChecked()).commit();
                            pref.edit().putBoolean("isFocusLast",check_request.isChecked()).commit();
                            Toast.makeText(context,"تم الحفظ",Toast.LENGTH_SHORT).show();
                        }
                    });
                    ((ViewPager) collection).addView(view2, 0);
                    return view2;
            }
            return view;
        }
    }
}
