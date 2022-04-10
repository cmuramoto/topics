package com.nc.topics.quarkus;

import javax.enterprise.context.ApplicationScoped;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

@ApplicationScoped
public class ProcessingService {

	final boolean verbose = Boolean.parseBoolean(System.getenv("LOG_INPUT"));

	public OutputObject process(Object input, LambdaLogger logger) {
		if (verbose) {
			logger.log(input.toString());
		}
		var out = new OutputObject();
		out.setResult(System.currentTimeMillis());
		return out;
	}
}
