package com.fabiani.domohome.app.controller;

import java.util.UUID;

public class CommandActivity extends SingleFragmentActivity {
   @Override
   protected CommandFragment createFragment() {
       UUID commandId=(UUID)getIntent().getSerializableExtra(CommandFragment.EXTRA_COMMAND_ID);
       return  CommandFragment.newInstance(commandId);
   }

}
