package com.msf.ifsc.services;

import java.util.HashMap;

import com.msf.ifsc.DAO.IfscDAO;
import com.msf.ifsc.common.BaseRequest;
import com.msf.ifsc.common.BaseResponse;
import com.msf.ifsc.common.BaseService;
import com.msf.ifsc.common.exceptions.InvalidRequestException;
import com.msf.ifsc.jobs.IfscHelper;
import com.msf.log.Logger;
import com.msf.sbu2.service.config.InfoMessage;

public class GetBankDetails extends BaseService {

	public static Logger log = Logger.getLogger(GetBankDetails.class);

	@Override
	protected void process(BaseRequest request, BaseResponse response) throws Exception {

		String ifscCode = request.getReqParams().get("ifsc");
		if (ifscCode == null) {
			throw new InvalidRequestException(InfoMessage.getInfoMSG("info_msg.missing.ifscCode"));
		}
		if (IfscHelper.isValidIfsc(ifscCode)) {
			IfscDAO daoObj = new IfscDAO();
			HashMap<String, String> bankDetails = daoObj.getBankByIFSC(ifscCode);
			response.addToData("bankName", bankDetails.get("bankName"));
			response.addToData("branch", bankDetails.get("branch"));
			response.addToData("address", bankDetails.get("address"));
			response.addToData("city", bankDetails.get("city"));
			response.addToData("state", bankDetails.get("state"));
			response.setSuccess();
		} else {
			throw new InvalidRequestException(InfoMessage.getInfoMSG("info_msg.invalid.ifscCode"));
		}
	}
}
