package com.example.wsClientDemo.component.ftp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class FtpsClient extends FtpServerInfo implements FtpClientInterface {
    FTPSClient client=null;
    public FtpsClient(String host, int port, String username, String pwd, String workingDirectory ) {
        super(host, port, username, pwd);
        if ( workingDirectory!=null && workingDirectory.length()>0 ) {
            super.setRemoteDir(workingDirectory);
        }
    }
    
    @Override
    public void connectToFtpServer() throws Exception {  
        SSLContext sslContext = getSSLContext();
        client = new FTPSClient(sslContext);
        client.setRemoteVerificationEnabled(false);

        client.setBufferSize(4096);
        client.setControlEncoding("utf-8");
        
        //ftps.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            client.connect( remoteHost, remotePort);
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("connect ftp {}:{} failed", remoteHost, remotePort);
                client.disconnect();
                client= null;
            }
            boolean logres= client.login( ftpUsername, ftpPassword);
            if ( !logres) {
                client.logout();
                client= null;
            }
            client.execPBSZ(0);      // Set protection buffer size
            client.execPROT("P");    // Set data channel protection to private
            client.enterLocalPassiveMode();    // Enter local passive mode
            client.changeWorkingDirectory( workingDir);
            client.setFileType(FTP.BINARY_FILE_TYPE);    //设置文件传输模式为二进制
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Fail ------->>>{}", e.getCause());
            client.disconnect();
            client= null;
        }
    }

    private static SSLContext getSSLContext() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, UnrecoverableKeyException, IOException {
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

    @Override
    public boolean upload(String filename, InputStream is) throws Exception {
        if ( ! (client!=null && client.isConnected()) )
            connectToFtpServer();
        
        if ( client==null)
            return false;
        
        return client.storeFile( filename, is);
    }

}
