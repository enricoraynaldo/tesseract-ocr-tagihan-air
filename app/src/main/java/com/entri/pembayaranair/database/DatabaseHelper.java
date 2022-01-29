package com.entri.pembayaranair.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_bill";
    public static final String TABLE_ADMIN = "tb_admin";
    public static final String COL_NAMA_ADMIN = "nama_admin";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_NO_HP_ADMIN = "no_admin";
    public static final String TABLE_USER = "tb_user";
    public static final String COL_NOAIR = "noair";
    public static final String COL_NAME = "name";
    public static final String COL_ALAMAT = "alamat";
    public static final String COL_NO_HP = "no_hp";
    public static final String TABLE_TAGIHAN = "tb_tagihan";
    public static final String COL_ID_TAGIHAN = "id_tagihan";
    public static final String COL_TOTAL_AIR = "tot_air";
    public static final String COL_TOTAL_TAGIHAN = "tot_tagihan";


    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys=ON");

        db.execSQL("create table " + TABLE_ADMIN + " (" + COL_USERNAME + " TEXT PRIMARY KEY, " + COL_PASSWORD +
                " TEXT, " + COL_NO_HP_ADMIN + " TEXT, " + COL_NAMA_ADMIN + " TEXT )");

        db.execSQL("create table " + TABLE_USER + " (" + COL_NOAIR + " TEXT PRIMARY KEY, " + COL_NAME +
                " TEXT, " + COL_NO_HP + " TEXT, " + COL_ALAMAT + " TEXT )");

        db.execSQL("create table " + TABLE_TAGIHAN + " (" + COL_ID_TAGIHAN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TOTAL_AIR +
                " INTEGER, " + COL_TOTAL_TAGIHAN + " INTEGER, FOREIGN KEY(" + COL_NOAIR + ") REFERENCES " + TABLE_USER + ")");

        db.execSQL("insert into tb_admin values ('entri@gmail.com','123','0896','Entri');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        onCreate(db);
    }


    public void open() throws SQLException {
        db = this.getWritableDatabase();
    }

    public boolean RegisterAdmin(String username, String password, String no_admin, String nama_admin) throws SQLException {

        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("INSERT INTO " + TABLE_ADMIN + "(" + COL_USERNAME + ", " + COL_PASSWORD + ", " + COL_NO_HP_ADMIN + ", "+ COL_NAMA_ADMIN + ")  VALUES (?,?,?,?)", new String[]{username, password, no_admin, nama_admin});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public boolean Register(String noair, String name, String no_hp, String alamat) throws SQLException {

        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("INSERT INTO " + TABLE_USER + "(" + COL_NOAIR + ", " + COL_NAME + ", " + COL_NO_HP + ", " + COL_ALAMAT + ")  VALUES (?,?,?,?)", new String[]{noair, name, no_hp, alamat});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public boolean Login(String username, String password) throws SQLException {
        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_ADMIN + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?", new String[]{username, password});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public boolean Ceknoair(String noair) throws SQLException {
        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COL_NOAIR + "=?", new String[]{noair});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }
}