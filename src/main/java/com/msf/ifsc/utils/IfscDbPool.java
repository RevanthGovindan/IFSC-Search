package com.msf.ifsc.utils;

import com.msf.log.Logger;
import com.msf.sbu2.jmx.Monitor;
import com.msf.sbu2.service.config.AppConfig;
import com.msf.sbu2.service.exception.AppConfigNoKeyFoundException;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class IfscDbPool {

	private static Logger log = Logger.getLogger(IfscDbPool.class);
	public static String MONITOR_BO_DB = "";

	private static IfscDbPool instance = null;
	private static InitialContext context;
	private static BasicDataSource datasource;

	public static void initTomcatDataSource() {

		try {
			context = new InitialContext();
			datasource = (BasicDataSource) context.lookup("java:comp/env/jdbc/quotedata");
		}

		catch (NamingException e) {
			log.error("Exception" + e);
			Monitor.markFailure(MONITOR_BO_DB, e.getMessage());
		}
	}

	public static IfscDbPool getInstance() {

		if (null == instance)

			instance = new IfscDbPool();

		return instance;
	}

	public static void initDataSource() throws AppConfigNoKeyFoundException {

		String driver = AppConfig.getValue("db.ifsc.driver");
		String userName = AppConfig.getValue("db.ifsc.username");
		String password = AppConfig.getValue("db.ifsc.password");
		String dbUrl = AppConfig.getValue("db.ifsc.dburl");

		datasource = new BasicDataSource();
		datasource.setDriverClassName(driver);
		datasource.setUrl(dbUrl);
		datasource.setUsername(userName);
		datasource.setPassword(password);

	}

	public Connection getConnection() throws SQLException {

		try {
			Connection c = datasource.getConnection();
			return c;
		} catch (SQLException e) {
			Monitor.markFailure(MONITOR_BO_DB, e.getMessage());
			throw e;
		}
	}

	public void releasePool() {

		try {

			if (datasource != null) {
				datasource.close();

				log.info("Closing datasource " + datasource);
			}

		} catch (SQLException e) {
			log.warn("Closing datasource ", e);
		}

	}

}
