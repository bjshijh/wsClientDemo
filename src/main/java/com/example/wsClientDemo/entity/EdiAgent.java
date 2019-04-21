package com.example.wsClientDemo.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/agent.properties")
@ConfigurationProperties 
public class EdiAgent {

	@Value("${agent.id}") 
	public int agentId;
	
	@Value("${agent.name}")
	public String agentName;
	
	@Value("${agent.taskInterval}")
	public int interval;
	
        @Value("${server.data.httpChannel}")
        public String serverHttpChannel;
        
        @Value("${server.data.ftpChannel}")
        public String serverFtpChannel;
        
	public String remoteIp;
	public int status=0;
	
        @Autowired
	public EdiDataSource dataSource;
        
        public String getServerDataChannel() {
            if ( this.serverHttpChannel!=null ){
                return "http";
            }else if ( this.serverFtpChannel!=null ){
                return "ftp";
            }else{
                return "unknown";
            }
        }
}
