package com.fabiani.domohome.model;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.UUID;

//POJO

public class Command {
	public static final Integer[] sWhereChoices = new Integer[99]; //TODO: Check boundaries
	public static final Integer[] sTimeOutChoices={0,1,3,5,60,90,120};
	private static final String JSON_ID = "id";
	private static final String JSON_COMMAND_TITLE = "command_title";
	private static final String JSON_WHO = "who";
	private static final String JSON_WHERE = "where";
	private static final String JSON_TIMEOUT = "timeout";
	private UUID mId;
	private String mTitle;
	private int mWhat;
	private int mWho;
	private int mWhere;
	private int mTimeout;

	public enum WhoChoice 	{SCENARIOS(0), LIGHTING(1),AUTOMATISM(2), MH200N_SCENARIOS(17);

		private  int mValue=0;

		WhoChoice(int value){
			mValue=value;
		}

		public int getValue() {
			return mValue;
		}
	}

	public Command() {
		mId = UUID.randomUUID();
		Arrays.setAll(sWhereChoices, i -> i + 1);
	}

	// JSON  object extraction
	Command(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));// pay attention to this!
		mTitle = json.getString(JSON_COMMAND_TITLE);
		mWho = json.getInt(JSON_WHO);
		mWhere = json.getInt(JSON_WHERE);
		mTimeout=json.getInt(JSON_TIMEOUT);
	}

	//JSON object creation
	JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_COMMAND_TITLE, mTitle);
		json.put(JSON_WHO, mWho);
		json.put(JSON_WHERE, mWhere);
		json.put(JSON_TIMEOUT,mTimeout);
		return json;
	}

	//getters and setters
	public UUID getId() {
		return mId;
	}

	public void setTitle(String title) {
				mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setWho(int who) {
		mWho = who;
	}

	public int getWho() {
		return mWho;
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

	public int getTimeout() {
		return mTimeout;
	}

	public void setTimeout(int timeout) {
		mTimeout = timeout;
	}


	@Override
	public String toString() {
		return mTitle;
	}
}
