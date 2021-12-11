package com.msf.ifsc.services;

import java.util.List;

import com.msf.ifsc.DAO.IfscDAO;
import com.msf.ifsc.common.BaseRequest;
import com.msf.ifsc.common.BaseResponse;
import com.msf.ifsc.common.BaseService;
import com.msf.log.Logger;

public class GetAllBanks extends BaseService {

	public static Logger log = Logger.getLogger(GetAllBanks.class);

	@Override
	protected void process(BaseRequest request, BaseResponse response) throws Exception {
		IfscDAO daoObj = new IfscDAO();
		List<String> allBanks = daoObj.getAllBanks();
		response.addToData("bankNames", allBanks);
	}

}
