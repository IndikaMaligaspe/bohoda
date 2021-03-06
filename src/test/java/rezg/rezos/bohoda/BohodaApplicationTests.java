package rezg.rezos.bohoda;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
@TestPropertySource(locations =  "/application.properties")
public class BohodaApplicationTests {
	

	@Value(value = "${bohoda.server.uri}")
	String baseURL = null;


	@Configuration
	public static class Config {
		
		@Bean
		public ClientController clientController() {
			return new ClientController();
		}
	}

	
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;



	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testJSONOutput() throws Exception {
		mockMvc.perform(get(baseURL+"bohoda/JSON?file=oman_config_portals&project=development&environment=master").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}
 
	@Test
	public void testJSONOutput_withoutInparameters() throws Exception {
		mockMvc.perform(get(baseURL+"bohoda/JSON").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().is(400));

	}
	
	@Test
	public void testJSONOutput_withoutNullParameters() throws Exception {
		mockMvc.perform(get(baseURL+"bohoda/JSON?file=&project=&environment=").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().is(400));

	}
	
	@Test
	public void testPropertyTextInputOutput() throws Exception {
		mockMvc.perform(get(baseURL+"bohoda/PROPS?file=oman_config_portals&project=development&environment=master").accept(MediaType.TEXT_PLAIN_VALUE)).andDo(print())
		.andExpect(status().isOk());
	}
//	mockMvc.perform(get("/path"))
//	   .andExpect(content(containsString("text")));
	
	@Test
	public void testTextPropertyOutput_withoutInparameters() throws Exception {
		mockMvc.perform(get(baseURL+"bohoda/PROPS").accept(MediaType.TEXT_PLAIN_VALUE)).andDo(print())
				.andExpect(status().is(400));

	}
	
	@Test
	public void testTextPropertyOutput_withoutNullParameters() throws Exception {
		mockMvc.perform(get(baseURL+"bohoda/JSON?file=&project=&environment=").accept(MediaType.TEXT_PLAIN_VALUE)).andDo(print())
				.andExpect(status().is(400));

	}
	
	@Test
	public void testPropertyOutput() throws Exception {
		mockMvc.perform(get(baseURL+"bohoda/PROPERTIES?file=oman_config_portals&project=development&environment=master").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}
	
	
}
