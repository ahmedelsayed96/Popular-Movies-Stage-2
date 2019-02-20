package com.example.mido.moviesapp.Movies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.util.HashMap;

/**
 * Created by mido on 4/9/17.
 */

public class MovieProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.mido.moviesapp";
    public static final int MOVIES = 1;
    public static final String MOVIES_TABLE = "Movies";
    public static final String MOVIE_NAME = "Name";
    private static final String MOVIE_URL = "content://" + AUTHORITY + "/"+MOVIES_TABLE;
    public static final String MOVIE_IMAGE = "Image";
    public static final String MOVIE_ID = "Id";
    public static final String CREATE_TABLE = "CREATE TABLE " + MOVIES_TABLE +
            "(" +
            MOVIE_ID + " INTEGER PRIMARY KEY ," +
            MOVIE_NAME + " TEXT ," +
            MOVIE_IMAGE + " TEXt )";
    private final UriMatcher sUriMatcher = buildUriMatcher();

    public static final Uri MOVIE_URI = Uri.parse(MOVIE_URL);
    private MyDataBaseHelper myDBHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MOVIES_TABLE, MOVIES);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        myDBHelper = new MyDataBaseHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArg,
                        String sortOrder
    ) {
        SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();

        Cursor cursor;
        int match=sUriMatcher.match(uri);
        Log.e("Match",""+match);
        switch (match) {
            case MOVIES:

                cursor = sqlDB.query(MOVIES_TABLE,
                                     projection,
                                     selection,
                                     selectionArg,
                                     null,
                                     null,
                                     sortOrder
                );
                Log.e("CursonProvider",""+cursor.getColumnCount());
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case MOVIES:

                Long id = sqLiteDatabase.insert(MOVIES_TABLE, null, contentValues);
                if (id > 0) {

                    returnUri = ContentUris.withAppendedId(MOVIE_URI, id);

                    getContext().getContentResolver().notifyChange(returnUri, null);

                } else {
                    Toast.makeText(getContext(), "Row Insert Failed ", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
        int rowDeleted = 0;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                rowDeleted = sqLiteDatabase.delete(MOVIES_TABLE, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Fail");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private class MyDataBaseHelper extends SQLiteOpenHelper {

        MyDataBaseHelper(Context context) {
            super(context, MOVIES_TABLE, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + MOVIES_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
