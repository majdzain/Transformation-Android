package com.transformation.zezoo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomCustomerEditArrayAdapter extends ArrayAdapter<CustomerListChildItem> {
    Context context;
    LayoutInflater inflater;
    private ArrayList<CustomerListChildItem> customers;
    TransformSQLDatabaseHandler dbt;

    public CustomCustomerEditArrayAdapter(Context context, int resId, ArrayList<CustomerListChildItem> customers) {
        super(context, 0, customers);
        this.customers = customers;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.list_customer_edit_item, parent, false);
        CustomerListChildItem customer = customers.get(position);
        LinearLayout linear = (LinearLayout) listItem.findViewById(R.id.linear);
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
            linear.setBackgroundColor(context.getResources().getColor(R.color.trans_red));
        } else {
            linear.setBackgroundColor(context.getResources().getColor(R.color.trans_yellow));
        }
        TextView name = (TextView) listItem.findViewById(R.id.txtName);
        name.setText(customer.getName());
        TextView number = (TextView) listItem.findViewById(R.id.txtNumber);
        TextView last = (TextView) listItem.findViewById(R.id.txtLast);
        TextView total = (TextView) listItem.findViewById(R.id.txtTotal);
        dbt = new TransformSQLDatabaseHandler(context);
        last.setText("0");
        total.setText("0");
        if(customer.getBalance().contains("t")) {
            last.setText(customer.getBalance().substring(0, customer.getBalance().indexOf("t")));
            total.setText(customer.getBalance().substring( customer.getBalance().indexOf("t")+1));
        }
        String mode = "";
        if (customer.getMode().matches("code_pre_mtn") || customer.getMode().matches("code_pre_syr"))
            mode = "وحدات";
        else if (customer.getMode().matches("code_post_mtn") || customer.getMode().matches("code_post_syr") || customer.getMode().matches("code_post_adsl_mtn"))
            mode = "فاتورة";
        else if (customer.getMode().matches("code_pre_who_mtn"))
            mode = "جملة وحدات";
        else if (customer.getMode().matches("code_post_who_mtn"))
            mode = "جملة فاتورة";
        else if (customer.getMode().matches("code_pre_post_who_syr"))
            mode = "جملة";
        else if (customer.getMode().matches("code_check"))
            mode = "فحص";
        else
            mode = customer.getMode();
        if (customer.getCode().matches(".*[0-9]+.*"))
            number.setText(mode + "/" + customer.getNumber() + "/" + customer.getCode());
        else
            number.setText(mode + "/" + customer.getNumber());
        return listItem;
    }
}
