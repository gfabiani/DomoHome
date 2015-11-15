package com.fabiani.domohome.app.model;

import java.util.UUID;

/**
 * Created by Giovanni on 12/11/2015.
 */
public class Group {
   // private static final String JSON_GROUP_TITLE = "group_title";
    private String mGroupTitle;
    private int mGroupSpinnerPosition;
    private UUID mId;
    Group(UUID id){
        mId=id;
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
}
