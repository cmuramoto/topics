package com.nc.quarkus.app;

import java.lang.StackWalker.Option;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.nc.service.api.greet.GreetService;

@Path("/hello")
public class GreetingResource {

	static class FrameInfo {
		String clazz;
		String method;
		String descriptor;

		@JsonCreator
		public FrameInfo(String clazz, String method, String descriptor) {
			super();
			this.clazz = clazz;
			this.method = method;
			this.descriptor = descriptor;
		}
	}

	@Inject
	TransactionManager tm;

	@Inject
	GreetService gs;

	@Transactional(TxType.REQUIRES_NEW)
	public Transaction get() throws SystemException {
		return tm.getTransaction();
	}

	@GET
	@Path("/greet")
	@Produces(MediaType.TEXT_PLAIN)
	public String greet() throws SystemException {
		return gs.greet();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello RESTEasy";
	}

	@GET
	@Path("/world")
	@Produces(MediaType.TEXT_PLAIN)
	public String helloWorld() throws SystemException {
		return "Hello World RESTEasy";
	}

	@GET
	@Path("/stack")
	@Produces(MediaType.APPLICATION_JSON)
	@JacksonFeatures(serializationEnable = { SerializationFeature.INDENT_OUTPUT })
	public Map.Entry<String, List<FrameInfo>> stack() {

		var stack = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).walk(frames -> frames.map(frame -> {
			var info = new FrameInfo(frame.getClassName(), frame.getMethodName(), frame.getDescriptor());
			return info;
		}).toList());

		return new AbstractMap.SimpleImmutableEntry<>("stack", stack);
	}
}