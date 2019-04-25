package com.example.wsClientDemo.component.ftp;

import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

@Slf4j
public class FtpClient  extends FtpServerInfo implements FtpClientInterface {
    FTPClient client= null;
    
    public FtpClient(String host, int port, String username, String pwd, String workingDirectory ) {
        super(host, port, username, pwd);
        if ( workingDirectory!=null && workingDirectory.length()>0) {
            super.setRemoteDir(workingDirectory);
        }
    }
    
    @Override
    public void connectToFtpServer() throws Exception {
        client = new FTPClient();
        client.setConnectTimeout(1000 * 30);
        client.setControlEncoding("utf-8");
        // 被动模式
        client.enterLocalPassiveMode();
        try {
            client.connect( remoteHost, remotePort);
            client.login( ftpUsername, ftpPassword);
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("connect ftp {}:{} failed", remoteHost, remotePort);
                client.disconnect();
                client=null;
            }
            log.info("replyCode==========={}", replyCode);
            client.changeWorkingDirectory(  workingDir);
            client.setFileType(FTP.BINARY_FILE_TYPE);//设置文件传输模式为二进制
        } catch (Exception e) {
            log.error("Fail ------->>>{}", e.getCause());
            client.disconnect();
            client=null;
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
