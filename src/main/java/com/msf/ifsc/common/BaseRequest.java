package com.msf.ifsc.common;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.msf.log.Logger;

public class BaseRequest {

	public static Logger log = Logger.getLogger(BaseRequest.class);

	private HttpServletRequest request;

	private String requestBody = "";

	private Map<String, String> reqParams = new HashMap<>();

	public BaseRequest(HttpServletRequest httpRequest) {
		this.setRequest(httpRequest);
		setReqParams(request.getParameterMap());

		boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
		if (!isMultiPart) {
			requestBody = getRequestBody(request);
		}

	}

	protected String getRequestBody(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		String reqString = null;

		try {
			BufferedReader reader = request.getReader();

			int len;
			char[] chars = new char[4 * 1024];

			while ((len = reader.read(chars)) >= 0) {
				sb.append(chars, 0, len);
			}

		} catch (Exception e) {
			log.error("Error in reading Request : ", e);
		}

		reqString = sb.toString();

		return reqString;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Map<String, String> getReqParams() {
		return reqParams;
	}

	public void setReqParams(Map<String, String[]> reqParams) {
		for (Map.Entry<String, String[]> entry : reqParams.entrySet()) {
			this.reqParams.put(entry.getKey(), entry.getValue()[0]);
		}
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getHeader(String name) {
		return Optional.ofNullable(this.request.getHeader(name)).orElse("");
	}

	public String getClientIP() {
		return getClientIP("X-Real-IP");
	}

	public String getClientIP(String fromHTTPHeader) {
		String ipAddress = getRequest().getHeader(fromHTTPHeader);
		if (ipAddress == null) {
			ipAddress = getRequest().getRemoteAddr();
		}
		return ipAddress;
	}
}
