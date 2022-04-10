package com.nc.topics.spell.frontend;

import javax.inject.Inject;
import javax.inject.Named;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

@Named("spellChecker")
public class SpellCheckLambda implements RequestHandler<InputObject, OutputObject<String>> {

	@Inject
	ProcessingService service;

	@Override
	public OutputObject<String> handleRequest(InputObject input, Context context) {
		return service.process(input).withRequestId(context.getAwsRequestId());
	}
}
