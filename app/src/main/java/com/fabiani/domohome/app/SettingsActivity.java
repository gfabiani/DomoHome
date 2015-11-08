package com.fabiani.domohome.app;

import android.app.Fragment;

 public class SettingsActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return  SettingsFragment.newInstance();
	}

}
