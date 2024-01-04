package com.mustapha.fishingsystemgs.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mustapha.fishingsystemgs.classes.TS;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TSDatabase extends SQLiteOpenHelper {
    public static final String TAG = "TS Tag";
    private static final String TS_TABLE_NAME = "ts";
    private static final String COLUMN_TS_ID = "id";

    private static final String COLUMN_TS_PGR_DNA = "dna";
    private static final String COLUMN_THIRD_DECIMAL_NUMBER = "thirddecimal";
    private static final String COLUMN_TS_NAME = "tsname";

    public TSDatabase(@Nullable Context context, @Nullable String name,
                      @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TS_TABLE_NAME + "(id TEXT PRIMARY KEY, dna TEXT, thirddecimal TEXT, tsname TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(TS ts) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_TS_ID, ts.getId());
            values.put(COLUMN_TS_PGR_DNA, ts.getDnaOfMother());
            values.put(COLUMN_THIRD_DECIMAL_NUMBER, ts.getThirdDecimalOfMother());
            values.put(COLUMN_TS_NAME, ts.getName());


            db.insert(TS_TABLE_NAME, null, values);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public long size() {
        SQLiteDatabase db = this.getReadableDatabase();
        return new File(db.getPath()).length();
    }

    public int delete(String id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TS_TABLE_NAME, "id = ? ", new String[]{id});
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }

    public void deleteAll() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TS_TABLE_NAME);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public TS get(String id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TS_TABLE_NAME + " WHERE id='" + id + "'", null);
            cursor.moveToFirst();
            if (cursor.getCount() >= 0) {
                TS ts = new TS();

                ts.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TS_ID)));
                ts.setDnaOfMother(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TS_PGR_DNA)));
                ts.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TS_NAME)));
                ts.setThirdDecimalOfMother(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THIRD_DECIMAL_NUMBER))));

                cursor.close();

                return ts;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    public int count() {
        Cursor cursor = null;
        int size = 0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TS_TABLE_NAME, null);
            cursor.moveToFirst();
            size = cursor.getCount();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return size;
    }

    public List<TS> getTss() {
        List<TS> tss = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TS_TABLE_NAME, null);
            cursor.moveToLast();
            int count = cursor.getCount();
            if (count > 0) {
                do {
                    TS ts = new TS();

                    ts.setDnaOfMother(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TS_ID)));
                    ts.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TS_NAME)));
                    ts.setThirdDecimalOfMother(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THIRD_DECIMAL_NUMBER))));

                    tss.add(ts);

                } while (cursor.moveToPrevious());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tss;
    }
}
