package com.mustapha.fishingsystemgs.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mustapha.fishingsystemgs.classes.TSG;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TSGDatabase extends SQLiteOpenHelper {
        public static final String TAG = "TSG Tag";
        private static final String TSG_TABLE_NAME = "tsg";
        private static final String COLUMN_TSG_ID = "id";
        private static final String COLUMN_TSG_NAME = "tsgname";


        public TSGDatabase(@Nullable Context context, @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TSG_TABLE_NAME + "(id TEXT PRIMARY KEY, tsgname TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }

        public void insert(TSG tsg) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(COLUMN_TSG_ID, tsg.getDna());
                values.put(COLUMN_TSG_NAME, tsg.getName());

                db.insert(TSG_TABLE_NAME, null, values);

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
                return db.delete(TSG_TABLE_NAME, "id = ? ", new String[]{id});
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
            return -1;
        }

        public int update(String id, TSG tsg) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(COLUMN_TSG_ID, tsg.getDna());
                values.put(COLUMN_TSG_NAME, tsg.getName());
                return db.update(TSG_TABLE_NAME, values,"id = ? ", new String[]{id});
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
            return -1;
        }

        public void deleteAll() {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("DELETE FROM " + TSG_TABLE_NAME);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
        }

        public TSG get(String id) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TSG_TABLE_NAME + " WHERE id='" + id + "'", null);
                cursor.moveToFirst();
                if (cursor.getCount() >= 0) {
                    TSG tsg = new TSG();

                    tsg.setDna(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TSG_ID)));
                    tsg.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TSG_NAME)));

                    cursor.close();

                    return tsg;
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
                cursor = db.rawQuery("SELECT * FROM " + TSG_TABLE_NAME, null);
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

        public List<TSG> getTSGs() {
            List<TSG> tsgs = new ArrayList<>();
            Cursor cursor = null;
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + TSG_TABLE_NAME, null);
                cursor.moveToLast();
                int count = cursor.getCount();
                if (count > 0) {
                    do {
                        TSG tsg = new TSG();

                        tsg.setDna(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TSG_ID)));
                        tsg.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TSG_NAME)));

                        tsgs.add(tsg);

                    } while (cursor.moveToPrevious());
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return tsgs;
        }
    }

