package com.msf.ifsc.DAO;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.msf.ifsc.utils.DBConstants;
import com.msf.ifsc.utils.IfscDbPool;
import com.msf.utils.helper.Helper;

public class IfscDAO {
	public HashMap<String, String> getBankByIFSC(String ifscCode) throws SQLException, UnsupportedEncodingException {
		HashMap<String, String> map = new HashMap<String, String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = IfscDbPool.getInstance().getConnection();
			ps = conn.prepareStatement(DBConstants.GET_BANK_BY_IFSC);
			ps.setString(1, ifscCode);
			rs = ps.executeQuery();
			rs.next();
			map.put("bankName", rs.getString("BANK"));
			map.put("branch", rs.getString("BRANCH"));
			map.put("address", URLDecoder.decode(rs.getString("ADDRESS"), StandardCharsets.UTF_8.toString()));
			map.put("city", rs.getString("CITY2"));
			map.put("state", rs.getString("STATE"));

		} finally {
			Helper.closeResultSet(rs);
			Helper.closeStatement(ps);
			Helper.closeConnection(conn);
		}

		return map;
	}
}
