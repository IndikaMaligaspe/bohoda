package rezg.rezos.bohoda;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rezg.rezos.bohoda.connectors.JSONConnector;
import rezg.rezos.bohoda.connectors.PropertyObjectConnector;
import rezg.rezos.bohoda.connectors.PropertyTextConnector;


@RestController
@RefreshScope
@Configuration

public class ClientController {
	
	
	@Value("${service.url}")
	private String bohodaService ;
	
	
	@RequestMapping(value = "/bohoda/JSON", produces = "application/json")
	public ResponseEntity<?> bohodaJSON( @RequestParam("file") String file, @RequestParam("project") String project, @RequestParam("environment") String environment) {
		if((file.length()==0) || (project.length()==0) || (environment.length()==0)){
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(new JSONConnector().handleRequest(file, project, environment, bohodaService),HttpStatus.OK);
	}

	@RequestMapping(value = "/bohoda/PROPS", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> bohodaPROPS( @RequestParam("file") String file, @RequestParam("project") String project, @RequestParam("environment") String environment) {
		if((file.length()==0) || (project.length()==0) || (environment.length()==0)){
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(new PropertyTextConnector().handleRequest(file, project, environment, bohodaService),HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/bohoda/PROPERTIES")
	@Produces({MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public ResponseEntity<?> bohodaPropertyObject( @RequestParam("file") String file, @RequestParam("project") String project, @RequestParam("environment") String environment) {
		if((file.length()==0) || (project.length()==0) || (environment.length()==0)){
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(new PropertyObjectConnector().handleRequest(file, project, environment, bohodaService),HttpStatus.OK);
	}
}
