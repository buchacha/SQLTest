package com.example.sqltest.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PeopleContract {

    private PeopleContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.sqltest.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_PEOPLE= "people";

    public static final class PeopleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PEOPLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PEOPLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PEOPLE;

        public final static String TABLE_NAME = "people";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME ="name";

        public final static String COLUMN_SURNAME = "surname";


    }

}
