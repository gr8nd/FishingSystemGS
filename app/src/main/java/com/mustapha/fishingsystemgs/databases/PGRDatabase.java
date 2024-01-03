package com.mustapha.fishingsystemgs.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mustapha.fishingsystemgs.classes.PGR;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PGRDatabase extends SQLiteOpenHelper {
    public static final String TAG = "PGR Tag";
    private static final String PGR_TABLE_NAME = "pgr";
    private static final String COLUMN_PGR_ID = "id";
    private static final String COLUMN_FIRST_DECIMAL_NUMBER = "firstdecimal";
    private static final String COLUMN_SECOND_DECIMAL_NUMBER = "seconddecimal";
    private static final String COLUMN_THIRD_DECIMAL_NUMBER = "thirddecimal";
    private static final String COLUMN_PGR_NAME = "pgrname";


    public PGRDatabase(@Nullable Context context, @Nullable String name,
                       @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PGR_TABLE_NAME + "(id TEXT PRIMARY KEY, firstdecimal TEXT, seconddecimal TEXT, thirddecimal TEXT, pgrname TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void insert(PGR PGR) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_PGR_ID, String.valueOf(PGR.getDna()));
            values.put(COLUMN_PGR_NAME, PGR.getName());
            values.put(COLUMN_FIRST_DECIMAL_NUMBER, PGR.getFirstDecimalNumber());
            values.put(COLUMN_SECOND_DECIMAL_NUMBER, PGR.getSecondDecimalNumber());
            values.put(COLUMN_THIRD_DECIMAL_NUMBER, PGR.getThirdDecimalNumber());

            db.insert(PGR_TABLE_NAME, null, values);

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
            return db.delete(PGR_TABLE_NAME, "id = ? ", new String[]{id});
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }

    public void deleteAll() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + PGR_TABLE_NAME);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public PGR get(String id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + PGR_TABLE_NAME + " WHERE id='" + id + "'", null);
            cursor.moveToFirst();
            if (cursor.getCount() >= 0) {
                PGR PGR = new PGR();

                PGR.setDna(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PGR_ID)));
                PGR.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PGR_NAME)));
                PGR.setFirstDecimalNumber(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_DECIMAL_NUMBER))));
                PGR.setSecondDecimalNumber(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SECOND_DECIMAL_NUMBER))));
                PGR.setThirdDecimalNumber(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THIRD_DECIMAL_NUMBER))));

                cursor.close();

                return PGR;
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
            cursor = db.rawQuery("SELECT * FROM " + PGR_TABLE_NAME, null);
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

    public List<PGR> getPGRs() {
        List<PGR> PGRS = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + PGR_TABLE_NAME, null);
            cursor.moveToLast();
            int count = cursor.getCount();
            if (count > 0) {
                do {
                    PGR PGR = new PGR();

                    PGR.setDna(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PGR_ID)));
                    PGR.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PGR_NAME)));
                    PGR.setFirstDecimalNumber(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_DECIMAL_NUMBER))));
                    PGR.setSecondDecimalNumber(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SECOND_DECIMAL_NUMBER))));
                    PGR.setThirdDecimalNumber(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THIRD_DECIMAL_NUMBER))));

                    PGRS.add(PGR);

                } while (cursor.moveToPrevious());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return PGRS;
    }
}
