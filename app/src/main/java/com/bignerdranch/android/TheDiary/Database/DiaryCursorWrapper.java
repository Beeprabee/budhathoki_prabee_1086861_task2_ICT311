package com.bignerdranch.android.TheDiary.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.TheDiary.Diary;
import com.bignerdranch.android.TheDiary.Settings;


import java.util.Date;
import java.util.UUID;

import com.bignerdranch.android.TheDiary.Database.DiaryDbSchema.SettingsTable;

public class DiaryCursorWrapper extends CursorWrapper {
    public DiaryCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Diary getDiary() {
        String uuidString = getString(getColumnIndex(DiaryDbSchema.DiaryTable.Cols.UUID));
        String title = getString(getColumnIndex(DiaryDbSchema.DiaryTable.Cols.TITLE));
        long date = getLong(getColumnIndex(DiaryDbSchema.DiaryTable.Cols.DATE));
        String comment = getString(getColumnIndex(DiaryDbSchema.DiaryTable.Cols.COMMENT));
        String duration = getString(getColumnIndex(DiaryDbSchema.DiaryTable.Cols.DURATION));


        Diary diary = new Diary(UUID.fromString(uuidString));
        diary.setTitle(title);
        diary.setDate(new Date(date));
        diary.setComment(comment);
        diary.setDuration(duration);


        return diary;
    }public Settings getSettings() {
        String id = getString(getColumnIndex(SettingsTable.Cols.ID));
        String name = getString(getColumnIndex(SettingsTable.Cols.NAME));
        String email = getString(getColumnIndex(SettingsTable.Cols.EMAIL));
        String gender = getString(getColumnIndex(SettingsTable.Cols.GENDER));
        String comment = getString(getColumnIndex(SettingsTable.Cols.COMMENT));

        Settings settings = new Settings();
        settings.setId(id);
        settings.setName(name);
        settings.setEmail(email);
        settings.setGender(gender);
        settings.setComment(comment);

        return settings;
    }
}


