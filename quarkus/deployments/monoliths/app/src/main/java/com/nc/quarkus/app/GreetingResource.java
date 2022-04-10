package com.nc.quarkus.app;

import java.lang.StackWalker.Option;
import java.net.InetAddress;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.nc.topics.quarkus.services.api.greet.GreetService;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.SneakyThrows;

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

	private static String hostname;

	@SneakyThrows
	static String hostname() {
		var rv = hostname;
		if (rv == null) {
			hostname = rv = InetAddress.getLocalHost().getHostName();
		}
		return rv;
	}

	static long pid() {
		return ProcessHandle.current().pid();
	}

	@Inject
	TransactionManager tm;

	@Inject
	GreetService gs;

	@Context
	UriInfo info;

	@Context
	HttpServerRequest req;

	@Context
	HttpServerResponse res;

	@SuppressWarnings("unchecked")
	@GET
	@Path("/echo")
	@Produces(MediaType.APPLICATION_JSON)
	@JacksonFeatures(serializationEnable = { SerializationFeature.INDENT_OUTPUT })
	public Map<String, Object> echo() {
		var rv = new TreeMap<String, Object>();
		rv.put("host", hostname());
		rv.put("pid", pid());

		var h = (TreeMap<String, Object>) rv.computeIfAbsent("http-headers", __ -> new TreeMap<String, Object>());

		var map = req.headers();

		if (map != null) {
			var entries = map.entries();
			for (var e : entries) {
				var key = e.getKey();
				var v = h.computeIfAbsent(key, __ -> new ArrayList<>(0));
				if (v instanceof @SuppressWarnings("rawtypes") List l) {
					l.add(e.getValue());
				}
			}
		}

		h.putIfAbsent("X-Forwarded-For", List.of("Unknown"));

		rv.put("cookies", req.cookies());

//		var cookie = req.getCookie("AWSALB");
//
//		if (cookie != null) {
//			var age = cookie.getMaxAge();
//			cookie.setMaxAge(300);
//			rv.put("AWSALB-MaxAge", List.of(age, 300));
//
//			res.removeCookies("AWSALB");
//			res.addCookie(cookie);
//		}
//
//		cookie = req.getCookie("AWSALBCORS");
//
//		if (cookie != null) {
//			var age = cookie.getMaxAge();
//			cookie.setMaxAge(300);
//			rv.put("AWSALBCORS-MaxAge", List.of(age, 300));
//
//			res.removeCookies("AWSALBCORS");
//			res.addCookie(cookie);
//		}

		return rv;
	}

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