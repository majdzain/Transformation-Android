package com.transformation.zezoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;

public class CommonSettings extends Fragment {
    View view;
    Resources res;
    Context context;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_price, container, false);
        context = view.getContext();
        res = context.getResources();
        setHasOptionsMenu(true);
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        return view;
    }
}
