package com.msf.ifsc.DAO;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.msf.ifsc.common.exceptions.InvalidRequestException;
import com.msf.ifsc.common.exceptions.NotFoundException;
import com.msf.ifsc.utils.DBConstants;
import com.msf.ifsc.utils.IfscDbPool;
import com.msf.log.Logger;
import com.msf.sbu2.service.config.InfoMessage;
import com.msf.utils.helper.Helper;

public class IfscDAO {

	public static Logger log = Logger.getLogger(IfscDAO.class);

	public HashMap<String, String> getBankByIFSC(String ifscCode) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = IfscDbPool.getInstance().getConnection();
			ps = conn.prepareStatement(DBConstants.GET_BANK_BY_IFSC);
			ps.setString(1, ifscCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				map.put("bankName", rs.getString("BANK"));
				map.put("branch", rs.getString("BRANCH"));
				map.put("address", URLDecoder.decode(rs.getString("ADDRESS"), StandardCharsets.UTF_8.toString()));
				map.put("city", rs.getString("CITY2"));
				map.put("state", rs.getString("STATE"));
			} else {
				throw new NotFoundException(InfoMessage.getInfoMSG("info_msg.invalid.ifscCode"));
			}
		} finally {
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

		return map;
	}

	public List<String> getAllBanks() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<String> allBanks = new ArrayList<String>();
		try {
			conn = IfscDbPool.getInstance().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(DBConstants.GET_ALL_BANKS);
			while (rs.next()) {
				allBanks.add(rs.getString("BANK"));
			}
		} finally {
			Helper.closeResultSet(rs);
			Helper.closeStatement(stmt);
			Helper.closeConnection(conn);
		}
		return allBanks;
	}

	public List<String> searchBank(String bankName) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> allBanks = new ArrayList<String>();
		try {
			conn = IfscDbPool.getInstance().getConnection();
			ps = conn.prepareStatement(DBConstants.SEARCH_BANK);
			ps.setString(1, String.format("%%%s%%", bankName));
			rs = ps.executeQuery();
			while (rs.next()) {
				allBanks.add(rs.getString("BANK"));
			}
		} finally {
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
		return allBanks;
	}

	public List<JSONObject> searchBankBranch(String bankName, String branch) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<JSONObject> bankBranches = new ArrayList<JSONObject>();
		try {
			conn = IfscDbPool.getInstance().getConnection();
			ps = conn.prepareStatement(DBConstants.SEARCH_BRANCH);
			ps.setString(1, bankName);
			ps.setString(2, String.format("%%%s%%", branch));
			rs = ps.executeQuery();
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("branch", rs.getString("BRANCH"));
				obj.put("city", rs.getString("CITY2"));
				obj.put("state", rs.getString("STATE"));
				bankBranches.add(obj);
			}
		} finally {
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
		return bankBranches;
	}

	public String getIfscCode(String bankName, String branch) throws SQLException, InvalidRequestException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = IfscDbPool.getInstance().getConnection();
			ps = conn.prepareStatement(DBConstants.GET_IFSC);
			ps.setString(1, bankName);
			ps.setString(2, branch);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("IFSC");
			} else {
				throw new InvalidRequestException(InfoMessage.getInfoMSG("info_msg.invalid.bankandbranch"));
			}
		} finally {
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}
	}

}
