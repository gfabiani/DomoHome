package com.fabiani.domohome.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//singleton class

public class Dashboard {
	private static final String TAG = "Dashboard";
	private static final String JSON_FILENAME = "commands.json";
	private static final int PORT = 20000;
	private static Dashboard sDashboard;
	static String sIp;
	static int sPasswordOpen;
	static GestioneSocketMonitor gestSocketMonitor;
	private Context mAppContext;
	private JSONSerializer mJSONSerializer;
	private ArrayList<Command> mCommands;

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
				.getInt(SettingsFragment.PASSWORD_OPEN_KEY, 0);//check
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


	public static boolean isNetworkActiveConnected(Context c) {
		ConnectivityManager cm =
				(ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null &&
				activeNetwork.isConnected();
	}

	static void startMonitoring(Context c) {
		if (!SettingsFragment.isIpValid)
			Toast.makeText(c, R.string.connector_ip_error, Toast.LENGTH_SHORT).show();
		if (!SettingsFragment.isPassordOpenValid)
			Toast.makeText(c, R.string.commandgridgragment_valid_password, Toast.LENGTH_SHORT).show();
		else {
			try {
				gestSocketMonitor = new GestioneSocketMonitor(); // improve exception handling
				gestSocketMonitor.connect(sIp, PORT, sPasswordOpen);
			} catch (Exception e) {
				Toast.makeText(c, R.string.host_unricheable, Toast.LENGTH_LONG).show();
				gestSocketMonitor.close();
			}
		}
	}

	static void invia(Context c, String openwebnetString) {
		try {
			GestioneSocketComandi gestSocketComandi = new GestioneSocketComandi();
			gestSocketComandi.connect(sIp, PORT, sPasswordOpen);
			gestSocketComandi.invia(openwebnetString);
			gestSocketComandi.close();
		} catch (Exception e) {
			try {
				throw new IOException();
			} catch (IOException e1) {
				Toast.makeText(c, R.string.host_unricheable, Toast.LENGTH_LONG).show();
			}
		}
	}

	public static ArrayList<Command> filter(List<Command> commands, CommandPredicate p) {
		ArrayList<Command> result = new ArrayList<>();
		for (Command c : commands)
			if (p.test(c))
				result.add(c);
		return result;
	}

	public ArrayList<Command> getCommands() {
		return mCommands;
	}

	public Command getCommand(UUID id) {
		for (Command c : mCommands) {
			if (c.getId().equals(id))
				return c;
		}
		return null;
	}

	public void addCommand(Command c) {
		mCommands.add(c);
	}

	public void deleteCommand(Command c) {
		mCommands.remove(c);
	}

	public void saveCommands() {
		try {
			mJSONSerializer.saveCommands(mCommands);
			Log.i(TAG, "Commands saved");
		} catch (Exception e) {
			Log.i(TAG, "Error saving commands");
		}
	}

}

