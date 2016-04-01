package com.fabiani.domohome.app.controller;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Outline;
import android.os.*;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fabiani.domohome.app.R;
import com.fabiani.domohome.app.model.Command;
import com.fabiani.domohome.app.model.Dashboard;
import java.util.List;


/**
 * Created by Giovanni on 26/12/2014.
 */
public class CommandGridFragment extends Fragment {
    static final String TAG = "CommandGridFragment";
    private static final int LIGHTING_TAB_SELECTED=0;
    private static final int AUTOMATISM_TAB_SELECTED=1;
    private static final int ALL_TAB_SELECTED=2;
    private ToggleButton mItemToggleButton;
    private List<Command> mCommands;
    private List<Command> mAutomatismCommands;
    private List<Command> mLightingCommands;
    private GridView mGridView;
    private Command mCommand;
    private CommandAdapter mCommandAdapter;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mCommands = Dashboard.get(getActivity()).getCommands();
        if (!Dashboard.isNetworkActiveConnected(getActivity()))
            Toast.makeText(getActivity(), R.string.commandgridfragment_network_inactive, Toast.LENGTH_LONG).show();
        if (!SettingsFragment.isIpValid)
            Toast.makeText(getActivity(), R.string.connector_ip_error, Toast.LENGTH_SHORT).show();
        if (!SettingsFragment.isPassordOpenValid)
            Toast.makeText(getActivity(), R.string.commandgridgragment_valid_password, Toast.LENGTH_SHORT).show();
        else {
                new Thread(Dashboard::startMonitoring).start();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            v = inflater.inflate(R.layout.fragment_grid, parent, false);
        else
            super.onCreateView(inflater, parent, savedInstanceState);
        mToolbar= (Toolbar) v.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mGridView = (GridView) v.findViewById(R.id.gridView);
        setHasOptionsMenu(true);
        registerForContextMenu(mGridView);
        mLightingCommands = Stream.of(mCommands)
                .filter(c -> c.getWho() == Command.WhoChoice.LIGHTING.getValue())
                .collect(Collectors.toList());
        setupAdapter(mLightingCommands);
        TabLayout tabs=(TabLayout)v.findViewById(R.id.tabs);
        TabLayout.Tab tabLighting=tabs.newTab().setText(Command.WhoChoice.LIGHTING.toString());
        TabLayout.Tab tabAutomatism=tabs.newTab().setText(Command.WhoChoice.AUTOMATISM.toString());
        TabLayout.Tab tabAll=tabs.newTab().setText(R.string.commandgridfragment_all_commands);
        tabs.addTab(tabLighting);
        tabs.addTab(tabAutomatism);
        tabs.addTab(tabAll);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case LIGHTING_TAB_SELECTED:
                        mLightingCommands = Stream.of(mCommands)
                                .filter(command -> command.getWho() == Command.WhoChoice.LIGHTING.getValue())
                                .collect(Collectors.toList());
                        setupAdapter(mLightingCommands);
                        break;
                    case AUTOMATISM_TAB_SELECTED:
                        mAutomatismCommands = Stream.of(mCommands)
                                .filter(command -> command.getWho() == Command.WhoChoice.AUTOMATISM.getValue())
                                .collect(Collectors.toList());
                        setupAdapter(mAutomatismCommands);
                        break;
                    case ALL_TAB_SELECTED:
                        setupAdapter(mCommands);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        View addCommandButton = v.findViewById(R.id.add_button);
        addCommandButton.setOnClickListener(view -> newCommandSelected());
        addCommandButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int diameter = getActivity().getResources().getDimensionPixelSize(R.dimen.add_button_diameter);
                outline.setOval(0, 0, diameter, diameter);
            }
        });
        addCommandButton.setClipToOutline(true);
        return v;
    }

    public void setupAdapter(List<Command> commands) {
        if (mCommands != null) {
            mCommandAdapter = new CommandAdapter(commands);
            mGridView.setAdapter(mCommandAdapter);
        } else mGridView.setAdapter(null);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                if (mCommand.getWho() == Command.WhoChoice.LIGHTING.getValue())
                    mLightingCommands.remove(mCommand);
                else if (mCommand.getWho() == Command.WhoChoice.AUTOMATISM.getValue())
                    mAutomatismCommands.remove(mCommand);
                mCommandAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    public class CommandAdapter extends ArrayAdapter<Command> {
        public CommandAdapter(List<Command> commands) {
            super(getActivity(), 0, commands);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_grid_item, parent, false);
            mItemToggleButton = (ToggleButton) convertView.findViewById(R.id.command_grid_item_toggleButton);
            mCommand = getItem(position);
            mItemToggleButton.setText(mCommand.getTitle());
            mItemToggleButton.setTextOff(mCommand.getTitle());
            mItemToggleButton.setTextOn(mCommand.getTitle());
            mItemToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mCommand.setWhat(isChecked ? 1 : 0);
                    new Thread(() -> {
                        Dashboard.invia("*" + mCommand.getWho() + "*" + mCommand.getWhat() + "*" + mCommand.getWhere() + "##");
                    }).start();

            });
            mItemToggleButton.setOnLongClickListener(v -> {
                getActivity().openContextMenu(v);
                return false;
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
    public void onDestroy() {
        super.onDestroy();
        if (Dashboard.gestSocketMonitor != null)
            Dashboard.gestSocketMonitor.close();
    }
}






