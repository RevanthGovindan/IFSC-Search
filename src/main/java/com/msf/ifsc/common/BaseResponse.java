package com.msf.ifsc.common;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class BaseResponse extends JSONObject {

	transient protected HttpServletResponse response;

	public BaseResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStatus(String status, String msg) {
		this.put("status", status);
		this.put("msg", msg);
	}

	public void setFailure(String msg) {
		this.put("status", "FAILED");
		this.put("msg", msg);
	}

	public void setSuccess() {
		this.put("status", "SUCCESS");
	}

	public HttpServletResponse getHttpResponse() {
		return response;
	}

}
