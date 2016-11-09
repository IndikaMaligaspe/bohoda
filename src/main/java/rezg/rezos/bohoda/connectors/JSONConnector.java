package rezg.rezos.bohoda.connectors;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class JSONConnector {

	private URL mURL = null;
	private HttpURLConnection uConn = null;
	private DataInputStream dis = null;
	private Logger logger = (Logger) Logger.getInstance(JSONConnector.class);
	private String JSONString;

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
		int statusCode = 500;
		try {
			mURL = new URL(bohodaService);
			uConn = (HttpURLConnection) mURL.openConnection();
			uConn.setDoInput(true);
			uConn.setDoOutput(true);
			statusCode = uConn.getResponseCode();
			dis = new DataInputStream(uConn.getInputStream());
			StringBuffer sbr = new StringBuffer();
			String line = dis.readLine();
			sbr.append(line);
			while (null != (line = dis.readLine())) {
				line = dis.readLine();
				;
				sbr.append(line);
			}
			this.JSONString = sbr.toString();
			logger.debug("This JSON String --- "+this.JSONString );
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = 500;
		}

		return statusCode;
	}

	public String decodeJSON(String jsonString) {
		String sourceList = "";
		JSONArray array = JsonPath.parse(jsonString).read("$.propertySources..source");
		sourceList = array.toJSONString();
		return sourceList;
	}

}
