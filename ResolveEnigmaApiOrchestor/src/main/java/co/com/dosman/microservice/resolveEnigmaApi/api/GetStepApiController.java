package co.com.dosman.microservice.resolveEnigmaApi.api;

import io.swagger.annotations.ApiParam;

import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;

import co.com.dosman.microservice.resolveEnigmaApi.model.ErrorDetail;
import co.com.dosman.microservice.resolveEnigmaApi.model.JsonApiBodyRequest;
import co.com.dosman.microservice.resolveEnigmaApi.model.JsonApiBodyResponseErrors;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;


@RestController
public class GetStepApiController implements GetStepApi {
	private static final Logger logger = LoggerFactory.getLogger( GetStepApiController.class);
	private Object response;
		
	@EndpointInject(uri="direct:resolve-enigma")
	private FluentProducerTemplate producerTemplateEnigma;

    public ResponseEntity<?> getStepsPost(@ApiParam(value ="body", required = true ) @Valid @RequestBody JsonApiBodyRequest body ){
    	boolean isCorrectEnigma = (body.getData().get(0).getEnigma().equalsIgnoreCase("How to put a giraffe into a refrigerator?"));
      	
    	if (!isCorrectEnigma) {
        	return new ResponseEntity<>(createResponseErrors(body), HttpStatus.BAD_REQUEST);
        }
    	
    	try {
    		response = producerTemplateEnigma.withBody(body).request();
    		
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Couldn't Serialize response for content type application/json", e);
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private List<JsonApiBodyResponseErrors> createResponseErrors(JsonApiBodyRequest body) {
    	ErrorDetail errorDetail = new ErrorDetail();
    	errorDetail.setCode("0000");
    	errorDetail.setDetail("Enigma: ".concat(body.getData().get(0).getEnigma()).concat(" not supported - Expected: How to put a giraffe into a refrigerator?"));
    	errorDetail.setId(body.getData().get(0).getHeader().getId());
    	errorDetail.setSource("/getSteps");
    	errorDetail.setStatus("400");
    	errorDetail.setTitle("Enigma not supported");
    	
    	JsonApiBodyResponseErrors responseError = new JsonApiBodyResponseErrors();
    	responseError.addErrorsItem(errorDetail);
    	
    	List<JsonApiBodyResponseErrors> responseErrorsList = new ArrayList<JsonApiBodyResponseErrors>(); 
    	responseErrorsList.add(responseError);
    	
    	return responseErrorsList;
    }
}
