package rezg.rezos.bohoda;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rezg.rezos.bohoda.connectors.JSONConnector;

@RestController
@Component
@RefreshScope
public class ClientController {
		
	@Value("${server.uri}")
	String bohodaService;
	
	@RequestMapping(value = "/bohoda/JSON", produces = "application/json")
	public ResponseEntity<?> bohodaJSON( @RequestParam("file") String file, @RequestParam("project") String project, @RequestParam("environment") String environment) {
		if((file.length()==0) || (project.length()==0) || (environment.length()==0)){
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(new JSONConnector().handleRequest(file, project, environment, bohodaService),HttpStatus.OK);
	}

	@RequestMapping(value = "/bohoda/PROPS", produces = MediaType.TEXT_PLAIN_VALUE)
	public String bohodaPROPS( @RequestParam("file") String file, @RequestParam("project") String project, @RequestParam("environment") String environment) {
		List<String> env = Arrays.asList("message=message from Bohoda", "type=text", "age=pretty old",
				"sarrong=andegena");
		return env.toString();
	}
}
