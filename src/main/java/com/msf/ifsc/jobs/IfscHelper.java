package com.msf.ifsc.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.msf.ifsc.utils.IfscDbPool;
import com.msf.sbu2.service.config.AppConfig;
import com.msf.sbu2.service.exception.AppConfigNoKeyFoundException;
import com.msf.utils.helper.Helper;

public class IfscHelper {

	public static void downloadExcel(String url) throws AppConfigNoKeyFoundException, IOException {
		URL website = new URL(url);
		ReadableByteChannel rbc;
		rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(AppConfig.getValue("ifsc.excel.path"));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
	}

	public static List<IFSCRow> readFile() throws AppConfigNoKeyFoundException, IOException {
		FileInputStream fis = new FileInputStream(new File(AppConfig.getValue("ifsc.excel.path")));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		int totalSheets = wb.getNumberOfSheets();
		List<IFSCRow> ifscList = new ArrayList<IFSCRow>();
		for (int i = 0; i < totalSheets; i++) {
			XSSFSheet sheet = wb.getSheetAt(i);
			Iterator<Row> itr = sheet.iterator();
			itr.next();
			List<IFSCRow> sheetIfsc = IfscHelper.getList(itr);
			ifscList.addAll(sheetIfsc);
		}
		wb.close();
		fis.close();
		return ifscList;
	}

	public static List<IFSCRow> getList(Iterator<Row> itr) {
		List<IFSCRow> ifscList = new ArrayList<IFSCRow>();
		while (itr.hasNext()) {
			Row row = itr.next();
			IFSCRow dataRow = new IFSCRow();
			dataRow.setBank(row.getCell(0).getStringCellValue());
			dataRow.setIfsc(row.getCell(1).getStringCellValue());
			dataRow.setBranch(row.getCell(2).getStringCellValue());
			dataRow.setAddress(row.getCell(3).getStringCellValue());
			dataRow.setCity1(row.getCell(4).getStringCellValue());
			dataRow.setCity2(row.getCell(5).getStringCellValue());
			dataRow.setState(row.getCell(6).getStringCellValue());
			dataRow.setStdCode((int) row.getCell(7).getNumericCellValue());
			if (row.getCell(8).getCellType().equals(CellType.STRING))
				dataRow.setPhone(row.getCell(8).getStringCellValue());
			if (row.getCell(8).getCellType().equals(CellType.NUMERIC))
				dataRow.setPhone(String.valueOf(row.getCell(8).getNumericCellValue()));
			ifscList.add(dataRow);
		}
		return ifscList;
	}

	public static void backUpIfsc() throws SQLException {
		CallableStatement cs = null;
		Connection conn = null;
		try {
			conn = IfscDbPool.getInstance().getConnection();
			cs = conn.prepareCall("{call BackupIfsc()}");
			cs.executeQuery();
		} finally {
			Helper.closeStatement(cs);
			Helper.closeConnection(conn);
		}

	}

	public static void insertIFSCList(List<IFSCRow> ifscList) throws SQLException, UnsupportedEncodingException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String query = "INSERT INTO IFSC_DATA(BANK,IFSC,BRANCH,ADDRESS,CITY1,CITY2,STATE,STD_CODE,PHONE) VALUES(?,?,?,?,?,?,?,?,?)";
			conn = IfscDbPool.getInstance().getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(query);

			for (int i = 0; i < ifscList.size(); i++) {
				IFSCRow row = ifscList.get(i);
				ps.setString(1, row.getBank());
				ps.setString(2, row.getIfsc());
				ps.setString(3, row.getBranch());
				ps.setString(4, URLEncoder.encode(row.getAddress(), StandardCharsets.UTF_8.toString()));
				ps.setString(5, row.getCity1());
				ps.setString(6, row.getCity2());
				ps.setString(7, row.getState());
				ps.setInt(8, row.getStdCode());
				ps.setString(9, row.getPhone());
				ps.addBatch();
				if (((i + 1) % 1000) == 0) {
					ps.executeBatch();
					ps.clearBatch();
				}
			}

			ps.executeBatch();
			conn.commit();

		} finally {
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
	}

	public static void readIfsc() throws SQLException, UnsupportedEncodingException {
		Connection conn = null;
		Statement st = null;
		try {
			String query = "SELECT * FROM IFSC_DATA LIMIT 20";
			conn = IfscDbPool.getInstance().getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				String decodedMsg = URLDecoder.decode(rs.getString(4), StandardCharsets.UTF_8.toString());
				System.out.println(decodedMsg);
			}

		} finally {
			Helper.closeStatement(st);
			Helper.closeConnection(conn);
		}
	}

}
