package com.fabiani.domohome.app.model;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Giovanni on 12/06/2016.
 */
public class SSLSetup {
    public static void overrideTrustManager() {
        X509TrustManager tm = new X509TrustManager();
        KeyManager[] km = null;
        X509TrustManager[] tma = new X509TrustManager[] { tm };
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(km, tma, new java.security.SecureRandom());
            SSLSocketFactory sf1 = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sf1);
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifierSetup());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace(System.out);
        }
    }
}
