package com.fabiani.domohome.app.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Giovanni on 12/11/2015.
 */
public class Group implements Serializable{
    private static final long serialVersionUID = 2L;
    private String mGroupTitle;
    private int mGroupSpinnerPosition;
    private UUID mId;

    public Group(){
        mId=UUID.randomUUID();
    }

    public void setGroupTitle(String title) {
        mGroupTitle = title;
    }

    public String getGroupTitle() {
        return mGroupTitle;
    }

    public int getGroupSpinnerPosition() {
        return mGroupSpinnerPosition;
    }

    public void setGroupSpinnerPosition(int GroupSpinnerPosition) {
        mGroupSpinnerPosition = GroupSpinnerPosition;
    }

    @Override
    public String toString(){
        return mGroupTitle;
    }
}
