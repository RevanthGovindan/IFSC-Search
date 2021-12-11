package com.msf.ifsc.services;

import java.util.List;

import org.json.JSONObject;

import com.msf.ifsc.DAO.IfscDAO;
import com.msf.ifsc.common.BaseRequest;
import com.msf.ifsc.common.BaseResponse;
import com.msf.ifsc.common.BaseService;
import com.msf.ifsc.common.exceptions.InvalidRequestException;
import com.msf.log.Logger;
import com.msf.sbu2.service.config.InfoMessage;

public class GetIFSCCode extends BaseService {

	public static Logger log = Logger.getLogger(GetIFSCCode.class);

	@Override
	protected void process(BaseRequest request, BaseResponse response) throws Exception {
		String bankName = request.getReqParams().get("bankName");
		String branch = request.getReqParams().get("branch");
		if (bankName == null || branch == null) {
			throw new InvalidRequestException(InfoMessage.getInfoMSG("info_msg.missing.requestParam"));
		}
		IfscDAO daoObj = new IfscDAO();
		String ifscCode = daoObj.getIfscCode(bankName, branch);
		response.addToData("ifsc", ifscCode);
		response.setSuccess();
	}
}
