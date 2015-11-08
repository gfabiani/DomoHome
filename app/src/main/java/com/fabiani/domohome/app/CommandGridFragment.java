package com.fabiani.domohome.app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

/**
 * Created by Giovanni on 26/12/2014.
 */
public class CommandGridFragment extends Fragment {
    static final String TAG = "CommandGridFragment";
    private ToggleButton mItemToggleButton;
    private ArrayList<Command> mCommands;
    private ArrayList<Command> mLightingCommands;
    private ArrayList<Command> mAutomatismCommands;
    private GridView mGridView;
    private Command mCommand;
    private CommandAdapter mCommandAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setTitle(R.string.app_name);
        mCommands = Dashboard.get(getActivity()).getCommands();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dashboard.startMonitoring(getActivity());
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            v = inflater.inflate(R.layout.fragment_grid, parent, false);
        else
            super.onCreateView(inflater, parent, savedInstanceState);
        mGridView = (GridView) v.findViewById(R.id.gridView);
        setHasOptionsMenu(true);
        registerForContextMenu(mGridView);
        setupActionBar();
        setupAdapter(mLightingCommands);
        View addCommandButton = v.findViewById(R.id.add_button);
        addCommandButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                newCommandSelected();
            }
        });
        addCommandButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int diameter = getActivity().getResources().getDimensionPixelSize(R.dimen.round_button_diameter);
                outline.setOval(0, 0, diameter, diameter);
            }
        });
        addCommandButton.setClipToOutline(true);
        return v;
    }

    void setupAdapter(ArrayList<Command> commands) {
        if (mCommands != null) {
            mCommandAdapter = new CommandAdapter(commands);
            mGridView.setAdapter(mCommandAdapter);
        } else mGridView.setAdapter(null);
    }

    @SuppressWarnings("deprecation")
    void setupActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        ActionBar.Tab tabLighting = actionBar.newTab().setText(Command.WhoChoice.LIGHTING.toString());
        tabLighting.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                mLightingCommands = Dashboard.filter(mCommands, new CommandPredicate() {//lambda: mLightingCommands=Dashboard.filter(mCommands, (Command command)-> command.getWho()==1)
                    @Override
                    public boolean test(Command command) {
                        return command.getWho() == 1;
                    }
                });
                setupAdapter(mLightingCommands);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        });
        actionBar.addTab(tabLighting);
        ActionBar.Tab tabAutomatism = actionBar.newTab().setText((Command.WhoChoice.AUTOMATISM.toString()));
        tabAutomatism.setTabListener(new ActionBar.TabListener() {//Lambda:Dashboard.Filter((ActionBarTab,FragmentTransation ft)->command.getWho()==1))) doubt
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mAutomatismCommands = Dashboard.filter(mCommands, new CommandPredicate() {//lambda: mLightingCommands =Dashboard.filter(mCommands, (Command command)-> command.getWho()==1)
                    @Override
                    public boolean test(Command command) {
                        return command.getWho() == 2;
                    }
                });
                setupAdapter(mAutomatismCommands);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        });
        actionBar.addTab(tabAutomatism);
        ActionBar.Tab tabAll = actionBar.newTab().setText(R.string.commandgridfragment_all_commands);
        tabAll.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                setupAdapter(mCommands);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        });
        actionBar.addTab(tabAll);
    }

    void deleteFilteredCommand() {
        switch (mCommand.getWho()) {
            case 1:
                mLightingCommands.remove(mCommand);
                break;
            case 2:
                mAutomatismCommands.remove(mCommand);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_command:
                newCommandSelected();
                break;
            case R.id.menu_item_settings:
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
                break;
        }
        return true;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.fragment_grid_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        mCommand = mCommandAdapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.menu_item_edit_command:
                mCommand = mCommandAdapter.getItem(position);
                Intent i = new Intent(getActivity(), CommandActivity.class);
                i.putExtra(CommandFragment.EXTRA_COMMAND_ID, mCommand.getId());
                startActivity(i);
                break;
            case R.id.menu_item_delete_command:
                Dashboard.get(getActivity()).deleteCommand(mCommand);
                deleteFilteredCommand();
                mCommandAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }


    public class CommandAdapter extends ArrayAdapter<Command> {
        public CommandAdapter(ArrayList<Command> commands) {
            super(getActivity(), 0, commands);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_grid_item, parent, false);
            mItemToggleButton = (ToggleButton) convertView.findViewById(R.id.command_grid_item_toggleButton);
            mCommand = getItem(position);
            mItemToggleButton.setText(mCommand.getCommandTitle());
            mItemToggleButton.setTextOff(mCommand.getCommandTitle());
            mItemToggleButton.setTextOn(mCommand.getCommandTitle());
            mItemToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    mCommand.setWhat(isChecked ? 1 : 0);
                    if (!Dashboard.isNetworkActiveConnected(getActivity()))
                        Toast.makeText(getActivity(), R.string.commandgridfragment_network_inactive, Toast.LENGTH_LONG).show();
                    else
                        Dashboard.invia(getActivity(), "*" + mCommand.getWho() + "*" + mCommand.getWhat() + "*" + mCommand.getWhere() + "##");
                }
            });
            mItemToggleButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    getActivity().openContextMenu(v);
                    return false;
                }
            });
            return convertView;
        }
    }

    public void newCommandSelected() {
        Command command = new Command();
        Dashboard.get(getActivity()).addCommand(command);
        Intent i = new Intent(getActivity(), CommandActivity.class);
        i.putExtra(CommandFragment.EXTRA_COMMAND_ID, command.getId());
        startActivity(i);
    }

    @Override
    public void onStop() {
        super.onStop();
        Dashboard.get(getActivity()).saveCommands();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCommandAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Dashboard.gestSocketMonitor != null)
            Dashboard.gestSocketMonitor.close();
    }
}
