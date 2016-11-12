package rezg.rezos.bohoda.connectors;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class PropertyTextConnector {

	private URL mURL;
	private HttpURLConnection uConn;
	private DataInputStream dis;
	private Logger logger = (Logger) Logger.getInstance(PropertyTextConnector.class);
	private String serviceResponse = null;

	public String handleRequest(String file, String project, String environment, String bohodaService) {
		// TODO Auto-generated method stub
		String properties = null;
		try{
			properties = getContent(bohodaService + "/" + file + "/" + project + "/" + environment);
		}catch(Exception err){
			properties = null;
		}
		return properties;
	}

	public String getContent(String string) {
		String response = "messae=OK";
		try {
			if(connect(string)==200){
				response = convertJsonToText(this.serviceResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response=e.getMessage();
		}
		return response;
	}

	public int connect(String bohodaService){
		int statusCode = 500;
		try {
			logger.info(bohodaService);
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
				sbr.append(line);
			}
			this.serviceResponse = sbr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = 500;
		}
		return statusCode;
	}

	public String convertJsonToText(String JSONText) {
		logger.info("Inside convertJsonToText with string lebgth - "+JSONText.length());
		JSONArray array = JsonPath.parse(JSONText).read("$.propertySources..source");
		Iterator iter = array.iterator();
		StringBuffer sbr = new StringBuffer();
		while (iter.hasNext()) {
			sbr.append(iter.next().toString());
		}
		String responseString = sbr.toString();
		return responseString.substring(1,responseString.length()-1);
	}

}
