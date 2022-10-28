package com.transformation.zezoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static android.content.Context.MODE_PRIVATE;

public class TransformSettings extends Fragment {
    View view;
    Resources res;
    Context context;
    SharedPreferences pref;
    EditText code_pre_syr, code_check_pre_syr, code_post_syr, code_check_post_syr, code_pre_post_who_syr, code_pre_mtn, code_check_pre_mtn, code_post_mtn, code_pre_who_mtn, code_post_who_mtn, code_post_adsl_mtn, code_check;
    RadioGroup rg_s, rg_m;
    RadioButton rb_m_1, rb_m_2, rb_s_1, rb_s_2;
    Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transform, container, false);
        context = view.getContext();
        res = context.getResources();
        setHasOptionsMenu(true);
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        rg_s = (RadioGroup) view.findViewById(R.id.radio_group_s);
        rg_m = (RadioGroup) view.findViewById(R.id.radio_group_m);
        rb_m_1 = (RadioButton) view.findViewById(R.id.radio_sim_1_m);
        rb_m_2 = (RadioButton) view.findViewById(R.id.radio_sim_2_m);
        rb_s_1 = (RadioButton) view.findViewById(R.id.radio_sim_1_s);
        rb_s_2 = (RadioButton) view.findViewById(R.id.radio_sim_2_s);
        code_pre_syr = (EditText) view.findViewById(R.id.code_pre_syr);
        code_check_pre_syr = (EditText) view.findViewById(R.id.code_check_pre_syr);
        code_post_syr = (EditText) view.findViewById(R.id.code_post_syr);
        code_check_post_syr = (EditText) view.findViewById(R.id.code_check_post_syr);
        code_pre_post_who_syr = (EditText) view.findViewById(R.id.code_pre_post_who_syr);
        code_pre_mtn = (EditText) view.findViewById(R.id.code_pre_mtn);
        code_check_pre_mtn = (EditText) view.findViewById(R.id.code_check_pre_mtn);
        code_post_mtn = (EditText) view.findViewById(R.id.code_post_mtn);
        code_pre_who_mtn = (EditText) view.findViewById(R.id.code_pre_who_mtn);
        code_post_who_mtn = (EditText) view.findViewById(R.id.code_post_who_mtn);
        code_post_adsl_mtn = (EditText) view.findViewById(R.id.code_post_adsl_mtn);
        code_check = (EditText) view.findViewById(R.id.code_check_);
        save = (Button) view.findViewById(R.id.btn_save);
        setClicksAndContents();
        return view;
    }

    private void setClicksAndContents() {
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
            }
        });
    }
}
