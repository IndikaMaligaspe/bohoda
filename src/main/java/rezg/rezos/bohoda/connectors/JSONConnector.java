package rezg.rezos.bohoda.connectors;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import rezg.rezos.bohoda.util.ConnectorUtil;

public class JSONConnector {

	private URL mURL = null;
	private HttpURLConnection uConn = null;
	private DataInputStream dis = null;
	private Logger logger = (Logger) Logger.getInstance(JSONConnector.class);
	private String JSONString;
	private ConnectorUtil connectUtil = new ConnectorUtil();

	public String handleRequest(String file, String project, String environment, String bohodaService) {
		String sourceList = null;
		logger.info("Inside handleRequest with request - file -" + file + ", project - " + project + ", environment - "
				+ environment);
		logger.debug("Calling Service - "+ bohodaService + "/" + file + "/" + project + "/" + environment );
		if (null == bohodaService) {
			return null;
		}

		try {
			connect(bohodaService + "/" + file + "/" + project + "/" + environment);
			sourceList = decodeJSON(this.JSONString);
			logger.info(sourceList);
		} catch (Exception e) {
			e.printStackTrace();
			sourceList = "ERROR - "+e.getMessage();
		} finally {
			try {
				this.dis.close();
			} catch (Exception e2) {
			}
			try {
				this.uConn.disconnect();
			} catch (Exception e2) {
			}
		}
		return sourceList;
	}

	public int connect(String bohodaService) {
		Object[] connectionRresponse = connectUtil.connect(bohodaService, this.JSONString);
		int returnVal = 500;
		try {
			this.JSONString = connectionRresponse[1].toString();
			System.out.println("this.serviceRespons --- " + this.JSONString);
			returnVal =  (int) connectionRresponse[0];
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnVal;
	}

	public String decodeJSON(String jsonString) {
		String sourceList = "";
		JSONArray array = JsonPath.parse(jsonString).read("$.propertySources..source");
		sourceList = array.toJSONString();
		return sourceList;
	}

}
