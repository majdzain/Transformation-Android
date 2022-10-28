package com.transformation.zezoo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListTransformsActivity extends Fragment implements SearchView.OnQueryTextListener {
    Resources res;
    private TransformSQLDatabaseHandler db;

    ExpandableListView expListView;
    CustomTransformExpandableListAdapter expandableListAdapter;
    CustomSearchTransformsAdapter customSearchTransformsAdapter;
    ArrayList<String> listGroupTitles;
    HashMap<String, List<TransformListChildItem>> listChildData;
    ListView searchList;
    SearchView searchView;
    ArrayList<TransformListChildItem> listAllTransforms;

    int currentColumn;
    String currentName;
    String currentNumber;
    String currentAmount;
    String currentDate;
    String currentCode;
    String currentDebt;
    Context context;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_transforms, container, false);
        context = view.getContext();
        res = getResources();
        createSQLDatabase();
        createListFromSQL();
        setHasOptionsMenu(true);
        return view;
    }

    private void createSQLDatabase() {
        // create our sqlite helper class
        db = new TransformSQLDatabaseHandler(context);
    }

    private void addToSQL(int Column, String Name, String Number, String Amount, String Date, String Code,String Debt) {
        TransformListChildItem transform = new TransformListChildItem(Column, Name, Number, Amount, Date, Code, Debt);
        db.addTransform(transform);
    }

    private void deleteFromSQL(int Column, String Name, String Number, String Amount, String Date, String Code,String Debt) {
        TransformListChildItem transform = new TransformListChildItem(Column, Name, Number, Amount, Date, Code, Debt);
        db.deleteTransform(transform);
    }

    private void editFromSQL(int Column, String Name, String Number, String Amount, String Date, String Code,String Debt) {
        TransformListChildItem transform = new TransformListChildItem(Column, Name, Number, Amount, Date, Code, Debt);
        db.updateTransform(transform);
    }

    private void createListFromSQL() {
        // list all transforms
        List<TransformListChildItem> transforms = db.allTransforms();
        List<String> folders = db.allFolders();
        if (transforms != null) {
            createList(folders, transforms);
        }
    }

    private void createList(List<String> folders, List<TransformListChildItem> transforms) {
        // Get the expandable list
        expListView = (ExpandableListView) view.findViewById(R.id.transformsList);
        String[] itemsFolders = new String[folders.size()];
        for (int i = 0; i < folders.size(); i++) {
            itemsFolders[i] = folders.get(i);
        }
        int[] itemsTransformsColumns = new int[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            itemsTransformsColumns[i] = transforms.get(i).getColumn();
        }
        String[] itemsTransformsNames = new String[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            itemsTransformsNames[i] = transforms.get(i).getName();
        }
        String[] itemsTransformsNumbers = new String[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            itemsTransformsNumbers[i] = transforms.get(i).getNumber();
        }
        String[] itemsTransformsAmounts = new String[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            itemsTransformsAmounts[i] = transforms.get(i).getAmount();
        }
        String[] itemsTransformsDates = new String[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            itemsTransformsDates[i] = transforms.get(i).getDate();
        }
        String[] itemsTransformsCodes = new String[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            itemsTransformsCodes[i] = transforms.get(i).getCode();
        }
        String[] itemsTransformsDebts = new String[transforms.size()];
        for (int i = 0; i < transforms.size(); i++) {
            itemsTransformsDebts[i] = transforms.get(i).getDebt();
        }
        // Setting up list
        listGroupTitles = new ArrayList<String>(Arrays.asList(itemsFolders));
        listChildData = new HashMap<String, List<TransformListChildItem>>();
        // Adding district names and number of population as children
        for (int i1 = 0; i1 < listGroupTitles.size(); i1++) {
            String folder = itemsFolders[i1];
            List<TransformListChildItem> pDistricts = pDistricts = new ArrayList<TransformListChildItem>();
            for (int i = 0; i < transforms.size(); i++) {
                if(transforms.get(i).getName().matches("")){
                    if (transforms.get(i).getNumber().matches(folder)) {
                        pDistricts.add(new TransformListChildItem(itemsTransformsColumns[i], itemsTransformsNames[i], itemsTransformsNumbers[i], itemsTransformsAmounts[i], itemsTransformsDates[i],itemsTransformsCodes[i],itemsTransformsDebts[i]));
                    }
                }else {
                    if (transforms.get(i).getName().matches(folder)) {
                        pDistricts.add(new TransformListChildItem(itemsTransformsColumns[i], itemsTransformsNames[i], itemsTransformsNumbers[i], itemsTransformsAmounts[i], itemsTransformsDates[i],itemsTransformsCodes[i],itemsTransformsDebts[i]));
                    }
                }
            }
            listChildData.put(folder, pDistricts);
        }
        expandableListAdapter = new CustomTransformExpandableListAdapter(context, listGroupTitles, listChildData);
        // Setting list adapter
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                return true;
            }
        });
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ExpandableListView listView = (ExpandableListView) parent;
                long pos = listView.getExpandableListPosition(position);

                // get type and correct positions
                int itemType = ExpandableListView.getPackedPositionType(pos);
                int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                int childPos = ExpandableListView.getPackedPositionChild(pos);

                // if child is long-clicked
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    TransformListChildItem TLCI = (TransformListChildItem) expandableListAdapter.getChild(groupPos, childPos);
                    createPopupChildItemMenu(view, TLCI);
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    createPopupGroupItemMenu(view, groupPos);
                }
                return true;
            }
        });

    }
    
    private void createPopupChildItemMenu(View v, final TransformListChildItem TLCI) {
        PopupMenu popup = new PopupMenu(context, v);
        currentColumn = TLCI.getColumn();
        currentName = TLCI.getName();
        currentNumber = TLCI.getNumber();
        currentAmount = TLCI.getAmount();
        currentDate = TLCI.getDate();
        currentCode = TLCI.getCode();
        currentDebt = TLCI.getDebt();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pop_delete_item:
                        deleteFromSQL(TLCI.getColumn(), TLCI.getName(), TLCI.getNumber(), TLCI.getAmount(), TLCI.getDate(), TLCI.getCode(),TLCI.getDebt());
                        createListFromSQL();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.popup_menu_list_transform);
        popup.show();
    }

    private void createPopupGroupItemMenu(View view, final int groupPos) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.pop_delete_item:
                        createAlertDeleteFolderDialog(groupPos);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.popup_menu_list_transform);
        popup.show();
    }

    private void createAlertDeleteFolderDialog(final int groupPos) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("تحذير");
        alertDialog.setMessage("سوف يتم حذف جميع سجلات التحويل لهذا الرقم/الاسم");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "موافق",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        List<TransformListChildItem> transforms = db.allTransforms();
                        for (int i = 0; i < transforms.size(); i++) {
                            TransformListChildItem transform = transforms.get(i);
                            if(transform.getName().matches("")){
                                if (transform.getNumber().matches(listGroupTitles.get(groupPos))) {
                                    deleteFromSQL(transform.getColumn(), transform.getName(), transform.getNumber(), transform.getAmount(), transform.getDate(),transform.getCode(),transform.getDebt());
                                }
                            }else {
                                if (transform.getName().matches(listGroupTitles.get(groupPos))) {
                                    deleteFromSQL(transform.getColumn(), transform.getName(), transform.getNumber(), transform.getAmount(), transform.getDate(),transform.getCode(),transform.getDebt());
                                }
                            }
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
                        List<TransformListChildItem> transforms = db.allTransforms();
                        for (int i = 0; i < transforms.size(); i++) {
                            TransformListChildItem TLCI = transforms.get(i);
                            deleteFromSQL(TLCI.getColumn(), TLCI.getName(), TLCI.getNumber(), TLCI.getAmount(), TLCI.getDate(), TLCI.getCode(),TLCI.getDebt());
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
        d1.setContentView(R.layout.dialog_search_transform);
        d1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancel_search = (Button) d1.findViewById(R.id.cancel_dialog_search);
        searchList = (ListView) d1.findViewById(R.id.list_transforms_search);
        searchView = (SearchView) d1.findViewById(R.id.edit_search_transforms);
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
        listAllTransforms = new ArrayList<TransformListChildItem>();
        listAllTransforms.addAll(db.allTransforms());
        customSearchTransformsAdapter = new CustomSearchTransformsAdapter(context, R.layout.list_transforms_item, listAllTransforms);
        searchList.setAdapter(customSearchTransformsAdapter);
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
        customSearchTransformsAdapter.filter(text);
        return false;
    }


}
