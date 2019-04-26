package com.example.wsClientDemo.component.ftp;

import java.io.InputStream;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import com.example.wsClientDemo.component.ssl.SSLContextInit;
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
        SSLContext sslContext = SSLContextInit.getSSLContext();
        client = new FTPSClient(sslContext);
        client.setRemoteVerificationEnabled(false);

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
            /*PROT command.
                C - Clear, unsecured
                P - Private, encrypted*/
            client.execPROT("P");
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

    @Override
    public boolean upload(String filename, InputStream is) throws Exception {
        if ( ! (client!=null && client.isConnected()) )
            connectToFtpServer();
        
        if ( client==null)
            return false;
        
        return client.storeFile( filename, is);
    }

}
