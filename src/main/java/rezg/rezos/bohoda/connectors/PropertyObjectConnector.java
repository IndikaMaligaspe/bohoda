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

public class PropertyObjectConnector {

	private String bohodaService;
	private URL mURL;
	private HttpURLConnection uConn;
	private DataInputStream dis;
	private String serviceResponse;
	private Logger logger = (Logger) Logger.getInstance(PropertyObjectConnector.class);

	public Object handleRequest(String file, String project, String environment,String bohodaService) {
		Properties props = null;
		try {
			props = new Properties();
			if (connect(bohodaService + "/" + file + "/" + project + "/" + environment)==200)
				props = convertServicResponse(serviceResponse);
		} catch (Exception e) {
			e.printStackTrace();
			props = new Properties();
		}
		// TODO Auto-generated method stub
		return props;
	}

	public int connect(String bohodaService) {
		int responseCode = 500;
		try {
			logger.info(bohodaService);
			mURL = new URL(bohodaService);
			uConn = (HttpURLConnection) mURL.openConnection();
			uConn.setDoInput(true);
			uConn.setDoOutput(true);
			responseCode = uConn.getResponseCode();
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
			responseCode = 500;
		}

		return responseCode;
	}

	public Properties convertServicResponse(String JSONText) throws IllegalArgumentException, JsonParseException, JsonMappingException, IOException{
		Properties prop = new Properties();
		Map <String,String>propertyMap = new HashMap<String,String>();
		
			JSONArray array = JsonPath.parse(JSONText).read("$.propertySources..source");
			
			if(array.isEmpty())
				throw new IllegalArgumentException("Source List missing from property file!!!");
			if(array.size()==0)
				throw new IllegalArgumentException("Source List size is 0 for property file!!!");
			if((array.size()==1) && (null==array.get(0)))
				throw new IllegalArgumentException("Source is NULL !!!");
			
			String propertyString = new String(array.toJSONString());
			logger.debug("Converted JSON String *************** " + propertyString);
			propertyString = propertyString.substring(1, propertyString.length() - 1);
			propertyMap = new ObjectMapper().readValue( propertyString,
					new com.fasterxml.jackson.core.type.TypeReference<Map<String,String>>() {
					});
			prop.putAll(propertyMap);
			propertyMap.clear();
		
		return prop;
	}

}
