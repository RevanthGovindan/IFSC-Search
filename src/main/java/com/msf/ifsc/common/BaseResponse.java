package com.msf.ifsc.common;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.msf.log.Logger;

public class BaseResponse {

	public static Logger log = Logger.getLogger(BaseResponse.class);

	transient protected HttpServletResponse httpResponse;
	protected JSONObject response;
	protected JSONObject dataObj;

	public BaseResponse(HttpServletResponse httpResponse) {
		this.httpResponse = httpResponse;
		this.dataObj = new JSONObject();
		this.response = new JSONObject();
	}

	public void setStatus(String status, String msg) {
		this.response.put("status", status);
		this.response.put("msg", msg);
	}

	public void setFailure(String msg) {
		this.response.put("status", "FAILED");
		this.response.put("msg", msg);
	}

	public void setSuccess() {
		this.response.put("status", "SUCCESS");
	}

	public void addToData(String key, String value) {
		this.dataObj.put(key, value);
	}

	public void addToData(String key, int value) {
		this.dataObj.put(key, value);
	}

	public void addToData(String key, JSONObject value) {
		this.dataObj.put(key, value);
	}

	public JSONObject getCompleteResponse() {
		this.response.put("data", this.dataObj);
		return this.response;
	}

	public String getStringResponse() {
		log.debug("get string method");
		this.response.put("data", this.dataObj);
		log.debug(this.dataObj);
		log.debug(this.response);
		return this.response.toString();
	}

	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}

}
