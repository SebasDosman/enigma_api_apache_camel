package co.com.dosman.microservice.resolveEnigmaApi.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import co.com.dosman.microservice.resolveEnigmaApi.model.client.ClientJsonApiBodyResponseSuccess;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


@Component
public class GetStepTwoClientRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:get-step-two")
        .routeId("getStepTwo")
    	.setHeader(Exchange.HTTP_METHOD, constant("POST"))
    	.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .to("freemarker:templates/GetStepTwoClientTemplate.ftl")
        .hystrix()
        .hystrixConfiguration().executionTimeoutInMilliseconds(7000).end()
        .to("http4://localhost:8082/EnigmaSteps/getStep")
    	.convertBodyTo(String.class)
    	.unmarshal().json(JsonLibrary.Jackson, ClientJsonApiBodyResponseSuccess[].class)
        .process(new Processor() {
    		@Override
    		public void process(Exchange exchange) throws Exception {
    			ClientJsonApiBodyResponseSuccess stepTwoResponse = ( ClientJsonApiBodyResponseSuccess ) exchange.getIn().getBody(ClientJsonApiBodyResponseSuccess.class);
                
    			boolean isStepTwo = stepTwoResponse.getData().get(0).getStep().equalsIgnoreCase("2");
    			
    			if (isStepTwo) {
    				exchange.setProperty("Step2", "Step 2: ".concat(stepTwoResponse.getData().get(0).getStepDescription()));
    			} else {
    				exchange.setProperty("Error", "0003");
    				exchange.setProperty("descError", "Step two is not valid");
    			}
    		}
        })
        .endHystrix()
        .onFallback()
        .process( new Processor() {
        	@Override
        	public void process(Exchange exchange) throws Exception {
        		exchange.setProperty("Error", "0004");
        		exchange.setProperty("descError", "Error consulting the step two");
        	}
        })
        .end()
        .log("Response error code: ${exchangeProperty[Error]}")
        .log("Response error description: ${exchangeProperty[descError]}")
        .log("Response step description: ${exchangeProperty[Step2]}");
    }
}
