package com.fabiani.domohome.app.model;

/**
 * Created by Giovanni on 06/12/2015.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.util.Log;


public class Serializer {
    public static final String TAG="Serializer";
    Context mContext;

    Serializer(Context c){
        mContext=c;
    }

    public <T>void saveArrayList(ArrayList<T>arraylist, String serializeFileName){
        try(FileOutputStream fileout=mContext.openFileOutput
                (serializeFileName, Context.MODE_PRIVATE);
            ObjectOutputStream outStream=new ObjectOutputStream
                    (fileout)){
            outStream.writeObject(arraylist);
        } catch (IOException e) {
            Log.i(TAG,"problems writing file   "+e);
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public  <T>ArrayList <T> loadArrayList(String mSerializeFileName){
        ArrayList arraylist = new ArrayList<>();
        try (FileInputStream filein=mContext.openFileInput
                (mSerializeFileName);
             ObjectInputStream inStream=new ObjectInputStream(filein)){
             arraylist =(ArrayList<T>) inStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG,"problems reading file   "+e);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return arraylist;
    }
}



