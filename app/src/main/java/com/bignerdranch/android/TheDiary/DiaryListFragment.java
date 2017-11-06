package com.bignerdranch.android.TheDiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class DiaryListFragment extends Fragment {
    private RecyclerView mDiaryRecyclerView;
    private DiaryAdapter mAdapter;

    @Override

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_list, container, false);

        mDiaryRecyclerView = (RecyclerView) view.findViewById(R.id.diary_recycler_view);
        mDiaryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_diary_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_diary:
                Diary diary =  new Diary();
                DiaryLab.get(getActivity()).addDiary(diary);
                Intent intent = DiaryPagerActivity
                        .newIntent(getActivity(), diary.getId());
                startActivity(intent);
                return true;
            case R.id.user_settings:
                Settings mSettings = DiaryLab.get(getActivity()).getSettings();

                Intent settingsIntent = SettingsActivity
                        .settings(getActivity(), mSettings.getId());
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void updateUI(){
        DiaryLab diaryLab = DiaryLab.get(getActivity());
        List<Diary> diaries = diaryLab.getDiaries();

        if (mAdapter == null) {
            mAdapter = new DiaryAdapter(diaries);
            mDiaryRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setDiaries(diaries);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class DiaryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
               private Diary mDiary;


        public DiaryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_diary, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_diary_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_diary_date_text_view);

        }
        private void bind(Diary diary){
            mDiary = diary;
            mTitleTextView.setText(mDiary.getTitle());
            mDateTextView.setText(mDiary.getDate().toString());

        }
        @Override
        public void onClick(View view){
            Intent intent = DiaryPagerActivity.newIntent(getActivity(), mDiary.getId());
            startActivity(intent);
        }
    }

    private class DiaryAdapter extends RecyclerView.Adapter<DiaryHolder> {
        private List<Diary> mDiaries;

        public DiaryAdapter(List<Diary> diaries) {
            mDiaries = diaries;
        }

        @Override
        public DiaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DiaryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DiaryHolder holder, int position) {
            Diary diary = mDiaries.get(position);
            holder.bind(diary);

        }

        @Override
        public int getItemCount() {
            return mDiaries.size();
        }

        public void setDiaries(List<Diary> diaries){
            mDiaries = diaries;
        }


    }
}
