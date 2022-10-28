package com.transformation.zezoo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codekidlabs.storagechooser.Content;
import com.codekidlabs.storagechooser.StorageChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ListCustomerActivity extends Fragment implements SearchView.OnQueryTextListener {
    private CustomerSQLDatabaseHandler db;
    Resources res;
    Context context;
    View view;
    ListView list;
    ArrayList<CustomerListChildItem> listItems;
    CustomCustomerListAdapter customAdapter;
    int currentColumn;
    String currentName;
    String currentNumber;
    String currentAmount;
    String currentBalance;
    String currentCode;
    String currentMode;
    ListView searchList;
    SearchView searchView;
    ArrayList<CustomerListChildItem> listAllCustomers;
    CustomSearchCustomerAdapter customSearchCustomerAdapter;
    Button btn_close, btn_save;
    EditText edit_code, edit_number, edit_name;
    Spinner spin_oper;
    String mode;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_customers, container, false);
        context = view.getContext();
        res = getResources();
        createSQLDatabase();
        createListFromSQL();
        setHasOptionsMenu(true);
        return view;
    }

    private void createSQLDatabase() {
        // create our sqlite helper class
        db = new CustomerSQLDatabaseHandler(context);
    }

    private void addToSQL(int Column, String Name, String Number, String Amount, String Balance,String Code,String Mode) {
        CustomerListChildItem customer = new CustomerListChildItem(Column, Name, Number, Amount, Balance,Code,Mode);
        db.addCustomer(customer);
    }

    private void deleteFromSQL(int Column, String Name, String Number, String Amount, String Balance,String Code,String Mode) {
        CustomerListChildItem customer = new CustomerListChildItem(Column, Name, Number, Amount, Balance,Code,Mode);
        db.deleteCustomer(customer);
    }

    private void editFromSQL(int Column, String Name, String Number, String Amount, String Balance,String Code,String Mode) {
        CustomerListChildItem customer = new CustomerListChildItem(Column, Name, Number, Amount, Balance,Code,Mode);
        db.updateCustomer(customer);
    }

    private void createListFromSQL() {
        // list all customers
        List<CustomerListChildItem> customers = db.allCustomers();
        if (customers != null) {
            createList(customers);
        }
    }

    private void createList(List<CustomerListChildItem> customers) {
        // Get the expandable list
        list = (ListView) view.findViewById(R.id.customers_list);
        int[] itemsCustomersColumns = new int[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            itemsCustomersColumns[i] = customers.get(i).getColumn();
        }
        String[] itemsCustomersNames = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            itemsCustomersNames[i] = customers.get(i).getName();
        }
        String[] itemsCustomersNumbers = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            itemsCustomersNumbers[i] = customers.get(i).getNumber();
        }
        String[] itemsCustomersAmounts = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            itemsCustomersAmounts[i] = customers.get(i).getAmount();
        }
        String[] itemsCustomersBalances = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            itemsCustomersBalances[i] = customers.get(i).getBalance();
        }
        String[] itemsCustomersCodes = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            itemsCustomersCodes[i] = customers.get(i).getCode();
        }
        String[] itemsCustomersModes = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            itemsCustomersModes[i] = customers.get(i).getMode();
        }
        listItems = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++) {
            listItems.add(new CustomerListChildItem(itemsCustomersColumns[i], itemsCustomersNames[i], itemsCustomersNumbers[i], itemsCustomersAmounts[i], itemsCustomersBalances[i],itemsCustomersCodes[i],itemsCustomersModes[i]));
        }
        customAdapter = new CustomCustomerListAdapter(context,listItems);
        list.setAdapter(customAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomerListChildItem CLCI = db.allCustomers().get(i);
                currentColumn = CLCI.getColumn();
                currentName = CLCI.getName();
                currentNumber = CLCI.getNumber();
                currentAmount = CLCI.getAmount();
                currentBalance = CLCI.getBalance();
                currentCode = CLCI.getCode();
                currentMode = CLCI.getMode();
                createAddNewDialog();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                createPopupItemMenu(view,db.allCustomers().get(position));
                // Return true to consume the click event. In this case the
                // onListItemClick listener is not called anymore.
                return true;
            }
        });
    }

    private void createAddNewDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_add_new);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        edit_number = (EditText) d.findViewById(R.id.edit_number);
        edit_name = (EditText) d.findViewById(R.id.edit_name);
        edit_code = (EditText) d.findViewById(R.id.edit_code);
        btn_save = (Button) d.findViewById(R.id.btn_save);
        btn_close = (Button) d.findViewById(R.id.btn_close);
        spin_oper = (Spinner) d.findViewById(R.id.spin_operation);
        String[] transformTypes = res.getStringArray(R.array.transformSpecial);
        ArrayAdapter<CharSequence> spinnerOperArrayAdapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_item_operation, transformTypes);
        spinnerOperArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_operation);
        spin_oper.setAdapter(spinnerOperArrayAdapter);
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
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_number.getText().toString().matches(".*[0-9]+.*") && edit_name.getText().toString().matches(".*[a-zA-Z0-9أ-ي]+.*")) {
                    if (edit_number.getText().toString().length() > 8) {
                            db = new CustomerSQLDatabaseHandler(context);
                            CustomerListChildItem customer = new CustomerListChildItem();
                            customer.setColumn(currentColumn);
                            customer.setName(edit_name.getText().toString());
                            customer.setNumber(edit_number.getText().toString());
                            customer.setBalance(currentBalance);
                            customer.setAmount(currentAmount);
                            customer.setCode(edit_code.getText().toString());
                            customer.setMode(mode);
                            db.updateCustomer(customer);
                            d.cancel();
                            createListFromSQL();
                    } else {
                        edit_number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                        Toast.makeText(context, "الرقم صغير", 1000).show();
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
        edit_name.setText(currentName);
        edit_number.setText(currentNumber);
        edit_code.setText(currentCode);
        int i = 0;
        switch (currentMode) {
            case "code_pre_mtn":
                i = 0;
                break;
            case "code_pre_syr":
                i = 1;
                break;
            case  "code_post_mtn":
                i = 2;
                break;
            case "code_post_syr":
                i = 3;
                break;
            case "code_post_adsl_mtn":
                i = 4;
                break;
            case "code_pre_post_who_syr":
                i = 5;
                break;
            case "code_pre_who_mtn":
                i = 6;
                break;
            case "code_post_who_mtn":
                i = 7;
                break;
            case "code_check":
                i = 8;
                break;
        }
        spin_oper.setSelection(i);
        d.show();
    }
    
    private void createPopupItemMenu(View v, final CustomerListChildItem CLCI) {
        PopupMenu popup = new PopupMenu(context, v);
        currentColumn = CLCI.getColumn();
        currentName = CLCI.getName();
        currentNumber = CLCI.getNumber();
        currentAmount = CLCI.getAmount();
        currentBalance = CLCI.getBalance();
        currentCode = CLCI.getCode();
        currentMode = CLCI.getMode();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pop_delete_item:
                        deleteFromSQL(CLCI.getColumn(), CLCI.getName(), CLCI.getNumber(), CLCI.getAmount(), CLCI.getBalance(), CLCI.getCode(),CLCI.getMode());
                        createListFromSQL();
                        return true;
                    case R.id.pop_edit_item:
                        createAddNewDialog();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.popup_menu_list_transform_item);
        popup.show();
    }
    Menu men;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_transforms, menu);
        men = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_t_search:
                createSearchDialog();
                return true;
            case R.id.menu_t_deleteAll:
                createAlertDeleteAllDialog();
                return true;
            case android.R.id.home:
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createAlertDeleteAllDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("تحذير");
        alertDialog.setMessage("سوف يتم حذف سجل التحويلات كله");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "موافق",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        List<CustomerListChildItem> customers = db.allCustomers();
                        for (int i = 0; i < customers.size(); i++) {
                            CustomerListChildItem CLCI = customers.get(i);
                            deleteFromSQL(CLCI.getColumn(), CLCI.getName(), CLCI.getNumber(), CLCI.getAmount(), CLCI.getBalance(), CLCI.getCode(),CLCI.getMode());
                        }
                        createListFromSQL();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "إلغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void createSearchDialog() {
        final Dialog d1 = new Dialog(context);
        d1.setContentView(R.layout.dialog_search_customer);
        d1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancel_search = (Button) d1.findViewById(R.id.cancel_dialog_search);
        searchList = (ListView) d1.findViewById(R.id.list_customers_search);
        searchView = (SearchView) d1.findViewById(R.id.edit_search_customers);
        createSearchList();
        cancel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d1.cancel();
            }
        });
        d1.setCancelable(false);
        d1.setCanceledOnTouchOutside(false);
        d1.show();
    }

    private void createSearchList() {
        listAllCustomers = new ArrayList<CustomerListChildItem>();
        listAllCustomers.addAll(db.allCustomers());
        customSearchCustomerAdapter = new CustomSearchCustomerAdapter(context, R.layout.list_customer_item, listAllCustomers);
        searchList.setAdapter(customSearchCustomerAdapter);
        searchView.setOnQueryTextListener(this);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        customSearchCustomerAdapter.filter(text);
        return false;
    }
}
