package rezg.rezos.bohoda.util;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class ConnectorUtil {
	private URL mURL;
	private HttpURLConnection uConn;
	private DataInputStream dis;
	private Logger logger = (Logger) Logger.getInstance(ConnectorUtil.class);
	

	public Object[] connect(String bohodaService,String serviceResponse) {
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
			serviceResponse = sbr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			responseCode = 500;
		}
		System.out.println("responseCode-- "+responseCode);
		return new Object[]{responseCode,serviceResponse};
	}

}
