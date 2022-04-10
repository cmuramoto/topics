package com.nc.topics.quarkus;

public class OutputObject {

	private long result;

	private String requestId;

	public String getRequestId() {
		return requestId;
	}

	public long getResult() {
		return result;
	}

	public void setResult(long result) {
		this.result = result;
	}

	public OutputObject with(String requestId) {
		this.requestId = requestId;
		return this;
	}
}
