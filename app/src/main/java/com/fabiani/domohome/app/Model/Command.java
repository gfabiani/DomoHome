package com.fabiani.domohome.app.model;
/* Author: Giovanni Fabiani
* 
*/
import java.io.Serializable;
import java.util.UUID;

public class Command implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final transient Integer[] sWhereChoices = new Integer[99];
	private UUID mId;
	private String mTitle;
	private int mWhat;
	private int mWho;
	private int mWhoSpinnerPosition;
	private int mWhere;
	private int mWhereSpinnerPosition;

	public enum WhoChoice {
		SCENARIOS(0), LIGHTING(1), AUTOMATISM(2), ELECTRICAL_LOADS(3), WARMING_MANAGEMENT(4), ANTITHEFT(5),
		BASIC_VIDEO_DOORPHONE(6), GATEWAY_MANAGEMENT(13), CEN_COMMANDS(15), SOUND_DIFFUSION(16), MH200N_SCENARIOS(17),
		POWER_MANAGEMENT(18), SOUND_DIFFUSION_2(22), CEN_PLUS_ETC(25), AUTOMATION_DIAGNOSTICS(1001),
		THERMOREGULATION_DIAGNOSTICS(1004), DEVICE_DIAGNOSTICS(1013);

		transient private int mValue = 0;

		WhoChoice(int value) {
			mValue = value;
		}

		public int getValue() {
			return mValue;
		}
	}

	public Command() {
		mId = UUID.randomUUID();
		for (int i = 0; i < sWhereChoices.length; i++)
			sWhereChoices[i] = i + 1;
	}

	//getters and setters
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public int getWho() {
		return mWho;
	}

	public void setWho(int who) {
		mWho = who;
	}

	public int getWhat() {
		return mWhat;
	}

	public void setWhat(int what) {
		mWhat = what;
	}

	public int getWhere() {
		return mWhere;
	}

	public void setWhere(int where) {
		mWhere = where;
	}

	public int getWhoSpinnerPosition() {
		return mWhoSpinnerPosition;
	}

	public void setWhoSpinnerPosition(int whoSpinnerPosition) {
		mWhoSpinnerPosition = whoSpinnerPosition;
	}

	public int getWhereSpinnerPosition() {
		return mWhereSpinnerPosition;
	}

	public void setWhereSpinnerPosition(int whereSpinnerPosition) {
		mWhereSpinnerPosition = whereSpinnerPosition;
	}

	public UUID getId() {
		return mId;
	}
	@Override
	public String toString() {
		return mTitle;
	}
}