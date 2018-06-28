package com.example.sebas.hotdogadmin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


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
        final TextView orderBbq = (TextView) row.findViewById(R.id.order_bbq_in);
        final TextView orderKetchup = (TextView) row.findViewById(R.id.order_ketchup_in);
        final TextView orderMayonnaise = (TextView) row.findViewById(R.id.order_mayonnaise_in);
        final TextView orderCurry = (TextView) row.findViewById(R.id.order_curry_in);
        final TextView orderOnion = (TextView) row.findViewById(R.id.order_onion_in);
        final TextView orderCheese = (TextView) row.findViewById(R.id.order_cheese_in);
        final CheckBox checkBox = (CheckBox) row.findViewById(R.id.paid_checkbox);

        orderNumber.setText(currentItem.getHotdog());
        orderBbq.setText(String.valueOf(currentItem.isBbqSauce()));
        orderKetchup.setText(String.valueOf(currentItem.isKetchup()));
        orderMayonnaise.setText(String.valueOf(currentItem.isMayonnaise()));
        orderCurry.setText(String.valueOf(currentItem.isCurry()));
        orderOnion.setText(String.valueOf(currentItem.isOnion()));
        orderCheese.setText(String.valueOf(currentItem.isCheese()));
        checkBox.setText(String.valueOf(currentItem.getTotalPrice() + " €"));
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