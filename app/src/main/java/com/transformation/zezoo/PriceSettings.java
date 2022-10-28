package com.transformation.zezoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

public class PriceSettings extends Fragment {
    View view;
    Resources res;
    Context context;
    SharedPreferences pref;
    EditText syr_pre_pur_price, syr_pre_sale_price, syr_post_pur_price, syr_post_sale_price, mtn_pre_pur_price, mtn_pre_sale_price, mtn_post_pur_price, mtn_post_sale_price, mtn_post_a_pur_price, mtn_post_a_sale_price, syr_pre_who_pur_price, syr_pre_who_sale_price, mtn_pre_who_pur_price, mtn_pre_who_sale_price, syr_post_who_pur_price, syr_post_who_sale_price, mtn_post_who_pur_price, mtn_post_who_sale_price;
    Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_price, container, false);
        context = view.getContext();
        res = context.getResources();
        setHasOptionsMenu(true);
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        syr_pre_pur_price = (EditText) view.findViewById(R.id.syr_pre_pur_price);
        syr_pre_sale_price = (EditText) view.findViewById(R.id.syr_pre_sale_price);
        syr_post_pur_price = (EditText) view.findViewById(R.id.syr_post_pur_price);
        syr_post_sale_price = (EditText) view.findViewById(R.id.syr_post_sale_price);
        mtn_pre_pur_price = (EditText) view.findViewById(R.id.mtn_pre_pur_price);
        mtn_pre_sale_price = (EditText) view.findViewById(R.id.mtn_pre_sale_price);
        mtn_post_pur_price = (EditText) view.findViewById(R.id.mtn_post_pur_price);
        mtn_post_sale_price = (EditText) view.findViewById(R.id.mtn_post_sale_price);
        mtn_post_a_pur_price = (EditText) view.findViewById(R.id.mtn_post_a_pur_price);
        mtn_post_a_sale_price = (EditText) view.findViewById(R.id.mtn_post_a_sale_price);
        mtn_pre_who_pur_price = (EditText) view.findViewById(R.id.mtn_pre_who_pur_price);
        mtn_pre_who_sale_price = (EditText) view.findViewById(R.id.mtn_pre_who_sale_price);
        syr_pre_who_pur_price = (EditText) view.findViewById(R.id.syr_pre_who_pur_price);
        syr_pre_who_sale_price = (EditText) view.findViewById(R.id.syr_pre_who_sale_price);
        syr_post_who_pur_price = (EditText) view.findViewById(R.id.syr_post_who_pur_price);
        syr_post_who_sale_price = (EditText) view.findViewById(R.id.syr_post_who_sale_price);
        mtn_post_who_pur_price = (EditText) view.findViewById(R.id.mtn_post_who_pur_price);
        mtn_post_who_sale_price = (EditText) view.findViewById(R.id.mtn_post_who_sale_price);
        save = (Button) view.findViewById(R.id.btn_save);
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
        syr_pre_who_pur_price.setText(pref.getString("syr_pre_who_pur_price", "5300"));
        syr_pre_who_sale_price.setText(pref.getString("syr_pre_who_sale_price", "5000"));
        syr_post_who_pur_price.setText(pref.getString("syr_post_who_pur_price", "5300"));
        syr_post_who_sale_price.setText(pref.getString("syr_post_who_sale_price", "5000"));
        mtn_pre_who_pur_price.setText(pref.getString("mtn_pre_who_pur_price", "5300"));
        mtn_pre_who_sale_price.setText(pref.getString("mtn_pre_who_sale_price", "5000"));
        mtn_post_who_pur_price.setText(pref.getString("mtn_post_who_pur_price", "5300"));
        mtn_post_who_sale_price.setText(pref.getString("mtn_post_who_sale_price", "5000"));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (syr_pre_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_pre_sale_price.getText().toString().matches(".*[0-9]+.*") && syr_post_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_post_sale_price.getText().toString().matches(".*[0-9]+.*") && mtn_pre_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_pre_sale_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_sale_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_a_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_a_sale_price.getText().toString().matches(".*[0-9]+.*")
                        && mtn_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*")&& mtn_post_who_pur_price.getText().toString().matches(".*[0-9]+.*") && mtn_post_who_sale_price.getText().toString().matches(".*[0-9]+.*")&& syr_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*")&& syr_post_who_pur_price.getText().toString().matches(".*[0-9]+.*") && syr_post_who_sale_price.getText().toString().matches(".*[0-9]+.*")) {
                    pref.edit().putString("syr_pre_sale_price", "5000").commit();
                    pref.edit().putString("syr_post_pur_price", "5000").commit();
                    pref.edit().putString("mtn_pre_sale_price", "5000").commit();
                    pref.edit().putString("mtn_post_sale_price", "5000").commit();
                    pref.edit().putString("mtn_post_a_sale_price", "5000").commit();
                    pref.edit().putString("mtn_post_who_sale_price", "5000").commit();
                    pref.edit().putString("mtn_pre_who_sale_price", "5000").commit();
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
                }else{
                    if(!syr_pre_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_pre_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!syr_pre_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_pre_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!syr_post_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_post_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!syr_post_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_post_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_pre_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_pre_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_pre_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_pre_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_post_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_post_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_post_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_post_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_post_a_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_post_a_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_post_a_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_post_a_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_post_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_post_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_post_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_post_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_pre_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!mtn_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        mtn_pre_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!syr_pre_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_pre_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!syr_pre_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_pre_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!syr_post_who_pur_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_post_who_pur_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    if(!syr_post_who_sale_price.getText().toString().matches(".*[0-9]+.*"))
                        syr_post_who_sale_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                }
            }
        });
        return view;
    }
}