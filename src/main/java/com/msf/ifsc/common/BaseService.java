package com.msf.ifsc.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.me.JSONException;

import com.msf.log.Logger;
import com.msf.sbu2.service.common.SBU2Request;
import com.msf.sbu2.service.config.AppConfig;
import com.msf.sbu2.service.exception.AppConfigNoKeyFoundException;

public abstract class BaseService extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(BaseService.class);

	private void addOriginDomainToAllowOrigin(HttpServletRequest req, HttpServletResponse res)
			throws AppConfigNoKeyFoundException {

		String originDomain = req.getHeader("Origin");
		if (originDomain == null)
			originDomain = "*";

		res.setHeader("Access-Control-Allow-Origin", originDomain);

	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			setCORSHeaders(req, res);
		} catch (AppConfigNoKeyFoundException e) {
			log.error(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {

		BaseRequest callRequest = new BaseRequest(request);
		BaseResponse callResponse = new BaseResponse(response);

		logRequest(callRequest, callRequest.getRequestBody());
		try {
			process(callRequest, callResponse);
			sendResponse(callResponse, request, response);
		} catch (Exception e) {
			log.error(e);
			callResponse.setFailure(e.getMessage());
			sendResponse(callResponse, request, response);
		}
	}

	protected void sendResponse(BaseResponse sResponse, HttpServletRequest request, HttpServletResponse res) {

		try {

			res.setContentType("application/json; charset=UTF-8");

			res.getWriter().print(sResponse.getStringResponse());
			log.info(request.getServletPath() + "-- thread id -- " + Thread.currentThread().getId()
					+ " -- response sent -- " + sResponse.getStringResponse());

		} catch (Exception e) {
			log.error("Exception in sending Response : ", e);
		}
	}

	private void setCORSHeaders(HttpServletRequest req, HttpServletResponse res) throws AppConfigNoKeyFoundException {

		if (AppConfig.getValue("cors.headers.enable").equals("true")) {

			addOriginDomainToAllowOrigin(req, res);
			res.setHeader("Access-Control-Allow-Credentials", "true"); // enable session sharing in CORS
			res.setHeader("Access-Control-Allow-Headers",
					"Access-Control-Allow-Origin,cache-control, content-type, DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Set-Cookie,origin,accept,Service-Type,Cookie");
			res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS"); // these methods will be allowed
																					// after preflight
			res.setHeader("Access-Control-Expose-Headers", "Set-Cookie"); // to enable sh
		}

		res.setStatus(HttpServletResponse.SC_ACCEPTED);
	}

	protected void logRequest(BaseRequest req, String request) {
		HttpServletRequest httpReq = req.getRequest();
		log.info(String.format("%s -- appID=%s, IP=%s -- request received --  %s", httpReq.getRequestURI(), "",
				req.getClientIP(), request));
	}

	abstract protected void process(BaseRequest request, BaseResponse response) throws Exception;

}
