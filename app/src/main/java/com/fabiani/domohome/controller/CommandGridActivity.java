package com.fabiani.domohome.controller;

import android.app.Fragment;

/**
 * Created by Giovanni on 26/12/2014.
 */
public class CommandGridActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CommandGridFragment();
    }

}
