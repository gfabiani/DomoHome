package com.fabiani.domohome.controller;

import android.app.Fragment;


public class VideoActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return VideoFragment.newInstance();
    }

}

