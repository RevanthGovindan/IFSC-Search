package com.msf.ifsc.services;

import java.util.List;

import com.msf.ifsc.DAO.IfscDAO;
import com.msf.ifsc.common.BaseRequest;
import com.msf.ifsc.common.BaseResponse;
import com.msf.ifsc.common.BaseService;
import com.msf.ifsc.common.exceptions.InvalidRequestException;
import com.msf.log.Logger;
import com.msf.sbu2.service.config.InfoMessage;

public class SearchBank extends BaseService {

	public static Logger log = Logger.getLogger(SearchBank.class);

	@Override
	protected void process(BaseRequest request, BaseResponse response) throws Exception {
		String bankName = request.getReqParams().get("bankName");
		IfscDAO daoObj = new IfscDAO();
		List<String> allBanks = daoObj.searchBank(bankName);
		if (!(allBanks.size() > 0)) {
			throw new InvalidRequestException(InfoMessage.getInfoMSG("info_msg.missing.bank.search"));
		}
		response.addToData("banks", allBanks);
	}

}
