package com.fabiani.domohome.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.fabiani.domohome.R;
import com.fabiani.domohome.model.Command;
import com.fabiani.domohome.model.Dashboard;
import com.fabiani.domohome.model.GestioneSocketComandi;
import com.fabiani.domohome.model.GestioneSocketMonitor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Giovanni on 26/12/2014.
 */
public  class CommandGridFragment extends Fragment {
    //TODO: CoordinatorLayout
    static final String TAG = "CommandGridFragment";
    private List<Command> mCommands;
    private GridView mGridView;
    private CommandAdapter mCommandAdapter;
    private GestioneSocketMonitor mGestioneSocketMonitor;
    private Scheduler scheduler = Schedulers.newThread();
    private Scheduler.Worker worker = scheduler.createWorker();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mCommands = Dashboard.get(getActivity()).getCommands();
        mGestioneSocketMonitor = new GestioneSocketMonitor();
        new Thread(() -> mGestioneSocketMonitor.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen)).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grid, parent, false);
        Toolbar mToolbar = v.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mGridView = v.findViewById(R.id.gridView);
        setHasOptionsMenu(true);
        registerForContextMenu(mGridView);
        setupAdapter(mCommands);
        FloatingActionButton addCommandButton = v.findViewById(R.id.add_button);
        addCommandButton.setOnClickListener(fab -> newCommandSelected());
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
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.menu_item_video:
                startActivity(new Intent(getActivity(), VideoActivity.class));
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
        Command mCommand = mCommandAdapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.menu_item_edit_command:
                Intent i = new Intent(getActivity(), CommandActivity.class);
                i.putExtra(CommandFragment.EXTRA_COMMAND_ID, mCommand.getId());
                startActivity(i);
                break;
            case R.id.menu_item_delete_command:
                Dashboard.get(getActivity()).deleteCommand(mCommand);
                mCommandAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    public class CommandAdapter extends ArrayAdapter<Command> {
        public CommandAdapter(List<Command> commands) {
            super(getActivity(), 0, commands);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_grid_item, parent, false);
            ToggleButton mItemToggleButton = convertView.findViewById(R.id.command_grid_item_toggleButton);
            Command mCommand = getItem(position);
            mItemToggleButton.setText(mCommand.getTitle());
            mItemToggleButton.setTextOff(mCommand.getTitle());
            mItemToggleButton.setTextOn(mCommand.getTitle());
            mItemToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mCommand.setWhat(isChecked ? 1 : 0);
                    worker.schedule(() -> {
                        inviaCommand("*" + mCommand.getWho() + "*" + mCommand.getWhat() + "*" + mCommand.getWhere() + "##");
                    });
                    if(mCommand.getTimeout()==0)
                        worker.dispose();
                worker.schedule(() -> {
                    mCommand.setWhat(0);
                    inviaCommand("*" + mCommand.getWho() + "*" + mCommand.getWhat() + "*" + mCommand.getWhere() + "##");
                }, mCommand.getTimeout(), TimeUnit.SECONDS);
            });
            mItemToggleButton.setOnLongClickListener((View v) -> {
                getActivity().openContextMenu(v);
                return true;
            });
            return convertView;
        }

    }

    private void newCommandSelected() {
        Command command = new Command();
        Dashboard.get(getActivity()).addCommand(command);
        Intent i = new Intent(getActivity(), CommandActivity.class);
        i.putExtra(CommandFragment.EXTRA_COMMAND_ID, command.getId());
        startActivity(i);
    }
    private void inviaCommand(String openwebnetString) {
        GestioneSocketComandi gestioneSocketComandi= new GestioneSocketComandi();
        gestioneSocketComandi.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen);
        gestioneSocketComandi.invia(openwebnetString);
        gestioneSocketComandi.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        Dashboard.get(getActivity()).saveCommands();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGestioneSocketMonitor != null)
            mGestioneSocketMonitor.close();
    }
}
