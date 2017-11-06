package com.bignerdranch.android.TheDiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.TheDiary.Database.DiaryBaseHelper;
import com.bignerdranch.android.TheDiary.Database.DiaryCursorWrapper;
import com.bignerdranch.android.TheDiary.Database.DiaryDbSchema;
import com.bignerdranch.android.TheDiary.Database.DiaryDbSchema.DiaryTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class DiaryLab {
    private static DiaryLab sDiaryLab;


    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DiaryLab get (Context context){
        if (sDiaryLab == null){
            sDiaryLab = new DiaryLab(context);
        }
        return sDiaryLab;
    }
    private DiaryLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DiaryBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addDiary(Diary d){
        ContentValues values = getContentValues(d);
        mDatabase.insert(DiaryTable.NAME, null, values);

    }

    public  List<Diary> getDiaries(){
        List<Diary> diaries = new ArrayList<>();

        DiaryCursorWrapper cursor = queryDiaries(null ,null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                diaries.add(cursor.getDiary());
                cursor.moveToNext();
            }
            } finally {
                cursor.close();
            }
            return diaries;
        }



    public Diary getDiary(UUID id){
        DiaryCursorWrapper cursor = queryDiaries(
                DiaryTable.Cols.UUID + " = ?",
                new String[]{id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getDiary();

        } finally {
            cursor.close();

        }
    }
    public File getPhotoFile (Diary diary){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, diary.getPhotoFileName());
    }

    public void updateDiary( Diary diary){
        String uuidString = diary.getId().toString();
        ContentValues values = getContentValues(diary);

        mDatabase.update(DiaryTable.NAME, values,
                DiaryTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private DiaryCursorWrapper queryDiaries(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                DiaryTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null

        );
        return new DiaryCursorWrapper(cursor);

    }

    private static ContentValues getContentValues(Diary diary){
        ContentValues values = new ContentValues();
        values.put(DiaryTable.Cols.UUID, diary.getId().toString());
        values.put(DiaryTable.Cols.TITLE, diary.getTitle());
        values.put(DiaryTable.Cols.DATE, diary.getDate().getTime());
        values.put(DiaryTable.Cols.COMMENT, diary.getComment());
        values.put(DiaryTable.Cols.DURATION, diary.getDuration());


        return values;

    }

    private ContentValues getSettingsValues(Settings settings) {
        ContentValues values = new ContentValues();

        values.put(DiaryDbSchema.SettingsTable.Cols.ID, settings.getId());
        values.put(DiaryDbSchema.SettingsTable.Cols.NAME, settings.getName());
        values.put(DiaryDbSchema.SettingsTable.Cols.EMAIL, settings.getEmail());
        values.put(DiaryDbSchema.SettingsTable.Cols.GENDER, settings.getGender());
        values.put(DiaryDbSchema.SettingsTable.Cols.COMMENT, settings.getComment());

        return values;
    }

    public void updateSettings(Settings settings) {
        ContentValues values = getSettingsValues(settings);
        String id = settings.getId();
        mDatabase.update(DiaryDbSchema.SettingsTable.NAME, values,
                DiaryDbSchema.SettingsTable.Cols.ID + " = ?",
                new String[]{id});
    }

    private DiaryCursorWrapper querySettings() {
        Cursor cursor = mDatabase.query(
                DiaryDbSchema.SettingsTable.NAME,
                null, // Columns - null selects all columns
                null,
                null,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new DiaryCursorWrapper(cursor);
    }

    public Settings getSettings() {
        DiaryCursorWrapper cursor = querySettings();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getSettings();
        } finally {
            cursor.close();
        }
    }
    public void deleteLog(Diary diary) {
        String uuidString = diary.getId().toString();
        ContentValues values = getContentValues(diary);

        mDatabase.delete(DiaryTable.NAME, DiaryTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

}

