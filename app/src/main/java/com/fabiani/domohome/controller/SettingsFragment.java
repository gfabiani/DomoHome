package com.fabiani.domohome.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.ViewGroup;
import android.widget.Toast;


import com.fabiani.domohome.R;
import com.fabiani.domohome.model.Dashboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Giovanni on 27/08/2016.
 */
public class SettingsFragment extends PreferenceFragment {
    static final String TAG = "SettingsFragment";
    public static final String IP_KEY = "ip";
    public static final String PASSWORD_OPEN_KEY = "passwordopen";
    public static final String EXTRA_IP_IS_VALID = "com.fabiani.domohome.app.extra_ip_is_valid";
    public static final String EXTRA_PASSWORD_OPEN_IS_VALID = "com.fabiani.domohome.app.extra_passord_open_is_valid";
    public static String sAddressInput;
    public static String sPasswordOpenInput;
    public static boolean isIpValid = false;
    public static boolean isPassordOpenValid = false;
    private EditTextPreference mPasswordOpenEditTextPreference;
    private static Pattern mIpPattern = Patterns.IP_ADDRESS;
    private static Matcher mIpMatcher;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getLayoutInflater().inflate(R.layout.app_bar, (ViewGroup) getActivity().findViewById(android.R.id.content));
        mToolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.preferences);
        isIpValid = getArguments().getBoolean(EXTRA_IP_IS_VALID);
        isPassordOpenValid = getArguments().getBoolean(EXTRA_PASSWORD_OPEN_IS_VALID);
        EditTextPreference mIpEditTextPreference = (EditTextPreference) findPreference("IP_KEY");
        mIpEditTextPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            sAddressInput = newValue.toString();
            Dashboard.sIp = sAddressInput;
            SharedPreferences.Editor editor = PreferenceManager.
                    getDefaultSharedPreferences(getActivity()).edit();
            editor.putString(IP_KEY, sAddressInput);
            if (validateIp(sAddressInput))
                isIpValid = true;
            else {
                Toast.makeText(getActivity(), R.string.connector_ip_error, Toast.LENGTH_SHORT).show();
                isIpValid = false;
            }
            editor.putBoolean(EXTRA_IP_IS_VALID, isIpValid);
            editor.apply();
            return true;
        });
        mIpEditTextPreference.setText(Dashboard.sIp);

        mPasswordOpenEditTextPreference = (EditTextPreference) findPreference("PASSWORD_OPEN_KEY");
        mPasswordOpenEditTextPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            final SharedPreferences.Editor editor = PreferenceManager.
                    getDefaultSharedPreferences(getActivity()).edit();
            sPasswordOpenInput = newValue.toString();
            try {
                Dashboard.sPasswordOpen = Integer.parseInt(sPasswordOpenInput);
                editor.putInt(PASSWORD_OPEN_KEY, Integer.parseInt(sPasswordOpenInput));
                isPassordOpenValid = true;
            } catch (NumberFormatException e) {
                isPassordOpenValid = false;
                Toast.makeText(getActivity(), R.string.commandgridgragment_valid_password, Toast.LENGTH_SHORT).show();
            } finally {
                editor.putBoolean(EXTRA_PASSWORD_OPEN_IS_VALID, isPassordOpenValid);
                editor.apply();
            }
            return true;
        });
        if (Dashboard.sPasswordOpen == 0)
            mPasswordOpenEditTextPreference.setText("");
        else
            mPasswordOpenEditTextPreference.setText(Integer.toString(Dashboard.sPasswordOpen));
    }

    public static boolean validateIp(String addressinput) {
        mIpMatcher = mIpPattern.matcher(addressinput);
        return mIpMatcher.matches();
    }

    public static boolean isNetworkActiveConnected(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }


    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_IP_IS_VALID, isIpValid);
        args.putBoolean(EXTRA_PASSWORD_OPEN_IS_VALID, isPassordOpenValid);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
