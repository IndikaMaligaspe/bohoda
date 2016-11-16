package rezg.rezos.bohoda.connectors;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import rezg.rezos.bohoda.util.ConnectorUtil;

public class PropertyTextConnector {

	private URL mURL;
	private HttpURLConnection uConn;
	private DataInputStream dis;
	private Logger logger = (Logger) Logger.getInstance(PropertyTextConnector.class);
	private String serviceResponse = null;
	private ConnectorUtil connectUtil = new ConnectorUtil();

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

	public int connect(String bohodaService) {
		Object[] connectionRresponse = connectUtil.connect(bohodaService, this.serviceResponse);
		int returnVal = 500;
		try {
			this.serviceResponse = connectionRresponse[1].toString();
			System.out.println("this.serviceRespons --- " + this.serviceResponse);
			returnVal =  (int) connectionRresponse[0];
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnVal;
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
