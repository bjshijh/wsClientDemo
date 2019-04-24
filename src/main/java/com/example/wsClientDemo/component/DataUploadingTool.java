package com.example.wsClientDemo.component;

import com.example.wsClientDemo.entity.EdiAgent;
import com.example.wsClientDemo.entity.TaskResult;
public interface DataUploadingTool {
    public void setAgent( EdiAgent ediAgent);
    public TaskResult doRetrieveAndUpload();
}
