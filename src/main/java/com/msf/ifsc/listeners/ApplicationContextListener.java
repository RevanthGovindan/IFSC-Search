package com.msf.ifsc.listeners;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.ConfigurationException;

import com.msf.ifsc.utils.IfscDbPool;
import com.msf.log.Logger;
import com.msf.sbu2.jmx.Monitor;
import com.msf.sbu2.service.config.AppConfig;
import com.msf.sbu2.service.config.InfoMessage;
import com.msf.utils.helper.Helper;

public class ApplicationContextListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(ApplicationContextListener.class);

	public void contextInitialized(ServletContextEvent event) {

		ServletContext ctx = event.getServletContext();
		String classFolder = ctx.getRealPath("/") + "WEB-INF" + System.getProperty("file.separator") + "classes"
				+ System.getProperty("file.separator");
		String logFile = classFolder + "jslog.properties";

		try {
			setLogger(logFile);
			log = Logger.getLogger(ApplicationContextListener.class);
		} catch (Exception e) {
			log.error("Not able to set Logger");
		}

		String configFile = classFolder + "config.properties";
		log.info("Config " + configFile);

		try {
			AppConfig.loadFile(configFile);
		} catch (ConfigurationException e) {
			log.error("Cannot load  config properties ");
			log.error("", e);
		}

		String infoMessageFile = classFolder + "InfoMessage.properties";
		log.info("info message file  " + infoMessageFile);

		try {
			InfoMessage.loadFile(infoMessageFile);
		} catch (ConfigurationException e) {
			log.error("Cannot load info message properties ");
			log.error("", e);
		}

		try {
			IfscDbPool.initTomcatDataSource();
			log.info("Checking for Ifsc db Data source Connection ");
			Helper.closeConnection(IfscDbPool.getInstance().getConnection());
			log.info("Data source connection exists");
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage(), e);
			System.exit(1);
		}

	}

	public static void setLogger(String file) throws Exception {
		Properties properties = new Properties();
		FileInputStream _f = new FileInputStream(file);
		properties.load(_f);
		_f.close();

		Logger.setLogger(properties);
	}

	public void contextDestroyed(ServletContextEvent sce) {

		try {
			Monitor.release();
		} catch (Exception e) {
			log.error("Error while unregistering monitoring beans", e);
		}

		try {
			log.close();
		} catch (Exception e) {
			log.error(e);
		}

	}

}
