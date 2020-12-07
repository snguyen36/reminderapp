package com.example.ReminderApp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] reminder;
    private final String[] setDate;
    public CustomAdapter(Activity context,
                      String[] reminder, String[] setDate) {
        super(context, R.layout.item_layout, reminder);
        this.context = context;
        this.reminder = reminder;
        this.setDate = setDate;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.item_layout, null, true);
        TextView Title = (TextView) rowView.findViewById(R.id.item1);
        TextView date = (TextView) rowView.findViewById(R.id.item2);
        Title.setText(reminder[position]);
        date.setText(setDate[position]);
        return rowView;
    }
}
