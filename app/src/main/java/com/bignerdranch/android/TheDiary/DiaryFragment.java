package com.bignerdranch.android.TheDiary;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bignerdranch.android.TheDiary.R;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.R.attr.data;

public class DiaryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_DIARY_ID = "diary_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_DATE = 0;

    private Diary mDiary;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private EditText mCommentField;
    private Spinner mTypeSpinner;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private EditText mDurationField;
    private Button mSaveButton;
    private Button mCancelButton;
    private Button mDeleteButton;





    public static DiaryFragment newInstance(UUID diaryId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DIARY_ID, diaryId);

        DiaryFragment fragment = new DiaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID diaryId = (UUID) getArguments().getSerializable(ARG_DIARY_ID);
        mDiary = DiaryLab.get(getActivity()).getDiary(diaryId);
        mPhotoFile = DiaryLab.get(getActivity()).getPhotoFile(mDiary);
    }
    @Override
    public void onPause(){
        super.onPause();

        DiaryLab.get(getActivity())
                .updateDiary(mDiary);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        mTitleField = (EditText) v.findViewById(R.id.diary_title);
        mTitleField.setText(mDiary.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after){

            }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode != Activity.RESULT_OK) {
                    return;        }
                if (requestCode == REQUEST_DATE) {
                    Date date = (Date) data
                            .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                    mDiary.setDate(date);
                    mDateButton.setText(mDiary.getDate().toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiary.setTitle(s.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        mCommentField = (EditText) v.findViewById(R.id.diary_comment);
        mCommentField.setText(mDiary.getComment());
        mCommentField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(
                CharSequence s, int start, int count, int after){

        }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDiary.setComment(s.toString());

        }

            @Override
            public void afterTextChanged(Editable editable) {

        }
        });

        mDurationField = (EditText) v.findViewById(R.id.diary_duration);
        mDurationField.setText(mDiary.getDuration());
        mDurationField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDiary.setDuration(s.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.diary_date);
        mDateButton.setText(mDiary.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mDiary.getDate());
                dialog.setTargetFragment(DiaryFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);            }        });

        mSaveButton = (Button) v.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DiaryLab.get(getActivity()).updateDiary(mDiary);
                getActivity().onBackPressed();
            }
        });

        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DiaryLab.get(getActivity());
                getActivity().onBackPressed();
            }
        });

        mDeleteButton = (Button) v.findViewById(R.id.delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryLab.get(getActivity()).deleteLog(mDiary);
                getActivity().onBackPressed();
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mPhotoButton = (ImageButton) v.findViewById(R.id.diary_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.TheDiary.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.diary_photo);
        updatePhotoView();

        mTypeSpinner = (Spinner) v.findViewById(R.id.activity_type_spinner);
        mTypeSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.activity_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);


        return v;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.TheDiary.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}


