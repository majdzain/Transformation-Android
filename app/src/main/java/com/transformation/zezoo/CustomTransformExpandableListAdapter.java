package com.transformation.zezoo;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CustomTransformExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listGroupTitle; // header titles
    private HashMap<String, List<TransformListChildItem>> listChildData;
    private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET = {android.R.attr.state_expanded};
    private static final int[][] GROUP_STATE_SETS = {EMPTY_STATE_SET, // 0
            GROUP_EXPANDED_STATE_SET // 1
    };

    public CustomTransformExpandableListAdapter(Context context, List<String> listGroupTitle, HashMap<String, List<TransformListChildItem>> listChildData) {
        this.context = context;
        this.listGroupTitle = listGroupTitle;
        this.listChildData = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
        return this.listChildData.get(this.listGroupTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TransformListChildItem memData = (TransformListChildItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_transforms_item, null);
        }
        int type = 1;
        if (memData.getNumber().startsWith("093") || memData.getNumber().startsWith("93") || memData.getNumber().startsWith("098") || memData.getNumber().startsWith("98") || memData.getNumber().startsWith("099") || memData.getNumber().startsWith("99")) {
            type = 1;
        } else if (memData.getNumber().startsWith("094") || memData.getNumber().startsWith("94") || memData.getNumber().startsWith("095") || memData.getNumber().startsWith("95") || memData.getNumber().startsWith("096") || memData.getNumber().startsWith("96") || memData.getNumber().startsWith("011") || memData.getNumber().startsWith("033") || memData.getNumber().startsWith("052") || memData.getNumber().startsWith("014") || memData.getNumber().startsWith("021") || memData.getNumber().startsWith("051") || memData.getNumber().startsWith("013") || memData.getNumber().startsWith("016") || memData.getNumber().startsWith("022") || memData.getNumber().startsWith("031") || memData.getNumber().startsWith("041") || memData.getNumber().startsWith("043") || memData.getNumber().startsWith("023") || memData.getNumber().startsWith("015")) {
            type = 2;
        } else if (memData.getNumber().length() == 8 && !(memData.getNumber().startsWith("0") || memData.getNumber().startsWith("9"))) {
            type = 1;
        } else if (memData.getNumber().length() == 9 && !(memData.getNumber().startsWith("*") || memData.getNumber().startsWith("#") || memData.getNumber().startsWith("0") || memData.getNumber().startsWith("9"))) {
            type = 2;
        }
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.transforms_linear);
        if (type == 1) {
            ll.setBackground(context.getResources().getDrawable(R.drawable.list_syr_item_selector));
        } else {
            ll.setBackground(context.getResources().getDrawable(R.drawable.list_mtn_item_selector));
        }
        TextView transformName = (TextView) convertView.findViewById(R.id.transformName);
        TextView transformAmount = (TextView) convertView.findViewById(R.id.transformAmount);
        TextView transformNumber = (TextView) convertView.findViewById(R.id.transformNumber);
        TextView transformDate = (TextView) convertView.findViewById(R.id.transformDate);

        if (memData.getCode().matches(".*[0-9]+.*"))
            transformNumber.setText(memData.getNumber() + "/" + memData.getCode());
        else
            transformNumber.setText(memData.getNumber());
        transformAmount.setText(memData.getAmount());
        transformName.setText(memData.getName());
        transformDate.setText(memData.getDate());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildData.get(this.listGroupTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroupTitle.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listGroupTitle.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_transforms_item, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTransformItemTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        //Image view which you put in row_group_list.xml
        View ind = convertView.findViewById(R.id.groupTransformsIndicator);
        if (ind != null) {
            ImageView indicator = (ImageView) ind;
            if (getChildrenCount(groupPosition) == 0) {
                indicator.setVisibility(View.INVISIBLE);
            } else {
                indicator.setVisibility(View.VISIBLE);
                int stateSetIndex = (isExpanded ? 1 : 0);
                Drawable drawable = indicator.getDrawable();
                drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
            }
        }
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}