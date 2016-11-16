package rezg.rezos.bohoda.connectors;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import rezg.rezos.bohoda.util.ConnectorUtil;

public class PropertyObjectConnector {

	private String bohodaService;
	private URL mURL;
	private HttpURLConnection uConn;
	private DataInputStream dis;
	private String serviceResponse;
	private Logger logger = (Logger) Logger.getInstance(PropertyObjectConnector.class);
	private ConnectorUtil connectUtil = new ConnectorUtil();

	public Object handleRequest(String file, String project, String environment, String bohodaService) {
		Properties props = null;
		try {
			props = new Properties();
			if (connect(bohodaService + "/" + file + "/" + project + "/" + environment) == 200)
				System.out.println("Service Response ... " + this.serviceResponse);
			props = convertServicResponse(this.serviceResponse);
		} catch (Exception e) {
			e.printStackTrace();
			props = new Properties();
		}
		// TODO Auto-generated method stub
		return props;
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

	public Properties convertServicResponse(String JSONText)
			throws IllegalArgumentException, JsonParseException, JsonMappingException, IOException {
		Properties prop = new Properties();
		Map<String, String> propertyMap = new HashMap<String, String>();

		JSONArray array = JsonPath.parse(JSONText).read("$.propertySources..source");

		if (array.isEmpty())
			throw new IllegalArgumentException("Source List missing from property file!!!");
		if (array.size() == 0)
			throw new IllegalArgumentException("Source List size is 0 for property file!!!");
		if ((array.size() == 1) && (null == array.get(0)))
			throw new IllegalArgumentException("Source is NULL !!!");

		String propertyString = new String(array.toJSONString());
		logger.debug("Converted JSON String *************** " + propertyString);
		propertyString = propertyString.substring(1, propertyString.length() - 1);
		propertyMap = new ObjectMapper().readValue(propertyString,
				new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {
				});
		prop.putAll(propertyMap);
		propertyMap.clear();

		return prop;
	}

}
