package com.example.sqltest;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sqltest.data.PeopleContract;

public class PeopleCursorAdapter extends CursorAdapter  {


    public PeopleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.surname);

        int nameColumnIndex = cursor.getColumnIndex(PeopleContract.PeopleEntry.COLUMN_NAME);
        int surnameColumnIndex = cursor.getColumnIndex(PeopleContract.PeopleEntry.COLUMN_SURNAME);

        String petName = cursor.getString(nameColumnIndex);
        String petSurname = cursor.getString(surnameColumnIndex);

        nameTextView.setText(petName);
        summaryTextView.setText(petSurname);
    }
}
