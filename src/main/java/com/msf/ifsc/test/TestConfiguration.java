package com.msf.ifsc.test;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;

import com.msf.ifsc.utils.IfscDbPool;
import com.msf.log.Logger;
import com.msf.sbu2.service.config.AppConfig;
import com.msf.sbu2.service.config.InfoMessage;
import com.msf.sbu2.service.exception.AppConfigNoKeyFoundException;

public class TestConfiguration {

	private static Logger log;

	public static void setLogger(String file) throws Exception {

		Properties properties = new Properties();
		FileInputStream _f = new FileInputStream(file);
		properties.load(_f);
		_f.close();

		Logger.setLogger(properties);
	}

	public static void loadConfiguration() throws SQLException {

		String classFolder = System.getProperty("user.dir") + "/src/main/resources/";
		String logFile = classFolder + "jslog.properties";

		try {
			setLogger(logFile);
			log = Logger.getLogger(TestConfiguration.class);
		} catch (Exception e) {
			System.out.println("Not able to set Logger");
		}

		String configFile = classFolder + "config.properties";
		System.out.println(configFile);
		try {
			AppConfig.loadFile(configFile);
		} catch (ConfigurationException e) {
			System.out.println(e);
		}

		String infoMessageFile = classFolder + "InfoMessage.properties";

		try {
			InfoMessage.loadFile(infoMessageFile);

		} catch (ConfigurationException e) {
			log.error("Cannot load info message properties");
			log.error("", e);
		}

		try {
			IfscDbPool.initDataSource();
		} catch (AppConfigNoKeyFoundException e) {
			log.error("Cannot load info message properties");
			log.error("", e);
		}

	}

}
