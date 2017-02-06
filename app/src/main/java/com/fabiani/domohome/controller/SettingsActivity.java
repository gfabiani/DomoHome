package com.fabiani.domohome.controller;

import android.app.Fragment;

public class SettingsActivity extends SingleFragmentActivity {

   @Override
   protected Fragment createFragment() {
       return  SettingsFragment.newInstance();
   }

}
