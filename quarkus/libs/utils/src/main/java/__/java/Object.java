package __.java;

import com.nc.quarkus.topics.util.JSON;

public class Object {

	public static String toJSON(Object o, boolean pretty) {
		return pretty ? JSON.pretty(o) : JSON.stringify(o);
	}

}
