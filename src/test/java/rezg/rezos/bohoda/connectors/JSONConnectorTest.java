package rezg.rezos.bohoda.connectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@PropertySource("classpath:application.properties")
public class JSONConnectorTest {

	private JSONConnector target = new JSONConnector();
	String file = null;
	String project = null;
	String environment = null;

	// @Value("${bohoda.server.uri}")
	private String bohodaService = "http://localhost:8888";

	@Test
	@Ignore
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleRequest() {
		file = "oman_config_portals";
		project = "development";
		environment = "master";
		String response = target.handleRequest(file, project, environment, bohodaService);
		assertNotNull(response);
	}

	@Test
	@Ignore
	public void testConnectToConfigserver() {
		file = "oman_config_portals";
		project = "development";
		environment = "master";
		int response = target
				.connect(bohodaService + "?file=" + file + "&project=" + project + "&environment=" + environment);
		assertNotNull(response);
		assertEquals(200, response);
	}

	@Test
	public void testDecodeJsonResponse(){
		String JSONString = "{\"name\":\"configclient\",\"profiles\":[\"et\"],\"label\""
				+ ":\"qa\",\"version\":\"39f68bd3a9839e3cdd26053e41ad35a1fea2519a\",\"state\":null,\""
				+ "propertySources\":[{\"name\":\"https://github.com/IndikaMaligaspe/bohoda-config-test/configclient.yml\","
				+ "\"source\":{\"configuration.projectName\":\"configclient\",\"server.port\":8000,\"message\":\""
				+ "\",\"greeting\":\"Hello from the configuration in QA Environment!\"}}]}";
		String source = "[{\"configuration.projectName\":\"configclient\",\"server.port\":8000,\"message\":\""
				+ "\",\"greeting\":\"Hello from the configuration in QA Environment!\"}]";
		String response = target.decodeJSON(JSONString);
		assertNotNull(response);
		assertEquals(source, response);
	}
	
	@Test
	public void testDecodeJesonResponseWhenNoSource(){
		String JSONString = "{\"name\":\"configclient\",\"profiles\":[\"et\"],\"label\""
				+ ":\"qa\",\"version\":\"39f68bd3a9839e3cdd26053e41ad35a1fea2519a\",\"state\":null,\""
				+ "propertySources\":[]}";
		String source = "[]";
		String response = target.decodeJSON(JSONString);
		System.out.println("Response - "+response);
		assertNotNull(response);
		assertEquals(source,response);
	}

}
