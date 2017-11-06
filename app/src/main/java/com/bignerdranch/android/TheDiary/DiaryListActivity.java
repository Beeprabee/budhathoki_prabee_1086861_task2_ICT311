package com.bignerdranch.android.TheDiary;

import android.support.v4.app.Fragment;


public class DiaryListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new DiaryListFragment();
    }
}

