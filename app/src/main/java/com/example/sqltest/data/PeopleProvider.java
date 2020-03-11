package com.example.sqltest.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class PeopleProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PeopleProvider.class.getSimpleName();

    private static final int PEOPLE = 100;

    private static final int PEOPLE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(PeopleContract.CONTENT_AUTHORITY, PeopleContract.PATH_PEOPLE, PEOPLE);

        sUriMatcher.addURI(PeopleContract.CONTENT_AUTHORITY, PeopleContract.PATH_PEOPLE + "/#", PEOPLE_ID);
    }

    /** Database helper object */
    private PeopleDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PeopleDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PEOPLE:

                cursor = database.query(PeopleContract.PeopleEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PEOPLE_ID:
                selection = PeopleContract.PeopleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PeopleContract.PeopleEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PEOPLE:
                return insertPeople(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPeople(Uri uri, ContentValues values) {
        String name = values.getAsString(PeopleContract.PeopleEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("People requires a name");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(PeopleContract.PeopleEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PEOPLE:
                return updatePeople(uri, contentValues, selection, selectionArgs);
            case PEOPLE_ID:
                selection = PeopleContract.PeopleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePeople(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePeople(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(PeopleContract.PeopleEntry.COLUMN_NAME)) {
            String name = values.getAsString(PeopleContract.PeopleEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(PeopleContract.PeopleEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PEOPLE:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(PeopleContract.PeopleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PEOPLE_ID:
                // Delete a single row given by the ID in the URI
                selection = PeopleContract.PeopleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(PeopleContract.PeopleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PEOPLE:
                return PeopleContract.PeopleEntry.CONTENT_LIST_TYPE;
            case PEOPLE_ID:
                return PeopleContract.PeopleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
