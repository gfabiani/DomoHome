package com.fabiani.domohome.app.model;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.annimon.stream.Stream;
import com.fabiani.domohome.app.controller.SettingsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//singleton class

public class Dashboard  {
	private static final String TAG = "Dashboard";
	private static Dashboard sDashboard;
	private static final String JSON_FILENAME = "commands.json";
	public  static final int PORT = 20000;
	public static String sIp;
	public static int sPasswordOpen;
	private Context mAppContext;
	private JSONSerializer mJSONSerializer;
	private List<Command> mCommands;

	private Dashboard(Context appContext) {
		mAppContext = appContext;
		mJSONSerializer = new JSONSerializer(mAppContext, JSON_FILENAME);
		//commands loading....
		try {
			mCommands = mJSONSerializer.loadCommands();
			Log.i(TAG, "Commands loaded  ");
		} catch (Exception e) {
			mCommands = new ArrayList<>();
			Log.i(TAG, "commands not  loaded  ");
		}
		//Preferences loading....
		sIp = PreferenceManager.getDefaultSharedPreferences(mAppContext)
				.getString(SettingsFragment.IP_KEY, SettingsFragment.sAddressInput);
		sPasswordOpen = PreferenceManager.getDefaultSharedPreferences(mAppContext)
				.getInt(SettingsFragment.PASSWORD_OPEN_KEY, 0);
		SettingsFragment.isIpValid = PreferenceManager.getDefaultSharedPreferences(mAppContext)
				.getBoolean(SettingsFragment.EXTRA_IP_IS_VALID, SettingsFragment.isIpValid);
		SettingsFragment.isPassordOpenValid = PreferenceManager.getDefaultSharedPreferences(mAppContext)
				.getBoolean(SettingsFragment.EXTRA_PASSWORD_OPEN_IS_VALID, SettingsFragment.isPassordOpenValid);
	}

	public static Dashboard get(Context c) {
		if (sDashboard == null)
			sDashboard = new Dashboard(c.getApplicationContext());
		return sDashboard;
	}
	public static  void inviaCommand(String openwebnetString) {
		GestioneSocketComandi gestioneSocketComandi= new GestioneSocketComandi();
		gestioneSocketComandi.connect(sIp, PORT, sPasswordOpen);
		gestioneSocketComandi.invia(openwebnetString);
		gestioneSocketComandi.close();
	}

	public List<Command> getCommands() {
		return mCommands;
	}

	public Command getCommand(UUID id) {
		return Stream.of(mCommands).filter(command->command.getId().equals(id)).findFirst().get();
	}

	public void addCommand(Command c) {
		mCommands.add(c);
	}

	public void deleteCommand(Command c) {
		mCommands.remove(c);
	}

	public void saveCommands() {
		try {
			mJSONSerializer.saveCommands((ArrayList<Command>) mCommands);
			Log.i(TAG, "Commands saved");
		} catch (Exception e) {
			Log.i(TAG, "Error saving commands");
		}
	}
}

