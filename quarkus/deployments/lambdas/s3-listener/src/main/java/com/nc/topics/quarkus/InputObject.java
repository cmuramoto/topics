package com.nc.topics.quarkus;

import java.util.HashMap;

public class InputObject extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	private int max;

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
