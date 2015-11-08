package com.fabiani.domohome.app;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import java.io.*;
import java.util.ArrayList;

public class JSONSerializer {
    public static final String TAG = "JSONSerializer";
    private Context mContext;
    private String mSerializeFileName;

    JSONSerializer(Context c, String f) {
        mContext = c;
        mSerializeFileName = f;
    }

    public void saveCommands(ArrayList<Command> commands) throws JSONException {
        JSONArray array = new JSONArray();
        for (Command c : commands)
            array.put(c.toJSON());
        try (OutputStream out = mContext.openFileOutput(mSerializeFileName, Context.MODE_PRIVATE);
             Writer writer = new OutputStreamWriter(out)) {
             writer.write(array.toString());
        } catch (IOException e) {
            Log.i(TAG, "Unable to write file");
        }
    }

    public ArrayList<Command> loadCommands() throws JSONException {
        ArrayList<Command> commands = new ArrayList<>();
        try (InputStream in = mContext.openFileInput(mSerializeFileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
             StringBuilder jsonString = new StringBuilder();
             String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                commands.add(new Command(array.getJSONObject(i)));
            }
        } catch (IOException e) {
            Log.i(TAG, "Unable to read file");
        }
        return commands;
    }
}
