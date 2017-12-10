package com.fabiani.domohome.model;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Giovanni on 12/06/2016.
 */
class SSLSetup {
     static void overrideTrustManager() {
        TrustManager tm = new TrustManager();
        TrustManager[] tma = new TrustManager[]{tm};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tma, new java.security.SecureRandom());
            SSLSocketFactory sf1 = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sf1);
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifierSetup());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace(System.out);
        }
    }

    private static class TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }
    }

    private static class HostnameVerifierSetup implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
                return true;
        }
    }
}
