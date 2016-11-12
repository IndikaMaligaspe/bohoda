package rezg.rezos.bohoda.connectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@PropertySource("classpath:bootstrap.yml")
public class PropertyObjectConnectorTest {

	private PropertyObjectConnector target = new PropertyObjectConnector();
	private String file;
	private String project;
	private String environment;

	
	// @Value("${bohoda.server.uri}")
	private String bohodaService = "http://localhost:8888";
	
	@Test
	@Ignore
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testHandleRequest(){
		file = "oman_config_portals";
		project = "development";
		environment = "master";
		Object response = target.handleRequest(file,project,environment,bohodaService);
		assertNotNull(response);
	}
	
	@Test
	public void testConnect(){
		file = "oman_config_portals";
		project = "development";
		environment = "master";
		int response = target
				.connect(bohodaService + "?file=" + file + "&project=" + project + "&environment=" + environment);
		assertNotNull(response);
		assertEquals(200, response);
	}
	
	@Test
	public void testConvertToPropertiesObject() throws Exception{
		String JSONString = "{\"name\":\"configclient\",\"profiles\":[\"et\"],\"label\""
				+ ":\"qa\",\"version\":\"39f68bd3a9839e3cdd26053e41ad35a1fea2519a\",\"state\":null,\""
				+ "propertySources\":[{\"name\":\"https://github.com/IndikaMaligaspe/bohoda-config-test/configclient.yml\","
				+ "\"source\":{\"configuration.projectName\":\"configclient\",\"server.port\":8000,\"message\":\""
				+ "\",\"greeting\":\"Hello from the configuration in QA Environment!\"}}]}";
		String source = "[{\"configuration.projectName\":\"configclient\",\"server.port\":8000,\"message\":\""
				+ "\",\"greeting\":\"Hello from the configuration in QA Environment!\"}]";
		Properties response = target.convertServicResponse(JSONString);
		assertNotNull(response);
		assertEquals("configclient", response.get("configuration.projectName").toString());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConvertToPropertyObjectIfSourceNull() throws Exception{
		String JSONString = "{\"name\":\"configclient\",\"profiles\":[\"et\"],\"label\""
				+ ":\"qa\",\"version\":\"39f68bd3a9839e3cdd26053e41ad35a1fea2519a\",\"state\":null,\""
				+ "propertySources\":[{\"name\":\"https://github.com/IndikaMaligaspe/bohoda-config-test/configclient.yml\","
				+ "\"source\":null}]}";
		Properties response = target.convertServicResponse(JSONString);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConvertToPropertyObjectIfPropertySourceEmpty() throws Exception{
		String JSONString = "{\"name\":\"configclient\",\"profiles\":[\"et\"],\"label\""
				+ ":\"qa\",\"version\":\"39f68bd3a9839e3cdd26053e41ad35a1fea2519a\",\"state\":null,\""
				+ "propertySources\":[]}";
		Properties response = target.convertServicResponse(JSONString);
		assertNotNull(response);
	}

}
