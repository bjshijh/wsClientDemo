package com.example.wsClientDemo.component.ftp;

import java.io.InputStream;

public interface FtpClientInterface {
    public void connectToFtpServer() throws Exception;
    
    public boolean upload(String filename, InputStream is) throws Exception;
}
