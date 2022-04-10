package com.nc.topics.quarkus;

import javax.inject.Inject;
import javax.inject.Named;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

@Named("forwarder")
public class DynamoForwarder implements RequestHandler<InputObject, OutputObject> {

	@Inject
	ProcessingService service;

	@Override
	public OutputObject handleRequest(InputObject input, Context context) {
		return service.process(input, context).with(context.getAwsRequestId());
	}
}
