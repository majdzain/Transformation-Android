package com.transformation.zezoo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class CustomCustomerListAdapter extends ArrayAdapter<CustomerListChildItem> {

    private Context mContext;
    private List<CustomerListChildItem> customersList = new ArrayList<>();

    public CustomCustomerListAdapter(@NonNull Context context, ArrayList<CustomerListChildItem> list) {
        super(context, 0, list);
        mContext = context;
        customersList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_customer_item, parent, false);

        CustomerListChildItem customer = customersList.get(position);

        LinearLayout linear = (LinearLayout) listItem.findViewById(R.id.customers_linear);
        int type = 1;
        if (customer.getNumber().startsWith("093") || customer.getNumber().startsWith("93") || customer.getNumber().startsWith("098") || customer.getNumber().startsWith("98") || customer.getNumber().startsWith("099") || customer.getNumber().startsWith("99")) {
            type = 1;
        } else if (customer.getNumber().startsWith("094") || customer.getNumber().startsWith("94") || customer.getNumber().startsWith("095") || customer.getNumber().startsWith("95") || customer.getNumber().startsWith("096") || customer.getNumber().startsWith("96") || customer.getNumber().startsWith("011") || customer.getNumber().startsWith("033") || customer.getNumber().startsWith("052") || customer.getNumber().startsWith("014") || customer.getNumber().startsWith("021") || customer.getNumber().startsWith("051") || customer.getNumber().startsWith("013") || customer.getNumber().startsWith("016") || customer.getNumber().startsWith("022") || customer.getNumber().startsWith("031") || customer.getNumber().startsWith("041") || customer.getNumber().startsWith("043") || customer.getNumber().startsWith("023") || customer.getNumber().startsWith("015")) {
            type = 2;
        } else if (customer.getNumber().length() == 8 && !(customer.getNumber().startsWith("0") || customer.getNumber().startsWith("9"))) {
            type = 1;
        } else if (customer.getNumber().length() == 9 && !(customer.getNumber().startsWith("*") || customer.getNumber().startsWith("#") || customer.getNumber().startsWith("0") || customer.getNumber().startsWith("9"))) {
            type = 2;
        }
        if (type == 1) {
            linear.setBackground(mContext.getResources().getDrawable(R.drawable.list_syr_item_selector));
        } else {
            linear.setBackground(mContext.getResources().getDrawable(R.drawable.list_mtn_item_selector));
        }
        TextView name = (TextView) listItem.findViewById(R.id.customerName);
        name.setText(customer.getName());
        TextView number = (TextView) listItem.findViewById(R.id.customerNumber);
        String mode = "";
        if (customer.getMode().matches("code_pre_mtn") || customer.getMode().matches("code_pre_syr"))
            mode = "و";
        else if (customer.getMode().matches("code_post_mtn") || customer.getMode().matches("code_post_syr") || customer.getMode().matches("code_post_adsl_mtn"))
            mode = "ف";
        else if (customer.getMode().matches("code_pre_who_mtn"))
            mode = "ج.و";
        else if (customer.getMode().matches("code_post_who_mtn"))
            mode = "ج.ف";
        else if (customer.getMode().matches("code_pre_post_who_syr"))
            mode = "ج";
        else if (customer.getMode().matches("code_check"))
            mode = "ت";
        if (customer.getCode().matches(".*[0-9]+.*"))
            number.setText(mode + "/" + customer.getNumber() + "/" + customer.getCode());
        else
            number.setText(mode + "/" + customer.getNumber());
        TextView amount = (TextView) listItem.findViewById(R.id.customerAmount);
        amount.setText(customer.getAmount());
        TextView balance = (TextView) listItem.findViewById(R.id.customerBalance);
        balance.setText(customer.getBalance());

        return listItem;
    }
}