package com.bignerdranch.android.TheDiary;

import android.widget.Button;

import java.util.Date;
import java.util.UUID;


public class Diary
{
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mComment;
    private Button mSave;
    private Button mCancel;
    private Button mDelete;

    public Diary(){
        this(UUID.randomUUID());
    }


    public Diary(UUID id){
        mId = id;
        mDate = new Date();


    }



    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public Date getDate() {
        return mDate;
    }
    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    private String mType;

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    private String mDuration;


    public void setDate(Date date) {
        mDate = date;
    }
    public String getComment() {
        return mComment;
     }

    public void setComment(String comment){
        mComment= comment;


    }
    public Button getSave() {
        return mSave;
    }

    public void setSave(Button save) {
        mSave = save;
    }

    public Button getCancel() {
        return mCancel;
    }

    public void setCancel(Button cancel) {
        mCancel = cancel;
    }

    public Button getDelete() {
        return mDelete;
    }

    public void setDelete(Button delete) {
        mDelete = delete;
    }

    public String getPhotoFileName(){
        return "IMG_" +getId().toString() +".jpg";
    }








}
