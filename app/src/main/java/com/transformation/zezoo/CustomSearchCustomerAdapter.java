package com.transformation.zezoo;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomSearchCustomerAdapter extends ArrayAdapter<CustomerListChildItem> {
    Context context;
    LayoutInflater inflater;
    private List<CustomerListChildItem> customersList;
    private ArrayList<CustomerListChildItem> filteredCustomersList;
    String filteredText;

    public CustomSearchCustomerAdapter(Context context, int resId, ArrayList<CustomerListChildItem> customers) {
        super(context, resId);
        this.customersList = customers;
        this.filteredCustomersList = customers;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.filteredCustomersList = new ArrayList<CustomerListChildItem>();
        this.filteredCustomersList.addAll(customersList);
        customersList.clear();
    }

    /*private view holder class*/
    private class ViewHolder {
        LinearLayout customerLinear;
        TextView customerName;
        TextView customerBalance;
        TextView customerNumber;
        TextView customerAmount;
    }

    @Override
    public int getCount() {
        return customersList.size();
    }

    @Override
    public CustomerListChildItem getItem(int position) {
        return customersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomSearchCustomerAdapter.ViewHolder holder = null;
        CustomerListChildItem customer = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_customer_item, null);
            holder = new CustomSearchCustomerAdapter.ViewHolder();
            holder.customerLinear = (LinearLayout) convertView.findViewById(R.id.customers_linear);
            holder.customerName = (TextView) convertView.findViewById(R.id.customerName);
            holder.customerBalance = (TextView) convertView.findViewById(R.id.customerBalance);
            holder.customerNumber = (TextView) convertView.findViewById(R.id.customerNumber);
            holder.customerAmount = (TextView) convertView.findViewById(R.id.customerAmount);
            convertView.setTag(holder);
        } else
            holder = (CustomSearchCustomerAdapter.ViewHolder) convertView.getTag();
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
            holder.customerLinear.setBackground(context.getResources().getDrawable(R.drawable.list_syr_item_selector));
        } else {
            holder.customerLinear.setBackground(context.getResources().getDrawable(R.drawable.list_mtn_item_selector));
        }
        String name = customer.getName();
        String number = customer.getNumber();
        String code = customer.getCode();
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
        int startPosNa = name.toLowerCase(Locale.getDefault()).indexOf(filteredText.toLowerCase(Locale.getDefault()));
        int endPosNa = startPosNa + filteredText.length();
        int startPosNu = number.toLowerCase(Locale.getDefault()).indexOf(filteredText.toLowerCase(Locale.getDefault()));
        int endPosNu = startPosNu + filteredText.length();
        if (startPosNa != -1) // This should always be true, just a sanity check
        {
            Spannable spannableN = new SpannableString(name);
            ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.light_green)});
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
            spannableN.setSpan(highlightSpan, startPosNa, endPosNa, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.customerName.setText(spannableN);
        } else
            holder.customerName.setText(name);
        if (startPosNu != -1) // This should always be true, just a sanity check
        {
            Spannable spannableNu = new SpannableString(number);
            ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.light_green)});
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
            spannableNu.setSpan(highlightSpan, startPosNu, endPosNu, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (code.matches(".*[0-9]+.*"))
                holder.customerNumber.setText(mode + "/" + spannableNu + "/" + code);
            else
                holder.customerNumber.setText(mode + "/" + spannableNu);
        } else {
            if (code.matches(".*[0-9]+.*"))
                holder.customerNumber.setText(mode + "/" + number + "/" + code);
            else
                holder.customerNumber.setText(mode + "/" + number);
        }
        if (code.matches(".*[0-9]+.*")) {
            int startPosC = code.toLowerCase(Locale.getDefault()).indexOf(filteredText.toLowerCase(Locale.getDefault()));
            int endPosC = startPosC + filteredText.length();
            if (startPosC != -1) // This should always be true, just a sanity check
            {
                Spannable spannableC = new SpannableString(number);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.light_green)});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannableC.setSpan(highlightSpan, startPosC, endPosC, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.customerNumber.setText(mode + "/" + spannableC);
            } else
                holder.customerNumber.setText(mode + "/" + number + "/" + code);
        }
        holder.customerBalance.setText(customer.getBalance());
        holder.customerAmount.setText(customer.getAmount());
        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredText = charText;
        customersList.clear();
        if (charText.length() == 0) {
        } else {
            for (CustomerListChildItem customer : filteredCustomersList) {
                if ((customer.getName() + customer.getCode() + customer.getNumber()).toLowerCase(Locale.getDefault()).contains(charText)) {
                    customersList.add(customer);
                }
            }
        }
        notifyDataSetChanged();
    }
}
