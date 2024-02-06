package com.mustapha.fishingsystemgs.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mustapha.fishingsystemgs.classes.KVS;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KVSDatabase extends SQLiteOpenHelper {
        public static final String TAG = "PGR Tag";
        private static final String KVS_TABLE_NAME = "kvs";
        private static final String COLUMN_KVS_ID = "id";

        private static final String COLUMN_KVS_DNA = "dna";
        private static final String COLUMN_KVS_NAME = "kvsname";


        public KVSDatabase(@Nullable Context context, @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + KVS_TABLE_NAME + "(id TEXT PRIMARY KEY, dna TEXT,  kvsname TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }

        public void insert(KVS kvs) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(COLUMN_KVS_ID, kvs.getId());
                values.put(COLUMN_KVS_NAME, kvs.getName());
                values.put(COLUMN_KVS_DNA, kvs.getDnaOfMother());

                db.insert(KVS_TABLE_NAME, null, values);

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
                return db.delete(KVS_TABLE_NAME, "id = ? ", new String[]{id});
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
            return -1;
        }

        public int update(String id, KVS kvs) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(COLUMN_KVS_ID, kvs.getId());
                values.put(COLUMN_KVS_NAME, kvs.getName());
                values.put(COLUMN_KVS_DNA, kvs.getDnaOfMother());

                return db.update(KVS_TABLE_NAME, values,"id = ? ", new String[]{id});
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
            return -1;
        }

        public void deleteAll() {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("DELETE FROM " + KVS_TABLE_NAME);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
        }

        public KVS get(String id) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + KVS_TABLE_NAME + " WHERE id='" + id + "'", null);
                cursor.moveToFirst();
                if (cursor.getCount() >= 0) {
                    KVS kvs = new KVS();

                    kvs.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KVS_ID)));
                    kvs.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KVS_NAME)));
                    kvs.setDnaOfMother(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KVS_DNA)));

                    cursor.close();

                    return kvs;
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
                cursor = db.rawQuery("SELECT * FROM " + KVS_TABLE_NAME, null);
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

        public List<KVS> getKVSs() {
            List<KVS> kvs = new ArrayList<>();
            Cursor cursor = null;
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + KVS_TABLE_NAME, null);
                cursor.moveToLast();
                int count = cursor.getCount();
                if (count > 0) {
                    do {
                        KVS kvs1 = new KVS();

                        kvs1.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KVS_ID)));
                        kvs1.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KVS_NAME)));
                        kvs1.setDnaOfMother(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KVS_DNA)));

                        kvs.add(kvs1);

                    } while (cursor.moveToPrevious());
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return kvs;
        }
    }

