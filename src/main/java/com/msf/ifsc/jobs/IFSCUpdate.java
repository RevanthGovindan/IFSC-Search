package com.msf.ifsc.jobs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.msf.ifsc.test.TestConfiguration;
import com.msf.log.Logger;
import com.msf.sbu2.service.config.AppConfig;
import com.msf.sbu2.service.exception.AppConfigNoKeyFoundException;

public class IFSCUpdate {

	private static final long serialVersionUID = 1L;
	protected static Logger log = Logger.getLogger(IFSCUpdate.class);

	public static void main(String[] args) throws UnsupportedEncodingException {
		try {
			TestConfiguration.loadConfiguration();
			IfscHelper.readIfsc();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		List<IFSCRow> ifscList = new ArrayList<IFSCRow>();

		try {
			IfscHelper.downloadExcel(AppConfig.getValue("ifsc.rbi.url"));
		} catch (AppConfigNoKeyFoundException | IOException e) {
			log.error(e.getStackTrace());
		}

		try {
			ifscList = IfscHelper.readFile();
		} catch (FileNotFoundException | AppConfigNoKeyFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e.getStackTrace());
		}

		try {
			if (ifscList.size() > 0) {
				IfscHelper.backUpIfsc();
				IfscHelper.insertIFSCList(ifscList);
				IfscHelper.readIfsc();
			}
		} catch (UnsupportedEncodingException | SQLException e) {
			e.printStackTrace();
		}

	}

}
