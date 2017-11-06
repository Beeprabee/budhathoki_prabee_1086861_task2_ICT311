package com.bignerdranch.android.TheDiary;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class DiaryActivity extends SingleFragmentActivity {
    private static final String EXTRA_DIARY_ID =
            "com.bignerdranch.android.TheDiary.diary_id";
    public static Intent newIntent (Context packageContext, UUID diaryId){
        Intent intent = new Intent(packageContext, DiaryActivity.class);
        intent.putExtra(EXTRA_DIARY_ID, diaryId);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        UUID diaryId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_DIARY_ID);
        return DiaryFragment.newInstance(diaryId);
    }
}
