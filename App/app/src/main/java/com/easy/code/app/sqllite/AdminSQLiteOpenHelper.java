package com.easy.code.app.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tanieska on 23/3/2017.
 */

public class AdminSQLiteOpenHelper  extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ SQLiteReferences.TablePerson + "(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                SQLiteReferences.TablePersonName +" TEXT,"+ SQLiteReferences.TablePersonProfession+" TEXT,"
                +SQLiteReferences.TablePersonAge +" INTEGER,"+SQLiteReferences.TablePersonPhoto+ " IMAGE)");

        sqLiteDatabase.execSQL("create table "+ SQLiteReferences.TableAddress + "(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                SQLiteReferences.TableAddressPlace +" TEXT,"+ SQLiteReferences.TableAddressPersonId + " INTEGER)");

        sqLiteDatabase.execSQL("create table "+ SQLiteReferences.TablePhone + "(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                SQLiteReferences.TablePhoneNumber +" TEXT,"+ SQLiteReferences.TablePhonePersonId + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
