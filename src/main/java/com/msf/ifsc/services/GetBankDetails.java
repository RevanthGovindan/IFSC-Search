package com.msf.ifsc.services;

import java.util.HashMap;

import com.msf.ifsc.DAO.IfscDAO;
import com.msf.ifsc.common.BaseRequest;
import com.msf.ifsc.common.BaseResponse;
import com.msf.ifsc.common.BaseService;
import com.msf.log.Logger;

public class GetBankDetails extends BaseService {

	public static Logger log = Logger.getLogger(GetBankDetails.class);

	@Override
	protected void process(BaseRequest request, BaseResponse response) throws Exception {
		String ifscCode = request.getReqParams().get("ifsc");
		IfscDAO daoObj = new IfscDAO();
		HashMap<String, String> bankDetails = daoObj.getBankByIFSC(ifscCode);
		response.addToData("bankName", bankDetails.get("bankName"));
		response.addToData("branch", bankDetails.get("branch"));
		response.addToData("address", bankDetails.get("address"));
		response.addToData("city", bankDetails.get("city"));
		response.addToData("state", bankDetails.get("state"));
		response.setSuccess();
	}
}
