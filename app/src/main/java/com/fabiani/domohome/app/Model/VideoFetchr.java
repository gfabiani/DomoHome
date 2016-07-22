package com.fabiani.domohome.app.model;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Giovanni on 23/06/2016.
 */
public class VideoFetchr {
    private static final String TAG = "VideoFetchr";
    private static GestioneSocketComandi socketCom=new GestioneSocketComandi();
    byte[] bytes1 = new byte[100000];
    byte[] bytes2; // contiene l'immagine jpeg
    DataInputStream br;

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        switch (GestioneSocketComandi.stato){
            case 0:
                if (socketCom.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen)) {
                    socketCom.invia("*6*9**##");
                    Log.i(TAG," Video grabber activated  ");
                    socketCom.invia("*6*0*4000##");//TODO: make 40 partially and the rest variable
                    Log.i(TAG,"Camera sctivated  "); //TODO: Thread time out scaduto

        }
        break;
            case 3:
                socketCom.invia("*6*0*4000##");//TODO: make 40 partially and the rest variable
                Log.i(TAG,"Camera sctivated  "); //TODO: Thread time out scaduto

        }
        URL myUrl = new URL(urlSpec);
        SSLSetup.overrideTrustManager();
        HttpsURLConnection urlc = (HttpsURLConnection) myUrl.openConnection();
        urlc.setRequestMethod("GET");
        urlc.setDoOutput(true);
        urlc.setUseCaches(false);
        urlc.connect();

        //bytes0 = new byte[100000];
        bytes1 = new byte[100000];

        br = new DataInputStream(urlc.getInputStream());

        int length = 0;
        int temp;

        while(true){
            try{
                temp = br.read();
                if(temp == -1) break;
                bytes1[length] = (byte)temp;
                length++;
            } catch (IOException e1) {

                Log.i(TAG,"IOException reading datastream ");
                e1.printStackTrace();

            }
        }

        bytes2 = new byte[length];

        for(int z = 0; z < length; z++){
            bytes2[z] = (byte)bytes1[z];
        }
        return bytes2;
    }
}
