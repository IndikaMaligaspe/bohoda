package rezg.rezos.bohoda.connectors;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource(locations =  "/application.properties")
public class PropertyTextConnectorTest {

	private PropertyTextConnector target = new PropertyTextConnector();
	String file = null;
	String project = null;
	String environment = null;
	
	@Value(value = "${bohoda.server.uri}")
	String bohodaService = null;
	
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
		String response = target.handleRequest(file, project, environment, bohodaService);
		assertNotNull(response);
		assertTrue(response.indexOf("=")>1);
	}
	
	@Test
	@Ignore
	public void testGetContent(){
		file = "oman_config_portals";
		project = "development";
		environment = "master";
		String response = target.getContent(bohodaService + "/" + file + "/" + project + "/" + environment);
		assertNotNull(response);
		assertTrue(response.indexOf("portal.name =")>1);
	}
	
	@Test
	public void testConnectToService(){
		file = "oman_config_portals";
		project = "development";
		environment = "master";
		int response = target.connect(bohodaService + "/" + file + "/" + project + "/" + environment);
		assertNotNull(response);
	}
	
	@Test
	public void testConvertJsontoString(){
		String JSONString = "{\"name\":\"configclient\",\"profiles\":[\"et\"],\"label\""
				+ ":\"qa\",\"version\":\"39f68bd3a9839e3cdd26053e41ad35a1fea2519a\",\"state\":null,\""
				+ "propertySources\":[{\"name\":\"https://github.com/IndikaMaligaspe/bohoda-config-test/configclient.yml\","
				+ "\"source\":{\"configuration.projectName\":\"configclient\",\"server.port\":8000,\"message\":\""
				+ "\",\"greeting\":\"Hello from the configuration in QA Environment!\"}}]}";
		String source = "configuration.projectName=configclient, server.port=8000, message=, greeting=Hello from the configuration in QA Environment!";
		String response = target.convertJsonToText(JSONString);
		assertNotNull(response);
		assertTrue(source.contentEquals(response));
	}

}
