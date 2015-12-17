package com.fabiani.domohome.app.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.fabiani.domohome.app.R;
import com.fabiani.domohome.app.model.Dashboard;
import com.fabiani.domohome.app.model.Group;


/**
 * Created by Giovanni on 15/11/2015.
 */
public class GroupDialogFragment extends DialogFragment {
    private Group mGroup;
    private EditText mGroupEditText;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mGroup=new Group();
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_group,null);
        mGroupEditText=(EditText)v.findViewById(R.id.group_title_edit_text);
        builder.setView(v);
        builder.setPositiveButton("OK", (dialog, which) -> {
            mGroup.setGroupTitle(mGroupEditText.getText().toString());
        })
                .setNegativeButton("Cancel", (dialog, which) -> {
                        GroupDialogFragment.this.dismiss();
                });
        return builder.create();
    }

}