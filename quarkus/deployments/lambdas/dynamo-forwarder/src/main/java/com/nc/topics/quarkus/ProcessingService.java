package com.nc.topics.quarkus;

import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.amazonaws.services.lambda.runtime.Context;

import lombok.experimental.ExtensionMethod;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@ApplicationScoped
@ExtensionMethod({ Long.class })
public class ProcessingService {

	@Inject
	DynamoDbClient client;

	public OutputObject process(InputObject input, Context context) {
		var client = this.client;

		var req = PutItemRequest.builder().item(toItem(input, context)).tableName("lambda-invocations").build();
		client.putItem(req);

		var out = new OutputObject();
		out.setResult(System.currentTimeMillis());
		return out;
	}

	private Map<String, AttributeValue> toItem(Object input, Context context) {
		var rv = new TreeMap<String, AttributeValue>();
		rv.put("reqId", AttributeValue.builder().s(context.getAwsRequestId()).build());
		rv.put("timestamp", AttributeValue.builder().n(System.currentTimeMillis().toString()).build());
		rv.put("payload", AttributeValue.builder().s(input.toString()).build());

		return rv;
	}
}
