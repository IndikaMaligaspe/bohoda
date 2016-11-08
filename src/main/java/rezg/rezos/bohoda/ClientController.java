package rezg.rezos.bohoda;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@RefreshScope
public class ClientController {
	@RequestMapping(value = "/bohoda/JSON", produces = "application/json")
	public List<String> bohodaJSON() {
		List<String> env = Arrays.asList(
				"message: message from Bohoda"
		);
		return env;
	}
	
	@RequestMapping(value = "/bohoda/PROPS", produces = MediaType.TEXT_PLAIN_VALUE)
	public String bohodaPROPS() {
		List<String> env = Arrays.asList(
				"message=message from Bohoda",
				"type=text",
				"age=pretty old",
				"sarrong=andegena"
		);
		return env.toString();
	}
}
