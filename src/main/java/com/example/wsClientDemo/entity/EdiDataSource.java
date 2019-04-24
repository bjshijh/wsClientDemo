package com.example.wsClientDemo.entity;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Component
@PropertySource("classpath:/agent.properties")
@ConfigurationProperties 
public class EdiDataSource {

	@Value("${dataSource.type}") 
	 public String dataSourceType;      // HTTP, FTP, Database
	
	@Value("${dataSource.url}")
	public String url;
	
	@Value("${dataSource.userName}")
	public String userName="";
	
	@Value("${dataSource.password}")
	public String password="";
	
	@Value("${dataSource.driverClassName}")
	public String driverName;
	
	@Value("${dataSource.retrievingSql}")
	public String retrievingSql;
	
	@Value("${dataSource.localFileDir}")
        public String localFileDir;
}
