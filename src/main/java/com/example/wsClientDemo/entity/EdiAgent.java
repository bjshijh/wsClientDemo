package com.example.wsClientDemo.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.net.URL;

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
	
        @Value("${server.target.httpUrl}")
        public String serverHttpUrl;
        
        @Value("${server.target.ftpHost}")
        public String serverFtpHost;
        
        @Value("${server.target.ftpPort}")
        public int serverFtpPort;
        
        
        @Value("${server.target.dir}")
        public String remoteDir;
        @Value("${server.target.username}")
        public String targetUsername;
        @Value("${server.target.password}")
        public String targetPassowrd;
        
        @Value("${server.target.useSSL}")
        public String useSSL;
                
	public String remoteIp;
	public int status=0;
	
        @Autowired
	public EdiDataSource dataSource;
        
        public String getServerDataChannel() {
            if ( this.serverHttpUrl!=null && this.serverHttpUrl.length()>10){
                return "http";
            }else if ( this.serverFtpHost !=null ){
                if ( this.serverFtpPort==0)
                    serverFtpPort=21;
                
                return "ftp";
            }else{
                return "unknown";
            }
        }
        
        public boolean isTargetInfoCorrect() throws Exception {
            if ( "http".equals(getServerDataChannel()) ){
                new URL(serverHttpUrl);
            }else{
                new URL("ftp://" + serverFtpHost + ":" + serverFtpPort);
            }
            return true;
        }
}
