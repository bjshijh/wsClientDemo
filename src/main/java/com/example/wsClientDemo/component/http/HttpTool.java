package com.example.wsClientDemo.component.http;

import com.example.wsClientDemo.component.DataUploadingTool;
import com.example.wsClientDemo.entity.EdiAgent;
import com.example.wsClientDemo.entity.TaskResult;

public class HttpTool implements DataUploadingTool {
    EdiAgent agent;
    
    @Override
    public void setAgent(EdiAgent ediAgent) {
        this.agent =ediAgent;
    }

    @Override
    public TaskResult doRetrieveAndUpload() {
        TaskResult result= new TaskResult();
        
        return result;
    }
    
}
