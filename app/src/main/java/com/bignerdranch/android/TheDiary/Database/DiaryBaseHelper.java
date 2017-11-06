package com.bignerdranch.android.TheDiary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.TheDiary.Database.DiaryDbSchema.DiaryTable;
import com.bignerdranch.android.TheDiary.Database.DiaryDbSchema.SettingsTable;

public class DiaryBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "diaryBase.db";

    public DiaryBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + DiaryTable.NAME + "("
                + " _id integer primary key autoincrement, " +
        DiaryTable.Cols.UUID + ", "+
        DiaryTable.Cols.TITLE + ", " +
        DiaryTable.Cols.DATE + ", " +
        DiaryTable.Cols.COMMENT + "," +
                        DiaryTable.Cols.DURATION + ")"
        );
        db.execSQL("create table " + SettingsTable.NAME + "(" +
                SettingsTable.Cols.ID + ", " +
                SettingsTable.Cols.NAME + ", " +
                SettingsTable.Cols.EMAIL + ", " +
                SettingsTable.Cols.GENDER + ", " +
                SettingsTable.Cols.COMMENT +
                ")"
        );
        // only one record/ only updates after
        db.execSQL("insert into " + SettingsTable.NAME + " values ('1086861', 'Prabee Budhathoki', 'pra_bee@b.com', '1', 'ICT311. Diary.')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
