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

public class CustomSearchTransformsAdapter extends ArrayAdapter<TransformListChildItem> {

    Context context;
    LayoutInflater inflater;
    private List<TransformListChildItem> transformsList;
    private ArrayList<TransformListChildItem> filteredTransformsList;
    String filteredText;

    public CustomSearchTransformsAdapter(Context context, int resId, ArrayList<TransformListChildItem> transforms) {
        super(context, resId);
        this.transformsList = transforms;
        this.filteredTransformsList = transforms;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.filteredTransformsList = new ArrayList<TransformListChildItem>();
        this.filteredTransformsList.addAll(transformsList);
        transformsList.clear();
    }

    /*private view holder class*/
    private class ViewHolder {
        LinearLayout transformLinear;
        TextView transformName;
        TextView transformDate;
        TextView transformNumber;
        TextView transformAmount;
    }

    @Override
    public int getCount() {
        return transformsList.size();
    }

    @Override
    public TransformListChildItem getItem(int position) {
        return transformsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomSearchTransformsAdapter.ViewHolder holder = null;
        TransformListChildItem transform = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_transforms_item, null);
            holder = new CustomSearchTransformsAdapter.ViewHolder();
            holder.transformLinear = (LinearLayout) convertView.findViewById(R.id.transforms_linear);
            holder.transformName = (TextView) convertView.findViewById(R.id.transformName);
            holder.transformDate = (TextView) convertView.findViewById(R.id.transformDate);
            holder.transformNumber = (TextView) convertView.findViewById(R.id.transformNumber);
            holder.transformAmount = (TextView) convertView.findViewById(R.id.transformAmount);
            convertView.setTag(holder);
        } else
            holder = (CustomSearchTransformsAdapter.ViewHolder) convertView.getTag();
        int type = 1;
        if (transform.getNumber().startsWith("093") || transform.getNumber().startsWith("93") || transform.getNumber().startsWith("098") || transform.getNumber().startsWith("98") || transform.getNumber().startsWith("099") || transform.getNumber().startsWith("99")) {
            type = 1;
        } else if (transform.getNumber().startsWith("094") || transform.getNumber().startsWith("94") || transform.getNumber().startsWith("095") || transform.getNumber().startsWith("95") || transform.getNumber().startsWith("096") || transform.getNumber().startsWith("96") || transform.getNumber().startsWith("011") || transform.getNumber().startsWith("033") || transform.getNumber().startsWith("052") || transform.getNumber().startsWith("014") || transform.getNumber().startsWith("021") || transform.getNumber().startsWith("051") || transform.getNumber().startsWith("013") || transform.getNumber().startsWith("016") || transform.getNumber().startsWith("022") || transform.getNumber().startsWith("031") || transform.getNumber().startsWith("041") || transform.getNumber().startsWith("043") || transform.getNumber().startsWith("023") || transform.getNumber().startsWith("015")) {
            type = 2;
        } else if (transform.getNumber().length() == 8 && !(transform.getNumber().startsWith("0") || transform.getNumber().startsWith("9"))) {
            type = 1;
        } else if (transform.getNumber().length() == 9 && !(transform.getNumber().startsWith("*") || transform.getNumber().startsWith("#") || transform.getNumber().startsWith("0") || transform.getNumber().startsWith("9"))) {
            type = 2;
        }
        if (type == 1) {
            holder.transformLinear.setBackground(context.getResources().getDrawable(R.drawable.list_syr_item_selector));
        } else {
            holder.transformLinear.setBackground(context.getResources().getDrawable(R.drawable.list_mtn_item_selector));
        }
        String name = transform.getName();
        String date = transform.getDate();
        String number = transform.getNumber();
        String code = transform.getCode();


        int startPosNa = name.toLowerCase(Locale.getDefault()).indexOf(filteredText.toLowerCase(Locale.getDefault()));
        int endPosNa = startPosNa + filteredText.length();
        int startPosNu = number.toLowerCase(Locale.getDefault()).indexOf(filteredText.toLowerCase(Locale.getDefault()));
        int endPosNu = startPosNu + filteredText.length();
        int startPosD = date.toLowerCase(Locale.getDefault()).indexOf(filteredText.toLowerCase(Locale.getDefault()));
        int endPosD = startPosD + filteredText.length();
        int startPosC = code.toLowerCase(Locale.getDefault()).indexOf(filteredText.toLowerCase(Locale.getDefault()));
        int endPosC = startPosC + filteredText.length();
        if (startPosNa != -1) // This should always be true, just a sanity check
        {
            Spannable spannableN = new SpannableString(name);
            ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.light_green)});
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
            spannableN.setSpan(highlightSpan, startPosNa, endPosNa, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.transformName.setText(spannableN);
        } else {
            holder.transformName.setText(name);
        }
        if (startPosNu != -1) // This should always be true, just a sanity check
        {
            Spannable spannableNu = new SpannableString(number);
            ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.light_green)});
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
            spannableNu.setSpan(highlightSpan, startPosNu, endPosNu, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (code.matches(".*[0-9]+.*"))
                holder.transformNumber.setText(spannableNu + "/" + code);
            else
                holder.transformNumber.setText(spannableNu);
        } else {
            if (code.matches(".*[0-9]+.*"))
                holder.transformNumber.setText(number + "/" + code);
            else
                holder.transformNumber.setText(number);
        }
        if (startPosD != -1) // This should always be true, just a sanity check
        {
            Spannable spannableD = new SpannableString(date);
            ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.light_green)});
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);

            spannableD.setSpan(highlightSpan, startPosD, endPosD, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.transformDate.setText(spannableD);
        } else
            holder.transformDate.setText(date);
        if (code.matches(".*[0-9]+.*")) {
            if (startPosC != -1) // This should always be true, just a sanity check
            {
                Spannable spannableC = new SpannableString(code);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.light_green)});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);

                spannableC.setSpan(highlightSpan, startPosC, endPosC, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.transformNumber.setText(number + "/" + spannableC);
            } else
                holder.transformNumber.setText(number + "/" + code);
        }
        holder.transformAmount.setText(transform.getAmount());
        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredText = charText;
        transformsList.clear();
        if (charText.length() == 0) {
        } else {
            for (TransformListChildItem transform : filteredTransformsList) {
                if ((transform.getName() + transform.getDate() + transform.getNumber() + transform.getCode()).toLowerCase(Locale.getDefault()).contains(charText)) {
                    transformsList.add(transform);
                }
            }
        }
        notifyDataSetChanged();
    }

}
