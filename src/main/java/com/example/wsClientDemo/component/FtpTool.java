package com.example.wsClientDemo.component;

import com.example.wsClientDemo.entity.EdiAgent;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.ftp.FTPReply;

import com.example.wsClientDemo.entity.TaskResult;

@Slf4j
public class FtpTool implements DataUploadingTool {
    EdiAgent agent;
    
    @Override
    public void setAgent(EdiAgent ediAgent) {
        this.agent=ediAgent;
    }
    public FtpTool() {
    
    }
    public FtpTool(EdiAgent ediAgent) {
        this.agent=ediAgent;
    }

    public FTPClient connectFtpServer() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(1000 * 30);
        ftpClient.setControlEncoding("utf-8");
        // 被动模式, 必须的
        ftpClient.enterLocalPassiveMode();
        try {
            ftpClient.connect(agent.serverFtpHost, agent.serverFtpPort);
            ftpClient.login(agent.targetUsername, agent.targetPassowrd);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("connect ftp {}:{} failed", agent.serverFtpHost, agent.serverFtpPort);
                ftpClient.disconnect();
                return null;
            }
            log.info("replyCode==========={}", replyCode);
            ftpClient.changeWorkingDirectory( agent.remoteDir);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//设置文件传输模式为二进制
        } catch (Exception e) {
            log.error("Fail ------->>>{}", e.getCause());
            ftpClient.disconnect();
            return null;
        }
        return ftpClient;
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

        FTPClient ftpClient = connectFtpServer();
        if (ftpClient == null) {
            return;
        }

        String filename="";
        try {
            for (File f : files ) {
                InputStream is = new FileInputStream( f );
                filename = f.getName();
                ftpClient.changeWorkingDirectory(agent.remoteDir); //进入到文件保存的目录
                Boolean isSuccess = ftpClient.storeFile( filename, is);//保存文件
                if (!isSuccess) {
                    log.error(filename + "---》上传失败！");
                }else{
                    is.close();
                    f.delete();
                }
                log.info("{}---》上传成功！", filename);
            }
            ftpClient.logout();
        } catch (Exception e) {
            log.error("{}---》上传失败！", filename);
            throw e;
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
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
