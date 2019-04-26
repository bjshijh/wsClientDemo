package com.example.wsClientDemo.component.ftp;

import com.example.wsClientDemo.component.DataUploadingTool;
import com.example.wsClientDemo.entity.EdiAgent;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.List;
import java.util.ArrayList;

import com.example.wsClientDemo.entity.TaskResult;

@Slf4j
public class FtpFtpsTool implements DataUploadingTool {
    EdiAgent agent;
    FtpClientInterface ftp_ftps_Client = null;
    
    @Override
    public void setAgent(EdiAgent ediAgent) {
        this.agent=ediAgent;
    }
    public FtpFtpsTool() {
    
    }
    public FtpFtpsTool(EdiAgent ediAgent) {
        this.agent=ediAgent;
    }

    List<File> retrieveLocalFiles() {
        String filepath=agent.dataSource.localFileDir;
        File curPath = new File(filepath);
        List<File> files = new ArrayList<>();
        
        if (curPath.isDirectory()) {
            File [] filelist = curPath.listFiles();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = filelist[i];
                if (!readfile.isDirectory()) {
                    files.add(readfile);
                }
            }
        }
        return files;
    }

    void uploadFiles() throws Exception {
        List<File> files = this.retrieveLocalFiles();
        if ( agent.useSSL.equalsIgnoreCase("YES")) 
            ftp_ftps_Client = new FtpsClient( agent.serverFtpHost, agent.serverFtpPort, 
                agent.targetUsername, agent.targetPassowrd, agent.remoteDir);
        else
            ftp_ftps_Client = new FtpClient( agent.serverFtpHost, agent.serverFtpPort, 
                agent.targetUsername, agent.targetPassowrd, agent.remoteDir);
        
        ftp_ftps_Client.connectToFtpServer();
        if (ftp_ftps_Client == null) {
            return;
        }

        String filename="";
        try {
            for (File f : files ) {
                InputStream is = new FileInputStream( f );
                filename = f.getName();
                Boolean isSuccess = ftp_ftps_Client.upload(filename, is);//保存文件
                    if (!isSuccess) {
                    log.error(filename + "---》上传失败！");
                }else{
                    is.close();
                    f.delete();
                }
                log.info("{}---》上传成功！", filename);
            }
        } catch (Exception e) {
            log.error("{}---》上传失败！", filename);
            throw e;
        } 
    }

    @Override
    public TaskResult doRetrieveAndUpload()  {
        TaskResult result = new TaskResult();
        try {
            uploadFiles();
            log.info("All Files Done.");
        }catch(Exception e){
            result = new TaskResult(e);
            log.error(TaskResult.getExceptionStackTrace(e));
        }
        return result;
    }


}
