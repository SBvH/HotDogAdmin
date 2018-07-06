package com.example.sebas.hotdogadmin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Adapter to bind a ToDoItem List to a view
 */
public class HotDogItemAdapter extends ArrayAdapter<HotDogItem> {

    /**
     * Adapter context
     */
    Context mContext;

    /**
     * Adapter View layout
     */
    int mLayoutResourceId;

    public HotDogItemAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final HotDogItem currentItem = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
        }

        row.setTag(currentItem);
        final TextView orderNumber = (TextView) row.findViewById(R.id.order_number_in);
        final TextView orderBbq = (TextView) row.findViewById(R.id.order_bbq);
        final TextView orderKetchup = (TextView) row.findViewById(R.id.order_ketchup);
        final TextView orderMayonnaise = (TextView) row.findViewById(R.id.order_mayonnaise);
        final TextView orderCurry = (TextView) row.findViewById(R.id.order_curry);
        final TextView orderOnion = (TextView) row.findViewById(R.id.order_onion);
        final TextView orderCheese = (TextView) row.findViewById(R.id.order_cheese);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.paid_checkbox);
        final TextView orderExtras = (TextView) row.findViewById(R.id.order_extra);
        ArrayList<String> wordArrayList = new ArrayList<String>();

        orderNumber.setText(currentItem.getHotdog());

        if (currentItem.isBbqSauce()){
            wordArrayList.add("BBQ-Sauce");
        }

        if (currentItem.isKetchup()){
            wordArrayList.add("Ketchup");
        }

        if (currentItem.isMayonnaise()){
            wordArrayList.add("Mayonnaise");
        }

        if (currentItem.isCurry()){
            wordArrayList.add("Curry");
        }

        if (currentItem.isOnion()){
            wordArrayList.add("Onion");
        }

        if (currentItem.isCheese()){
            wordArrayList.add("Cheese");
        }

        orderExtras.setText(String.valueOf(wordArrayList));

        checkBox.setText(String.format("%.2f",currentItem.getTotalPrice()) + " €");
        checkBox.setChecked(false);
        checkBox.setEnabled(true);

        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (checkBox.isChecked()) {
                    checkBox.setEnabled(false);
                    if (mContext instanceof OrderActivity) {
                        OrderActivity activity = (OrderActivity) mContext;
                        activity.checkItem(currentItem);
                    }
                }
            }
        });

        return row;
    }

}