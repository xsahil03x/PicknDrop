package com.magarex.pickndrop;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by HP on 7/30/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_Name = "Mob_Database";
    private static final String Table_Name = "Users";

    private static final String COL_1 = "Id";
    private static final String COL_2 = "Username";
    private static final String COL_3 = "Email";
    private static final String COL_4 = "Password";
    private static final String COL_5 = "PhoneNo";
    private static final String COL_6 = "ImeiNo";

    public SQLiteHandler(Context context) {
        super(context, DB_Name, null, 1);

        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + Table_Name + "("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT UNIQUE,"
                + COL_3 + " TEXT UNIQUE," + COL_4 + " TEXT,"
                + COL_5 + " TEXT," + COL_6 + " TEXT)";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addUser(int id, String username, String email, String password, int phoneNo, String city, String state, String imeiNo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put(COL_2, username);
        values.put(COL_3, email);
        values.put(COL_4, password);
        values.put(COL_5, phoneNo);
        values.put(COL_6, imeiNo);

        // Inserting Row
        long result = db.insert(Table_Name, null, values);
        db.close();
    }
}
