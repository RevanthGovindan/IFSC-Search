package com.msf.ifsc.DAO;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.msf.ifsc.common.exceptions.NotFoundException;
import com.msf.ifsc.utils.DBConstants;
import com.msf.ifsc.utils.IfscDbPool;
import com.msf.sbu2.service.config.InfoMessage;
import com.msf.utils.helper.Helper;

public class IfscDAO {
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
}
