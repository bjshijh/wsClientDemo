package com.example.wsClientDemo.component;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.wsClientDemo.entity.EdiAgent;
import com.example.wsClientDemo.component.ftp.FtpFtpsTool;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataWorker {
    @Autowired
    EdiAgent agent ;
    
    DataUploadingTool duTool = null;
    
    public void doJob() throws Exception {
        String dsType = agent.dataSource.dataSourceType;
        
        if ( dsType.equalsIgnoreCase("http")){
            log.info("Getting data from MES http interface......");
        }else if ( dsType.equalsIgnoreCase("database")) {
            log.info("Getting data from MES system......");
        }else if ( dsType.equalsIgnoreCase("file")) {
            log.info("Getting data from file(s)......");
        }else {
            throw new Exception("Unknown Data Source");
        }
        
        String serverChannel = agent.getServerDataChannel();
        if ( serverChannel.equalsIgnoreCase("http") ) {
            log.info("Sending data to Server via HTTP......");
        }else if ( serverChannel.equalsIgnoreCase("ftp")) {            
            log.info("Sending data to Server via FTP(S)......");
            duTool = new FtpFtpsTool();
            duTool.setAgent(agent);
            duTool.doRetrieveAndUpload();
        }else{
            throw new Exception("Unknown Server Channel");
        }
    }
    
    
}
