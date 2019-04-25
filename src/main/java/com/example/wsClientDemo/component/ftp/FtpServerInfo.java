package com.example.wsClientDemo.component.ftp;

public class FtpServerInfo {
    public String remoteHost;
    public int remotePort;
    public String ftpUsername, ftpPassword;
    public String workingDir;       
    public FtpServerInfo(String host, int port, String username, String pwd) {
        this.remoteHost =host;
        this.remotePort=port;
        this.ftpUsername= username;
        this.ftpPassword =pwd;
    }
    
    public void setRemoteDir(String rdir) {
        this.workingDir = rdir;
    }
}
