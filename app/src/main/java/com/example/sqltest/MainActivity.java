package com.example.sqltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sqltest.data.PeopleContract;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private EditText et1;
    private EditText et2;
    private ListView lv;
    private Button b;

    private static final int LOADER = 0;

    PeopleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mCursorAdapter = new PeopleCursorAdapter(this, null);
        lv.setAdapter(mCursorAdapter);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPeople(et1.getText().toString(), et2.getText().toString());
            }
        });

        getLoaderManager().initLoader(LOADER, null, this);
    }

    void initViews() {
        et1 = findViewById(R.id.editText);
        et2 = findViewById(R.id.editText2);
        lv = findViewById(R.id.list);
        b = findViewById(R.id.button);

    }

    private void insertPeople(String name, String surname) {

        ContentValues values = new ContentValues();
        values.put(PeopleContract.PeopleEntry.COLUMN_NAME, name);
        values.put(PeopleContract.PeopleEntry.COLUMN_SURNAME, surname);

        Uri newUri = getContentResolver().insert(PeopleContract.PeopleEntry.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PeopleContract.PeopleEntry._ID,
                PeopleContract.PeopleEntry.COLUMN_NAME,
                PeopleContract.PeopleEntry.COLUMN_SURNAME };

        return new CursorLoader(this,
                PeopleContract.PeopleEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
