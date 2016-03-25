package com.fabiani.domohome.app.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.fabiani.domohome.app.R;
import com.fabiani.domohome.app.model.Command;
import com.fabiani.domohome.app.model.Dashboard;
import java.util.UUID;


public class CommandFragment extends Fragment {
	static final String TAG = "CommandFragment";
	static final String EXTRA_COMMAND_ID = "com.fabiani.domohome.app_extra_command_id";
	private static String sWhoSelected = null;
	private static String sWhereSelected = null;
	private Spinner mWhoSpinner;
	private Spinner mWhereSpinner;
	private EditText mCommandTitleEditText;
	private Toolbar mToolbar;
	private Command mCommand;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCommand = new Command();
		UUID commandId = (UUID) getArguments().getSerializable(EXTRA_COMMAND_ID);
		mCommand = Dashboard.get(getActivity()).getCommand(commandId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_command, parent, false);
		mToolbar= (Toolbar) v.findViewById(R.id.tool_bar);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		//noinspection ConstantConditions
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mCommandTitleEditText = (EditText) v.findViewById(R.id.command_title_edit_text);
		mCommandTitleEditText.setText(mCommand.getTitle());
		mCommandTitleEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
					mCommand.setTitle(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {


			}
		});
		mWhoSpinner = (Spinner) v.findViewById(R.id.command_who_spinner);
		ArrayAdapter<CharSequence> mWhoAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.who_array, android.R.layout.simple_spinner_item);
		mWhoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWhoSpinner.setAdapter(mWhoAdapter);
		mWhoSpinner.setSelection(mCommand.getWhoSpinnerPosition());
		mWhoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				mCommand.setWhoSpinnerPosition(mWhoSpinner.getSelectedItemPosition());
				sWhoSelected = mWhoSpinner.getItemAtPosition(position).toString();
				switch (sWhoSelected) {
					//check mCommand settings!  Check OK
					case "Scenarios":
						mCommand.setWho(Command.WhoChoice.SCENARIOS.getValue());
						break;
					case "Lighting":
						mCommand.setWho(Command.WhoChoice.LIGHTING.getValue());
						break;
					case "Automatism":
						mCommand.setWho(Command.WhoChoice.AUTOMATISM.getValue());
						break;
					case "Electrical loads":
						mCommand.setWho(Command.WhoChoice.ELECTRICAL_LOADS.getValue());
						break;
					case "Warming management":
						mCommand.setWho(Command.WhoChoice.WARMING_MANAGEMENT.getValue());
						break;
					case "Antitheft":
						mCommand.setWho(Command.WhoChoice.ANTITHEFT.getValue());
						break;
					case "Basic video doorphone":
						mCommand.setWho(Command.WhoChoice.BASIC_VIDEO_DOORPHONE.getValue());
						break;
					case "Gateway management":
						mCommand.setWho(Command.WhoChoice.GATEWAY_MANAGEMENT.getValue());
						break;
					case "CEN commands":
						mCommand.setWho(Command.WhoChoice.CEN_COMMANDS.getValue());
						break;
					case "Sound diffusion":
						mCommand.setWho(Command.WhoChoice.SOUND_DIFFUSION.getValue());
						break;
					case "Sound diffusion 2":
						mCommand.setWho(Command.WhoChoice.SOUND_DIFFUSION_2.getValue());
						break;
					case "MH200N scenarios":
						mCommand.setWho(Command.WhoChoice.MH200N_SCENARIOS.getValue());
						break;
					case "Power management":
						mCommand.setWho(Command.WhoChoice.POWER_MANAGEMENT.getValue());
						break;
					case "CEN plus etc":
						mCommand.setWho(Command.WhoChoice.CEN_PLUS_ETC.getValue());
						break;
					case "Automation diagnostics":
						mCommand.setWho(Command.WhoChoice.AUTOMATION_DIAGNOSTICS.getValue());
						break;
					case "Thermoregulation diagnostics":
						mCommand.setWho(Command.WhoChoice.THERMOREGULATION_DIAGNOSTICS.getValue());
						break;
					case "Device diagnostics":
						mCommand.setWho(Command.WhoChoice.DEVICE_DIAGNOSTICS.getValue());
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mWhereSpinner = (Spinner) v.findViewById(R.id.command_where_spinner);
		ArrayAdapter<Integer> mWhereAdapter = new ArrayAdapter<>
				(getActivity(), android.R.layout.simple_spinner_item, Command.sWhereChoices);
		mWhereSpinner.setAdapter(mWhereAdapter);
		mWhereSpinner.setSelection(mCommand.getWhereSpinnerPosition());
		mWhereSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				mCommand.setWhereSpinnerPosition(mWhereSpinner.getSelectedItemPosition());
				sWhereSelected = mWhereSpinner.getItemAtPosition(position).toString();
				mCommand.setWhere(Command.sWhereChoices[Integer.parseInt(sWhereSelected) - 1]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		return v;
	}

	public static CommandFragment newInstance(UUID commandId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_COMMAND_ID, commandId);
		CommandFragment fragment = new CommandFragment();
		fragment.setArguments(args);
		return fragment;
	}
}
