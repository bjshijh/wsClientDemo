package com.example.wsClientDemo.component.ssl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.core.io.ClassPathResource;

public class SSLContextInit {
    public static SSLContext getSSLContext() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, UnrecoverableKeyException, IOException {
        TrustManager[] tm = getTrustManagers();
        System.out.println("Init SSL Context");
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(null, tm, null);

        return sslContext;
    }


    private static TrustManager[] getTrustManagers() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ClassPathResource jksResource = new ClassPathResource("wsClientKeystore.jks");
        ks.load( jksResource.getInputStream(), "123456".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        return tmf.getTrustManagers();
    }
    
}
