package com.transformation.zezoo;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListFragment extends Fragment{
    View view;
    Resources res;
    Context context;
    LinearLayout panel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_panel, container, false);
        context = view.getContext();
        res = context.getResources();

        return view;
    }

}
